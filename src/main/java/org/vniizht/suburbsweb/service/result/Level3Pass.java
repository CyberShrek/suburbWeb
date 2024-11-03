package org.vniizht.suburbsweb.service.result;

import org.vniizht.suburbsweb.service.data.entities.level2.*;
import org.vniizht.suburbsweb.service.data.entities.route.PassRoute;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.util.Util;

import java.util.*;

public final class Level3Pass extends Level3 <Level2Dao.PassRecord> {
    
    public Level3Pass(Set<Level2Dao.PassRecord> records,
                      Handbook handbook,
                      RoutesDao routes) {
        super(records, handbook, routes);
        transform();
    }

    @Override
    protected void assignVariablesForEachRecord(Level2Dao.PassRecord record) {
        main     = record.getMain();
        costList = record.getCost();
        ex       = record.getEx().isEmpty() ? null : record.getEx().get(0);
        route    = null;
        routes.getPassRoute(
                main.train_num,
                main.train_thread,
                main.departure_date,
                main.departure_station,
                main.arrival_station
        );
    }
    // Переменные для каждой записи
    private PassMain       main;
    private List<PassCost> costList;
    private PassEx         ex;
    private PassRoute      route;

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
    protected Integer getYyyymm() {
        return Integer.parseInt(Util.formatDate(main.oper_date, "yyyyMM"));
    }

    @Override
    protected Date getRequestDate() {
        return main.requestDate;
    }

    @Override
    protected String getT1P1() {
        return "tab1";
    }

    @Override
    protected Integer getT1P2() {
        return 1;
    }

    @Override
    protected String getT1P3() {
        return Util.formatDate(main.oper_date, "yyyy");
    }

    @Override
    protected String getT1P4() {
        return Util.formatDate(main.oper_date, "mm");
    }

    @Override
    protected String getT1P5() {
        return "17";
    }

    @Override
    protected String getT1P6() {
        return handbook.getRoad3(main.sale_station, main.oper_date);
    }

    @Override
    protected String getT1P7() {
        return handbook.getRoad3(main.sale_station, main.oper_date);
    }

    @Override
    protected String getT1P8() {
        return "00" + main.sale_station;
    }

    @Override
    protected String getT1P9() {
        return String.format("%09d", main.carrier_code);
    }

    @Override
    protected String getT1P10() {
        return main.saleregion_code;
    }

    @Override
    protected String getT1P11() {
        return handbook.getOkatoByStation(main.sale_station, main.oper_date);
    }

    @Override
    protected String getT1P12() {
        return Util.formatDate(main.departure_date, "yymm");
    }

    @Override
    protected String getT1P13() {
        return route.getRoadStart();
    }

    @Override
    protected String getT1P14() {
        return route.getDepartmentStart();
    }

    @Override
    protected String getT1P15() {
        return main.departure_station;
    }

    @Override
    protected String getT1P16() {
        return route.getRegionStart();
    }

    @Override
    protected String getT1P17() {
        return handbook.getOkatoByStation(
                main.departure_station,
                main.departure_date);
    }

    @Override
    protected String getT1P18() {
        return handbook.getArea(
                main.departure_station,
                main.oper_date);
    }

    @Override
    protected Character getT1P19() {
        return '4';
    }

    @Override
    protected String getT1P20() {
        return "0" + main.carriage_class;
    }

    @Override
    protected Character getT1P21() {
        return '1';
    }

    @Override
    protected Character getT1P22() {
        return Objects.requireNonNull(main.f_tick).length > 2 && main.f_tick[2] == 1 ? '2'                                        // Детский
                : !main.benefit_code.equals("000") || main.f_tick.length > 4 && main.f_tick[4] == 1 ? '3'  // Льготный
                :  main.f_tick.length > 1 && main.f_tick[1] == 1 ? '1'                                     // Полный
                : '4' ;
    }

    @Override
    protected Character getT1P23() {
        return '3';
    }

    @Override
    protected String getT1P24() {
        return main.paymenttype == 'В'
                ? "21" + String.format("%02d", main.military_code)
                : ex != null && ex.lgot_info != null && !main.benefit_code.equals("000") && !main.benefit_code.equals("013")
                ? ex.lgot_info.substring(1, 4)
                : null;
    }

    @Override
    protected Character getT1P25() {
        switch (main.paymenttype) {
            case '8':                     return '3'; // Банковские карты
            case '9': case 'В': case 'Б': return '1'; // Льготные
            case '1': case '3':           return '2'; // Наличные
            case '6':                     return '5'; // Безнал для юр. лиц
            default:                      return '4'; // Электронный кошелёк
        }
    }

