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
    @Getter private final List<T1>   t1Result   = new ArrayList<>();
    @Getter private final List<Lgot> lgotResult = new ArrayList<>();

    // Подсчитанное время на каждую операцию (с)
    @Getter private float t1TransformationTime   = 0;
    @Getter private float lgotTransformationTime = 0;
    @Getter private float t1TripsSearchTime      = 0;

    // Функция обработки каждой записи второго уровня
    abstract protected void assignVariablesForEachRecord(L2_RECORD record);

    // Мультипликатор
    abstract protected Set<T1> multiplyT1(T1 t1);

    //
//    abstract protected Route

    // Детали общие
    abstract protected Integer getYyyymm();
    abstract protected Date    getRequestDate();

    // Детали T1
    abstract protected String    getT1P1();
    abstract protected Integer   getT1P2();
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
    abstract protected String    getT1P13();
    abstract protected String    getT1P14();
    abstract protected String    getT1P15();
    abstract protected String    getT1P16();
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
    abstract protected String    getT1P27();
    abstract protected String    getT1P28();
    abstract protected String    getT1P29();
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
    abstract protected Short     getT1P62();
    abstract protected Character getT1P63();

    // Детали Lgot
    abstract protected String        getLgotList();
    abstract protected Integer       getLgotP1();
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
    abstract protected Integer       getLgotP27();
    abstract protected Integer       getLgotP28();
    abstract protected String        getLgotP29();
    abstract protected String        getLgotP30();
    abstract protected String        getLgotP31();
    abstract protected String        getLgotP32();
    abstract protected Short         getLgotP33();

    protected final Set<L2_RECORD> records;
    protected final Handbook handbook;
    protected final RoutesDao routes;

    protected Level3(Set<L2_RECORD> records,
           Handbook handbook,
           RoutesDao routes) {
        this.records = records;
        this.handbook  = handbook;
        this.routes    = routes;
    }

    protected void transform() {
        for (L2_RECORD record : records) {
            assignVariablesForEachRecord(record);
            T1 t1 = getT1();
            this.t1Result.addAll(multiplyT1(t1));
            if(t1.getKey().getP25() == '1')
                lgotResult.add(getLgot());
        }
        roundTimes();
    }

    private T1 getT1() {
        AtomicReference<T1> t1 = new AtomicReference<>();
        t1TransformationTime += Util.measureTime(() -> {
            t1.set(convertToT1());
            t1TripsSearchTime += Util.measureTime(() -> addTripsTo1(t1.get()));
        });
        return t1.get();
    }

    private Lgot getLgot() {
        AtomicReference<Lgot> lgot = new AtomicReference<>();
        lgotTransformationTime += Util.measureTime(() ->
            lgot.set(convertToLgot())
        );
        return lgot.get();
    }

    private void roundTimes() {
        t1TransformationTime   = (float) Math.round(t1TransformationTime   * 100) / 100;
        lgotTransformationTime = (float) Math.round(lgotTransformationTime * 100) / 100;
        t1TripsSearchTime      = (float) Math.round(t1TripsSearchTime      * 100) / 100;
    }

    private T1 convertToT1() {
        return T1.builder()
                .key(T1.Key.builder()
                        .requestDate(getRequestDate())
                        .yyyymm(getYyyymm())
                        .p1(getT1P1())
//                        .p2(getT1P2())
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
                        // trips
                        .p15(getT1P15())
                        // trips
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
                        // trips
                        .p30(getT1P30())
                        .p31(getT1P31())
                        .p32(getT1P32())
                        // values
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
                        // trips
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
    private void addTripsTo1(T1 t1) {
            t1.setKey(t1.getKey().toBuilder()
                    // key
                    .p13(getT1P13())
                    .p14(getT1P14())
                    .p16(getT1P16())
                    // key
                    .p27(getT1P27())
                    .p28(getT1P28())
                    .p29(getT1P29())
                    // key
                    .p62(getT1P62())
                    .p63(getT1P63())
                    // key
                    .build()
            );
    }

    private Lgot convertToLgot() {
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
                .p27(getLgotP27())
                .p28(getLgotP28())
                .p33(getLgotP33())
                .build();
    }
}