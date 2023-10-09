package org.vniizht.suburbsweb.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SalesReportJdbc extends ReportJdbc{

    SalesReportJdbc(){
        this.sqlQueryFileName = "sql/sales.sql";

        propertiesToDeleteLines.put("$region_code", "mainSection.regionsField");
        propertiesToDeleteLines.put("$calculation_type", "additionalSection.calculationTypesField");
        propertiesToDeleteLines.put("$document_type", "documentTypesField");
        propertiesToDeleteLines.put("$operation_type", "additionalSection.operationTypesField");

        propertiesToReplace.put("$region_codes:numeric", "mainSection.regionsField");
        propertiesToReplace.put("$calculation_types", "additionalSection.calculationTypesField");
        propertiesToReplace.put("$document_types", "documentTypesField");
        propertiesToReplace.put("$operation_types", "additionalSection.operationTypesField");
    }

    @Override
    public String getQueryByFieldValues(Map<String, Object> formValues){
        // Merging document types
        List<String> documentList = new ArrayList<>();
        Optional.ofNullable((List<String>)formValues.get("additionalSection.travelDocumentTypesField"))
                .ifPresent(documentList::addAll);
        Optional.ofNullable((List<String>)formValues.get("additionalSection.shippingDocumentTypesField"))
                .ifPresent(documentList::addAll);

        formValues.put("documentTypesField", documentList);
        return super.getQueryByFieldValues(formValues);
    }
}
