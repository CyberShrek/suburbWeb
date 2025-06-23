package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.ng_logger.NgLogger;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.service.data.dao.TripsDao;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.*;
import org.vniizht.suburbsweb.service.data.entities.level3.meta.CO22Meta;
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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private Level2Dao level2;
    @Autowired private Level3Dao level3;
    @Autowired private Handbook  handbook;
    @Autowired private RoutesDao routes;
    @Autowired private TripsDao  trips;

    @Autowired private NgLogger ngLogger;

    private Log log;

    public synchronized void transform(TransformationOptions options) throws Exception {

        Date startTime = new Date();
        log = new Log(ngLogger);
        try {
            if(!options.pass && !options.prig) {
                options.pass = true;
                options.prig = true;
            }
            if(options.date == null) {
                options.date = getRequestDate();
                if(options.date == null) return;
            }

            log.addTimeLine("Выполняю трансформацию записей за "
                    + Util.formatDate(options.date, "dd.MM.yyyy"));
            log.sumUp((options.prig ? " l2_prig" : "") + (options.pass ? " l2_pass" : ""));

            calculatePortionSize(level2.findPrigRecord(options.date));
//            calculatePortionSize(level2.findPassRecord(options.date));

//            log.addTimeLine("Получаю справочники...");
//            LogWS.spreadProgress(0);
//            handbook.loadCache();
//            log.addTimeLine("Удаляю старые записи третьего уровня за " + Util.formatDate(options.date, "dd.MM.yyyy") + "...");
//            LogWS.spreadProgress(0);
//            level3.deleteForDate(options.date);
//            log.sumUp();
//            if (options.prig) complete(transformPrigOrNull(options.date));
//            if (options.pass) complete(transformPassOrNull(options.date));
//            log.addTimeLine("Трансформация завершена успешно.");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw e;
        } finally {
            handbook.clearCache();
            routes.clearCache();
            LogWS.spreadProgress(-1);
            log.finish("Итоговое время выполнения: " + (new Date().getTime() - startTime.getTime()) / 1000 + "с");
        }
    }

    private int calculatePortionSize(Level2Dao.Record estimatedRecord) throws IOException {
        log.addTimeLine("Вычисляю размер порции...");
        long availableMemoryInMB = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        log.addTimeLine("Доступная память: " + availableMemoryInMB + "MB");
        long estimatedRecordSizeInB = Util.getObjectSizeInBytes(estimatedRecord);
        log.addTimeLine("Расчетная размерность записи: " + estimatedRecordSizeInB + "B");
        int portionSize = (int) (availableMemoryInMB / estimatedRecordSizeInB / 1024 / 2);
        log.addTimeLine("Доступная память / расчетная размерность записи / 2 = размер порции = " + portionSize);
        return portionSize;
    }

    private Level3Prig transformPrigOrNull(Date requestDate) throws Exception {
        Set<Level2Dao.PrigRecord> records = loadRecords(
                () -> level2.findPrigRecords(requestDate), "l2_prig");

        return records.isEmpty() ? null : (Level3Prig) transform(
                () -> new Level3Prig(records, handbook, routes, trips, level3.getLatestT1P2() + 1),
                "l2_prig"
        );
    }

    private Level3Pass transformPassOrNull(Date requestDate) throws Exception {
        Set<Level2Dao.PassRecord> records = loadRecords(
                () -> level2.findPassRecords(requestDate), "l2_pass");

        return records.isEmpty() ? null : (Level3Pass) transform(
                () -> new Level3Pass(records, handbook, routes, level3.getLatestT1P2() + 1),
                 "l2_pass"
        );
    }

    private Set loadRecords(Callable<Set> loader, String name) throws Exception {
        log.addTimeLine("Ищу записи " + name + "...");
        LogWS.spreadProgress(0);
        Set<Level2Dao.Record> records = loader.call();
        log.addTimeLine("Записи " + name + " успешно получены. Количество " + name + "_main: " + records.size());

        return records;
    }

    private Level3 transform(Callable<Level3> loader, String name) {
        log.addTimeLine("Трансформирую записи " + name + "...");
        Level3 level3 = null;
        try {
            level3 = loader.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.addTimeLine("Записи " + name + " успешно трансформированы.");

        return level3;
    }

    private Date getRequestDate() {
        log.addTimeLine("Определяю дату запроса...");
        LogWS.spreadProgress(0);
        Date requestDate = level3.getNextRequestDate();
        if(requestDate == null) {
            log.addTimeLine("На третьем уровне ещё нет данных. Повторите запрос с указанием даты.");
        }
        return requestDate;
    }

    private void complete(Level3 nullableLevel3) {

        if (nullableLevel3 == null) {
            log.sumUp("Нет данных для формирования ЦО-22.");
            return;
        }
        Set<Level3.CO22>       co22Set = new HashSet<>();
        co22Set.addAll(nullableLevel3.getCo22Result().values());

        log.sumUp("Сформировано записей ЦО-22:      " + co22Set.size(),
                "Сформировано записей Льготников: " + nullableLevel3.getLgotResult().size());

        update(co22Set, nullableLevel3.getLgotResult());
    }

    private void update(Set<Level3.CO22> co22Set, Set<Lgot> lgotSet) {
        log.sumUp("\tЗатрачено времени на перезапись: " + Util.measureTime(() -> {
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
            log.addTimeLine("Записываю метаданные ЦО-22...");
            Set<CO22Meta> co22MetaSet = new HashSet<>();
            co22Set.forEach(co22 -> co22MetaSet.addAll(co22.getMetas()));
            level3.saveCO22Metas(co22MetaSet);
            log.addTimeLine("Записываю льготников...");
            level3.saveLgots(lgotSet);
        }) + "c");
    }

//    @PostConstruct
    public void test() throws Exception {
        int yyyy = 2024, mm = 2, dd = 9;
        transform(new TransformationOptions(
                new Date(yyyy - 1900, mm - 1, dd),
                false,
                true
        ));
    }
}