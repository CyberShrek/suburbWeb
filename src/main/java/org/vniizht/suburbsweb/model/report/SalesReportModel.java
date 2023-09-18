package org.vniizht.suburbsweb.model.report;

import javax.sql.RowSet;
import java.util.Map;

public class SalesReportModel extends ReportModel{

    public SalesReportModel(Map<String, Object> formValues) {
        super(formValues);

        title = "Аналитическая отчетность продажи по пригородным пассажирским компаниям";

        addContextIfFieldHasOptions("Субъект", "mainSection.regionsField");

        addHeadCellIfFieldToggled("Дата продажи", "periodSection.detailsToggleField");
        addHeadCellIfFieldHasOptions("Вид расчёта", "additionalSection.calculationTypesField");
        addHeadCellIfFieldHasOptions("Вид проездного документа", "additionalSection.travelDocumentTypesField");
        addHeadCellIfFieldHasOptions("Вид перевозочного документа", "additionalSection.shippingDocumentTypesField");
        addHeadCellIfFieldHasOptions("Вид операции", "additionalSection.operationTypesField");

        addHeadCell("Количество человек или перевозок");
        addHeadCell("Доход");
        addHeadCell("Недополученный доход");
        addHeadCell("Сумма сбора");
        addHeadCell("Доход за провоз ручной клади");
    }
}
