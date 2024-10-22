package org.vniizht.suburbsweb.model.transformation.level3;

import lombok.Getter;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Routes;
import org.vniizht.suburbsweb.util.Util;

import java.util.Date;
import java.util.Set;

abstract class Level3 <T_Record extends Level2Data.Record> {

    // Базовая конвертация
    abstract protected T1   convertT1  ();
    abstract protected Lgot convertLgot(T1 t1);

    // Маршрутные поля
    abstract protected void addTrips(T1 t1);

    // Мультипликаторы
    abstract protected Set<T1> multiplyT1(T1 t1);

    // Конечные результаты
    @Getter private Set<T1> t1;
    @Getter private Lgot lgot;

    // Подсчитанное время на каждую операцию (мс)
    @Getter private float t1ConversionTime;
    @Getter private float lgotConversionTime;
    @Getter private float tripsCalculationTime;
    @Getter private float t1MultiplicationTime;
    @Getter private float totalTime;

    protected final T_Record record;
    protected final Handbook handbook;
    protected final Routes   routes;

    Level3(T_Record record,
           Handbook handbook,
           Routes   routes) {
        this.record   = record;
        this.handbook = handbook;
        this.routes   = routes;
    }

    protected void transform() {
        totalTime = Util.measureTime(() -> {
            T1 t1 = convertT1();
            tripsCalculationTime = Util.measureTime(() -> addTrips(t1));
            t1MultiplicationTime = Util.measureTime(() -> multiplyT1(t1));
            lgotConversionTime   = Util.measureTime(() -> convertLgot(t1));
        });
        t1ConversionTime = totalTime - tripsCalculationTime - t1MultiplicationTime - lgotConversionTime;
    }
}
