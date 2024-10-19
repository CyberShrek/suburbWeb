package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.misc.Route;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
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
import java.util.stream.Collectors;

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
            log.log("Записи l2_prig успешно конвертированы");
            log.log("Количество T1 (включая абонементные): " + t1List.size());
            log.log("Количество Lgot: " + lgotList.size());
            Set<T1> t1Aggregates = applyPrigRoutes(aggregate(t1List, log), requestDate, log);

        }
    }

    private Set<T1> applyPrigRoutes(Set<T1> t1Aggregates, Date requestDate, Log log) {
        log.log("Ищу и записываю маршруты для l2_prig...");
        t1Aggregates.forEach(t1 -> {
            String stationBeg = t1.getKey().getP15();
            String stationEnd = t1.getKey().getP54();
            List<Route> routesList = routes.findPrigRoutes(stationBeg, stationEnd, requestDate);
            T1.Key t1Key = t1.getKey();
            routesList.forEach(route -> {
                if (route.getStationBeg().equals(stationBeg)) {
                    switch (route.getType()) {
                        case 1 -> t1Key.setP16(route.getMatterStr());
                        case 2 -> t1Key.setP13(route.getMatterStr());
                        case 3 -> t1Key.setP14(route.getMatterStr());
                    }
                }
                if (route.getStationEnd().equals(stationEnd)) {
                    switch (route.getType()) {
                        case 1 -> t1Key.setP29(route.getMatterStr());
                        case 2 -> t1Key.setP27(route.getMatterStr());
                        case 3 -> t1Key.setP28(route.getMatterStr());
                    }
                }
                if(route.getType() == 5) {
                    t1Key.setP62((short) (route.getMatterStr().trim().equals("1") && route.getMcd() == 2
                            ? route.getDistance()
                            : 0));
                }
            });
            Set<Integer> mcdSet = routesList.stream()
                    .map(Route::getMcd)
                    .collect(Collectors.toSet());
            t1Key.setP63(switch (mcdSet.size()) {
                case 1 -> mcdSet.contains(1) ? '0' : '1';
                case 2 -> mcdSet.contains(1) && mcdSet.contains(2) ? '2' : '3';
                case 3 -> mcdSet.contains(1) && mcdSet.contains(2) && mcdSet.contains(3) ? '4' : null;
                default -> null;
            });
        });
        log.log("Маршруты добавлены");
        return t1Aggregates;
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
            log.log("Записи l2_pass успешно конвертированы");
            log.log("Количество T1 (включая абонементные): " + t1List.size());
            log.log("Количество Lgot: " + lgotList.size());
            Set<T1> t1Aggregates = aggregate(t1List, log);
            log.log("Ищу и записываю маршруты для этих станций...");
            t1Aggregates.forEach(t1 -> {
                routes.findPrigRoutes(t1.getKey().getP15(), t1.getKey().getP54(), requestDate);
            });
            log.log("Маршруты добавлены");
        }
    }

    private Set<T1> aggregate(List<T1> t1List, Log log) {
        log.log("Агрегирую записи T1...");
        Set<T1> aggregates = aggregate(t1List);
        log.log("Записи успешно агрегированы. Получено агрегатов: " + aggregates.size());
        Set<String> uniqueStationsSet = new HashSet<>();
        aggregates.forEach(t1 -> uniqueStationsSet.add(t1.getKey().getP15() + " " + t1.getKey().getP54()));
        log.log("Уникальных пар станций назначения и отправления: " + uniqueStationsSet.size());
        return aggregates;
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