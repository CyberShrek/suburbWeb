package org.vniizht.suburbsweb.service.transformation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level3.co22.*;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Reestr;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Stat;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.NsiData;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class Conversion {

    @Autowired private Logger     logger;
    @Autowired private NsiData    nsiData;

    public Converted convert(Level2Data.Record l2Record) {
//        logger.log("\tЦО22");
        T1 t1 = createT1(l2Record, 1);
//        T2 t2 = createT2(l2Record,(short)1, t1);
//        T3 t3 = createT3(l2Record,(short)1, t1);
//        T4 t4 = createT4(l2Record,(short)1, t1);
//        T5 t5 = createT5(l2Record,          t1, t2, t3);
//        T6 t6 = createT6(l2Record,(short)1, t1);
//        Stat   stat   = createStat  (l2Record);
//        Reestr reestr = createReestr(l2Record);
        return Converted.builder()
                .t1(t1)
//                .t2(t2)
//                .t3(t3)
//                .t4(t4)
//                .t5(t5)
//                .t6(t6)
//                .stat(stat)
//                .reestr(reestr)
                .build();
    }

    private T1 createT1(Level2Data.Record l2, int serial) {
//        logger.log("\t\tprig_co22_t1");

        // Даты
        Date operationDate = l2.main.getOperation_date(),
             ticketBegDate = l2.main.getTicket_begdate(),
             ticketEndDate = l2.main.getTicket_enddate();

        // Субъекты
        String operationStation = l2.main.getSale_station(),
                     depStation = l2.main.getDeparture_station(),
                     arrStation = l2.main.getArrival_station(),
               operationCountry = operationStation.substring(0, 2);

        // Флаги
        Character  so = l2.main.getFlg_so(),
                  bsp = l2.main.getFlg_bsp(),
                child = l2.main.getFlg_child(),
              carrion = l2.main.getFlg_carryon(),
              onboard = l2.main.getFlg_fee_onboard(),
               twoWay = l2.main.getFlg_2wayticket();


        // Прочее
        String   benefitCode = l2.main.getBenefit_code(),
                        webId = l2.main.getWeb_id(),
                   payagentId = l2.main.getPayagent_id();
        Character paymentType = l2.main.getPaymenttype(),
                abonementType = l2.main.getAbonement_type().charAt(0),
                  carrionType = l2.main.getCarryon_type(),
                trainCategory = l2.main.getTrain_category(),
                carriageClass = l2.main.getCarriage_class();

        // Конвертировано
        Character convertedTicketType = Converter.convertTicketType(abonementType, carrion, onboard, twoWay);

        // Игнорировать если действие билета истекло
        return T1.builder()
                .p1("tab1")
                .p2(serial)
                .p3(Converter.date2yyyy(operationDate)).p4(Converter.date2mm(operationDate))
                .p5("17")
//                .p6(nsiData.getRoad(operationStation, operationDate))
//                .p7(nsiData.getRoad(operationStation, operationDate))
                .p8(operationStation)
                .p9(Converter.convertCarriageCode(l2.main.getCarriage_code()))
                .p10("00")
                .p11(Converter.convertOkato(nsiData.getOkato(operationStation, operationDate)))
                .p12(Converter.convertDepartureDate2yymm(convertedTicketType, ticketBegDate, l2.main.getYyyymm()))
                .p13("ждёт функции")
                .p14("ждёт функции")
                .p15(depStation)
                .p16("ждёт функции")
//                .p17(Converter.convertOkato(nsiData.getOkato(depStation, operationDate)))
//                .p18(nsiData.getArea(depStation, operationDate))
                .p19(Converter.convertTrainCategory(trainCategory))
                .p20(Converter.convertCarriageClass(carriageClass))
                .p21(convertedTicketType)
                .p22(Converter.convertPassengerCategory(bsp, child, benefitCode))
                .p23('3')
                .p24(benefitCode)
//                .p25(Converter.convertPaymentType(paymentType, nsiData.getTSite(webId, operationCountry, operationDate), nsiData.getPlagnVr(payagentId, operationCountry, operationDate)))
//                .p26(nsiData.getNomgvc(l2.main.getBenefitgroup_code(), l2.main.getBenefit_code(), operationDate))
                .p27("ждёт функции")
                .p28("ждёт функции")
                .p29("ждёт функции")
//                .p30(Converter.convertOkato(nsiData.getOkato(arrStation, operationDate)))
//                .p31(nsiData.getArea(arrStation, operationDate))
                .p32((short) l2.cost.stream().mapToInt(Cost::getRoute_distance).sum())
                .p33(Converter.convertPassengersCount(convertedTicketType, l2.main.getPass_qty(), l2.main.getCarryon_weight()))
                .p34(0L)
                .p35(0L)
                .p36(l2.main.getTariff_sum())
                .p37(0L)
                .p38(0L)
                .p39(0L)
                .p40(0L)
                .p41(0L)
                .p42(0L)
                .p43(0L)
                .p44(l2.main.getDepartment_sum())
                .p45(0L)
                .p46(0L)
                .p47(0L)
                .p48(0L)
                .p49(0L)
                .p50(0L)
                .p51(Converter.convertDocumentsCount(l2.main.getOper(), l2.main.getOper_g(), l2.main.getPass_qty()))
                .p52('?') // ?
                .p53(String.valueOf(l2.main.getAgent_code()))
                .p54(arrStation)
                .p55(Converter.convertAbonementType(abonementType))
                .p56(Converter.convertSeatStickLimit(l2.main.getSeatstick_limit()))
                .p57(Converter.convertCarrionType(carrionType))
                .p58(so)
                .p59(so)
                .p60("000")
                .p61('?')
                .p62((short) -1)
                .p63('?')
                .routes(l2.cost.stream().map(Cost::getRoute_num).sorted().map(String::valueOf).collect(Collectors.joining(" ")))
                .build();
    }

    private T2 createT2(Level2Data.Record l2, short serial, T1 t1) {
//        logger.log("\t\tprig_co22_t2");
        return T2.builder()
                .p1("tab2")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
                .p5("ждёт функции")
                .p6("ждёт функции")
                .p7((short) -1)
                .build();
    }

    private T3 createT3(Level2Data.Record l2, short serial, T1 t1) {
//        logger.log("\t\tprig_co22_t3");
        return T3.builder()
                .p1("tab3")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
                .p5("ждёт функции")
                .p6("ждёт функции")
                .p7((short) -1)
                .build();
    }

    private T4 createT4(Level2Data.Record l2, short serial, T1 t1) {
//        logger.log("\t\tprig_co22_t4");
        return T4.builder()
                .p1("tab4")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
                .p5("ждёт функции")
                .p6("ждёт функции")
                .p7(-1L)
                .p8(-1L)
                .p9((short) -1)
                .build();
    }

    private T5 createT5(Level2Data.Record l2, T1 t1, T2 t2, T3 t3) {
//        logger.log("\t\tprig_co22_t5");
        return T5.builder()
                .p1("tab5")
                .p2(t1.getP5())
                .p3(1)
                .p4(1)
                .p5(1)
                .p6(t1.getP33())
                .p7(0L) // ??
                .p8(0L) // ??
                .p9(t1.getP36())
                .p10(t1.getP44())
                .build();
    }

    private T6 createT6(Level2Data.Record l2, short serial, T1 t1) {
//        logger.log("\t\tprig_co22_t6");
        return T6.builder()
                .p1("tab6")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
                .p5("ждёт функции")  // берётся из функции
                .p6(-1)  // берётся из функции
                .p7((short) -1)  // берётся из функции
                .build();
    }
    
    private Reestr createReestr(Level2Data.Record l2, T1 t1) {
//        logger.log("\t\tprig_lgot_reestr");
        return Reestr.builder()
                .build();
    }
    
    private Stat createStat(Level2Data.Record l2, T1 t1) {
//        logger.log("\t\tprig_lgot_stat");
        return Stat.builder()
                .list("Имя файла - ?")
                .build();
    }

    @Getter
    @Setter
    @Builder
    static public class Converted {
        private T1 t1;
        private T2 t2;
        private T3 t3;
        private T4 t4;
        private T5 t5;
        private T6 t6;
//        private Reestr reestr;
//        private Stat stat;
    }
}
