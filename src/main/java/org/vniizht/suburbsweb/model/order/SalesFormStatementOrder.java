package org.vniizht.suburbsweb.model.order;

import java.util.Map;

public class SalesFormStatementOrder extends FormStatementOrder{

    public SalesFormStatementOrder(boolean initial, Map<String, Object> formValues) {
        super(initial, formValues);
        if(initial) {
            putServiceBankSetup("mainSection.regionsField", "regions.json");
            putOptions("additionalSection.calculationTypesField","calculationTypes.json");
            putOptions("additionalSection.travelDocumentTypesField","travelDocumentTypes.json");
            putOptions("additionalSection.shippingDocumentTypesField","shippingDocumentTypes.json");
            putOptions("additionalSection.operationTypesField","operationTypes.json");
        }

        if((Boolean)formValues.get("additionalSection.switchField")) {
            show.add("additionalSection.calculationTypesField");
            show.add("additionalSection.travelDocumentTypesField");
            show.add("additionalSection.shippingDocumentTypesField");
            show.add("additionalSection.operationTypesField");
        }
        else {
            hide.add("additionalSection.calculationTypesField");
            hide.add("additionalSection.travelDocumentTypesField");
            hide.add("additionalSection.shippingDocumentTypesField");
            hide.add("additionalSection.operationTypesField");
        }
    }
}
