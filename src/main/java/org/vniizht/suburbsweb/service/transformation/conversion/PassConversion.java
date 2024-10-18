package org.vniizht.suburbsweb.service.transformation.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.PassCost;
import org.vniizht.suburbsweb.model.transformation.level2.PassEx;
import org.vniizht.suburbsweb.model.transformation.level2.PassMain;
import org.vniizht.suburbsweb.model.transformation.level3.co22.*;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.handbook.Handbook;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Service
@Scope("singleton")
public class PassConversion {

    @Autowired private Logger     logger;
    @Autowired private Handbook handbook;

    public List<T1> getT1(Level2Data.PassRecord l2) {

        // Даты
        Date    operationDate = l2.main.getOper_date(),
                departureDate = l2.main.getDeparture_date(),
                arrivalDate   = l2.main.getArrival_date();

        // Субъекты
        String operationStation = l2.main.getSale_station(),
                     depStation = l2.main.getDeparture_station(),
                     arrStation = l2.main.getArrival_station();

        // Прочее
        String  benefitCode = l2.main.getBenefit_code();
        Character paymentType = l2.main.getPaymenttype(),
                carriageClass = l2.main.getCarriage_class();

        List<T1> t1List = new ArrayList<>();

        for (int i = 0; i < l2.ex.size(); i++) {
            // Прочее
            String   lgotInfo = l2.ex.get(i).getLgot_info(),
                    benefitGroupCode = l2.ex.get(i).getLgot_info().substring(0, 2); // !!!
            t1List.add(T1.builder().key(
                            T1.Key.builder()
                                    .p1("tab1")
                                    .p2(i + 1)
                                    .p3(Converter.formatDate(operationDate, "yyyy"))
                                    .p4(Converter.formatDate(operationDate, "mm"))
                                    .p5("17")
                                    .p6(handbook.getRoad3(operationStation, operationDate))
                                    .p7(handbook.getRoad3(operationStation, operationDate))
                                    .p8(Converter.convertSaleStation(operationStation))
                                    .p9(Converter.convertCarriageCode(l2.main.getCarrier_code()))
                                    .p10(l2.main.getSaleregion_code())
                                    .p11(Converter.convertOkato(handbook.getOkatoByStation(operationStation, operationDate)))
                                    .p12(Converter.formatDate(departureDate, "yymm"))
                                    .p15(depStation)
                                    .p17(Converter.convertOkato(handbook.getOkatoByStation(depStation, departureDate)))
                                    .p18(handbook.getArea(depStation, operationDate))
                                    .p19('4')
                                    .p20(Converter.convertCarriageClass(carriageClass))
                                    .p21('1')
                                    .p22(Converter.convertPassengerCategory(List.of(l2.main.getF_tick()), benefitCode))
                                    .p23('3')
                                    .p24(Converter.convertBenefitCode(benefitCode, paymentType, l2.main.getMilitary_code(), lgotInfo))
                                    .p25(Converter.convertPaymentType(paymentType))
                                    .p26(handbook.getGvc(benefitGroupCode, benefitCode, operationDate))
                                    .p30(Converter.convertOkato(handbook.getOkatoByStation(arrStation, arrivalDate)))
                                    .p31(handbook.getArea(arrStation, arrivalDate))
                                    .p32(l2.main.getDistance())
                                    .p52('1')
                                    .p53(String.valueOf(l2.main.getAgent_code()))
                                    .p54(arrStation)
                                    .p55(null)
                                    .p56("000")
                                    .p57(null)
                                    .p58(Converter.convert58(lgotInfo))
                                    .p59(Converter.convert59(paymentType, lgotInfo))
                                    .p60(String.valueOf(l2.main.getSubagent_code()))
                                    .p61(null)
                                    .p62(null)
                                    .p63(null)
                                    .build()
                    )
                    .p33(Long.valueOf(l2.main.getSeats_qty()))
                    .p34(0L)
                    .p35(0L)
                    .p36((long) (l2.cost.stream().mapToDouble(PassCost::getSum_nde).sum() / 10))
                    .p37(0L)
                    .p38(0L)
                    .p39(l2.cost.stream().mapToLong(cost -> Converter.convert39(cost.getSum_nde(), cost.getSum_code())).sum())
                    .p40(l2.cost.stream().mapToLong(cost -> Converter.convert40(cost.getSum_nde(), cost.getSum_code())).sum())
                    .p41(0L)
                    .p42(0L)
                    .p43(0L)
                    .p44(l2.cost.stream().mapToLong(cost -> Converter.convert44(cost.getSum_nde(), cost.getSum_code(), paymentType)).sum())
                    .p45(0L)
                    .p46(0L)
                    .p47(l2.cost.stream().mapToLong(cost -> Converter.convert47(cost.getSum_nde(), cost.getSum_code(), paymentType)).sum())
                    .p48(l2.cost.stream().mapToLong(cost -> Converter.convert48(cost.getSum_nde(), cost.getSum_code(), paymentType)).sum())
                    .p49(0L)
                    .p50(0L)
                    .p51(Converter.convertDocumentsCount(l2.main.getOper(), l2.main.getOper_g(), (short) 1))
                    .build());
        }

        return t1List;
    }

