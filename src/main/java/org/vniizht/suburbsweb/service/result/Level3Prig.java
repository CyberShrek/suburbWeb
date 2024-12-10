package org.vniizht.suburbsweb.service.result;

import org.vniizht.suburbsweb.service.data.entities.Route;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigAdi;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigCost;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.service.data.dao.TripsDao;
import org.vniizht.suburbsweb.util.Util;

import java.util.*;

public final class Level3Prig extends Level3 <Level2Dao.PrigRecord> {

    private final TripsDao trips;
    private boolean isAbonement;

    public Level3Prig(Set<Level2Dao.PrigRecord> records,
                      Handbook handbook,
                      RoutesDao routes,
                      TripsDao trips,
                      Long initialT1Serial) {
        super(records, handbook, routes, initialT1Serial);
        this.trips = trips;
        switch (getT1P21()) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5': isAbonement = true;
            default : isAbonement = false;
        }

        transform();
    }

    @Override
    protected void assignVariablesForRecord(Level2Dao.PrigRecord record) {
        main     = record.getMain();
        costList = main.getCosts();
        adi      = main.getAdi();
    }
    // Переменные для каждой записи
    private PrigMain       main;
    private List<PrigCost> costList;
    private PrigAdi        adi;

    @Override
    protected Long getIdnum() {
        return main.idnum;
    }

    @Override
    protected List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        Map<Short, List<PrigCost>> costsPerRouteNum = new LinkedHashMap<>();
        costList.forEach(cost -> {
            if (!costsPerRouteNum.containsKey(cost.route_num)) {
                costsPerRouteNum.put(cost.route_num, new ArrayList<>());
            }
            costsPerRouteNum.get(cost.route_num).add(cost);
        });
        costsPerRouteNum.forEach((routeNum, costs) -> {
            PrigCost firstCost = costs.get(0);
            PrigCost lastCost  = costs.get(costs.size() - 1);
            Route route = routesDao.getRoute(
                    routeNum,
                    firstCost.departure_station,
                    lastCost.arrival_station,
                    firstCost.requestDate
            );
            route.incrementSerial();
            routes.add(route);
        });
        return routes;
    }

    @Override
    protected Set<T1> multiplyT1(T1 t1) {
        Set<T1> t1Set = new LinkedHashSet<>();

        if (isAbonement) {
            trips.calculateTripsPerMonth(main)
                    .forEach((yyyymm, trips) -> {
                        boolean isActual = t1.getKey().getYyyymm() == Integer.parseInt(yyyymm);
                        t1Set.add(t1.toBuilder()
                                .key(t1.getKey().toBuilder()
                                        .yyyymm(Integer.parseInt(yyyymm))
                                        .build())
                                .p33(Long.valueOf(trips))
                                // Стоимости
                                .p34(isActual ? t1.getP34() : 0)
                                .p35(isActual ? t1.getP35() : 0)
                                .p36(isActual ? t1.getP36() : 0)
                                .p37(isActual ? t1.getP37() : 0)
                                .p38(isActual ? t1.getP38() : 0)
                                .p39(isActual ? t1.getP39() : 0)
                                .p40(isActual ? t1.getP40() : 0)
                                .p41(isActual ? t1.getP41() : 0)
                                .p42(isActual ? t1.getP42() : 0)
                                .p43(isActual ? t1.getP43() : 0)
                                .p44(isActual ? t1.getP44() : 0)
                                .p45(isActual ? t1.getP45() : 0)
                                .p46(isActual ? t1.getP46() : 0)
                                .p47(isActual ? t1.getP47() : 0)
                                .p48(isActual ? t1.getP48() : 0)
                                .p49(isActual ? t1.getP49() : 0)
                                .p50(isActual ? t1.getP50() : 0)
                                .build());
                    });
        } else {
            t1Set.add(t1);
        }

        return t1Set;
    }

    @Override
    protected boolean t1Exists() {
        return true;
    }

    @Override
    protected Integer getYyyymm() {
        return Integer.parseInt(Util.formatDate(main.operation_date, "yyyyMM"));
    }

    @Override
    protected Date getRequestDate() {
        return main.requestDate;
    }

    @Override
    protected String getT1P3() {
        return Util.formatDate(main.operation_date, "yyyy");
    }

    @Override
    protected String getT1P4() {
        return Util.formatDate(main.operation_date, "MM");
    }

    @Override
    protected String getT1P6() {
        return handbook.getRoad3(
                main.sale_station,
                main.operation_date
        );
    }

    @Override
    protected String getT1P8() {
        return "00" + main.sale_station;
    }

    @Override
    protected String getT1P9() {
        return String.format("%09d", main.carriage_code); // Заполнение слева нулями до 9 знаков
    }

    @Override
    protected String getT1P10() {
        return main.region_code;
    }

    @Override
    protected String getT1P11() {
        return handbook.getOkatoByStation(
                main.sale_station,
                main.operation_date
        );
    }


    @Override
    protected String getT1P12() {
        switch (getT1P21()) {
            // Для разового билета и абонементов - yymm даты начала действия
            case '2': case '3': case '5':
                return Util.formatDate(main.ticket_begdate, "yyMM");
        }
        return String.valueOf(main.yyyymm/10000*10000); // yymm
    }

    @Override
    protected String getT1P15() {
        return main.departure_station;
    }

    @Override
    protected String getT1P17() {
        return handbook.getOkatoByStation(
                main.departure_station,
                main.operation_date
        );
    }

    @Override
    protected String getT1P18() {
        return handbook.getArea(
                main.departure_station,
                main.operation_date
        );
    }

    @Override
    protected Character getT1P19() {
        switch (main.train_category) {
            case 'С': return '6';           // скорые пригородные поезда типа «Спутник» (7ХХХ)
            case '7': return '5';           // скорые пригородные поезда без предоставления мест (7ХХХ)
            case 'А': return '8';           // рельсовые автобусы 6000-е
            case 'Б': return '7';           // рельсовые автобусы 7000-е
            case 'Г': return '9';           // городские линии
            case '1': case 'М': return '4'; // скорые пригородные с предоставлением мест (7XXX(8xx-c АМГ))
            default: return '1';            // пригородные пассажирские
        }
    }

    @Override
    protected String getT1P20() {
        return "0" + main.carriage_class;
    }

    @Override
    protected Character getT1P21() {
        if(main.flg_fee_onboard == '1') return '8'; // Квитанция за оформление в поезде
        if(main.carryon_type    == '1') return '6'; // Перевозочный документ (для багажа)
        switch (main.abonement_type.charAt(0)) {
            case '5': case '6': return '4';         // Билет выходного дня
            case '0': return main.flg_2wayticket == '1'
                    ? '3'                           // В обоих направлениях
                    : '2';                          // В одном направлении
        }
        return '5';
    }

    @Override
    protected Character getT1P22() {
        return    main.flg_bsp   == '1' ? '4'            // Бесплатный
                : main.flg_child == '1' ? '2'            // Детский
                : main.benefit_code.equals("00") ? '1'   // Полный
                : '3' ;
    }

    @Override
    protected Character getT1P23() {
        return '3';
    }

    @Override
    protected String getT1P24() {
        return main.benefitgroup_code + main.benefit_code;
    }

    @Override
    protected Character getT1P25() {
        if(getTSite(main)
                .equals("09") && handbook.getPlagnVr(main.payagent_id, main.departure_station, main.operation_date)
                .equals("6 "))
            return '4'; // Электронный кошелёк

        switch (main.paymenttype) {
            case '8': return '3';             // Банковские карты
            case '9': return '1';             // Льготные
            case '1': case '3': return '2';   // Наличные
            case '6': return '5';             // Безнал для юр. лиц
            default: return '6';              // Интернет
        }
    }

    @Override
    protected String getT1P26() {
        return handbook.getGvc(
                main.benefitgroup_code,
                main.benefit_code,
                main.operation_date);
    }

    @Override
    protected String getT1P30() {
        return handbook.getOkatoByStation(
                main.arrival_station,
                main.operation_date
        );
    }

    @Override
    protected String getT1P31() {
        return handbook.getArea(
                main.arrival_station,
                main.operation_date
        );
    }

    @Override
    protected Short getT1P32() {
        return (short) costList
                .stream()
                .mapToInt(costItem -> costItem.route_distance)
                .sum();
    }

    @Override
    protected Long getT1P33() {
        return (long) (isAbonement ? main.pass_qty : main.carryon_weight);
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
        return main.tariff_sum;
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
        switch (main.oper){
            case 'O': return main.fee_sum;
            case 'V': return main.refundfee_sum;
        }
        return 0L;
    }

    @Override
    protected Long getT1P40() {
        return 0L;
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
        return main.department_sum;
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
        return 0L;
    }

    @Override
    protected Long getT1P48() {
        return 0L;
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
        if(main.oper_g == 'N') switch (main.oper) {
            case 'O': return (long)  main.pass_qty;
            case 'V': return (long) -main.pass_qty;
        }
        return 0L;
    }

    @Override
    protected Character getT1P52() {
        String tSite = getTSite(main);
        if(tSite.equals("  ")) switch (main.request_subtype / 256){
            case 0:  return '3';
            case 1:  return '2';
            default: return '5';
        }
        return tSite.trim().charAt(0);
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
        switch (main.abonement_type.charAt(0)){
            case '1': return '5';           // билет на количество поездок
            case '2': switch (main.abonement_subtype){
                case '0': return '4';       // билет на определенные даты
                case '1': return '6';       // билет на определенные нечетные даты
                case '2': return '7';       // билет на определенные четные даты
            }
            case '3': case '4': return '1'; // билет «ежедневно»
            case '5': case '6': return '2'; // билет «выходного дня»
            case '7': case '8': return '3'; // билет «рабочего дня»
        }
        return '0';                         //
    }

    @Override
    protected String getT1P56() {
        if(main.abonement_type.charAt(0) == '0'
                || main.seatstick_limit == 0
                || main.seatstick_limit == 10
                || main.seatstick_limit == 11)
            return "000";

        char type;
        switch (main.abonement_type.charAt(0)) {
            case '3': case '5': case '7': type = '0'; // Месячный
            case '2': case '4': case '6': type = '1'; // Посуточный
            default: type = '4';                      // Количество поездок
        }
        return type + String.format("%02d", main.seatstick_limit);
    }

    @Override
    protected Character getT1P57() {
        char type = main.carryon_type;
        switch (type) {
            case 'Ж': type = '1'; break; // живность
            case 'Т': type = '2'; break; // телевизор
            case 'В': type = '3'; break; // велосипед
            case 'Р': type = '4'; break; // излишний вес ручной клади
        }
        return type;
    }

    @Override
    protected Character getT1P58() {
        return main.benefitgroup_code.equals("22")
                && adi != null
                ? (Integer.parseInt(adi.bilgroup_code) > 4 ? '1' : '0')
                : null;
    }

    @Override
    protected Character getT1P59() {
        return main.benefitgroup_code.equals("22")
                && adi != null
                ? adi.employee_cat == 'Ф' ? '1' : '0'
                : null;
    }

    @Override
    protected String getT1P60() {
        return "000";
    }

    @Override
    protected Character getT1P61() {
        return main.train_num.matches("^\\d {5}")
                ? main.train_num.trim().charAt(0)
                : 0;
    }

    @Override
    protected String getLgotList() {
        return "R064" + (main.benefitgroup_code.equals("22") ? 'Z' : 'G');
    }

    @Override
    protected boolean lgotExists() {
        return t1Exists() && main.benefit_code.equals("00");
    }

    @Override
    protected String getLgotP2() {
        return handbook.getRoad2(
                main.sale_station,
                main.operation_date
        );
    }

    @Override
    protected String getLgotP3() {
        return handbook.getDepartment(
                main.sale_station,
                main.operation_date
        );
    }

    @Override
    protected Character getLgotP4() {
        switch (main.request_subtype / 256){
            case 0:  return '2';
            case 1:  return '1';
            default: return '0';
        }
    }

    @Override
    protected Character getLgotP5() {
        switch (new String(new char[]{main.oper, main.oper_g})){
            case "ON": return '1';
            case "OG": return '2';
            case "VN": return '3';
            case "OO": return '4';
            default:   return '0';
        }
    }

    @Override
    protected Character getLgotP6() {
        return main.train_category;
    }

    @Override
    protected String getLgotP7() {
        return getT1P24();
    }

    @Override
    protected String getLgotP8() {
        return String.valueOf(main.carriage_code);
    }

    @Override
    protected String getLgotP9() {
        return handbook.getOkatoByRegion(
                main.benefit_region,
                main.operation_date
        );
    }

    @Override
    protected String getLgotP10() {
        return adi == null
                ? null
                : adi.benefit_doc;
    }

    @Override
    protected String getLgotP11() {
        return main.benefitgroup_code.equals("22") && adi != null
                ? adi.bilgroup_secur + adi.bilgroup_code
                : main.benefit_region;
    }

    @Override
    protected String getLgotP12() {
        return adi == null ? null : adi.employee_unit;
    }

    @Override
    protected Character getLgotP13() {
        return main.benefitgroup_code.equals("22") && adi != null
                ? adi.employee_cat
                : null;
    }

    @Override
    protected String getLgotP14() {
        if (adi == null || adi.surname == null) return null;

        String surname = adi.surname.trim();
        String initials = adi.initials == null ? null : adi.initials.trim();

        return surname + (initials == null ? "" : " " + initials);
    }

    @Override
    protected String getLgotP15() {
        if (adi == null || adi.dependent_surname == null) return null;

        String surname = adi.dependent_surname.trim();
        String initials = adi.dependent_initials == null ? null : adi.dependent_initials.trim();

        return surname + (initials == null ? "" : " " + initials);
    }

    @Override
    protected Byte getLgotP16() {
        return (byte) (main.abonement_type.equals("0 ")
                ?
                (main.oper_g == 'G'
                        ? -1
                        :
                        (main.oper == 'V'
                                ? 0 : 1
                                ))
                : 0
        );
    }

    @Override
    protected Boolean getLgotP17() {
        return main.flg_2wayticket == '1';
    }

    @Override
    protected Byte getLgotP18() {
        return getLgotP16();
    }

    @Override
    protected Short getLgotP19() {
        return main.pass_qty;
    }

    @Override
    protected Character getLgotP20() {
        switch (main.abonement_type.trim()) {
            case "1": return '9';
            case "2": return '7';
            case "3": return '0';
            case "4": return '1';
            case "5": return '2';
            case "7": return '4';
            case "8": return '5';
            default: return null;
        }
    }

    @Override
    protected Short getLgotP21() {
        return main.seatstick_limit;
    }

    @Override
    protected java.sql.Date getLgotP22() {
        return main.operation_date;
    }

    @Override
    protected java.sql.Date getLgotP23() {
        return main.ticket_begdate;
    }

    @Override
    protected String getLgotP24() {
        return main.ticket_ser.substring(0, 2) + main.ticket_num;
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
    protected Double  getLgotP27() {
        return (double) (main.department_sum);
    }

    @Override
    protected Double  getLgotP28() {
        return (double) (main.tariff_sum - main.department_sum);
    }

    @Override
    protected String getLgotP29() {
        return null;
    }

    @Override
    protected String getLgotP30() {
        return main.server_datetime;
    }

    @Override
    protected String getLgotP31() {
        return main.server_reqnum == null ? null : main.server_reqnum.toString();
    }

    @Override
    protected String getLgotP32() {
        return adi == null ? null : adi.snils;
    }

    @Override
    protected Short getLgotP33() {
        return main.carryon_weight;
    }

    private String getTSite(PrigMain main) {
        return handbook.getTSite(
                main.web_id,
                main.sale_station.substring(0, 2),
                main.operation_date);
    }
}