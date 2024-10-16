package org.vniizht.suburbsweb.service.transformation.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.PassCost;
import org.vniizht.suburbsweb.model.transformation.level3.co22.*;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.handbook.Handbook;

import java.util.Date;

@Service
@Scope("singleton")
public class PassConversion {

    @Autowired private Logger     logger;
    @Autowired private Handbook handbook;

    public T1 convert(Level2Data.PassRecord l2) {
        return createT1(l2);
    }

    private T1 createT1(Level2Data.PassRecord l2) {

        // Даты
        Date    operationDate = l2.main.getOper_date(),
                departureDate = l2.main.getDeparture_date(),
                arrivalDate   = l2.main.getArrival_date();

        // Субъекты
        String operationStation = l2.main.getSale_station(),
                     depStation = l2.main.getDeparture_station(),
                     arrStation = l2.main.getArrival_station();

        // Прочее
        String   lgotInfo = l2.ex.get(0).getLgot_info(),
                benefitCode = l2.main.getBenefit_code(),
                benefitGroupCode = l2.ex.get(0).getLgot_info().substring(0, 2);
        Character paymentType = l2.main.getPaymenttype(),
                carriageClass = l2.main.getCarriage_class();

        return T1.builder().key(
                T1.Key.builder()
                        .p1("tab1")
                        .p2(1)
                        .p3(Converter.date2yyyy(operationDate))
                        .p4(Converter.date2mm(operationDate))
                        .p5("17")
                        .p6(handbook.getRoad3(operationStation, operationDate))
                        .p7(handbook.getRoad3(operationStation, operationDate))
                        .p8(Converter.convertSaleStation(operationStation))
                        .p9(Converter.convertCarriageCode(l2.main.getCarrier_code()))
                        .p10(l2.main.getSaleregion_code())
                        .p11(Converter.convertOkato(handbook.getOkatoByStation(operationStation, operationDate)))
                        .p12(Converter.date2yymm(departureDate))
                        .p15(depStation)
                        .p17(Converter.convertOkato(handbook.getOkatoByStation(depStation, departureDate)))
                        .p18(handbook.getArea(depStation, operationDate))
                        .p19('4')
                        .p20(Converter.convertCarriageClass(carriageClass))
                        .p21('1')
                        .p22(Converter.convertPassengerCategory(l2.main.getF_tick(), benefitCode))
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
                        .list(Converter.convertPassList(paymentType, lgotInfo))
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
