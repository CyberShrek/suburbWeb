package org.vniizht.suburbsweb.model.order;

import java.util.Map;

public class DeparturesFormStatementOrder extends FormStatementOrder{

    public DeparturesFormStatementOrder(boolean initial, Map<String, Object> formValues) {
        super(initial, formValues);
        if(initial) {
            putOptions("additionalSection.trainCategoriesField","trainCategories.json");
        }

        if((Boolean)formValues.get("additionalSection.switchField")) {
            show.add("additionalSection.trainCategoriesField");
        }
        else {
            hide.add("additionalSection.trainCategoriesField");
        }
    }
}
