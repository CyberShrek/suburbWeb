package org.vniizht.suburbsweb.service.result;

import lombok.Getter;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.util.Util;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

abstract public class Level3 <L2_RECORD extends Level2Dao.Record> {

    // Конечные результаты - трансформированные записи второго уровня, готовые к записи в базу данных
    @Getter private final List<T1>   t1List   = new ArrayList<>();
    @Getter private final List<Lgot> lgotList = new ArrayList<>();

    // Подсчитанное время на каждую операцию (с)
    @Getter private float t1TransformationTime   = 0;
    @Getter private float lgotTransformationTime = 0;
    @Getter private float t1TripsSearchTime      = 0;

    // Базовая конвертация
    abstract protected T1   convertToT1  (L2_RECORD record);
    abstract protected Lgot convertToLgot(L2_RECORD record, T1 t1);

    // Маршрутные поля
    abstract protected void addT1Trips(L2_RECORD record, T1 t1);

    // Мультипликаторы
    abstract protected Set<T1> multiplyT1(L2_RECORD record, T1 t1);

    protected final Set<L2_RECORD> records;
    protected final Handbook handbook;
    protected final RoutesDao routes;

    Level3(Set<L2_RECORD> records,
           Handbook handbook,
           RoutesDao routes) {
        this.records = records;
        this.handbook  = handbook;
        this.routes    = routes;
    }

    protected void transform() {
        for (L2_RECORD record : records) {
            T1 t1 = transformT1(record);
            t1List.addAll(multiplyT1(record, t1));
            if(t1.getKey().getP25() == '1')
                lgotList.add(transformLgot(record, t1));
        }
        roundTimes();
    }

    private T1 transformT1(L2_RECORD record) {
        AtomicReference<T1> t1 = new AtomicReference<>();
        t1TransformationTime += Util.measureTime(() -> {
            t1.set(convertToT1(record));
            t1TripsSearchTime += Util.measureTime(() -> addT1Trips(record, t1.get()));
        });
        return t1.get();
    }

    private Lgot transformLgot(L2_RECORD record, T1 t1) {
        AtomicReference<Lgot> lgot = new AtomicReference<>();
        lgotTransformationTime += Util.measureTime(() ->
            lgot.set(convertToLgot(record, t1))
        );
        return lgot.get();
    }

    private void roundTimes() {
        t1TransformationTime = (float) Math.round(t1TransformationTime * 100) / 100;
        lgotTransformationTime = (float) Math.round(lgotTransformationTime * 100) / 100;
        t1TripsSearchTime = (float) Math.round(t1TripsSearchTime * 100) / 100;
    }
}