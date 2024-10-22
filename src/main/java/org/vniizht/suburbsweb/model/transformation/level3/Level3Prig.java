package org.vniizht.suburbsweb.model.transformation.level3;

import org.vniizht.suburbsweb.model.route.PrigRoute;
import org.vniizht.suburbsweb.model.transformation.level2.PrigAdi;
import org.vniizht.suburbsweb.model.transformation.level2.PrigCost;
import org.vniizht.suburbsweb.model.transformation.level2.PrigMain;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.transformation.conversion.Converter;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Routes;
import org.vniizht.suburbsweb.service.transformation.data.Trips;

import java.util.List;
import java.util.Set;

public final class Level3Prig extends Level3 <Level2Data.PrigRecord> {

    private final Trips trips;

    Level3Prig(Level2Data.PrigRecord record,
               Handbook handbook,
               Routes   routes,
               Trips    trips) {
        super(record, handbook, routes);
        this.trips = trips;
        transform();
    }

    @Override
    protected T1 convertT1() {

        // Используемые данные
        PrigMain        main = record.getMain();
        List<PrigCost>  cost = record.getCost();
        PrigAdi         adi  = record.getAdi();

        // Вычислено
        Character convertedTicketType = Converter.convertTicketType(main.abonement_type.charAt(0), main.flg_carryon, main.flg_fee_onboard, main.flg_2wayticket);

        return T1.builder().key(
                        T1.Key.builder()
                                .request_date(main.request_date)
                                .report_yyyymm(Converter.formatDate(main.operation_date, "yyyyMM"))
                                .p1("tab1")
                                .p2(1)
                                .p3(Converter.formatDate(main.operation_date, "yyyy"))
                                .p4(Converter.formatDate(main.operation_date, "mm"))
                                .p5("17")
                                .p6(handbook.getRoad3(main.sale_station, main.operation_date))
                                .p7(handbook.getRoad3(main.sale_station, main.operation_date))
                                .p8(Converter.convertSaleStation(main.sale_station))
                                .p9(Converter.convertCarriageCode(main.carriage_code))
                                .p10(main.region_code)
                                .p11(Converter.convertOkato(handbook.getOkatoByStation(main.sale_station, main.operation_date)))
                                .p12(Converter.convertDepartureDate2yymm(convertedTicketType, main.ticket_begdate, main.yyyymm))
                                .p15(main.departure_station)

                                .p17(Converter.convertOkato(handbook.getOkatoByStation(main.departure_station, main.operation_date)))
                                .p18(handbook.getArea(main.departure_station, main.operation_date))
                                .p19(Converter.convertTrainCategory(main.train_category))
                                .p20(Converter.convertCarriageClass(main.carriage_class))
                                .p21(convertedTicketType)
                                .p22(Converter.convertPassengerCategory(main.flg_bsp, main.flg_child, main.benefit_code))
                                .p23('3') // к = '3' з = '1'
                                .p24(main.benefitgroup_code + main.benefit_code)
                                .p25(Converter.convertPaymentType(main.paymenttype, handbook.getTSite(main.web_id, main.sale_station.substring(0, 2), main.operation_date), handbook.getPlagnVr(main.payagent_id, main.departure_station, main.operation_date)))
                                .p26(handbook.getGvc(main.benefitgroup_code, main.benefit_code, main.operation_date))

                                .p30(Converter.convertOkato(handbook.getOkatoByStation(main.arrival_station, main.operation_date)))
                                .p31(handbook.getArea(main.arrival_station, main.operation_date))
                                .p32((short) cost.stream().mapToInt(costItem -> costItem.route_distance).sum())

                                .p52(Converter.convertDocRegistration(handbook.getTSite(main.payagent_id, main.sale_station.substring(0, 2), main.operation_date), main.request_subtype))
                                .p53(String.valueOf(main.agent_code))
                                .p54(main.arrival_station)
                                .p55(Converter.convertAbonementType(main.abonement_type.charAt(0), main.abonement_subtype))
                                .p56(Converter.convertSeatStickLimit(main.seatstick_limit, main.abonement_type.charAt(0)))
                                .p57(Converter.convertCarrionType(main.carryon_type))
                                .p58(Converter.convert58(main.benefitgroup_code, adi.getBilgroup_code()))
                                .p59(Converter.convert59(main.benefitgroup_code, adi.getEmployee_cat()))
                                .p60("000")
                                .p61(Converter.covertMCD(main.train_num))

                                .build()
                )
                .p33(Converter.convertPassengersCount(convertedTicketType, main.pass_qty, main.carryon_weight))
                .p34(0L)
                .p35(0L)
                .p36(main.tariff_sum)
                .p37(0L)
                .p38(0L)
                .p39(Converter.convert39(main.oper, main.fee_sum, main.refundfee_sum))
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
                .p51(Converter.convertDocumentsCount(main.oper, main.oper_g, main.pass_qty))
                .build();
    }

    @Override
    protected Lgot convertLgot(T1 t1) {

        // Используемые данные
        PrigMain main = record.getMain();
        PrigAdi   adi = record.getAdi();
        
        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .list(Converter.convertPrigList(main.benefitgroup_code))
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
                        .p10(adi.getBenefit_doc())
                        .p11(main.benefitgroup_code.equals("22")
                                ? adi.getBilgroup_secur() + adi.getBilgroup_code()
                                : main.benefit_region)
                        .p12(adi.getEmployee_unit())
                        .p13(main.benefitgroup_code.equals("22")
                                ? adi.getEmployee_cat()
                                : null)
                        .p14(adi.getSurname() + ' ' + adi.getInitials())
                        .p15(adi.getDependent_surname() + ' ' + adi.getDependent_initials())
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
                        .p32(adi.getSnils())
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

        t1.toBuilder()
                .key(t1.getKey().toBuilder()
                        .p13(route.getRoadStart())
                        .p14(route.getDepartmentStart())
                        .p16(route.getRegionStart())

                        .p27(route.getRoadEnd())
                        .p28(route.getDepartmentEnd())
                        .p29(route.getRegionEnd())

                        .p62(route.getMcdDistance())
                        .p63(route.getMcdType())

                        .build()
                )
                .build();
    }

    @Override
    protected Set<T1> multiplyT1(T1 t1) {
        return trips.multiplyByTrips(t1, record.getMain());
    }
}