package org.vniizht.suburbsweb.service.result;

import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.entities.level3.meta.CO22Meta;
import org.vniizht.suburbsweb.service.data.entities.level2.*;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.routes.RouteGroup;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.util.Util;

import java.util.*;

public final class Level3Pass extends Level3 <Level2Dao.PassRecord> {
    
    public Level3Pass(Set<Level2Dao.PassRecord> records,
                      Handbook handbook,
                      RoutesDao routes,
                      Long initialT1Serial) {
        super(records, handbook, routes, initialT1Serial);
        transform();
    }

    @Override
    protected void next(Level2Dao.PassRecord record) {
        main     = record.getMain();
        costList = main.getCosts();
        ex       = main.getEx();
        yyyyMM = Integer.parseInt(Util.formatDate(main.oper_date, "yyyyMM"));
    }
    // Переменные для каждой записи
    private Integer yyyyMM;
    private PassMain       main;
    private List<PassCost> costList;
    private PassEx         ex;

    @Override
    protected boolean t1Exists() {
        return main.f_r10af3[8] == '1';
    }

    @Override
    protected boolean lgotExists() {
        return t1Exists() && main.benefit_code.equals("00");
    }

    @Override
    protected T1 getT1() {
        String saleRoad = handbook.getRoad3(main.sale_station, main.oper_date);
        return T1.builder()
                .key(T1.Key.builder()
                        .requestDate(main.requestDate)
                        .yyyymm(yyyyMM)
                        .p1("tab1")
                        .p3(Util.formatDate(main.oper_date, "yyyy"))
                        .p4(Util.formatDate(main.oper_date, "MM"))
                        .p5("017")
                        .p6(saleRoad)
                        .p7(saleRoad)
                        .p8("00" + main.sale_station)
                        .p9(String.format("%09d", main.carrier_code))
                        .p10(main.saleregion_code)
                        .p11(handbook.getOkatoByStation(main.sale_station, main.oper_date))
                        .p12(Util.formatDate(main.departure_date, "yyMM"))
                        .p15(main.departure_station)
                        .p17(handbook.getOkatoByStation(main.departure_station, main.oper_date))
                        .p18(handbook.getArea(main.departure_station, main.oper_date))
                        .p19('4')
                        .p20("0" + main.carriage_class)
                        .p21('1')
                        .p22(getT1P22())
                        .p23('3')
                        .p24(getT1P24())
                        .p25(getT1P25())
                        .p26(getT1P26())
                        .p30(handbook.getOkatoByStation(main.arrival_station, main.arrival_date))
                        .p31(handbook.getArea(main.arrival_station, main.arrival_date))
                        .p32(main.distance)
                        .p52('1')
                        .p53(String.valueOf(main.agent_code))
                        .p54(main.arrival_station)
                        .p56("000")
                        .p58(getT1P58())
                        .p59(getT1P59())
                        .p60(String.valueOf(main.subagent_code))
                        .build()
                )
                .p33(Long.valueOf(main.seats_qty))
                .p34(0L)
                .p35(0L)
                .p36((long) costList.stream().mapToDouble(costListItem -> costListItem.sum_nde).sum() * 10)
                .p37(0L)
                .p38(0L)
                .p39(getT1P39())
                .p40(getT1P40())
                .p41(0L)
                .p42(0L)
                .p43(0L)
                .p44(getT1P44())
                .p45(0L)
                .p46(0L)
                .p47(getT1P47())
                .p48(getT1P48())
                .p49(0L)
                .p50(0L)
                .p51(getT1P51())
                .build();
    }

