package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.service.data.dao.TripsDao;
import org.vniizht.suburbsweb.service.result.Level3;
import org.vniizht.suburbsweb.service.result.Level3Pass;
import org.vniizht.suburbsweb.service.result.Level3Prig;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.Level3Dao;
import org.vniizht.suburbsweb.util.Log;
import org.vniizht.suburbsweb.util.Util;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private Level2Dao level2;
    @Autowired private Level3Dao level3;
    @Autowired private Handbook handbook;
    @Autowired private RoutesDao routes;
    @Autowired private TripsDao trips;

    @Transactional
    public synchronized String transform(TransformationOptions options) {
        Log log = new Log();
        return log.sumUp("Итоговое время: " + Util.measureTime(() -> {
            if(!options.pass && !options.prig) return;
            if(options.date == null) {
                options.date = getRequestDate(log);
                if(options.date == null) return;
            }

            log.addTimeLine("Выполняю трансформацию записей за " + Util.formatDate(options.date, "dd.MM.yyyy"));
            log.sumUp();

            log.addTimeLine("Загружаю справочники...");
            handbook.loadCache();
            log.addTimeLine("Справочники загружены.");
            log.sumUp();
            try { complete(
                    options.date,
                    options.prig ? transformPrig(options.date, log) : null,
                    options.pass ? transformPass(options.date, log) : null,
                    log
            );
            } catch (Exception e) {
                throw new RuntimeException(log.toString() + "\n" + e.getLocalizedMessage(), e);
            } finally {
                handbook.clearCache();
                routes.clearCache();
            }
        }) + "c");
    }

    private Level3Prig transformPrig(Date requestDate, Log log) throws Exception {
        Set<Level2Dao.PrigRecord> records = loadRecords(
                () -> level2.findPrigRecords(requestDate), log, "l2_prig");

        return (Level3Prig) transform(
                () -> new Level3Prig(records, handbook, routes, trips),
                log, "l2_prig"
        );
    }

    private Level3Pass transformPass(Date requestDate, Log log) throws Exception {
        Set<Level2Dao.PassRecord> records = loadRecords(
                () -> level2.findPassRecords(requestDate), log, "l2_pass");

        return (Level3Pass) transform(
                () -> new Level3Pass(records, handbook, routes),
                log, "l2_pass"
        );
    }

    private Set loadRecords(Callable<Set> loader, Log log, String name) throws Exception {
        log.addTimeLine("Загружаю записи " + name + "...");
        Set<Level2Dao.Record> records = loader.call();
        log.addTimeLine("Записи" + name + " успешно загружены. Количество: " + records.size());

        return records;
    }

    private Level3 transform(Callable<Level3> loader, Log log, String name) throws Exception {
        log.addTimeLine("Трансформирую записи " + name + "...");
        Level3 level3 = loader.call();
        log.addTimeLine("Записи " + name + " успешно трансформированы.");
        log.sumUp(
//                "Затраты времени на трансформацию записей " + name,
//                "\tТрансформация t1: "          + level3.getT1TransformationTime() +
//                        "c (включая поиск маршрутов: " + level3.getT1TripsSearchTime() + "c)",
//                "\tТрансформация lgot: "        + level3.getLgotTransformationTime() + "c"
        );
        return level3;
    }

    private Date getRequestDate(Log log) {
        log.addTimeLine("Определяю дату запроса...");
        Date requestDate = level3.getNextRequestDate();
        if(requestDate == null) {
            log.addTimeLine("На третьем уровне ещё нет данных. Повторите запрос с указанием даты.");
        }
        return requestDate;
    }

    private void complete(Date date,
                          Level3Prig level3Prig,
                          Level3Pass level3Pass,
                          Log log) {

        Set<T1>    t1Set   = new HashSet<>();
                aggregateT1(
                Stream.concat(
                        level3Prig == null ? Stream.empty() : level3Prig.getT1Result().stream(),
                        level3Pass == null ? Stream.empty() : level3Pass.getT1Result().stream())
                        .collect(Collectors.toList()), log);

        Set<Lgot>  lgotSet = Stream.concat(
                level3Prig == null ? Stream.empty() : level3Prig.getLgotResult().stream(),
                level3Pass == null ? Stream.empty() : level3Pass.getLgotResult().stream())
                .collect(Collectors.toSet());

        log.sumUp("Итоговое количество T1: " + t1Set.size(),
                "Итоговое количество Lgot: " + lgotSet.size());

        update(date, t1Set, lgotSet, log);
    }

    private Set<T1> aggregateT1(List<T1> t1List, Log log) {
        log.addTimeLine("Агрегирую T1...");
        Map<T1.Key, T1> t1Map = new HashMap<>();

        for (T1 t1 : t1List) {
            T1.Key key = t1.getKey();
            if(t1Map.containsKey(key))
                t1Map.get(key).add(t1);
            else
                t1Map.put(key, t1);
        }
        log.addTimeLine("T1 агрегированы.");
        return new HashSet<>(t1Map.values());
    }

    private void update(Date date, Set<T1> t1Set, Set<Lgot> lgotSet, Log log) {
        log.sumUp("\tЗатрачено времени на перезапись: " + Util.measureTime(() -> {
            log.addTimeLine("Удаляю старые записи третьего уровня за " + Util.formatDate(date, "dd.MM.yyyy") + "...");
            level3.deleteForDate(date);
            log.addLine("Записываю T1...");
            t1Set.forEach(t1 -> level3.save(t1));
            log.addLine("Записываю Lgot...");
            lgotSet.forEach(lgot -> level3.save(lgot));
            log.addTimeLine("Записи успешно обновлены.");
        }) + "c");
    }

//    @PostConstruct
    public void test() {
        int yyyy = 2024, mm = 2, dd = 05;
        transform(new TransformationOptions(
                new Date(yyyy - 1900, mm - 1, dd),
//                null,
                false,
                true
        ));
    }
}