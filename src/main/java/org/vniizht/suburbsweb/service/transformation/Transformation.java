package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private Conversion conversion;
    @Autowired private Aggregation aggregation;
    @Autowired private Logger logger;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;

    public void run() {
        logger.log("Запуск трансформации данных по пригороду со второго уровня в третий");
        Long lastLevel3Id = level3Data.getLastId();

        logger.log("Последний индекс третьего уровня: " + lastLevel3Id + "; ищу записи второго уровня с индексом больше этого значения");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);

        logger.log("Найдено записей: " + records.size());
//        records.forEach(this::transform);

        logger.log("Конец трансформации");
    }

    public void transform(Long id, Level2Data.Record l2Record) {
        logger.log("\t" + id);
//        level3Data.addRecord(conversion.convert(l2Record));
    }

    public Conversion.Converted getConvertedByIdnum(Long idnum) {
        return conversion.convert(level2Data.getRecordByIdnum(idnum));
    }

    @PostConstruct
    public void speedCheck() {
        Date startDate = new Date();
        int yyyy = 2024, mm = 2, dd = 19;
        Date requestDate = new Date(yyyy - 1900, mm - 1, dd);
        logger.log("Проверка скорости выполнения трансформации записей за 2024-02-19");
        logger.log("Загружаю записи...");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByRequestDate(requestDate);
        logger.log("Загружено записей: " + records.size());
        if(!records.isEmpty()) {
            logger.log("Трансформирую записи...");
            List<Conversion.Converted> convertedList = new ArrayList<>();
            records.forEach((idnum, record) -> convertedList.add(conversion.convert(record)));
            logger.log("Записи успешно трансформированы");
        }
        logger.log("Итоговое время выполнения: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
    }
}