    @Override
    protected Lgot getLgot() {
        return Lgot.builder().key(
                        Lgot.Key.builder()
                                .requestDate(main.requestDate)
                                .list("R800" + (main.paymenttype == 'Ж' && ex != null && ex.lgot_info != null && ex.lgot_info.startsWith("22") ? 'Z' : 'G'))
                                .p2(handbook.getRoad2(main.sale_station, main.oper_date))
                                .p3(handbook.getDepartment(main.sale_station, main.oper_date))
                                .p4('0')
                                .p5(getLgotP5())
                                .p6('1')
                                .p7(getT1P24())
                                .p8(String.valueOf(main.carrier_code))
                                .p9(handbook.getOkatoByRegion(main.benefitcnt_code, main.oper_date))
                                .p10(ex != null && ex.lgot_info != null ? ex.nomlgud : null)
                                .p11(getLgotP11())
                                .p12(getLgotP12())
                                .p13(getLgotP13())
                                .p14(getLgotP14())
                                .p16((byte) (main.oper_g == 'G' ? -1 : (main.oper == 'V' ? 0 : 1)))
                                .p17(main.trip_direction == '3')
                                .p22(main.oper_date)
                                .p23(main.departure_date)
                                .p24(ex == null ? null : ex.ticket_ser.substring(0, 2) + ex.ticket_num)
                                .p25(main.departure_station)
                                .p26(main.arrival_station)
                                .p30(Util.formatDate(new Date(main.requestDate.getTime() + main.request_time.getTime()), "ddMMyyHHmm"))
                                .p32(ex == null ? null : ex.snils)
                                .p34(main.agent_code == null ? null : String.valueOf(main.agent_code))
                                .p35(main.sale_station)
                                .build())
                .p19(main.seats_qty)
                .p27(costList == null ? 0 : (costList.stream().mapToDouble(cost -> cost.sum_te).sum() * 10))
                .p28(getLgotP28())
                .build();
    }

    @Override
    protected RouteGroup getRouteGroup() {
        return routesDao.getRouteGroup(
                main.train_num,
                main.train_thread,
                main.departure_date,
                main.departure_station,
                main.arrival_station
        );
    }

    @Override
    protected Set<T1> multiplyT1(T1 t1) {
        Set<T1> result = new HashSet<>();
        result.add(t1);
        if (!main.oper_date.equals(main.departure_date)) {
            result.add(t1.toBuilder()
                    .key(t1.getKey().toBuilder()
                            .yyyymm(Integer.parseInt(Util.formatDate(main.departure_date, "yyyyMM")))
                            .build()
                    )
                    .build()
            );
        }
        return result;
    }

    @Override
    protected CO22Meta getMeta() {
        return CO22Meta.builder()
                .requestDate(main.requestDate)
                .l2PassIdnum(main.idnum)
                .build();
    }

    @Override
    protected double getRegionIncomePerKm(String region) {
        // TODO
        return 0;
    }

    @Override
    protected double getRegionOutcomePerKm(String region) {
        // TODO
        return 0;
    }

    private Character getT1P22() {
        return Objects.requireNonNull(main.f_tick).length > 2 && main.f_tick[2] == 1 ? '2'                                        // Детский
                : !main.benefit_code.equals("000") || main.f_tick.length > 4 && main.f_tick[4] == 1 ? '3'  // Льготный
                :  main.f_tick.length > 1 && main.f_tick[1] == 1 ? '1'                                     // Полный
                : '4' ;
    }

    private String getT1P24() {
        return main.paymenttype == 'В'
                ? "21" + String.format("%02d", main.military_code)
                : ex != null && ex.lgot_info != null && ex.lgot_info.length() > 4 && !main.benefit_code.equals("000") && !main.benefit_code.equals("013")
                ? ex.lgot_info.substring(0, 4)
                : null;
    }

    private Character getT1P25() {
        switch (main.paymenttype) {
            case '8':                     return '3'; // Банковские карты
            case '9': case 'В': case 'Б': return '1'; // Льготные
            case '1': case '3':           return '2'; // Наличные
            case '6':                     return '5'; // Безнал для юр. лиц
            default:                      return '4'; // Электронный кошелёк
        }
    }

    private String getT1P26() {
        return ex == null || ex.lgot_info == null || ex.lgot_info.length() < 2 ? null
                :
                handbook.getGvc(
                        ex.lgot_info.substring(0, 2),
                        main.benefit_code, main.oper_date);
    }

    private Long getT1P39() {
        return costList.stream().mapToLong(costListItem -> {
            switch (costListItem.sum_code) {
                case 104: case 105: case 106:
                    return Math.round(costListItem.sum_nde);
                default: return 0L;
            }}
        ).sum() * 10;
    }

