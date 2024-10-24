package org.vniizht.suburbsweb.service.transformation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class Transformation {

//    @Autowired private PrigConversion prigConversion;
//    @Autowired private PassConversion passConversion;
//    @Autowired private Level2Data level2Data;
//    @Autowired private Level3Data level3Data;
//    @Autowired private HandbookCache handbookCache;
//
//    public String run(Date requestDate) {
//        Log log = new Log();
//        Date startDate = new Date();
//        log.log("Выполняю трансформацию записей за " + new SimpleDateFormat("dd-MM-yyyy").format(requestDate));
//        log.log("Загружаю справочники...");
//        handbookCache.init();
//        log.log("Справочники загружены.");
//        log.log("Загружаю записи l2_prig...");
//        transformPrig(level2Data.findPrigRecords(requestDate), log);
//        log.log("Загружаю записи l2_pass...");
//        transformPass(level2Data.findPassRecords(requestDate), log);
//        log.log("Итоговое время: " + (((new Date()).getTime() - startDate.getTime()) / 1000) + "c.");
//        handbookCache.clear();
//        routes.clearCache();
//        return log.collect();
//    }
//
//    private void transformPrig(Map<Long, Level2Data.PrigRecord> prigRecords, Log log) {
//        log.log("Загружено записей l2_prig: " + prigRecords.size());
//        if(!prigRecords.isEmpty()) {
//            log.log("Конвертирую записи l2_prig...");
//            List<T1>   t1List   = new ArrayList<>();
//            List<Lgot> lgotList = new ArrayList<>();
//            prigRecords.forEach((idnum, prigRecord) -> {
//                T1 t1 = prigConversion.getT1(prigRecord);
//                t1List.addAll(trips.multiplyByTrips(t1, prigRecord.main));
//                if(t1.getKey().getP25() == '1')
//                    lgotList.add(prigConversion.getLgot(prigRecord, t1));
//            });
//            log.log("Записи l2_prig успешно конвертированы");
//            log.log("Количество T1 (включая абонементные): " + t1List.size());
//            log.log("Количество Lgot: " + lgotList.size());
//            Set<T1> t1Aggregates = applyPrigRoutes(aggregate(t1List, log), log);
//        }
//    }
//
//    private void transformPass(Map<Long, Level2Data.PassRecord> passRecords, Log log) {
//        log.log("Загружено записей l2_pass: " + passRecords.size());
//        if(!passRecords.isEmpty()) {
//            log.log("Конвертирую записи l2_pass...");
//            List<T1>   t1List   = new ArrayList<>();
//            List<Lgot> lgotList = new ArrayList<>();
//            passRecords.forEach((idnum, passRecord) -> {
//                passConversion.getT1(passRecord).forEach(t1 -> {
//                    t1List.add(t1);
//                    if(t1.getKey().getP25() == '1')
//                        lgotList.add(passConversion.getLgot(passRecord, t1));
//                });
//            });
//            log.log("Записи l2_pass успешно конвертированы");
//            log.log("Количество T1 (включая абонементные): " + t1List.size());
//            log.log("Количество Lgot: " + lgotList.size());
//            Set<T1> t1Aggregates = aggregate(t1List, log);
//        }
//    }
//
//    private Set<T1> applyPrigRoutes(Set<T1> t1Aggregates, Log log) {
//        log.log("Ищу и записываю маршруты для l2_prig...");
//        t1Aggregates.forEach(t1 -> {
//            String stationBeg = t1.getKey().getP15();
//            String stationEnd = t1.getKey().getP54();
//            PrigRoute prigRoute = routes.getPrigRoute(stationBeg, stationEnd, t1.getKey().getRequest_date());
//            addRoute(t1, prigRoute);
//
//            t1.getKey().setP62(prigRoute.getMcdDistance());
//            t1.getKey().setP63(prigRoute.getMcdType());
//        });
//        log.log("Маршруты добавлены");
//        return t1Aggregates;
//    }
//
//    private Set<T1> applyPassRoutes(Level2Data.PassRecord passRecord, Set<T1> t1Aggregates, Log log) {
//        log.log("Ищу и записываю маршруты для l2_pass...");
//        t1Aggregates.forEach(t1 -> {
//            String stationBeg = t1.getKey().getP15();
//            String stationEnd = t1.getKey().getP54();
//            PassRoute passRoute = routes.getPassRoute(
//                    passRecord.main.getTrain_num(),
//                    passRecord.main.getTrain_thread(),
//                    t1.getKey().getRequest_date(),
//                    stationBeg, stationEnd);
//            addRoute(t1, passRoute);
//        });
//        log.log("Маршруты добавлены");
//        return t1Aggregates;
//    }
//
//    private void addRoute(T1 t1, Route route){
//        T1.Key t1Key = t1.getKey();
//
//        t1Key.setP13(route.getRoadStart());
//        t1Key.setP14(route.getDepartmentStart());
//        t1Key.setP16(route.getRegionStart());
//        t1Key.setP27(route.getRoadEnd());
//        t1Key.setP28(route.getDepartmentEnd());
//        t1Key.setP29(route.getRegionEnd());
//    }
//
//    private Set<T1> aggregate(List<T1> t1List, Log log) {
//        log.log("Агрегирую записи T1...");
//        Set<T1> aggregates = aggregate(t1List);
//        log.log("Записи успешно агрегированы. Получено агрегатов: " + aggregates.size());
//        Set<String> uniqueStationsSet = new HashSet<>();
//        aggregates.forEach(t1 -> uniqueStationsSet.add(t1.getKey().getP15() + " " + t1.getKey().getP54()));
//        log.log("Уникальных пар станций назначения и отправления: " + uniqueStationsSet.size());
//        return aggregates;
//    }
//
//    private Set<T1> aggregate(List<T1> t1List) {
//        Map<T1.Key, T1> t1Map = new HashMap<>();
//
//        for (T1 t1 : t1List) {
//            T1.Key key = t1.getKey();
//            if(t1Map.containsKey(key))
//                t1Map.get(key).add(t1);
//            else
//                t1Map.put(key, t1);
//        }
//
//        return new HashSet<>(t1Map.values());
//    }
//
//    @PostConstruct
//    public String check() {
//        int yyyy = 2024, mm = 2, dd = 19;
//        Date requestDate = new Date(yyyy - 1900, mm - 1, dd);
//        return run(requestDate);
//    }
}