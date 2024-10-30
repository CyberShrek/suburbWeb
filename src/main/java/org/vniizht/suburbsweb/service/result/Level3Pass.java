package org.vniizht.suburbsweb.service.result;

import org.vniizht.suburbsweb.service.data.entities.route.PassRoute;
import org.vniizht.suburbsweb.service.data.entities.level2.PassCost;
import org.vniizht.suburbsweb.service.data.entities.level2.PassEx;
import org.vniizht.suburbsweb.service.data.entities.level2.PassMain;
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
    protected T1 convertToT1(Level2Dao.PassRecord record) {
        // Используемые данные
        PassMain        main = record.getMain();
        List<PassCost>  cost = record.getCost();
        PassEx          ex   = record.getEx().isEmpty() ? null : record.getEx().get(0);
        
        return T1.builder().key(
                        T1.Key.builder()
                                .request_date(main.requestDate)
                                .yyyymm(Integer.parseInt(Util.formatDate(main.oper_date, "yyyyMM")))
                                .p1("tab1")
                                .p2(1)
                                .p3(Util.formatDate(main.oper_date, "yyyy"))
                                .p4(Util.formatDate(main.oper_date, "mm"))
                                .p5("17")
                                .p6(handbook.getRoad3(main.sale_station, main.oper_date))
                                .p7(handbook.getRoad3(main.sale_station, main.oper_date))
                                .p8("00" + main.sale_station)
                                .p9(String.format("%09d", main.carrier_code))
                                .p10(main.saleregion_code)
                                .p11(handbook.getOkatoByStation(main.sale_station, main.oper_date))
                                .p12(Util.formatDate(main.departure_date, "yymm"))
                                .p15(main.departure_station)
                                .p17(handbook.getOkatoByStation(main.departure_station, main.departure_date))
                                .p18(handbook.getArea(main.departure_station, main.oper_date))
                                .p19('4')
                                .p20("0" + main.carriage_class)
                                .p21('1')
                                .p22(
                                        Objects.requireNonNull(main.f_tick).length > 2 && main.f_tick[2] == 1 ? '2'                                        // Детский
                                        : !main.benefit_code.equals("000") || main.f_tick.length > 4 && main.f_tick[4] == 1 ? '3'  // Льготный
                                        :  main.f_tick.length > 1 && main.f_tick[1] == 1 ? '1'                                     // Полный
                                        : '4'                                                       // Бесплатный
                                )
                                .p23('3')
                                .p24(main.paymenttype == 'В'
                                        ? "21" + String.format("%02d", main.military_code)
                                        : ex != null && ex.lgot_info != null && !main.benefit_code.equals("000") && !main.benefit_code.equals("013")
                                        ? ex.lgot_info.substring(1, 4)
                                        : null)
                                .p25(switch (main.paymenttype) {
                                    case '8' -> '3';            // Банковские карты
                                    case '9', 'В', 'Б' -> '1';  // Льготные
                                    case '1', '3' -> '2';       // Наличные
                                    case '6' -> '5';            // Безнал для юр. лиц
                                    default -> '4';             // Электронный кошелёк
                                })
                                .p26(ex == null ? null : handbook.getGvc(ex.lgot_info.substring(0, 2), main.benefit_code, main.oper_date))
                                .p30(handbook.getOkatoByStation(main.arrival_station, main.arrival_date))
                                .p31(handbook.getArea(main.arrival_station, main.arrival_date))
                                .p32(main.distance)
                                .p52('1')
                                .p53(String.valueOf(main.agent_code))
                                .p54(main.arrival_station)
                                .p55(null)
                                .p56("000")
                                .p57(null)
                                .p58(ex == null ? null : switch (ex.lgot_info.charAt(9)) {
                                    case '0', '1', '2', '3', '4' -> '0';
                                    case '5', '6', '7', '8', '9' -> '1';
                                    default -> null;
                                })
                                .p59(main.paymenttype == 'Ж' && ex != null && ex.lgot_info.startsWith("22")
                                        ? switch (ex.lgot_info.charAt(5)){
                                            case 'Ф', 'Д' -> '1';
                                            default -> '0';
                                        }
                                        : '0'
                                )
                                .p60(String.valueOf(main.subagent_code))
                                .p61(null)
                                .p62(null)
                                .p63(null)
                                .build()
                )
                .p33(Long.valueOf(main.seats_qty))
                .p34(0L)
                .p35(0L)
                .p36((long) (cost.stream().mapToDouble(costItem -> costItem.sum_nde).sum() / 10))
                .p37(0L)
                .p38(0L)
                .p39(cost.stream().mapToLong(costItem -> switch (costItem.sum_code) {
                    case 104, 105, 106 -> (long) Math.round(costItem.sum_nde);
                    default -> 0L;
                }).sum())
                .p40(cost.stream().mapToLong(costItem -> costItem.sum_code == 101 ? (long) Math.round(costItem.sum_nde) : 0L).sum())
                .p41(0L)
                .p42(0L)
                .p43(0L)
                .p44(cost.stream().mapToLong(costItem -> switch (costItem.sum_code) {
                    case 101, 116 -> switch (main.paymenttype) {
                        case 'Б', 'В', 'Ж', '9' -> (long) Math.round(costItem.sum_nde);
                        default -> 0L;
                    };
                    default -> 0L;
                }).sum())
                .p45(0L)
                .p46(0L)
                .p47(cost.stream().mapToLong(costItem -> switch (costItem.sum_code) {
                    case 104, 105, 106 -> switch (main.paymenttype) {
                        case 'Б', 'В', 'Ж', '9' -> (long) Math.round(costItem.sum_nde);
                        default -> 0L;
                    };
                    default -> 0L;
                }).sum())
                .p48(cost.stream().mapToLong(costItem -> costItem.sum_code == 101 ? switch (main.paymenttype) {
                    case 'Б', 'В', 'Ж', '9' -> (long) Math.round(costItem.sum_nde);
                    default -> 0L;
                } : 0L).sum())
                .p49(0L)
                .p50(0L)
                .p51(main.oper_g == 'N' ? (long) switch (main.oper) {
                    case 'O' -> 1;
                    case 'V' -> -1;
                    default -> 0;
                } : 0)
                .build();
        }

    @Override
    protected Lgot convertToLgot(Level2Dao.PassRecord record, T1 t1) {
        PassMain main = record.getMain();
        PassCost cost = record.getCost().isEmpty() ? null : record.getCost().get(0); // ??
        PassEx   ex   = record.getEx().isEmpty() ? null : record.getEx().get(0);

        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .yyyymm(Integer.parseInt(Util.formatDate(main.oper_date, "yyyyMM")))
                        .list( "R800" + (main.paymenttype == 'Ж' && ex != null && ex.lgot_info.startsWith("22") ? 'Z' : 'G'))
                        .p1(t1.getKey().getP2())
                        .p2(handbook.getRoad2(main.sale_station, main.oper_date))
                        .p3(handbook.getDepartment(main.sale_station, main.oper_date))
                        .p4('0')
                        .p5(switch (new String(new char[]{main.oper, main.oper_g})) {
                            case "ON" -> '1';
                            case "OG" -> '2';
                            case "VN" -> '3';
                            case "OO" -> '4';
                            case "VO" -> '5';
                            default   -> '0';
                        })
                        .p6('1')
                        .p7(t1.getKey().getP24())
                        .p8(String.valueOf(main.carrier_code))
                        .p9(handbook.getOkatoByRegion(main.benefitcnt_code, main.oper_date)) // Точно код государства??
                        .p10(ex == null ? null : ex.nomlgud)
                        .p11(ex != null && ex.lgot_info.startsWith("22")
                                ? ex.lgot_info.substring(7, 12)
                                : main.saleregion_code)
                        .p12(ex != null && ex.lgot_info.startsWith("22")
                                ? ex.lgot_info.substring(13, 23)
                                : null)
                        .p13(ex != null && ex.lgot_info.startsWith("22")
                                ? ex.lgot_info.charAt(5)
                                : null)
                        .p14(ex == null ? null : ex.last_name + ' ' + ex.first_name.charAt(0) + ex.patronymic.charAt(0))
                        .p15(null)
                        .p16((byte) (main.oper_g == 'G'
                                ? -1 :
                                (main.oper == 'V'
                                        ? 0 : 1
                                ))
                        )
                        .p17(main.trip_direction == '3')
                        .p18(null)
                        .p20(null)
                        .p21(null)
                        .p22(main.oper_date)
                        .p23(main.departure_date)
                        .p24(ex == null ? null : ex.ticket_ser.substring(0, 2) + ex.ticket_num)
                        .p25(main.departure_station)
                        .p26(main.arrival_station)
                        .p29(null)
                        .p30(Util.formatDate(new Date(main.requestDate.getTime() + main.request_time.getTime()), "ddMMyyHHmm"))
                        .p31(null)
                        .p32(ex == null ? null :ex.snils)
                        .build())
                .p19(main.seats_qty)
                .p27((int) (cost == null ? 0 : cost.sum_te * 10))
                .p28((int) switch (cost == null ? 0 : cost.sum_code) {
                    case 101:
                    case 102:
                    case 116: yield switch (main.paymenttype) {
                        case '1':
                        case '6':
                        case '8': yield cost.sum_nde;
                        default: yield 0;
                    };
                    default: yield 0;
                })
                .p33(null)
                .build();
    }

    @Override
    protected void addT1Trips(Level2Dao.PassRecord record, T1 t1) {
        // Используемые данные
        PassMain main = record.getMain();
        PassRoute route = this.routes.getPassRoute(
                main.train_num,
                main.train_thread,
                main.departure_date,
                main.departure_station,
                main.arrival_station);

        t1.setKey(t1.getKey().toBuilder()
                .p13(route.getRoadStart())
                .p14(route.getDepartmentStart())
                .p16(route.getRegionStart())

                .p27(route.getRoadEnd())
                .p28(route.getDepartmentEnd())
                .p29(route.getRegionEnd())

                .build()
        );
    }

    @Override
    protected Set<T1> multiplyT1(Level2Dao.PassRecord record, T1 t1) {
        PassMain main = record.getMain();
        Set<T1> result = new HashSet<>(Set.of(t1));
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
}