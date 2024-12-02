package org.vniizht.suburbsweb.service.result;

import lombok.Getter;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T2;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T3;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.entities.Route;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.util.Util;
import org.vniizht.suburbsweb.websocket.LogWS;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

abstract public class Level3 <L2_RECORD extends Level2Dao.Record> {

    // Конечные результаты - трансформированные записи второго уровня, готовые к записи в базу данных
    @Getter private final List<T1>   t1Result   = new ArrayList<>();
    @Getter private final List<Lgot> lgotResult = new ArrayList<>();

    // Подсчитанное время на каждую операцию (с)
    @Getter private float t1TransformationTime   = 0;
    @Getter private float t2TransformationTime   = 0;
    @Getter private float t3TransformationTime   = 0;
    @Getter private float t4TransformationTime   = 0;
//    @Getter private float t5TransformationTime   = 0;
    @Getter private float t6TransformationTime   = 0;
    @Getter private float lgotTransformationTime = 0;
    @Getter private float t1TripsSearchTime      = 0;

    // Функция обработки каждой записи второго уровня
    abstract protected void assignVariablesForEachRecord(L2_RECORD record);

    // Получение маршрутов для каждой записи
    abstract protected List<Route> getRoutes();

    // Мультипликатор
    abstract protected Set<T1> multiplyT1(T1 t1);

    // Детали общие
    abstract protected Integer getYyyymm();
    abstract protected Date    getRequestDate();

    // Проверка существования t1
    abstract protected boolean   t1Exists();

    // Компоненты T1
    abstract protected String    getT1P1();
             protected Long      getT1P2() {return null;}
    abstract protected String    getT1P3();
    abstract protected String    getT1P4();
    abstract protected String    getT1P5();
    abstract protected String    getT1P6();
    abstract protected String    getT1P7();
    abstract protected String    getT1P8();
    abstract protected String    getT1P9();
    abstract protected String    getT1P10();
    abstract protected String    getT1P11();
    abstract protected String    getT1P12();
             protected String    getT1P13(Route route) {return route.getRoadStart();}
             protected String    getT1P14(Route route) {return route.getDepartmentStart();}
    abstract protected String    getT1P15();
             protected String    getT1P16(Route route) {return route.getRegionStart();}
    abstract protected String    getT1P17();
    abstract protected String    getT1P18();
    abstract protected Character getT1P19();
    abstract protected String    getT1P20();
    abstract protected Character getT1P21();
    abstract protected Character getT1P22();
    abstract protected Character getT1P23();
    abstract protected String    getT1P24();
    abstract protected Character getT1P25();
    abstract protected String    getT1P26();
             protected String    getT1P27(Route route) {return route.getRoadEnd();}
             protected String    getT1P28(Route route) {return route.getDepartmentEnd();}
             protected String    getT1P29(Route route) {return route.getRegionEnd();}
    abstract protected String    getT1P30();
    abstract protected String    getT1P31();
    abstract protected Short     getT1P32();
    abstract protected Long      getT1P33();
    abstract protected Long      getT1P34();
    abstract protected Long      getT1P35();
    abstract protected Long      getT1P36();
    abstract protected Long      getT1P37();
    abstract protected Long      getT1P38();
    abstract protected Long      getT1P39();
    abstract protected Long      getT1P40();
    abstract protected Long      getT1P41();
    abstract protected Long      getT1P42();
    abstract protected Long      getT1P43();
    abstract protected Long      getT1P44();
    abstract protected Long      getT1P45();
    abstract protected Long      getT1P46();
    abstract protected Long      getT1P47();
    abstract protected Long      getT1P48();
    abstract protected Long      getT1P49();
    abstract protected Long      getT1P50();
    abstract protected Long      getT1P51();
    abstract protected Character getT1P52();
    abstract protected String    getT1P53();
    abstract protected String    getT1P54();
    abstract protected Character getT1P55();
    abstract protected String    getT1P56();
    abstract protected Character getT1P57();
    abstract protected Character getT1P58();
    abstract protected Character getT1P59();
    abstract protected String    getT1P60();
    abstract protected Character getT1P61();
             protected Short     getT1P62(Route route) {return route.getMcdDistance();}
             protected Character getT1P63(Route route) {return route.getMcd();}

    // Компоненты T2
    protected String    getT2P1()            {return "tab2";}
    protected String    getT2P2()            {return "017";}
    protected Integer   getT2P3()            {return null;}
    protected Short     getT2P4(Route route) {return route.getSerial();}
    protected String    getT2P5(Route route) {return route.getRoadStart();}
    protected String    getT2P6(Route route) {return route.getDepartmentStart();}
    protected Short     getT2P7(Route route) {return route.getDepartmentDistance();}