    @Override
    protected String getT1P26() {
        return ex == null ? null
                :
                handbook.getGvc(
                        ex.lgot_info.substring(0, 2),
                        main.benefit_code, main.oper_date);
    }

    @Override
    protected String getT1P27() {
        return route.getRoadEnd();
    }

    @Override
    protected String getT1P28() {
        return route.getDepartmentEnd();
    }

    @Override
    protected String getT1P29() {
        return route.getRegionEnd();
    }

    @Override
    protected String getT1P30() {
        return handbook.getOkatoByStation(
                main.arrival_station,
                main.arrival_date);
    }

    @Override
    protected String getT1P31() {
        return handbook.getArea(
                main.arrival_station,
                main.arrival_date);
    }

    @Override
    protected Short getT1P32() {
        return main.distance;
    }

    @Override
    protected Long getT1P33() {
        return Long.valueOf(main.seats_qty);
    }

    @Override
    protected Long getT1P34() {
        return 0L;
    }

    @Override
    protected Long getT1P35() {
        return 0L;
    }

    @Override
    protected Long getT1P36() {
        return (long) costList.stream().mapToDouble(
                costListItem -> costListItem.sum_nde
        ).sum() / 10;
    }

    @Override
    protected Long getT1P37() {
        return 0L;
    }

    @Override
    protected Long getT1P38() {
        return 0L;
    }

    @Override
    protected Long getT1P39() {
        return costList.stream().mapToLong(costListItem -> {
            switch (costListItem.sum_code) {
                case 104: case 105: case 106:
                    return Math.round(costListItem.sum_nde);
                default: return 0L;
            }}
        ).sum();
    }

    @Override
    protected Long getT1P40() {
        return costList.stream().mapToLong(
                costListItem -> costListItem.sum_code == 101
                        ? (long) Math.round(costListItem.sum_nde)
                        : 0L
        ).sum();
    }

    @Override
    protected Long getT1P41() {
        return 0L;
    }

    @Override
    protected Long getT1P42() {
        return 0L;
    }

    @Override
    protected Long getT1P43() {
        return 0L;
    }

