package org.vniizht.suburbsweb.service;

import org.springframework.stereotype.Service;

@Service
public class DeparturesReportJdbc extends ReportJdbc{

    DeparturesReportJdbc(){
        super();

        this.sqlQueryFileName = "sql/departures.sql";

        propertiesToDeleteLines.put("$train_category", "additionalSection.trainCategoriesField");
        propertiesToReplace.put("$train_categories", "additionalSection.trainCategoriesField");
    }
}