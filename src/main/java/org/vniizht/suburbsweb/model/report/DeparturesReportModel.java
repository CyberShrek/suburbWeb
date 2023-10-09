package org.vniizht.suburbsweb.model.report;

import org.vniizht.suburbsweb.service.DeparturesReportJdbc;

import java.util.HashMap;
import java.util.Map;

public class DeparturesReportModel extends ReportModel{
    public DeparturesReportModel(Map<String, Object> formValues, DeparturesReportJdbc jdbc) {
        super(formValues, jdbc);

        slot  = "departuresReportSlot";
        title = "Аналитическая отчетность по отправлению по пригородным пассажирским компаниям";

        int hasCarriersColumnAdd = hasCarriersColumn ? 1 : 0;

        table.put("primaryColumnsNumber", 2 + hasCarriersColumnAdd);

        Map<String[], Integer> dataFeaturesFieldToColumns = new HashMap<>();
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.trainCategoriesField"}, 2);

        setupDataFeatures("departures.json", dataFeaturesFieldToColumns);
    }
}