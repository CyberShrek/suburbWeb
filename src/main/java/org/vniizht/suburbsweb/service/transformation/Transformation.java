package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.handbook.HandbookCache;
import org.vniizht.suburbsweb.service.transformation.aggregation.PrigAggregation;
import org.vniizht.suburbsweb.service.transformation.conversion.PrigConversion;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;
import org.vniizht.suburbsweb.service.transformation.data.Routes;
import org.vniizht.suburbsweb.service.transformation.data.Trips;
import org.vniizht.suburbsweb.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private PrigConversion prigConversion;
    @Autowired private PrigAggregation prigAggregation;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;
    @Autowired private HandbookCache handbookCache;
    @Autowired private Routes routes;
    @Autowired private Trips trips;
    @Autowired private Logger logger;

    public void run() {

    }

    public void transform(Long id, Level2Data.PrigRecord l2PrigRecord) {
        logger.log("\t" + id);
//        level3Data.addRecord(conversion.convert(l2Record));
    }

/*    public PrigConversion.Converted getConvertedByIdnum(Long idnum) {
        return prigConversion.convert(level2Data.getRecordByIdnum(idnum));
    }*/
    public String tryPrig(Date requestDate) {
        Log log = new Log();
        Date startDate = new Date();
        log.log("Проверка скорости выполнения трансформации записей за " + new SimpleDateFormat("dd-MM-yyyy").format(requestDate));
        log.log("Загружаю справочники...");
        handbookCache.init();
        log.log("Справочники загружены.");
        log.log("Загружаю записи второго уровня...");
        Map<Long, Level2Data.PrigRecord> records = level2Data.findPrigRecords(requestDate);
        log.log("Загружено записей: " + records.size());
        log.log("Итого затрачено времени на загрузку: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
        if(!records.isEmpty()) {
            log.log("Конвертирую записи...");
            List<T1> convertedList = new ArrayList<>();
            records.forEach((idnum, prigRecord) -> convertedList.addAll(trips.multiplyByTrips(prigConversion.getT1(prigRecord), prigRecord.main)));
            log.log("Записи успешно конвертированы. Количество (включая абонементные поездки): " + convertedList.size());
            log.log("Агрегирую записи...");
            Set<T1> aggregatedSet = prigAggregation.aggregate(convertedList);
            log.log("Записи успешно агрегированы. Получено агрегатов: " + aggregatedSet.size());
            Set<String> uniqueStationsSet = new HashSet<>();
            aggregatedSet.forEach(t1 -> uniqueStationsSet.add(t1.getKey().getP15() + " " + t1.getKey().getP54()));
            log.log("Уникальных пар станций назначения и отправления: " + uniqueStationsSet.size());
            log.log("Ищу маршруты для этих станций...");
            aggregatedSet.forEach(t1 -> routes.findRoutes(t1.getKey().getP15(), t1.getKey().getP54(), requestDate));
            log.log("Маршруты найдены");
        }
        log.log("Итоговое время: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
        handbookCache.clear();
        routes.clearCache();
        return log.collect();
    }

//    @PostConstruct
    public String check() {
        Date startDate = new Date();
        int yyyy = 2024, mm = 2, dd = 19;
        Date requestDate = new Date(yyyy - 1900, mm - 1, dd);
        return tryPrig(requestDate);
    }
}
