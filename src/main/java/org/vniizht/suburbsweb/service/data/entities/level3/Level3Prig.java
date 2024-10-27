package org.vniizht.suburbsweb.service.data.entities.level3;

import org.vniizht.suburbsweb.service.data.entities.route.PrigRoute;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigAdi;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigCost;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.service.data.dao.TripsDao;
import org.vniizht.suburbsweb.util.Util;

import java.util.List;
import java.util.Set;

public final class Level3Prig extends Level3 <Level2Dao.PrigRecord> {

    private final TripsDao trips;

    Level3Prig(Level2Dao.PrigRecord record,
               Handbook handbook,
               RoutesDao routes,
               TripsDao trips) {
        super(record, handbook, routes);
        this.trips = trips;
        transform();
    }

    @Override
    protected T1 convertT1() {

        // Используемые данные
        PrigMain main = record.getMain();
        List<PrigCost>  cost = record.getCost();
        PrigAdi         adi  = record.getAdi();

        // Вычислено
        Character ticketType =
                main.flg_fee_onboard == '1' ? '8' // Квитанция за оформление в поезде
                        : main.carryon_type == '1' ? '6' // Перевозочный документ (для багажа)
                        : switch (main.abonement_type.charAt(0)) {
            case '5', '6' -> '4'; // Билет выходного дня
            case '0' -> main.flg_2wayticket == '1' ? '3' : '2'; // “туда+обратно” или только “туда”
            default -> '5'; // Абонементный
        };
        String tSite = handbook.getTSite(
                main.web_id,
                main.sale_station.substring(0, 2),
                main.operation_date);

        return T1.builder().key(
                        T1.Key.builder()
                                .request_date(main.request_date)
                                .report_yyyymm(Util.formatDate(main.operation_date, "yyyyMM"))
                                .p1("tab1")
                                .p2(1)
                                .p3(Util.formatDate(main.operation_date, "yyyy"))
                                .p4(Util.formatDate(main.operation_date, "mm"))
                                .p5("17")
                                .p6(handbook.getRoad3(main.sale_station, main.operation_date))
                                .p7(handbook.getRoad3(main.sale_station, main.operation_date))
                                .p8("00" + main.sale_station)
                                .p9(String.format("%09d", main.carriage_code)) // Заполнение слева нулями до 9 знаков
                                .p10(main.region_code)
                                .p11(handbook.getOkatoByStation(main.sale_station, main.operation_date))
                                .p12(switch (ticketType) {
                                            // Для разового билета и абонементов - yymm даты начала действия
                                            case '2', '3', '5' -> Util.formatDate(main.ticket_begdate, "yyMM");
                                            default -> String.valueOf(main.yyyymm/10000*10000); // yymm
                                })
                                .p15(main.departure_station)
                                .p16(null)
                                .p17(handbook.getOkatoByStation(main.departure_station, main.operation_date))
                                .p18(handbook.getArea(main.departure_station, main.operation_date))
                                .p19(switch (main.train_category) {
                                    case 'С' -> '6'; // скорые пригородные поезда типа «Спутник» (7ХХХ)
                                    case '7' -> '5'; // скорые пригородные поезда без предоставления мест (7ХХХ)
                                    case 'А' -> '8'; // рельсовые автобусы 6000-е
                                    case 'Б' -> '7'; // рельсовые автобусы 7000-е
                                    case 'Г' -> '9'; // городские линии
                                    case '1', 'М' -> '4'; // скорые пригородные с предоставлением мест (7XXX(8xx-c АМГ))
                                    default -> '1'; // пригородные пассажирские
                                })
                                .p20("0" + main.carriage_class)
                                .p21(ticketType)
                                .p22(
                                          main.flg_bsp   == '1' ? '4'            // Бесплатный
                                        : main.flg_child == '1' ? '2'            // Детский
                                        : main.benefit_code.equals("00") ? '1'   // Полный
                                        : '3'                                    // Льготный
                                )
                                .p23('3')
                                .p24(main.benefitgroup_code + main.benefit_code)
                                .p25(tSite.equals("09")
                                        && handbook.getPlagnVr(
                                                main.payagent_id,
                                                main.departure_station,
                                                main.operation_date).equals("6 ")
                                        ? '4' // Электронный кошелёк
                                        : switch (main.paymenttype) {
                                            case '8'      -> '3';   // Банковские карты
                                            case '9'      -> '1';   // Льготные
                                            case '1', '3' -> '2';   // Наличные
                                            case '6'      -> '5';   // Безнал для юр. лиц
                                            default       -> '6';   // Интернет
                                        }
                                )
                                .p26(handbook.getGvc(main.benefitgroup_code, main.benefit_code, main.operation_date))

                                .p30(handbook.getOkatoByStation(main.arrival_station, main.operation_date))
                                .p31(handbook.getArea(main.arrival_station, main.operation_date))
                                .p32((short) cost.stream().mapToInt(costItem -> costItem.route_distance).sum())

                                .p52(tSite.equals("  ")
                                        ? switch (main.request_subtype / 256) {
                                            case 0  -> '3';
                                            case 1  -> '2';
                                            default -> '5';
                                        }
                                        : String.valueOf(Integer.parseInt(tSite.trim())).charAt(0)
                                )
                                .p53(String.valueOf(main.agent_code))
                                .p54(main.arrival_station)
                                .p55(switch (main.abonement_type.charAt(0)) {
                                    case '1' -> '5'; // билет на количество поездок
                                    case '2' -> switch (main.abonement_subtype) {
                                        case '0' -> '4'; // билет на определенные даты
                                        case '1' -> '6'; // билет на определенные нечетные даты
                                        case '2' -> '7'; // билет на определенные четные даты
                                        default  -> '0';
                                    };
                                    case '3', '4' -> '1'; // билет «ежедневно»
                                    case '5', '6' -> '2'; // билет «выходного дня»
                                    case '7', '8' -> '3'; // билет «рабочего дня»
                                    default -> '0';
                                })
                                .p56((
                                        main.abonement_type.charAt(0) == '0' ||
                                        switch (main.seatstick_limit) {case 0, 10, 11 -> true; default -> false;} )
                                        ? "000"
                                        : (switch (main.abonement_type.charAt(0)) {
                                            case '3', '5', '7' -> '0';      // Месячный
                                            case '2', '4', '6', '8' -> '1'; // Посуточный
                                            default -> '4';                 // Количество поездок
                                        } + String.format("%02d", main.seatstick_limit))
                                )
                                .p57(switch (main.carryon_type) {
                                    case 'Ж' -> '1'; // живность
                                    case 'Т' -> '2'; // телевизор
                                    case 'В' -> '3'; // велосипед
                                    case 'Р' -> '4'; // излишний вес ручной клади
                                    default  -> main.carryon_type;
                                })
                                .p58(main.benefitgroup_code.equals("22")
                                        ? (Integer.parseInt(adi.bilgroup_code) > 4 ? '1' : '0' )
                                        : null)
                                .p59(main.benefitgroup_code.equals("22")
                                        ? (adi.employee_cat == 'Ф' ? '1' : '0')
                                        : null)
                                .p60("000")
                                .p61(main.train_num.isBlank()
                                        ? '0'
                                        : main.train_num.trim().charAt(0))

                                .build()
                )
                .p33((long)switch (ticketType) {
                    case '1', '2', '3', '4', '5' -> main.pass_qty;
                    default -> main.carryon_weight;
                })
                .p34(0L)
                .p35(0L)
                .p36(main.tariff_sum)
                .p37(0L)
                .p38(0L)
                .p39(switch (main.oper) {
                    case 'O' -> main.fee_sum;
                    case 'V' -> main.refundfee_sum;
                    default -> 0L;
                })
                .p40(0L)
                .p41(0L)
                .p42(0L)
                .p43(0L)
                .p44(main.department_sum)
                .p45(0L)
                .p46(0L)
                .p47(0L)
                .p48(0L)
                .p49(0L)
                .p50(0L)
                .p51(main.oper_g == 'N' ? (long) switch (main.oper) {
                    case 'O' -> main.pass_qty;
                    case 'V' -> -main.pass_qty;
                    default -> 0;
                } : 0)
                .build();
    }

