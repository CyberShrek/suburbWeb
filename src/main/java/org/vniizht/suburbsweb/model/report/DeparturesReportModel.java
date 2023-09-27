package org.vniizht.suburbsweb.model.report;

import org.vniizht.suburbsweb.service.report.DeparturesReportJdbc;
import org.vniizht.suburbsweb.util.ResourcesAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeparturesReportModel extends ReportModel{
    public DeparturesReportModel(Map<String, Object> formValues, DeparturesReportJdbc jdbc) {
        super(formValues, jdbc);

        slot  = "departuresReportSlot";
        title = "Аналитическая отчетность по отправлению по пригородным пассажирским компаниям";

        table.put("primaryColumnsNumber", 2);

        Map<String[], Integer> dataFeaturesFieldToColumns = new HashMap<>();
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.trainCategoriesField"}, 1);

        setupDataFeatures("departures.json", dataFeaturesFieldToColumns);
    }
}