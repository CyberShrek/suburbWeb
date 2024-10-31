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

    // Детали общие
    abstract protected Integer getYyyymm(Level2Dao.Record record);
    abstract protected Date    getRequestDate(Level2Dao.Record record);

    // Детали T1
    abstract protected String    getT1P1(Level2Dao.Record record);
    abstract protected Integer   getT1P2(Level2Dao.Record record);
    abstract protected String    getT1P3(Level2Dao.Record record);
    abstract protected String    getT1P4(Level2Dao.Record record);
    abstract protected String    getT1P5(Level2Dao.Record record);
    abstract protected String    getT1P6(Level2Dao.Record record);
    abstract protected String    getT1P7(Level2Dao.Record record);
    abstract protected String    getT1P8(Level2Dao.Record record);
    abstract protected String    getT1P9(Level2Dao.Record record);
    abstract protected String    getT1P10(Level2Dao.Record record);
    abstract protected String    getT1P11(Level2Dao.Record record);
    abstract protected String    getT1P12(Level2Dao.Record record);
    abstract protected String    getT1P13(Level2Dao.Record record);
    abstract protected String    getT1P14(Level2Dao.Record record);
    abstract protected String    getT1P15(Level2Dao.Record record);
    abstract protected String    getT1P16(Level2Dao.Record record);
    abstract protected String    getT1P17(Level2Dao.Record record);
    abstract protected String    getT1P18(Level2Dao.Record record);
    abstract protected Character getT1P19(Level2Dao.Record record);
    abstract protected String    getT1P20(Level2Dao.Record record);
    abstract protected Character getT1P21(Level2Dao.Record record);
    abstract protected Character getT1P22(Level2Dao.Record record);
    abstract protected Character getT1P23(Level2Dao.Record record);
    abstract protected String    getT1P24(Level2Dao.Record record);
    abstract protected Character getT1P25(Level2Dao.Record record);
    abstract protected String    getT1P26(Level2Dao.Record record);
    abstract protected String    getT1P27(Level2Dao.Record record);
    abstract protected String    getT1P28(Level2Dao.Record record);
    abstract protected String    getT1P29(Level2Dao.Record record);
    abstract protected String    getT1P30(Level2Dao.Record record);
    abstract protected String    getT1P31(Level2Dao.Record record);
    abstract protected Short     getT1P32(Level2Dao.Record record);
    abstract protected Long      getT1P33(Level2Dao.Record record);
    abstract protected Long      getT1P34(Level2Dao.Record record);
    abstract protected Long      getT1P35(Level2Dao.Record record);
    abstract protected Long      getT1P36(Level2Dao.Record record);
    abstract protected Long      getT1P37(Level2Dao.Record record);
    abstract protected Long      getT1P38(Level2Dao.Record record);
    abstract protected Long      getT1P39(Level2Dao.Record record);
    abstract protected Long      getT1P40(Level2Dao.Record record);
    abstract protected Long      getT1P41(Level2Dao.Record record);
    abstract protected Long      getT1P42(Level2Dao.Record record);
    abstract protected Long      getT1P43(Level2Dao.Record record);
    abstract protected Long      getT1P44(Level2Dao.Record record);
    abstract protected Long      getT1P45(Level2Dao.Record record);
    abstract protected Long      getT1P46(Level2Dao.Record record);
    abstract protected Long      getT1P47(Level2Dao.Record record);
    abstract protected Long      getT1P48(Level2Dao.Record record);
    abstract protected Long      getT1P49(Level2Dao.Record record);
    abstract protected Long      getT1P50(Level2Dao.Record record);
    abstract protected Long      getT1P51(Level2Dao.Record record);
    abstract protected Character getT1P52(Level2Dao.Record record);
    abstract protected String    getT1P53(Level2Dao.Record record);
    abstract protected String    getT1P54(Level2Dao.Record record);
    abstract protected Character getT1P55(Level2Dao.Record record);
    abstract protected String    getT1P56(Level2Dao.Record record);
    abstract protected Character getT1P57(Level2Dao.Record record);
    abstract protected Character getT1P58(Level2Dao.Record record);
    abstract protected Character getT1P59(Level2Dao.Record record);
    abstract protected String    getT1P60(Level2Dao.Record record);
    abstract protected Character getT1P61(Level2Dao.Record record);
    abstract protected Short     getT1P62(Level2Dao.Record record);
    abstract protected Character getT1P63(Level2Dao.Record record);

    // Детали Lgot
    abstract protected String        getLgotList(Level2Dao.Record record);
    abstract protected Integer       getLgotP1(Level2Dao.Record record);
    abstract protected String        getLgotP2(Level2Dao.Record record);
    abstract protected String        getLgotP3(Level2Dao.Record record);
    abstract protected Character     getLgotP4(Level2Dao.Record record);
    abstract protected Character     getLgotP5(Level2Dao.Record record);
    abstract protected Character     getLgotP6(Level2Dao.Record record);
    abstract protected String        getLgotP7(Level2Dao.Record record);
    abstract protected String        getLgotP8(Level2Dao.Record record);
    abstract protected String        getLgotP9(Level2Dao.Record record);
    abstract protected String        getLgotP10(Level2Dao.Record record);
    abstract protected String        getLgotP11(Level2Dao.Record record);
    abstract protected String        getLgotP12(Level2Dao.Record record);
    abstract protected Character     getLgotP13(Level2Dao.Record record);
    abstract protected String        getLgotP14(Level2Dao.Record record);
    abstract protected String        getLgotP15(Level2Dao.Record record);
    abstract protected Byte          getLgotP16(Level2Dao.Record record);
    abstract protected Boolean       getLgotP17(Level2Dao.Record record);
    abstract protected Byte          getLgotP18(Level2Dao.Record record);
    abstract protected Short         getLgotP19(Level2Dao.Record record);
    abstract protected Character     getLgotP20(Level2Dao.Record record);
    abstract protected Short         getLgotP21(Level2Dao.Record record);
    abstract protected java.sql.Date getLgotP22(Level2Dao.Record record);
    abstract protected java.sql.Date getLgotP23(Level2Dao.Record record);
    abstract protected String        getLgotP24(Level2Dao.Record record);
    abstract protected String        getLgotP25(Level2Dao.Record record);
    abstract protected String        getLgotP26(Level2Dao.Record record);
    abstract protected Integer       getLgotP27(Level2Dao.Record record);
    abstract protected Integer       getLgotP28(Level2Dao.Record record);
    abstract protected String        getLgotP29(Level2Dao.Record record);
    abstract protected String        getLgotP30(Level2Dao.Record record);
    abstract protected String        getLgotP31(Level2Dao.Record record);
    abstract protected String        getLgotP32(Level2Dao.Record record);
    abstract protected Short         getLgotP33(Level2Dao.Record record);

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

    private T1 convertToT1(L2_RECORD record) {
        return T1.builder()
                .key(T1.Key.builder()
                        .yyyymm(getYyyymm(record))
                        .p1(getT1P1(record))
                        .p2(getT1P2(record))
                        .p3(getT1P3(record))
                        .p4(getT1P4(record))
                        .p5(getT1P5(record))
                        .p6(getT1P6(record))
                        .p7(getT1P7(record))
                        .p8(getT1P8(record))
                        .p9(getT1P9(record))
                        .p10(getT1P10(record))
                        .p11(getT1P11(record))
                        .p12(getT1P12(record))
                        .p13(getT1P13(record))
                        .p14(getT1P14(record))
                        .p15(getT1P15(record))
                        .p16(getT1P16(record))
                        .p17(getT1P17(record))
                        .p18(getT1P18(record))
                        .p19(getT1P19(record))
                        .p20(getT1P20(record))
                        .p21(getT1P21(record))
                        .p22(getT1P22(record))
                        .p23(getT1P23(record))
                        .p24(getT1P24(record))
                        .p25(getT1P25(record))
                        .p26(getT1P26(record))
                        .p27(getT1P27(record))
                        .p28(getT1P28(record))
                        .p29(getT1P29(record))
                        .p30(getT1P30(record))
                        .p31(getT1P31(record))
                        .p32(getT1P32(record))
                        .p52(getT1P52(record))
                        .p53(getT1P53(record))
                        .p54(getT1P54(record))
                        .p55(getT1P55(record))
                        .p56(getT1P56(record))
                        .p57(getT1P57(record))
                        .p58(getT1P58(record))
                        .p59(getT1P59(record))
                        .p60(getT1P60(record))
                        .p61(getT1P61(record))
                        .p62(getT1P62(record))
                        .p63(getT1P63(record))
                        .build()
                )
}