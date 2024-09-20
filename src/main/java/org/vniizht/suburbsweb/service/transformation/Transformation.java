package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;
import org.vniizht.suburbsweb.service.transformation.data.Routes;
import org.vniizht.suburbsweb.service.transformation.data.Trips;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private PrigConversion prigConversion;
    @Autowired private PrigAggregation prigAggregation;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;
    @Autowired private Routes routes;
    @Autowired private Trips trips;
    @Autowired private Logger logger;

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

/*    public PrigConversion.Converted getConvertedByIdnum(Long idnum) {
        return prigConversion.convert(level2Data.getRecordByIdnum(idnum));
    }*/

    @PostConstruct
    public void speedCheck() {
        Date startDate = new Date();
        int yyyy = 2024, mm = 2, dd = 19;
        Date requestDate = new Date(yyyy - 1900, mm - 1, dd);
        logger.log("Проверка скорости выполнения трансформации записей за " + new SimpleDateFormat("dd-MM-yyyy").format(requestDate));
        logger.log("Загружаю записи...");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByRequestDate(requestDate);
        logger.log("Загружено записей: " + records.size());
        logger.log("Затрачено времени: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
        if(!records.isEmpty()) {
            logger.log("Конвертирую записи...");
            List<T1> convertedList = new ArrayList<>();
            records.forEach((idnum, record) -> convertedList.addAll(trips.multiplyByTrips(prigConversion.convert(record), record.prigMain)));
            logger.log("Записи успешно конвертированы. Количество (включая абонементные поездки): " + convertedList.size());
            logger.log("Агрегирую записи...");
            Set<T1> aggregatedSet = prigAggregation.aggregate(convertedList);
            logger.log("Записи успешно агрегированы. Получено агрегатов: " + aggregatedSet.size());
            Set<String> uniqueStationsSet = new HashSet<>();
            aggregatedSet.forEach(t1 -> uniqueStationsSet.add(t1.getKey().getP15() + " " + t1.getKey().getP54()));
            logger.log("Уникальных пар станций назначения и отправления: " + uniqueStationsSet.size());
            logger.log("Ищу маршруты для этих станций...");
            aggregatedSet.forEach(t1 -> routes.findRoutes(t1.getKey().getP15(), t1.getKey().getP54(), requestDate));
            logger.log("Маршруты найдены");
        }
        logger.log("Итоговое время: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
    }
}