    @Override
    protected Long getT1P44() {
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
        }).sum();
    }

    @Override
    protected Long getT1P45() {
        return 0L;
    }

    @Override
    protected Long getT1P46() {
        return 0L;
    }

    @Override
    protected Long getT1P47() {
        return costList.stream().mapToLong(costListItem -> {
            switch (costListItem.sum_code) {
                case 104: case 105: case 106:
                    switch (main.paymenttype) {
                        case 'Б': case 'В': case 'Ж': case '9':
                            return Math.round(costListItem.sum_nde);
                    }
            }
            return 0L;
        }).sum();
    }

    @Override
    protected Long getT1P48() {
        return costList.stream().mapToLong(costListItem -> {
            if (costListItem.sum_code == 101) switch (main.paymenttype) {
                case 'Б': case 'В': case 'Ж': case '9':
                    return Math.round(costListItem.sum_nde);
            }
            return 0L;
        }).sum();
    }

    @Override
    protected Long getT1P49() {
        return 0L;
    }

    @Override
    protected Long getT1P50() {
        return 0L;
    }

    @Override
    protected Long getT1P51() {
        if (main.oper_g == 'N') switch (main.oper) {
            case 'O': return  1L;
            case 'V': return -1L;
        }
        return 0L;
    }

    @Override
    protected Character getT1P52() {
        return '1';
    }

    @Override
    protected String getT1P53() {
        return String.valueOf(main.agent_code);
    }

    @Override
    protected String getT1P54() {
        return main.arrival_station;
    }

    @Override
    protected Character getT1P55() {
        return null;
    }

    @Override
    protected String getT1P56() {
        return "000";
    }

    @Override
    protected Character getT1P57() {
        return null;
    }

    @Override
    protected Character getT1P58() {
        if (ex != null) switch (ex.lgot_info.charAt(9)) {
            case '0': case '1': case '2': case '3': case '4': return '0';
            case '5': case '6': case '7': case '8': case '9': return '1';
        }
        return null;
    }

    @Override
    protected Character getT1P59() {
        if (main.paymenttype == 'Ж' && ex != null && ex.lgot_info.startsWith("22")) switch (ex.lgot_info.charAt(5)){
            case 'Ф': case 'Д': return '1';
        }
        return '0';
    }

    @Override
    protected String getT1P60() {
        return String.valueOf(main.subagent_code);
    }

    @Override
    protected Character getT1P61() {
        return null;
    }

    @Override
    protected Short getT1P62() {
        return null;
    }

    @Override
    protected Character getT1P63() {
        return null;
    }

    @Override
    protected String getLgotList() {
        return "R800" + (main.paymenttype == 'Ж' && ex != null && ex.lgot_info.startsWith("22") ? 'Z' : 'G');
    }

    @Override
    protected Integer getLgotP1() {
        return getT1P2();
    }

    @Override
    protected String getLgotP2() {
        return handbook.getRoad2(main.sale_station, main.oper_date);
    }

    @Override
    protected String getLgotP3() {
        return handbook.getDepartment(main.sale_station, main.oper_date);
    }

    @Override
    protected Character getLgotP4() {
        return '0';
    }

    @Override
    protected Character getLgotP5() {
        switch (new String(new char[]{main.oper, main.oper_g})) {
            case "ON": return '1';
            case "OG": return '2';
            case "VN": return '3';
            case "OO": return '4';
            case "VO": return '5';
        }
        return '0';
    }

    @Override
    protected Character getLgotP6() {
        return '1';
    }

    @Override
    protected String getLgotP7() {
        return getT1P24();
    }

    @Override
    protected String getLgotP8() {
        return String.valueOf(main.carrier_code);
    }

    @Override
    protected String getLgotP9() {
        return handbook.getOkatoByRegion(
                main.benefitcnt_code,
                main.oper_date);
    }

    @Override
    protected String getLgotP10() {
        return ex == null ? null : ex.nomlgud;
    }

    @Override
    protected String getLgotP11() {
        return ex != null && ex.lgot_info.startsWith("22")
                ? ex.lgot_info.substring(7, 12)
                : main.saleregion_code;
    }

    @Override
    protected String getLgotP12() {
        return ex != null && ex.lgot_info.startsWith("22")
                ? ex.lgot_info.substring(13, 23)
                : null;
    }

    @Override
    protected Character getLgotP13() {
        return ex != null && ex.lgot_info.startsWith("22")
                ? ex.lgot_info.charAt(5)
                : null;
    }

    @Override
    protected String getLgotP14() {
        return ex == null ? null :
                ex.last_name.trim() + ' ' + ex.first_name.trim().charAt(0) + ex.patronymic.trim().charAt(0);
    }

    @Override
    protected String getLgotP15() {
        return null;
    }

    @Override
    protected Byte getLgotP16() {
        return (byte) (main.oper_g == 'G'
                ? -1
                : (main.oper == 'V'
                ? 0
                : 1));
    }

    @Override
    protected Boolean getLgotP17() {
        return main.trip_direction == '3';
    }

    @Override
    protected Byte getLgotP18() {
        return null;
    }

    @Override
    protected Short getLgotP19() {
        return main.seats_qty;
    }

    @Override
    protected Character getLgotP20() {
        return null;
    }

    @Override
    protected Short getLgotP21() {
        return null;
    }

    @Override
    protected java.sql.Date getLgotP22() {
        return main.oper_date;
    }

    @Override
    protected java.sql.Date getLgotP23() {
        return main.departure_date;
    }

    @Override
    protected String getLgotP24() {
        return ex == null ? null : ex.ticket_ser.substring(0, 2) + ex.ticket_num;
    }

    @Override
    protected String getLgotP25() {
        return main.departure_station;
    }

    @Override
    protected String getLgotP26() {
        return main.arrival_station;
    }

    @Override
    protected Integer getLgotP27() {
        return costList == null ? 0
                : (int) (costList.stream().mapToDouble(
                        cost -> cost.sum_te).sum() * 10);
    }

    @Override
    protected Integer getLgotP28() {
        return costList == null ? 0 :
                (int) (costList.stream().mapToDouble(
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

    @Override
    protected String getLgotP29() {
        return null;
    }

    @Override
    protected String getLgotP30() {
        return Util.formatDate(
                new Date(main.requestDate.getTime() + main.request_time.getTime()),
                "ddMMyyHHmm");
    }

    @Override
    protected String getLgotP31() {
        return null;
    }

    @Override
    protected String getLgotP32() {
        return ex == null ? null :ex.snils;
    }

    @Override
    protected Short getLgotP33() {
        return null;
    }
}