    private Long getT1P40() {
        return costList.stream().mapToLong(
                costListItem -> costListItem.sum_code == 101
                        ? (long) Math.round(costListItem.sum_nde)
                        : 0L
        ).sum() * 10;
    }

    private Long getT1P44() {
        return costList.stream().mapToLong(costListItem -> {
            switch (costListItem.sum_code) {
                case 101: case 116: {
                    switch (main.paymenttype) {
                        case 'Б': case 'В': case 'Ж': case '9':
                            return Math.round(costListItem.sum_nde);
                    }
                }
            }
            return 0L;
        }).sum() * 10;
    }

    private Long getT1P47() {
        return costList.stream().mapToLong(costListItem -> {
            switch (costListItem.sum_code) {
                case 104: case 105: case 106:
                    switch (main.paymenttype) {
                        case 'Б': case 'В': case 'Ж': case '9':
                            return Math.round(costListItem.sum_nde);
                    }
            }
            return 0L;
        }).sum() * 10;
    }

    private Long getT1P48() {
        return costList.stream().mapToLong(costListItem -> {
            if (costListItem.sum_code == 101) switch (main.paymenttype) {
                case 'Б': case 'В': case 'Ж': case '9':
                    return Math.round(costListItem.sum_nde);
            }
            return 0L;
        }).sum() * 10;
    }

    private Long getT1P51() {
        if (main.oper_g == 'N') switch (main.oper) {
            case 'O': return  1L;
            case 'V': return -1L;
        }
        return 0L;
    }

    private Character getT1P58() {
        if (ex != null && ex.lgot_info != null && ex.lgot_info.startsWith("22") && ex.lgot_info.length() >= 10) switch (ex.lgot_info.charAt(9)) {
            case '0': case '1': case '2': case '3': case '4': return '0';
            case '5': case '6': case '7': case '8': case '9': return '1';
        }
        return null;
    }

    private Character getT1P59() {
        if (main.paymenttype == 'Ж' && ex != null && ex.lgot_info != null && ex.lgot_info.startsWith("22")) switch (ex.lgot_info.charAt(5)){
            case 'Ф': case 'Д': return '1';
        }
        return '0';
    }

    private Character getLgotP5() {
        switch (new String(new char[]{main.oper, main.oper_g})) {
            case "ON": return '1';
            case "OG": return '2';
            case "VN": return '3';
            case "OO": return '4';
            case "VO": return '5';
        }
        return '0';
    }

    private String getLgotP11() {
        return ex != null && ex.lgot_info != null && ex.lgot_info.startsWith("22") && ex.lgot_info.length() >= 12
                ? ex.lgot_info.substring(7, 12)
                : main.saleregion_code;
    }

    private String getLgotP12() {
        return ex != null && ex.lgot_info != null && ex.lgot_info.startsWith("22") && ex.lgot_info.length() >= 23
                ? ex.lgot_info.substring(13, 23)
                : null;
    }

    private Character getLgotP13() {
        return ex != null && ex.lgot_info != null && ex.lgot_info.startsWith("22") && ex.lgot_info.length() >= 5
                ? ex.lgot_info.charAt(5)
                : null;
    }

    private String getLgotP14() {
        if(ex == null || ex.last_name == null) return null;

        String lastName = ex.last_name.trim();
        String firstName = ex.first_name == null ? "" : ex.first_name.trim();
        String patronymic = ex.patronymic == null ? "" : ex.patronymic.trim();

        return lastName + ' '
                + (firstName.isEmpty() ? "" : firstName.charAt(0))
                + (patronymic.isEmpty() ? "" : patronymic.charAt(0));
    }

    private Double getLgotP28() {
        return costList == null ? 0 :
                 (costList.stream().mapToDouble(
                        cost -> {
                            switch (cost.sum_code) {
                                case 101: case 102: case 116:
                                    switch (main.paymenttype) {
                                        case '1': case '6': case '8':
                                            return cost.sum_nde;
                                }
                            }
                            return 0;
                        }).sum() * 10);
    }
}