package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vniizht.suburbsweb.model.order.DeparturesFormStatementOrder;
import org.vniizht.suburbsweb.model.order.FormStatementOrder;
import org.vniizht.suburbsweb.model.order.SalesFormStatementOrder;
import org.vniizht.suburbsweb.model.report.DeparturesReportModel;
import org.vniizht.suburbsweb.model.report.ReportModel;
import org.vniizht.suburbsweb.model.report.SalesReportModel;
import org.vniizht.suburbsweb.service.report.DeparturesReportJdbc;
import org.vniizht.suburbsweb.service.report.ReportJdbc;
import org.vniizht.suburbsweb.service.report.SalesReportJdbc;

import javax.xml.ws.http.HTTPException;
import java.util.Map;

@RestController
public class ResourcesController {

    @Autowired
    private SalesReportJdbc salesReportJdbc;

    @Autowired
    private DeparturesReportJdbc departuresReportJdbc;

    @PostMapping("/{formName}/form")
    public FormStatementOrder fetchFormStatement(
            @PathVariable String formName,
            @RequestParam(name = "trigger") String triggerKey,
            @RequestBody Map<String, Object> formValues)
    {
        boolean initial = triggerKey.equals("initial");
        switch (formName){
            case "sales": return new SalesFormStatementOrder(initial, formValues);
            case "departures": return new DeparturesFormStatementOrder(initial, formValues);
            default: throw new HTTPException(404);
        }
    }

    @PostMapping("/{formName}/report")
    public ReportModel fetchReport(
            @PathVariable String formName,
            @RequestBody Map<String, Object> formValues)
    {
        switch (formName){
            case "sales": return new SalesReportModel(formValues, salesReportJdbc);
            case "departures": return new DeparturesReportModel(formValues, departuresReportJdbc);
            default: throw new HTTPException(404);
        }
    }
}
