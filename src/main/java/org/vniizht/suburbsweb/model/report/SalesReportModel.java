package org.vniizht.suburbsweb.model.report;

import org.vniizht.suburbsweb.service.SalesReportJdbc;

import java.util.HashMap;
import java.util.Map;

public class SalesReportModel extends ReportModel{

    public SalesReportModel(Map<String, Object> formValues, SalesReportJdbc jdbc) {
        super(formValues, jdbc);

        slot = "salesReportSlot";
        title = "Аналитическая отчетность продажи по пригородным пассажирским компаниям";

        if(fieldIsTrueOrHasValues("mainSection.regionsField"))
            this.context.put("Субъект", "mainSection.regionsField");

        int hasCarriersColumnAdd = hasCarriersColumn ? 1 : 0;

        table.put("primaryColumnsNumber", 4 + hasCarriersColumnAdd);

        Map<String[], Integer> dataFeaturesFieldToColumns = new HashMap<>();
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.calculationTypesField"}, 2);
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.travelDocumentTypesField", "additionalSection.shippingDocumentTypesField"}, 3);
        dataFeaturesFieldToColumns.put(new String[]{"additionalSection.operationTypesField"}, 4);

        setupDataFeatures("sales.json", dataFeaturesFieldToColumns);
    }
}
