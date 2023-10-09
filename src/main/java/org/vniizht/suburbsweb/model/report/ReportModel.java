package org.vniizht.suburbsweb.model.report;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.vniizht.suburbsweb.service.ReportJdbc;
import org.vniizht.suburbsweb.util.ResourcesAccess;

import java.util.*;

public abstract class ReportModel {
    public String title = "";
    public String slot;
    public Map<String, String> context = new LinkedHashMap<>();
    public Map<String, Object> table = new HashMap<>();

    public List<Object[]> data = new ArrayList<>();
    public List<Object> dataFeatures = new ArrayList<>();

    protected final boolean hasCarriersColumn;

    private final Map<String, Object> formValues;
    private String fieldKey;

    protected ReportModel(Map<String, Object> formValues, ReportJdbc jdbc){
        this.formValues = formValues;

        hasCarriersColumn = ((List<?>)formValues.get("mainSection.carriersField")).size() >= 2;

        context.put("Период",     "periodSection.dateField");
        if(!hasCarriersColumn)
            context.put("Перевозчик", "mainSection.carriersField");
        context.put("Дорога",     "mainSection.roadsField");

        table.put("head", new ArrayList<>());
        table.put("groupedColumnsNumber", 1);

        applyData(jdbc.getDataByFieldValues(formValues));
    }

    protected boolean fieldIsTrueOrHasValues(String fieldKey){
        Object value = formValues.get(fieldKey);
        return (value instanceof List && (((List<?>)value).size() > (fieldKey.equals("mainSection.carriersField") ? 1 : 0))
             || value instanceof Boolean && (Boolean)value);
    }

    protected void setupDataFeatures(String featureFileName, Map<String[], Integer> fieldsToColumns){
        List<Object> dataFeaturesBuffer = ResourcesAccess.getList("dataFeatures/" + featureFileName);

        fieldsToColumns.put(new String[]{"periodSection.detailsToggleField"}, 0);
        fieldsToColumns.put(new String[]{"mainSection.carriersField"}, 1);

        fieldsToColumns.forEach((fieldKeys, columnIndex) -> {
            boolean columnIsPresent = Arrays.stream(fieldKeys).anyMatch(this::fieldIsTrueOrHasValues);

            if(!columnIsPresent) {
                // Erasing
                dataFeaturesBuffer.set(columnIndex, null);
                table.replace("primaryColumnsNumber", ((Integer)table.get("primaryColumnsNumber")) - 1);
            }
        });

        dataFeaturesBuffer.forEach(nullableDataFeature ->
                Optional.ofNullable(nullableDataFeature).ifPresent(dataFeature ->
                        dataFeatures.add(dataFeature)));
    }

    private void applyData(SqlRowSet sqlData){
        SqlRowSetMetaData metaData = sqlData.getMetaData();
        boolean headIsReady = false;
        while(sqlData.next()){
            List<Object> row = new ArrayList<>();
            for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                row.add(sqlData.getObject(columnIndex));
                if (!headIsReady)
                    ((List<String>)table.get("head"))
                            .add(metaData.getColumnName(columnIndex));
            }
            headIsReady = true;
            data.add(row.toArray());
        }
    }
}