    // Компоненты T3
    protected String    getT3P1()            {return "tab3";}
    protected String    getT3P2()            {return "017";}
    protected Integer   getT3P3()            {return null;}
    protected Short     getT3P4(Route route) {return route.getSerial();}
    protected String    getT3P5(Route route) {return route.getRegionStart();}
    protected String    getT3P5(T1 t1)       {return t1.getKey().getP17();}
    protected Short     getT3P6(Route route) {return route.getRegionDistance();}

    // Компоненты T4
    protected String    getT4P1()            {return "tab4";}
    protected String    getT4P2()            {return "017";}
    protected Integer   getT4P3()            {return null;}
    protected Short     getT4P4(Route route) {return route.getSerial();}
    protected String    getT4P5(Route route) {return route.getRoadStart();}
    protected String    getT4P6(T1 t1)       {return t1.getKey().getP17();}
    protected Long      getT4P7(Route route) {return 0L;}
    protected Long      getT4P8(Route route) {return 0L;}
    protected Short     getT4P9(Route route) {return 0;}

    // Компоненты T6
    protected String    getT6P1()            {return "tab6";}
    protected String    getT6P2()            {return "017";}
    protected Integer   getT6P3()            {return null;}
    protected Short     getT6P4(Route route) {return route.getSerial();}
    protected String    getT6P5(Route route) {return route.getRoadStart();}
    protected Short     getT6P6(Route route) {return route.getDcs();}
    protected Short     getT6P7(Route route) {return route.getDcsDistance();}

    // Проверка существования льготы
    abstract protected boolean   lgotExists();

    // Компоненты Lgot
    abstract protected String        getLgotList();
             protected Integer       getLgotP1() {return null;}
    abstract protected String        getLgotP2();
    abstract protected String        getLgotP3();
    abstract protected Character     getLgotP4();
    abstract protected Character     getLgotP5();
    abstract protected Character     getLgotP6();
    abstract protected String        getLgotP7();
    abstract protected String        getLgotP8();
    abstract protected String        getLgotP9();
    abstract protected String        getLgotP10();
    abstract protected String        getLgotP11();
    abstract protected String        getLgotP12();
    abstract protected Character     getLgotP13();
    abstract protected String        getLgotP14();
    abstract protected String        getLgotP15();
    abstract protected Byte          getLgotP16();
    abstract protected Boolean       getLgotP17();
    abstract protected Byte          getLgotP18();
    abstract protected Short         getLgotP19();
    abstract protected Character     getLgotP20();
    abstract protected Short         getLgotP21();
    abstract protected java.sql.Date getLgotP22();
    abstract protected java.sql.Date getLgotP23();
    abstract protected String        getLgotP24();
    abstract protected String        getLgotP25();
    abstract protected String        getLgotP26();
    abstract protected Double        getLgotP27();
    abstract protected Double        getLgotP28();
    abstract protected String        getLgotP29();
    abstract protected String        getLgotP30();
    abstract protected String        getLgotP31();
    abstract protected String        getLgotP32();
    abstract protected Short         getLgotP33();

    protected final Set<L2_RECORD> records;
    protected final Handbook handbook;
    protected final RoutesDao routesDao;

    protected Level3(Set<L2_RECORD> records,
           Handbook handbook,
           RoutesDao routesDao) {
        this.records = records;
        this.handbook  = handbook;
        this.routesDao = routesDao;
    }

    protected void transform() {
        int progress = 0;
        for (L2_RECORD record : records) {
            assignVariablesForEachRecord(record);
            if(t1Exists()) {
                List<Route> routes = getRoutes();
                T1 t1              = getT1(routes);
                t1Result.addAll(multiplyT1(t1));
            }
            if(lgotExists()) {
                lgotResult.add(getLgot());
            }
            progress++;
            LogWS.spreadProgress((int) ((float) progress / records.size() * 100));
        }
        roundTimes();
    }

    private T1 getT1(List<Route> routes) {
        AtomicReference<T1> t1 = new AtomicReference<>();
        t1TransformationTime += Util.measureTime(() -> {
            t1.set(buildT1());
            t1TripsSearchTime += Util.measureTime(() -> addTripsToT1(t1.get(), routes));
        });
        return t1.get();
    }

    private T2 getT2() {
        AtomicReference<T2> t2 = new AtomicReference<>();
        t2TransformationTime += Util.measureTime(() ->
            t2.set(convertToT2())
        );
        return t2.get();
    }

//    private T3 getT3() {
//        AtomicReference<T3> t3 = new AtomicReference<>();
//        t3TransformationTime += Util.measureTime(() ->
//            t3.set(convertToT3())
//        );
//        return t3.get();
//    }

    private Lgot getLgot() {
        AtomicReference<Lgot> lgot = new AtomicReference<>();
        lgotTransformationTime += Util.measureTime(() ->
            lgot.set(buildLgot())
        );
        return lgot.get();
    }

