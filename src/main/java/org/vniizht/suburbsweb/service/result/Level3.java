package org.vniizht.suburbsweb.service.result;

import lombok.Getter;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.*;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.entities.level3.meta.CO22Meta;
import org.vniizht.suburbsweb.service.data.entities.routes.*;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.util.Util;
import org.vniizht.suburbsweb.websocket.LogWS;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

abstract public class Level3 <L2_RECORD extends Level2Dao.Record> {

    // Конечные результаты - трансформированные записи второго уровня, готовые к записи в базу данных
    @Getter private final Map<String, CO22> co22Result = new HashMap<>();
    @Getter private final List<Lgot>        lgotResult = new ArrayList<>();

    // Подсчитанное время на сопутствующие операции (с)
    @Getter private float transformationTime   = 0;
    @Getter private float routesSearchTime     = 0;

    // Функция обработки каждой записи второго уровня
    abstract protected void assignVariablesForRecord(L2_RECORD record);

    // Мультипликатор
    abstract protected Set<T1> multiplyT1(T1 t1);

    // Детали общие и метаданные
    abstract protected Integer    getYyyyMM();
    abstract protected Date       getRequestDate();
    abstract protected RouteGroup getRouteGroup();
    abstract protected CO22Meta getMeta();

    // Проверка существования t1
    abstract protected boolean   t1Exists();

    // Компоненты T1
             protected String    getT1P1() {return "tab1";}
             protected Long      getT1P2() {return null;}
    abstract protected String    getT1P3();
    abstract protected String    getT1P4();
             protected String    getT1P5() {return "017";}
    abstract protected String    getT1P6();
             protected String    getT1P7() {return getT1P6();}
    abstract protected String    getT1P8();
    abstract protected String    getT1P9();
    abstract protected String    getT1P10();
    abstract protected String    getT1P11();
    abstract protected String    getT1P12();
             protected String    getT1P13(RoadRoute route)       {return route != null ? route.getRoad() : null;}
             protected String    getT1P14(DepartmentRoute route) {return route != null ? route.getDepartment() : null;}
    abstract protected String    getT1P15();
             protected String    getT1P16(RegionRoute route)     {return route != null ? route.getRegion() : null;}
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
             protected String    getT1P27(RoadRoute route)       {return route != null ? route.getRoad() : null;}
             protected String    getT1P28(DepartmentRoute route) {return route != null ? route.getDepartment() : null;}
             protected String    getT1P29(RegionRoute route)     {return route != null ? route.getRegion() : null;}
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
             protected Short     getT1P62(List<McdRoute> routes) {
                 return (short) routes.stream().mapToInt(McdRoute::getDistance).sum();
             }
             protected Character getT1P63(List<McdRoute> routes) {
                 if (routes.stream().anyMatch(mcdRoute -> mcdRoute.getCode() == '1')) {
                     switch (routes.size()) {
                         case 1:  return '1';
                         case 2:  return routes.get(0).getCode() == 0 ? '2' : '3';
                         default: return '4';
                     }
                 }
                 return '0';
             }

    // Проверка существования льготы
    abstract protected boolean lgotExists();

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
    abstract protected String        getLgotP34();
    abstract protected String        getLgotP35();

    // Подсчёт средних стоимостей на километр по регионам
    abstract protected double getRegionIncomePerKm(String region);
    abstract protected double getRegionOutcomePerKm(String region);

    protected final Set<L2_RECORD> records;
    protected final Handbook handbook;
    protected final RoutesDao routesDao;
    private Long t1Serial;

    protected Level3(
            Set<L2_RECORD> records,
            Handbook handbook,
            RoutesDao routesDao,
            Long initialT1Serial) {
        this.records   = records;
        this.handbook  = handbook;
        this.routesDao = routesDao;
        this.t1Serial  = initialT1Serial;
    }

