package org.vniizht.suburbsweb.model.report;

import javax.sql.RowSet;
import java.util.*;

public abstract class ReportModel {
    public String title = "";
    public Map<String, String> context = new LinkedHashMap<>();
    public Map<String, Object> table = new HashMap<>();

    List<Object[]> data = new ArrayList<>();

    private Map<String, Object> formValues;

    protected ReportModel(Map<String, Object> formValues){
        this.formValues = formValues;

        context.put("Период",     "periodSection.dateField");
        context.put("Перевозчик", "mainSection.carriersField");
        context.put("Дорога",     "mainSection.roadsField");

        table.put("head", new ArrayList<String>());
    }

    protected void addContextIfFieldHasOptions(String context, String fieldKey){
        Object options = formValues.get(fieldKey);
        if(options != null && !((Map)options).isEmpty())
            this.context.put(context, fieldKey);
    }

    protected void addHeadCellIfFieldToggled(String headCell, String fieldKey){
        Object value = formValues.get(fieldKey);
        if(value != null && ((Boolean)value))
            addHeadCell(headCell);
    }

    protected void addHeadCellIfFieldHasOptions(String headCell, String fieldKey){
        Object options = formValues.get(fieldKey);
        if(options != null && !((Map)options).isEmpty())
            addHeadCell(headCell);
    }

    protected void addHeadCell(String headCell){
        ((List<String>)table.get("head"))
                .add(headCell);
    }
}
