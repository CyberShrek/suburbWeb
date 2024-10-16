package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.handbook.HandbookCache;
import org.vniizht.suburbsweb.service.transformation.conversion.PassConversion;
import org.vniizht.suburbsweb.service.transformation.conversion.PrigConversion;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;
import org.vniizht.suburbsweb.service.transformation.data.Routes;
import org.vniizht.suburbsweb.service.transformation.data.Trips;
import org.vniizht.suburbsweb.util.Log;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private PrigConversion prigConversion;
    @Autowired private PassConversion passConversion;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;
    @Autowired private HandbookCache handbookCache;
    @Autowired private Routes routes;
    @Autowired private Trips trips;

    public String run(Date requestDate) {
        Log log = new Log();
        Date startDate = new Date();
        log.log("Выполняю трансформацию записей за " + new SimpleDateFormat("dd-MM-yyyy").format(requestDate));
        log.log("Загружаю справочники...");
        handbookCache.init();
        log.log("Справочники загружены.");
        log.log("Загружаю записи l2_prig...");
        transformPrig(level2Data.findPrigRecords(requestDate), requestDate, log);
        log.log("Загружаю записи l2_pass...");
        transformPass(level2Data.findPassRecords(requestDate), requestDate, log);
        log.log("Итоговое время: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
        handbookCache.clear();
        routes.clearCache();
        return log.collect();
    }

    private void transformPrig(Map<Long, Level2Data.PrigRecord> prigRecords, Date requestDate, Log log) {
        log.log("Загружено записей l2_prig: " + prigRecords.size());
        if(!prigRecords.isEmpty()) {
            log.log("Конвертирую записи l2_prig...");
            List<T1>   t1List   = new ArrayList<>();
            List<Lgot> lgotList = new ArrayList<>();
            prigRecords.forEach((idnum, prigRecord) -> {
                T1 t1 = prigConversion.getT1(prigRecord);
                t1List.addAll(trips.multiplyByTrips(t1, prigRecord.main));
                if(t1.getKey().getP25() == '1')
                    lgotList.add(prigConversion.getLgot(prigRecord, t1));
            });
            transformConverted(t1List, lgotList, requestDate, log);
        }
    }

    private void transformPass(Map<Long, Level2Data.PassRecord> passRecords, Date requestDate, Log log) {
        log.log("Загружено записей l2_pass: " + passRecords.size());
        if(!passRecords.isEmpty()) {
            log.log("Конвертирую записи l2_pass...");
            List<T1>   t1List   = new ArrayList<>();
            List<Lgot> lgotList = new ArrayList<>();
            passRecords.forEach((idnum, passRecord) -> {
                passConversion.getT1(passRecord).forEach(t1 -> {
                    t1List.add(t1);
                    if(t1.getKey().getP25() == '1')
                        lgotList.add(passConversion.getLgot(passRecord, t1));
                });
            });
            transformConverted(t1List, lgotList, requestDate, log);
        }
    }

    private void transformConverted(List<T1> t1List, List<Lgot> lgotList, Date requestDate, Log log) {
        log.log("Записи успешно конвертированы");
        log.log("Количество T1 (включая абонементные): " + t1List.size());
        log.log("Количество Lgot: " + lgotList.size());
        log.log("Агрегирую записи T1...");
        Set<T1> aggregates = aggregate(t1List);
        log.log("Записи успешно агрегированы. Получено агрегатов: " + aggregates.size());
        Set<String> uniqueStationsSet = new HashSet<>();
        aggregates.forEach(t1 -> uniqueStationsSet.add(t1.getKey().getP15() + " " + t1.getKey().getP54()));
        log.log("Уникальных пар станций назначения и отправления: " + uniqueStationsSet.size());
        log.log("Ищу маршруты для этих станций...");
        aggregates.forEach(t1 -> routes.findRoutes(t1.getKey().getP15(), t1.getKey().getP54(), requestDate));
        log.log("Маршруты найдены");
    }

    private Set<T1> aggregate(List<T1> t1List) {
        Map<T1.Key, T1> t1Map = new HashMap<>();

        for (T1 t1 : t1List) {
            T1.Key key = t1.getKey();
            if(t1Map.containsKey(key))
                t1Map.get(key).add(t1);
            else
                t1Map.put(key, t1);
        }

        return new HashSet<>(t1Map.values());
    }

    @PostConstruct
    public String check() {
        int yyyy = 2024, mm = 2, dd = 19;
        Date requestDate = new Date(yyyy - 1900, mm - 1, dd);
        return run(requestDate);
    }
}