    public Lgot getLgot(Level2Data.PassRecord l2, T1 t1) {
        PassMain main = l2.main;
        PassCost cost = l2.cost.get(0); // ??
        PassEx   ex   = l2.ex.get(t1.getKey().getP2() - 1);

        String benefitGroupCode = ex.getLgot_info().substring(0, 2); // !!!
        Character paymentType = l2.main.getPaymenttype();

        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .list(Converter.convertPassList(paymentType, ex.getLgot_info()))
                        .p1(t1.getKey().getP2())
                        .p2(handbook.getRoad2(main.getSale_station(), main.getOper_date()))
                        .p3(handbook.getDepartment(main.getSale_station(), main.getOper_date()))
                        .p4('0')
                        .p5(switch (new String(new char[]{main.getOper(), main.getOper_g()})) {
                            case "ON" -> '1';
                            case "OG" -> '2';
                            case "VN" -> '3';
                            case "OO" -> '4';
                            case "VO" -> '5';
                            default   -> '0';
                        })
                        .p6('1')
                        .p7(t1.getKey().getP24())
                        .p8(String.valueOf(main.getCarrier_code()))
                        .p9(handbook.getOkatoByRegion(main.getBenefitcnt_code(), main.getOper_date())) // Точно код государства??
                        .p10(ex.getNomlgud())
                        .p11(benefitGroupCode.equals("22")
                                ? ex.getLgot_info().substring(8, 13)
                                : main.getSaleregion_code())
                        .p12(benefitGroupCode.equals("22")
                                ? ex.getLgot_info().substring(14, 24)
                                : null)
                        .p13(benefitGroupCode.equals("22")
                                ? ex.getLgot_info().charAt(6)
                                : null)
                        .p14(ex.getLast_name() + ' ' + ex.getFirst_name().charAt(0) + ex.getPatronymic().charAt(0))
                        .p15(null)
                        .p16((byte) (main.getOper_g() == 'G'
                                ? -1 :
                                (main.getOper() == 'V'
                                        ? 0 : 1
                                ))
                        )
                        .p17(main.getTrip_direction() == '3')
                        .p18(null)
                        .p20(null)
                        .p21(null)
                        .p22(main.getOper_date())
                        .p23(main.getDeparture_date())
                        .p24(ex.getTicket_ser().substring(0, 2) + ex.getTicket_num())
                        .p25(main.getDeparture_station())
                        .p26(main.getArrival_station())
                        .p29(null)
                        .p30(Converter.formatDate(main.getRequest_date(), main.getRequest_time(), "ddMMyyHHmm"))
                        .p31(null)
                        .p32(ex.getSnils())
                        .build())
                .p19(main.getSeats_qty())
                .p27((int) (cost.getSum_nde() * 10))
                .p28((int) switch (cost.getSum_code()) {
                    case 101:
                    case 102:
                    case 116: yield switch (cost.getPaymenttype()) {
                        case '1':
                        case '6':
                        case '8': yield cost.getSum_nde();
                        default: yield 0;
                    };
                    default: yield 0;
                })
                .p33(null)
                .build();
    }

//    @Getter
//    @Setter
//    @Builder
//    static public class Converted {
//        private T1 t1;
//        private T2 t2;
//        private T3 t3;
//        private T4 t4;
//        private T5 t5;
//        private T6 t6;
////        private Reestr reestr;
////        private Stat stat;
//    }
}
