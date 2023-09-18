package org.vniizht.suburbsweb.model.report;

import javax.sql.RowSet;
import java.util.Map;

public class DeparturesReportModel extends ReportModel{

    public DeparturesReportModel(Map<String, Object> formValues) {
        super(formValues);

        title = "Аналитическая отчетность по отправлению по пригородным пассажирским компаниям";

        addHeadCellIfFieldToggled("Дата отправления", "periodSection.detailsToggleField");
        addHeadCellIfFieldHasOptions("Категория поезда", "additionalSection.trainCategoriesField");

        addHeadCell("Количество пассажиров");
        addHeadCell("Средняя дальность");
        addHeadCell("Пассажиро-км");
    }
}