    @Override
    protected Lgot convertLgot(T1 t1) {

        // Используемые данные
        PrigMain main = record.getMain();
        PrigAdi   adi = record.getAdi();
        
        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .list("R064" + (main.benefitgroup_code.equals("22") ? 'Z' : 'G'))
                        .p1(1)
                        .p2(handbook.getRoad2(main.sale_station, main.operation_date))
                        .p3(handbook.getDepartment(main.sale_station, main.operation_date))
                        .p4(switch(main.request_subtype / 256){
                            case 0  -> '2';
                            case 1  -> '1';
                            default -> '0';
                        })
                        .p5(switch (new String(new char[]{main.oper, main.oper_g})) {
                            case "ON" -> '1';
                            case "OG" -> '2';
                            case "VN" -> '3';
                            case "OO" -> '4';
                            default   -> '0';
                        })
                        .p6(main.train_category)
                        .p7(t1.getKey().getP24())
                        .p8(String.valueOf(main.carriage_code))
                        .p9(handbook.getOkatoByRegion(main.benefit_region, main.operation_date))
                        .p10(adi.benefit_doc)
                        .p11(main.benefitgroup_code.equals("22")
                                ? adi.bilgroup_secur + adi.bilgroup_code
                                : main.benefit_region)
                        .p12(adi.employee_unit)
                        .p13(main.benefitgroup_code.equals("22")
                                ? adi.employee_cat
                                : null)
                        .p14(adi.surname + ' ' + adi.initials)
                        .p15(adi.dependent_surname + ' ' + adi.dependent_initials)
                        .p16((byte) (
                                main.abonement_type.equals("0 ")
                                        ?
                                        (main.oper_g == 'G'
                                                ? -1 :
                                                (main.oper == 'V'
                                                        ? 0 : 1
                                                ))
                                        : 0
                        ))
                        .p17(main.flg_2wayticket == '1')
                        // Такой же, как p16?
                        .p18((byte) (
                                main.abonement_type.equals("0 ")
                                        ?
                                        (main.oper_g == 'G'
                                                ? -1 :
                                                (main.oper == 'V'
                                                        ? 0 : 1
                                                ))
                                        : 0
                        ))
                        .p20(switch(main.abonement_type.trim()){
                            case "1" -> '9';
                            case "2" -> '7';
                            case "3" -> '0';
                            case "4" -> '1';
                            case "5" -> '2';
                            case "7" -> '4';
                            case "8" -> '5';
                            default -> null;
                        })
                        .p21(main.seatstick_limit)
                        .p22(main.operation_date)
                        .p23(main.ticket_begdate)
                        .p24(main.ticket_ser.substring(0, 2) + main.ticket_num)
                        .p25(main.departure_station)
                        .p26(main.arrival_station)
                        .p29(null)
                        .p30(main.server_datetime)
                        .p31(String.valueOf(main.server_reqnum))
                        .p32(adi.snils)
                        .build())
                .p19(main.pass_qty)
                .p27((int) (main.tariff_sum * 10))
                .p28((int) (main.tariff_sum - main.department_sum))
                .p33(main.carryon_weight)
                .build();
    }

    @Override
    protected void addTrips(T1 t1) {

        // Используемые данные
        PrigMain main = record.getMain();
        PrigRoute route = this.routes.getPrigRoute(main.departure_station, main.arrival_station, main.operation_date);

        t1.setKey(t1.getKey().toBuilder()
                .p13(route.getRoadStart())
                .p14(route.getDepartmentStart())
                .p16(route.getRegionStart())

                .p27(route.getRoadEnd())
                .p28(route.getDepartmentEnd())
                .p29(route.getRegionEnd())

                .p62(route.getMcdDistance())
                .p63(route.getMcdType())

                .build()
        );
    }

    @Override
    protected Set<T1> multiplyT1(T1 t1) {
        return trips.multiplyByTrips(t1, record.getMain());
    }
}