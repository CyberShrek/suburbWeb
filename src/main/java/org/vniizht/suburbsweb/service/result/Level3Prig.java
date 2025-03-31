package org.vniizht.suburbsweb.service.result;

import org.vniizht.suburbsweb.service.data.entities.level2.PrigAdi;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigCost;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.*;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.entities.level3.meta.CO22Meta;
import org.vniizht.suburbsweb.service.data.entities.routes.RouteGroup;
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
        transform();
    }

    @Override
    protected void next(Level2Dao.PrigRecord record) {
        main     = record.getMain();
        costList = main.getCosts();
        adi      = main.getAdi();
        switch (getT1P21()) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5': isAbonement = true; break;
            default : isAbonement = false;
        }
        fullBenefit = main.benefitgroup_code + main.benefit_code;
        yyyyMM = Integer.parseInt(Util.formatDate(main.operation_date, "yyyyMM"));
    }
    // Переменные для каждой записи
    private Integer yyyyMM;
    private String fullBenefit;
    private PrigMain       main;
    private List<PrigCost> costList;
    private PrigAdi        adi;

    @Override
    protected boolean t1Exists() {
        return true;
    }

    @Override
    protected boolean lgotExists() {
        return t1Exists() && !main.benefit_code.equals("00") && !main.benefitgroup_code.equals("21");
    }

    @Override
    protected T1 getT1() {
        String saleRoad = handbook.getRoad3(main.sale_station, main.operation_date);
        return T1.builder()
                .key(T1.Key.builder()
                        .requestDate(main.requestDate)
                        .yyyymm(yyyyMM)
                        .p1("tab1")
                        .p3(Util.formatDate(main.operation_date, "yyyy"))
                        .p4(Util.formatDate(main.operation_date, "MM"))
                        .p5("017")
                        .p6(saleRoad)
                        .p7(saleRoad)
                        .p8(main.sale_station)
                        .p9(String.format("%09d", main.carriage_code))
                        .p10(main.region_code)
                        .p11(handbook.getOkatoByStation(main.sale_station, main.operation_date))
                        .p12(getT1P12())
                        .p15(main.departure_station)
                        .p17(handbook.getOkatoByStation(main.departure_station, main.operation_date))
                        .p18(handbook.getArea(main.departure_station, main.operation_date))
                        .p19(getT1P19())
                        .p20("0" + main.carriage_class)
                        .p21(getT1P21())
                        .p22(getT1P22())
                        .p23('3')
                        .p24(fullBenefit)
                        .p25(getT1P25())
                        .p26(handbook.getGvc(main.benefitgroup_code, main.benefit_code, main.operation_date))
                        .p30(handbook.getOkatoByStation(main.arrival_station, main.operation_date))
                        .p31(handbook.getArea(main.arrival_station, main.operation_date))
                        .p32((short) costList.stream().mapToInt(costItem -> costItem.route_distance).sum())
                        .p52(getT1P52())
                        .p53(String.valueOf(main.agent_code))
                        .p54(main.arrival_station)
                        .p55(getT1P55())
                        .p56(getT1P56())
                        .p57(getT1P57())
                        .p58(getT1P58())
                        .p59(getT1P59())
                        .p60("000")
                        .p61(main.train_num.matches("^\\d {4}") ? main.train_num.trim().charAt(0) : '0')
                        .build()
                )
                .p33((long) (isAbonement ? main.pass_qty : main.carryon_weight))
                .p34(0F)
                .p35(0F)
                .p36(main.tariff_sum)
                .p37(0F)
                .p38(0F)
                .p39(getT1P39())
                .p40(0F)
                .p41(0F)
                .p42(0F)
                .p43(0F)
                .p44(main.department_sum)
                .p45(0F)
                .p46(0F)
                .p47(0F)
                .p48(0F)
                .p49(0F)
                .p50(0F)
                .p51(getT1P51())
                .build();
    }

    @Override
    protected Lgot getLgot() {
        byte single_qty = 0;
        byte abonem_qty = 0;
        byte pass_qty = (byte) (short) main.pass_qty;
        if(main.abonement_type.equals("0  ")) single_qty = pass_qty;
        else                                  abonem_qty = pass_qty;

        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .requestDate(main.requestDate)
                        .yyyymm(yyyyMM)
                        .list("R064" + (main.benefitgroup_code.equals("22") ? 'Z' : 'G'))
                        .p2(getLgotP4() + handbook.getRoad2(main.sale_station, main.operation_date))
                        .p3(handbook.getDepartment(main.sale_station, main.operation_date))
                        .p4(getLgotP4())
                        .p5(getLgotP5())
                        .p6(main.train_category)
                        .p7(fullBenefit)
                        .p8(Util.addLeadingZeros(String.valueOf(main.carriage_code), 4))
                        .p9(handbook.getOkatoByRegion(main.benefit_region, main.operation_date))
                        .p10(adi == null ? null : adi.benefit_doc)
                        .p11(Util.addLeadingZeros(main.benefitgroup_code.equals("22") && adi != null
                                ? adi.bilgroup_secur + adi.bilgroup_code
                                : main.benefit_region, 5))
                        .p12(Util.addLeadingZeros(adi == null ? null : adi.employee_unit, 10))
                        .p13(main.benefitgroup_code.equals("22") && adi != null ? adi.employee_cat : '0')
                        .p14(getLgotP14())
                        .p15(getLgotP15())
                        .p16(single_qty)
                        .p17(main.flg_2wayticket == '1')
                        .p18(abonem_qty)
                        .p20(getLgotP20())
                        .p21(getLgotP20() != '9' && single_qty != 0 ? main.seatstick_limit : 0)
                        .p22(main.operation_date)
                        .p23(main.ticket_begdate)
                        .p24(main.ticket_ser + Util.addLeadingZeros(String.valueOf(main.ticket_num), 6))
                        .p25(main.departure_station)
                        .p26(main.arrival_station)
                        .p30(main.server_datetime)
                        .p31(Util.addLeadingZeros(main.server_reqnum == null ? null : main.server_reqnum.toString(), 7))
                        .p32(adi == null ? null : adi.snils)
                        .p34(main.agent_code == null ? null : String.valueOf(main.agent_code))
                        .p35(main.sale_station)
                        .build()
                )
                .p19(getLgotP20() == '9' ? main.seatstick_limit : 0)
                .p27(Math.ceil((double) main.department_sum * 100) / 100)
                .p28(Math.ceil((double) main.total_sum * 100) / 100)
                .p33(main.carryon_weight)
                .build();
    }

    @Override
    protected RouteGroup getRouteGroup() {
        RouteGroup routeGroup = new RouteGroup();
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
            routeGroup.merge(routesDao.getRouteGroup(
                    routeNum,
                    firstCost.departure_station,
                    lastCost.arrival_station,
                    firstCost.requestDate
            ));
        });
        return routeGroup;
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
                                        .p12(yyyymm.substring(2))
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
    protected CO22Meta getMeta() {
        return CO22Meta.builder()
                .requestDate(main.requestDate)
                .l2PrigIdnum(main.idnum)
                .operationDate(main.operation_date)
                .ticketBegDate(main.ticket_begdate)
                .ticketEndDate(main.ticket_enddate)
                .build();
    }

    @Override
    protected double getRegionIncomePerKm(String region) {
        int distance = 0;
        float incomeSum = 0;
        for (PrigCost cost : costList) {
            if (cost.region_code.equals(region)) {
                distance += cost.route_distance;
                incomeSum += cost.tariff_sum;
            }
        }
        incomeSum = incomeSum == 0 ? main.tariff_sum : incomeSum;
        return distance == 0 ? 0 : (double) incomeSum / distance;
    }

    @Override
    protected double getRegionOutcomePerKm(String region) {
        int distance = 0;
        float outcomeSum = 0;
        for (PrigCost cost : costList) {
            if (cost.region_code.equals(region)) {
                distance += cost.route_distance;
                outcomeSum += cost.department_sum;
            }
        }
        outcomeSum = outcomeSum == 0 ? main.department_sum : outcomeSum;
        return distance == 0 ? 0 : (double) outcomeSum / distance;
    }

    private String getT1P12() {
        switch (getT1P21()) {
            // Для разового билета и абонементов - yymm даты начала действия
            case '2': case '3': case '5':
                return Util.formatDate(main.ticket_begdate, "yyMM");
        }
        return main.yyyymm.toString().substring(2); // yymm
    }

    private Character getT1P19() {
        switch (main.train_category) {
            case 'С': return '6';           // скорые пригородные поезда типа «Спутник» (7ХХХ)
            case '7': return '5';           // скорые пригородные поезда без предоставления мест (7ХХХ)
            case 'А': return '8';           // рельсовые автобусы 6000-е
            case 'Б': return '7';           // рельсовые автобусы 7000-е
            case 'Г': return '9';           // городские линии
            case '1':   case 'М': return '4'; // скорые пригородные с предоставлением мест (7XXX(8xx-c АМГ))
            default: return '1';            // пригородные пассажирские
        }
    }

    private Character getT1P21() {
        if(main.flg_fee_onboard == '1') return '8'; // Квитанция за оформление в поезде
        if(main.flg_carryon     == '1') return '6'; // Перевозочный документ (для багажа)
        switch (main.abonement_type.charAt(0)) {
            case '5': case '6': return '4';         // Билет выходного дня
            case '0': return main.flg_2wayticket == '1'
                    ? '3'                           // В обоих направлениях
                    : '2';                          // В одном направлении
        }
        return '5';
    }

    private Character getT1P22() {
        return    main.flg_bsp   == '1' ? '4'            // Бесплатный
                : main.flg_child == '1' ? '2'            // Детский
                : main.benefit_code.equals("00") ? '1'   // Полный
                : '3' ;
    }

    private Character getT1P25() {
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

    private Float getT1P39() {
        switch (main.oper){
            case 'O': return  Float.valueOf(main.fee_sum);
            case 'V': return -Float.valueOf(main.refundfee_sum);
        }
        return 0F;
    }

    private Long getT1P51() {
        if(main.oper_g == 'N') switch (main.oper) {
            case 'O': return (long)  main.pass_qty;
            case 'V': return (long) -main.pass_qty;
        }
        return 0L;
    }

    private Character getT1P52() {
        String tSite = getTSite(main);
        if(tSite.equals("  ")) {
            if(main.request_type == 64)
                return '1';
            switch (main.request_subtype / 256) {
                case 0: return '3';
                case 1: return '2';
                default: return '5';
            }
        }

        return tSite.trim().charAt(1);
    }

    private Character getT1P55() {
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

    private String getT1P56() {
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

    private Character getT1P57() {
        char type = main.carryon_type;
        switch (type) {
            case 'Ж': type = '1'; break; // живность
            case 'Т': type = '2'; break; // телевизор
            case 'В': type = '3'; break; // велосипед
            case 'Р': type = '4'; break; // излишний вес ручной клади
        }
        return type;
    }

    private Character getT1P58() {
        return main.benefitgroup_code.equals("22")
                && adi != null
                ? (Integer.parseInt(adi.bilgroup_code) > 4 ? '1' : '0')
                : null;
    }

    private Character getT1P59() {
        return main.benefitgroup_code.equals("22")
                && adi != null
                ? adi.employee_cat == 'Ф' ? '1' : '0'
                : null;
    }

    private Character getLgotP4() {
        if (main.request_type != 64) switch (main.request_subtype / 256){
            case 1:  return '1';
            case 0:
            case 2:  return '2';
        }
        return '0';
    }

    private Character getLgotP5() {
        switch (new String(new char[]{main.oper, main.oper_g})){
            case "ON": return '1';
            case "OG": return '2';
            case "VN": return '3';
            case "OO": return '4';
            default:   return '0';
        }
    }

    private String getLgotP14() {
        if (adi == null || adi.surname == null) return null;

        String surname = adi.surname.trim();
        String initials = adi.initials == null ? null : adi.initials.trim();

        return surname + (initials == null ? "" : " " + initials);
    }

    private String getLgotP15() {
        if (adi == null || adi.dependent_surname == null) return null;

        String surname = adi.dependent_surname.trim();
        String initials = adi.dependent_initials == null ? null : adi.dependent_initials.trim();

        return surname + (initials == null ? "" : " " + initials);
    }

    private Character getLgotP20() {
        switch (main.abonement_type.trim()) {
            case "1": return '9';
            case "2": return '7';
            case "4": return '1';
            case "5": return '2';
            case "7": return '4';
            case "8": return '5';
            default: return  '0';
        }
    }

    private String getTSite(PrigMain main) {
        return handbook.getTSite(
                main.web_id,
                main.sale_station.substring(0, 2),
                main.operation_date);
    }
}