    protected void transform() {
        int progress = 0;
        for (L2_RECORD record : records) {
            transformRecord(record);
            progress++;
            LogWS.spreadProgress((int) ((float) progress / records.size() * 100));
        }
        assignSerials();
        roundTimes();
    }

    private void transformRecord(L2_RECORD record) {
        assignVariablesForRecord(record);
        if(t1Exists()) {
            AtomicReference<RouteGroup> routeGroup = new AtomicReference<>();
            routesSearchTime   += Util.measureTime(() -> routeGroup.set(getRouteGroup()));
            transformationTime += Util.measureTime(() -> {
                Set<T1> t1Set = multiplyT1(buildT1(routeGroup.get()));
                t1Set.forEach(t1 -> {
                    String key = t1.getKey().toString();
                    CO22 co22 = new CO22(t1, routeGroup.get());
                    if(co22Result.containsKey(key))
                        co22Result.get(key).merge(co22);
                    else
                        co22Result.put(key, co22);
                });
            });
        }
        if(lgotExists()) {
            transformationTime += Util.measureTime(() -> lgotResult.add(buildLgot()));
        }
    }

    private void assignSerials() {
        co22Result.values().forEach(CO22::assignSerials);
    }

    private void roundTimes() {
        transformationTime = (float) Math.round(transformationTime * 100) / 100;
        routesSearchTime   = (float) Math.round(routesSearchTime   * 100) / 100;
    }

    private T1 buildT1(RouteGroup routeGroup) {
        T1 t1 = T1.builder()
                .key(T1.Key.builder()
                        .requestDate(getRequestDate())
                        .yyyymm(getYyyyMM())
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

        t1.setKey(t1.getKey().toBuilder()
                .p13(getT1P13(routeGroup.getFirstRoadRoute()))
                .p14(getT1P14(routeGroup.getFirstDepartmentRoute()))
                .p16(getT1P16(routeGroup.getFirstRegionRoute()))
                .p27(getT1P27(routeGroup.getLastRoadRoute()))
                .p28(getT1P28(routeGroup.getLastDepartmentRoute()))
                .p29(getT1P29(routeGroup.getLastRegionRoute()))
                .p62(getT1P62(routeGroup.getMcdRoutes()))
                .p63(getT1P63(routeGroup.getMcdRoutes()))
                .build());

        return t1;
    }

    private T2 buildT2(DepartmentRoute route) {
        return T2.builder()
                .requestDate(getRequestDate())
                .p1("tab2")
                .p2("017")
                .p3(null)
                .p4(route.getSerial())
                .p5(Util.addLeadingZeros(route.getRoad(), 3))
                .p6(route.getDepartment())
                .p7(route.getDistance())
                .build();
    }

    private T3 buildT3(RegionRoute route) {
        return T3.builder()
                .requestDate(getRequestDate())
                .p1("tab3")
                .p2("017")
                .p3(null)
                .p4(route.getSerial())
                .p5(route.getRegion())
                .p6(route.getOkato())
                .p7(route.getDistance())
                .build();
    }

    private T4 buildT4(FollowRoute route, boolean hasCosts) {
        return T4.builder()
                .requestDate(getRequestDate())
                .p1("tab4")
                .p2("017")
                .p3(null)
                .p4(route.getSerial())
                .p5(Util.addLeadingZeros(route.getRoad(), 3))
                .p6(route.getOkato())
                .p7(hasCosts ? (long) (route.getDistance() * getRegionIncomePerKm(route.getRegion())) : 0)
                .p8(hasCosts ? (long) (route.getDistance() * getRegionOutcomePerKm(route.getRegion())) : 0)
                .p9(route.getDistance())
                .build();
    }

    private T6 buildT6(DcsRoute route) {
        return T6.builder()
                .requestDate(getRequestDate())
                .p1("tab6")
                .p2("017")
                .p3(null)
                .p4(route.getSerial())
                .p5(Util.addLeadingZeros(route.getRoad(), 3))
                .p6(Integer.valueOf(route.getDcs()))
                .p7(route.getDistance())
                .build();
    }

    private Lgot buildLgot() {
        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .requestDate(getRequestDate())
                        .yyyymm(getYyyyMM())
                        .list(getLgotList())
                        .p1(getLgotP1())
                        .p2(getLgotP2())
                        .p3(getLgotP3())
                        .p4(getLgotP4())
                        .p5(getLgotP5())
                        .p6(getLgotP6())
                        .p7(getLgotP7())
                        .p8(Util.addLeadingZeros(getLgotP8(), 4))
                        .p9(getLgotP9())
                        .p10(getLgotP10())
                        .p11(Util.addLeadingZeros(getLgotP11(), 5))
                        .p12(Util.addLeadingZeros(getLgotP12(), 10))
                        .p13(getLgotP13() == null ? '0' : getLgotP13())
                        .p14(getLgotP14())
                        .p15(getLgotP15())
                        .p16(getLgotP16())
                        .p17(getLgotP17())
                        .p18(getLgotP18())
                        .p20(getLgotP20())
                        .p21(getLgotP21())
                        .p22(getLgotP22())
                        .p23(getLgotP23())
                        .p24(getLgotP24())
                        .p25(getLgotP25())
                        .p26(getLgotP26())
                        .p29(getLgotP29())
                        .p30(getLgotP30())
                        .p31(getLgotP31())
                        .p32(getLgotP32())
                        .build()
                )
                .p19(getLgotP19())
                .p27(Math.ceil(getLgotP27() * 100) / 100)
                .p28(Math.ceil(getLgotP28() * 100) / 100)
                .p33(getLgotP33())
                .build();
    }

