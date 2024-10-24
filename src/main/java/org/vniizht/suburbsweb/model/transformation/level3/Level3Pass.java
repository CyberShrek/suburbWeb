package org.vniizht.suburbsweb.model.transformation.level3;

import org.vniizht.suburbsweb.model.route.PassRoute;
import org.vniizht.suburbsweb.model.transformation.level2.PassCost;
import org.vniizht.suburbsweb.model.transformation.level2.PassEx;
import org.vniizht.suburbsweb.model.transformation.level2.PassMain;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.transformation.conversion.Converter;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Routes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Level3Pass extends Level3 <Level2Data.PassRecord> {
    
    Level3Pass(Level2Data.PassRecord record,
               Handbook handbook,
               Routes   routes) {
        super(record, handbook, routes);
        transform();
    }

    @Override
    protected T1 convertT1() {
        // Используемые данные
        PassMain        main = record.getMain();
        List<PassCost>  cost = record.getCost();
        PassEx          ex   = record.getEx().get(0);
        
        // Прочее
        String   lgotInfo = ex.lgot_info,
                benefitGroupCode = ex.lgot_info.substring(0, 2); // !!!
        return T1.builder().key(
                        T1.Key.builder()
                                .request_date(main.request_date)
                                .report_yyyymm(Converter.formatDate(main.oper_date, "yyyyMM"))
                                .p1("tab1")
                                .p2(1)
                                .p3(Converter.formatDate(main.oper_date, "yyyy"))
                                .p4(Converter.formatDate(main.oper_date, "mm"))
                                .p5("17")
                                .p6(handbook.getRoad3(main.sale_station, main.oper_date))
                                .p7(handbook.getRoad3(main.sale_station, main.oper_date))
                                .p8(Converter.convertSaleStation(main.sale_station))
                                .p9(Converter.convertCarriageCode(main.carrier_code))
                                .p10(main.saleregion_code)
                                .p11(Converter.convertOkato(handbook.getOkatoByStation(main.sale_station, main.oper_date)))
                                .p12(Converter.formatDate(main.departure_date, "yymm"))
                                .p15(main.departure_station)
                                .p17(Converter.convertOkato(handbook.getOkatoByStation(main.departure_station, main.departure_date)))
                                .p18(handbook.getArea(main.departure_station, main.oper_date))
                                .p19('4')
                                .p20(Converter.convertCarriageClass(main.carriage_class))
                                .p21('1')
                                .p22(Converter.convertPassengerCategory(List.of(main.f_tick), main.benefit_code))
                                .p23('3')
                                .p24(Converter.convertBenefitCode(main.benefit_code, main.paymenttype, main.military_code, lgotInfo))
                                .p25(Converter.convertPaymentType(main.paymenttype))
                                .p26(handbook.getGvc(benefitGroupCode, main.benefit_code, main.oper_date))
                                .p30(Converter.convertOkato(handbook.getOkatoByStation(main.arrival_station, main.arrival_date)))
                                .p31(handbook.getArea(main.arrival_station, main.arrival_date))
                                .p32(main.distance)
                                .p52('1')
                                .p53(String.valueOf(main.agent_code))
                                .p54(main.arrival_station)
                                .p55(null)
                                .p56("000")
                                .p57(null)
                                .p58(Converter.convert58(lgotInfo))
                                .p59(Converter.convert59(main.paymenttype, lgotInfo))
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
                .p39(cost.stream().mapToLong(costItem -> Converter.convert39(costItem.sum_nde, costItem.sum_code)).sum())
                .p40(cost.stream().mapToLong(costItem -> Converter.convert40(costItem.sum_nde, costItem.sum_code)).sum())
                .p41(0L)
                .p42(0L)
                .p43(0L)
                .p44(cost.stream().mapToLong(costItem -> Converter.convert44(costItem.sum_nde, costItem.sum_code, main.paymenttype)).sum())
                .p45(0L)
                .p46(0L)
                .p47(cost.stream().mapToLong(costItem -> Converter.convert47(costItem.sum_nde, costItem.sum_code, main.paymenttype)).sum())
                .p48(cost.stream().mapToLong(costItem -> Converter.convert48(costItem.sum_nde, costItem.sum_code, main.paymenttype)).sum())
                .p49(0L)
                .p50(0L)
                .p51(Converter.convertDocumentsCount(main.oper, main.oper_g, (short) 1))
                .build();
        }

    @Override
    protected Lgot convertLgot(T1 t1) {
        PassMain main = record.getMain();
        PassCost cost = record.getCost().get(0); // ??
        PassEx   ex   = record.getEx().get(0);

        String benefitGroupCode = ex.lgot_info.substring(0, 2);

        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .list(Converter.convertPassList(main.paymenttype, ex.lgot_info))
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
                        .p10(ex.nomlgud)
                        .p11(benefitGroupCode.equals("22")
                                ? ex.lgot_info.substring(7, 12)
                                : main.saleregion_code)
                        .p12(benefitGroupCode.equals("22")
                                ? ex.lgot_info.substring(13, 23)
                                : null)
                        .p13(benefitGroupCode.equals("22")
                                ? ex.lgot_info.charAt(5)
                                : null)
                        .p14(ex.last_name + ' ' + ex.first_name.charAt(0) + ex.patronymic.charAt(0))
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
                        .p24(ex.ticket_ser.substring(0, 2) + ex.ticket_num)
                        .p25(main.departure_station)
                        .p26(main.arrival_station)
                        .p29(null)
                        .p30(Converter.formatDate(main.request_date, main.request_time, "ddMMyyHHmm"))
                        .p31(null)
                        .p32(ex.snils)
                        .build())
                .p19(main.seats_qty)
                .p27((int) (cost.department_sum * 10))
                .p28((int) switch (cost.sum_code) {
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
    protected void addTrips(T1 t1) {
        // Используемые данные
        PassMain main = record.getMain();
        PassRoute route = this.routes.getPassRoute(main.train_num, main.train_thread, main.departure_date, main.departure_station, main.arrival_station);
        
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
    protected Set<T1> multiplyT1(T1 t1) {
        PassMain main = record.getMain();
        Set<T1> result = new HashSet<>(Set.of(t1));
        if (!main.oper_date.equals(main.departure_date)) {
            result.add(t1.toBuilder()
                    .key(t1.getKey().toBuilder()
                            .report_yyyymm(Converter.formatDate(main.departure_date, "yyyyMM"))
                            .build()
                    )
                    .build()
            );
        }
        return result;
    }
}