    private void roundTimes() {
        t1TransformationTime   = (float) Math.round(t1TransformationTime   * 100) / 100;
        lgotTransformationTime = (float) Math.round(lgotTransformationTime * 100) / 100;
        t1TripsSearchTime      = (float) Math.round(t1TripsSearchTime      * 100) / 100;
    }

    private T1 buildT1() {
        return T1.builder()
                .key(T1.Key.builder()
                        .requestDate(getRequestDate())
                        .yyyymm(getYyyymm())
                        .p1(getT1P1())
                        .p2(getT1P2())
                        .p3(getT1P3())
                        .p4(getT1P4())
                        .p5(getT1P5())
                        .p6(getT1P6())
                        .p7(getT1P7())
                        .p8(getT1P8())
                        .p9(getT1P9())
                        .p10(getT1P10())
                        .p11(getT1P11())
                        .p12(getT1P12())
                        .p15(getT1P15())
                        .p17(getT1P17())
                        .p18(getT1P18())
                        .p19(getT1P19())
                        .p20(getT1P20())
                        .p21(getT1P21())
                        .p22(getT1P22())
                        .p23(getT1P23())
                        .p24(getT1P24())
                        .p25(getT1P25())
                        .p26(getT1P26())
                        .p30(getT1P30())
                        .p31(getT1P31())
                        .p32(getT1P32())
                        .p52(getT1P52())
                        .p53(getT1P53())
                        .p54(getT1P54())
                        .p55(getT1P55())
                        .p56(getT1P56())
                        .p57(getT1P57())
                        .p58(getT1P58())
                        .p59(getT1P59())
                        .p60(getT1P60())
                        .p61(getT1P61())
                        .build()
                )
                .p33(getT1P33())
                .p34(getT1P34())
                .p35(getT1P35())
                .p36(getT1P36())
                .p37(getT1P37())
                .p38(getT1P38())
                .p39(getT1P39())
                .p40(getT1P40())
                .p41(getT1P41())
                .p42(getT1P42())
                .p43(getT1P43())
                .p44(getT1P44())
                .p45(getT1P45())
                .p46(getT1P46())
                .p47(getT1P47())
                .p48(getT1P48())
                .p49(getT1P49())
                .p50(getT1P50())
                .p51(getT1P51())
                .build();
    }
    private void addTripsToT1(T1 t1, List<Route> routes) {
        T1.Key key = t1.getKey();
        key.setRoutes(routes.stream().mapToInt(Route::getNum).toArray());

        Route route = routes.get(0);
        if(route != null) {
            key = key.toBuilder()
                    .p13(getT1P13(route))
                    .p14(getT1P14(route))
                    .p16(getT1P16(route))
                    .p27(getT1P27(route))
                    .p28(getT1P28(route))
                    .p29(getT1P29(route))
                    .p62(getT1P62(route))
                    .p63(getT1P63(route))
                    .build();
        }

        t1.setKey(key);
    }

    private T2 convertToT2() {
        return T2.builder()
                .p1(getT2P1())
                .p2(getT2P2())
                .p3(getT2P3())
                .p4(getT2P4())
                .p5(getT2P5())
                .p6(getT2P6())
                .build();
    }

    private Lgot buildLgot() {
        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .requestDate(getRequestDate())
                        .yyyymm(getYyyymm())
                        .list(getLgotList())
//                        .p1(getLgotP1())
                        .p2(getLgotP2())
                        .p3(getLgotP3())
                        .p4(getLgotP4())
                        .p5(getLgotP5())
                        .p6(getLgotP6())
                        .p7(getLgotP7())
                        .p8(getLgotP8())
                        .p9(getLgotP9())
                        .p10(getLgotP10())
                        .p11(getLgotP11())
                        .p12(getLgotP12())
                        .p13(getLgotP13())
                        .p14(getLgotP14())
                        .p15(getLgotP15())
                        .p16(getLgotP16())
                        .p17(getLgotP17())
                        .p18(getLgotP18())
                        // values
                        .p20(getLgotP20())
                        .p21(getLgotP21())
                        .p22(getLgotP22())
                        .p23(getLgotP23())
                        .p24(getLgotP24())
                        .p25(getLgotP25())
                        .p26(getLgotP26())
                        // values
                        .p29(getLgotP29())
                        .p30(getLgotP30())
                        .p31(getLgotP31())
                        .p32(getLgotP32())
                        // values
                        .build()
                )
                .p19(getLgotP19())
                .p27(Math.ceil(getLgotP27() * 100) / 100)
                .p28(Math.ceil(getLgotP28() * 100) / 100)
                .p33(getLgotP33())
                .build();
    }
}