    // ЦО22 включая все дочерние записи
    @Getter public class CO22 {
        private final T1 t1;
        private final List<T2>       t2 = new ArrayList<>();
        private final List<T3>       t3 = new ArrayList<>();
        private final List<T4>       t4 = new ArrayList<>();
        private final List<T6>       t6 = new ArrayList<>();
        private final Set<CO22Meta> metas = new HashSet<>();

        CO22(T1 t1, RouteGroup routeGroup) {
            boolean hasCosts = String.valueOf(t1.getKey().getYyyymm()).equals(getT1P3() + getT1P4());
            this.t1 = t1;
            routeGroup.getDepartmentRoutes().forEach(route   -> t2.add(buildT2(route)));
            routeGroup.getRegionRoutes().forEach(regionRoute -> t3.add(buildT3(regionRoute)));
            routeGroup.getFollowRoutes().forEach(route       -> t4.add(buildT4(route, hasCosts)));
            routeGroup.getDcsRoutes().forEach(route          -> t6.add(buildT6(route)));
            metas.add(getMeta());

            if(hasCosts)
                arrangeCosts();
        }

        public void merge(CO22 co22) {
            // Агрегация
            t1.merge(co22.t1);
            t2.addAll(co22.t2);
            t3.addAll(co22.t3);
            t4.addAll(co22.t4);
            t6.addAll(co22.t6);
            metas.addAll(co22.metas);
        }

        void assignSerials() {
            t1.getKey().setP2(t1Serial);
            t2.forEach(t -> t.setP3(t1Serial));
            t3.forEach(t -> t.setP3(t1Serial));
            t4.forEach(t -> t.setP3(t1Serial));
            t6.forEach(t -> t.setP3(t1Serial));
            metas.forEach(t -> t.setT1id(t1Serial));
            t1Serial++;
        }

        private void arrangeCosts() {
            if (t4.isEmpty()) return;
            long incomeDelta = t1.getP36() - t4.stream().mapToLong(T4::getP7).sum();
            long outcomeDelta = t1.getP44() - t4.stream().mapToLong(T4::getP8).sum();
            T4 lastT4 = t4.get(t4.size() - 1);
            lastT4.setP7(lastT4.getP7() + incomeDelta);
            lastT4.setP8(lastT4.getP8() + outcomeDelta);
        }
    }
}