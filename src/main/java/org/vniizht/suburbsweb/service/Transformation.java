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

import javax.annotation.PostConstruct;
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

    public String transform(TransformationOptions options) {
        Log log = new Log();
        return log.sumUp("Итоговое время: " + Util.measureTime(() -> {
            if(!options.pass && !options.prig) return;

            log.addTimeLine("Выполняю трансформацию записей за " + Util.formatDate(options.date, "dd.MM.yyyy"));
            log.sumUp();

            log.addTimeLine("Загружаю справочники...");
            handbook.loadCache();
            log.addTimeLine("Справочники загружены.");
            log.sumUp();
            try { save(
                    options.prig ? transformPrig(options.date, log) : null,
                    options.pass ? transformPass(options.date, log) : null,
                    log
            );
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                handbook.clearCache();
                routes.clearCache();
            }
        }));
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
        log.addLine(
                "Затраты времени на трансформацию записей " + name + " (c): ",
                "\tТрансформация t1: "          + level3.getT1TransformationTime() +
                        " (включая поиск маршрутов: " + level3.getT1TripsSearchTime() + ")",
                "\tТрансформация lgot: "        + level3.getLgotTransformationTime()
        );
        return level3;
    }

    private void save(Level3Prig level3Prig,
                      Level3Pass level3Pass,
                      Log log) {

        Set<T1>    t1Set   = aggregateT1(
                Stream.concat(
                        level3Prig == null ? Stream.empty() : level3Prig.getT1List().stream(),
                        level3Pass == null ? Stream.empty() : level3Pass.getT1List().stream())
                        .collect(Collectors.toList()), log);

        Set<Lgot>  lgotSet = Stream.concat(
                level3Prig == null ? Stream.empty() : level3Prig.getLgotList().stream(),
                level3Pass == null ? Stream.empty() : level3Pass.getLgotList().stream())
                .collect(Collectors.toSet());

        log.addTimeLine("Итоговое количество T1: " + t1Set.size());
        log.addTimeLine("Итоговое количество Lgot: " + lgotSet.size());

        log.addTimeLine("Записываю результаты...");
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

    @PostConstruct
    public void test() {
        int yyyy = 2024, mm = 2, dd = 05;
        transform(new TransformationOptions(
                new Date(yyyy - 1900, mm - 1, dd),
                true,
                false
        ));
    }
}