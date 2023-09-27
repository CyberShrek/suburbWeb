package org.vniizht.suburbsweb.model.report;

import org.vniizht.suburbsweb.service.report.SalesReportJdbc;

import java.util.HashMap;
import java.util.Map;

public class SalesReportModel extends ReportModel{

    public SalesReportModel(Map<String, Object> formValues, SalesReportJdbc jdbc) {
        super(formValues, jdbc);

        slot = "salesReportSlot";
        title = "Аналитическая отчетность продажи по пригородным пассажирским компаниям";

        table.put("primaryColumnsNumber", 4);

        if(fieldIsTrueOrHasValues("mainSection.regionsField"))
            this.context.put("Субъект", "mainSection.regionsField");

        Map<String[], Integer> dataFeaturesFieldToColumns = new HashMap<>();
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.calculationTypesField"}, 1);
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.travelDocumentTypesField", "additionalSection.shippingDocumentTypesField"}, 2);
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.operationTypesField"}, 3);

        setupDataFeatures("sales.json", dataFeaturesFieldToColumns);
    }
}
