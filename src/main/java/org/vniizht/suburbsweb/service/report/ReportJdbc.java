package org.vniizht.suburbsweb.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.util.ResourcesAccess;

import java.util.*;
import java.util.stream.Collectors;

@Service
public abstract class ReportJdbc {

    // Sql query file name which will be used to retrieve the row set
    protected String sqlQueryFileName;

    // Key is a name of $<name> property.
    // Value is a field name.
    // In the query each line with specified name in the comment will be removed from query if the corresponding field has no value
    protected HashMap<String, String> propertiesToDeleteLines = new HashMap<>();

    // Key is a name of $<name> property.
    // Value is a field name.
    // In the query each property with specified name will be replaced with the corresponding field value
    protected HashMap<String, String> propertiesToReplace = new HashMap<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReportJdbc(){
        // Common
        propertiesToDeleteLines.put("$date", "periodSection.detailsToggleField");
        propertiesToReplace.put("$start_date", "periodSection.dateField");
        propertiesToReplace.put("$end_date", "periodSection.dateField");
        propertiesToReplace.put("$road_codes", "mainSection.roadsField");
    }

    public SqlRowSet getDataByFieldValues(Map<String, Object> formValues) {
        return jdbcTemplate.queryForRowSet(getQueryByFieldValues(formValues));
    }

    // Must be public for tests
    public String getQueryByFieldValues(Map<String, Object> formValues) {
        StringBuilder sqlBuilder = new StringBuilder();

        for (String queryLine: ResourcesAccess.getText(sqlQueryFileName).replaceAll(getDeleteLinesRegexp(formValues), "").split("\n"))
            sqlBuilder.append(replacePropertiesWithFieldValues(queryLine, formValues));

        return sqlBuilder.toString();
    }

    private String getDeleteLinesRegexp(Map<String, Object> formValues){
        StringJoiner regexpJoiner = new StringJoiner("|");
        propertiesToDeleteLines.forEach((propertyName, fieldKey) -> {
            Object formValue = formValues.get(fieldKey);
            if (formValue == null
                || formValue instanceof Boolean && !(Boolean) formValue
                || formValue instanceof List && ((List<?>) formValue).isEmpty())
                regexpJoiner.add(".*\\" + propertyName);
        });
        return regexpJoiner.toString();
    }

    private String replacePropertiesWithFieldValues(String target, Map<String, Object> formValues){
        for (String propertyKey: propertiesToReplace.keySet()) {
            if(target.contains(propertyKey)) {
                Object formValue = formValues.get(propertiesToReplace.get(propertyKey));
                String type = propertyKey.contains(":") ? propertyKey.split(":")[1] : "string";
                if(formValue instanceof List){
                    target = target.replaceAll("\\"+propertyKey,
                            propertyKey.equals("$start_date") ? String.valueOf(((List<?>)formValue).get(0)) :
                            propertyKey.equals("$end_date")   ? String.valueOf(((List<?>)formValue).get(1)) :
                                    ((List<?>)formValue)
                                            .stream()
                                            .map(valueItem -> type.equals("numeric") ? String.valueOf(valueItem) : "'"+valueItem+"%'")
                                            .collect(Collectors.joining(", ")));
                }
            }
        }
        return target;
    }
}
