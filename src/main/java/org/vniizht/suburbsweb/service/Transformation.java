package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.service.data.dao.TripsDao;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.*;
import org.vniizht.suburbsweb.service.result.Level3;
import org.vniizht.suburbsweb.service.result.Level3Pass;
import org.vniizht.suburbsweb.service.result.Level3Prig;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.Level3Dao;
import org.vniizht.suburbsweb.util.Log;
import org.vniizht.suburbsweb.util.Util;
import org.vniizht.suburbsweb.websocket.LogWS;

import javax.annotation.PostConstruct;
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
            try {
                if(!options.pass && !options.prig) return;
                if(options.date == null) {
                    options.date = getRequestDate(log);
                    if(options.date == null) return;
                }

                log.addTimeLine("Выполняю трансформацию записей за "
                        + Util.formatDate(options.date, "dd.MM.yyyy"));
                log.sumUp((options.prig ? " l2_prig" : "") + (options.pass ? " l2_pass" : ""));

                log.addTimeLine("Получаю справочники...");
                LogWS.spreadProgress(0);
                handbook.loadCache();
                log.addTimeLine("Справочники загружены.");
                log.sumUp();
                complete(
                        options.date,
                        options.prig ? transformPrig(options.date, log) : null,
                        options.pass ? transformPass(options.date, log) : null,
                        log
                );
            } catch (Exception e) {
                log.sumUp(e.getLocalizedMessage());
                LogWS.spreadProgress(-1);
                e.printStackTrace();
            } finally {
                handbook.clearCache();
                routes.clearCache();
                LogWS.spreadProgress(-1);
            }
        }) + "c");
    }

    private Level3Prig transformPrig(Date requestDate, Log log) throws Exception {
        Set<Level2Dao.PrigRecord> records = loadRecords(
                () -> level2.findPrigRecords(requestDate), log, "l2_prig");

        return (Level3Prig) transform(
                () -> new Level3Prig(records, handbook, routes, trips, level3.getLatestT1P2() + 1),
                log, "l2_prig"
        );
    }

    private Level3Pass transformPass(Date requestDate, Log log) throws Exception {
        Set<Level2Dao.PassRecord> records = loadRecords(
                () -> level2.findPassRecords(requestDate), log, "l2_pass");

        return (Level3Pass) transform(
                () -> new Level3Pass(records, handbook, routes, level3.getLatestT1P2() + 1),
                log, "l2_pass"
        );
    }

    private Set loadRecords(Callable<Set> loader, Log log, String name) throws Exception {
        log.addTimeLine("Ищу записи " + name + "...");
        LogWS.spreadProgress(0);
        Set<Level2Dao.Record> records = loader.call();
        log.addTimeLine("Записи " + name + " успешно получены. Количество " + name + "_main: " + records.size());

        return records;
    }

    private Level3 transform(Callable<Level3> loader, Log log, String name) throws Exception {
        log.addTimeLine("Трансформирую записи " + name + "...");
        Level3 level3 = loader.call();
        log.addTimeLine("Записи " + name + " успешно трансформированы.");

        return level3;
    }

    private Date getRequestDate(Log log) {
        log.addTimeLine("Определяю дату запроса...");
        LogWS.spreadProgress(0);
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

        Set<Level3.CO22>    co22Set   = aggregateCO22(
                level3Prig == null ? new HashMap<>() : level3Prig.getCo22Result(),
                level3Pass == null ? new HashMap<>() : level3Pass.getCo22Result());

        Set<Lgot>  lgotSet = Stream.concat(
                level3Prig == null ? Stream.empty() : level3Prig.getLgotResult().stream(),
                level3Pass == null ? Stream.empty() : level3Pass.getLgotResult().stream())
                .collect(Collectors.toSet());

        log.sumUp("Сформировано записей ЦО-22:      " + co22Set.size(),
                "Сформировано записей Льготников: " + lgotSet.size());

        update(date, co22Set, lgotSet, log);
    }

    // TODO refactor
    private Set<Level3.CO22> aggregateCO22(Map<String, Level3Prig.CO22> prigMap,
                                           Map<String, Level3Pass.CO22> passMap)
    {
        Set<Level3.CO22> co22Set = new HashSet<>();
        prigMap.forEach((co22Key, co22) -> {
            co22Set.add(co22);
        });
        passMap.forEach((co22Key, co22) -> {
            co22Set.add(co22);
        });
        return co22Set;
    }

    private void update(Date date, Set<Level3.CO22> co22Set, Set<Lgot> lgotSet, Log log) {
        log.sumUp("\tЗатрачено времени на перезапись: " + Util.measureTime(() -> {
            log.addTimeLine("Удаляю старые записи третьего уровня за " + Util.formatDate(date, "dd.MM.yyyy") + "...");
            LogWS.spreadProgress(0);
            level3.deleteForDate(date);
            log.addTimeLine("Записываю T1...");
            level3.saveT1s(co22Set.stream().map(Level3.CO22::getT1).collect(Collectors.toSet()));
            log.addTimeLine("Записываю T2...");
            Set<T2> t2Set = new HashSet<>();
            co22Set.forEach(co22 -> t2Set.addAll(co22.getT2()));
            level3.saveT2s(t2Set);
            log.addTimeLine("Записываю T3...");
            Set<T3> t3Set = new HashSet<>();
            co22Set.forEach(co22 -> t3Set.addAll(co22.getT3()));
            level3.saveT3s(t3Set);
            log.addTimeLine("Записываю T4...");
            Set<T4> t4Set = new HashSet<>();
            co22Set.forEach(co22 -> t4Set.addAll(co22.getT4()));
            level3.saveT4s(t4Set);
            log.addTimeLine("Записываю T6...");
            Set<T6> t6Set = new HashSet<>();
            co22Set.forEach(co22 -> t6Set.addAll(co22.getT6()));
            level3.saveT6s(t6Set);
            log.addTimeLine("Записываю Lgot...");
            level3.saveLgots(lgotSet);
            log.addTimeLine("Записи успешно обновлены.");
        }) + "c");
    }

    @PostConstruct
    public void test() {
        int yyyy = 2024, mm = 02, dd = 05;
        transform(new TransformationOptions(
                new Date(yyyy - 1900, mm - 1, dd),
                true,
                false
        ));
    }
}