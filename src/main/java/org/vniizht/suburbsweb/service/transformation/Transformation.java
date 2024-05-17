package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.tables_co22.*;
import org.vniizht.suburbsweb.model.transformation.level3.tables_lgot.Reestr;
import org.vniizht.suburbsweb.model.transformation.level3.tables_lgot.Stat;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;
import org.vniizht.suburbsweb.service.transformation.data.NsiData;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private Logger     logger;
    @Autowired private NsiData    nsiData;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;

    @PostConstruct
    public synchronized void run() {
        logger.log("Запуск трансформации данных по пригороду со второго уровня в третий");
        Long lastLevel3Id = level3Data.getLastId();

        logger.log("Последний индекс третьего уровня: " + lastLevel3Id + "; ищу записи второго уровня с индексом больше этого значения");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);

        logger.log("Найдено записей: " + records.size());
        records.forEach(this::transform);

        logger.log("Конец трансформации");
    }

    private void transform(Long id, Level2Data.Record l2Record) {
        logger.log("Трансформация " + id);

        logger.log("\tЦО22");
        T1 t1 = createT1(l2Record, 1);
        T2 t2 = createT2(l2Record,(short)1, t1);
        T3 t3 = createT3(l2Record,(short)1, t1);
        T4 t4 = createT4(l2Record,(short)1, t1);
        T5 t5 = createT5(l2Record,          t1, t2, t3);
        T6 t6 = createT6(l2Record,(short)1, t1);

        logger.log("\tЛьготные");
        Reestr reestr = createReestr(l2Record);
        Stat   stat   = createStat  (l2Record);
    }

    private T1 createT1(Level2Data.Record l2, int serial) {
        logger.log("\t\tprig_co22_t1");

        // Даты
        Date operationDate = l2.main.getOperation_date(),
             ticketBegDate = l2.main.getTicket_begdate(),
             ticketEndDate = l2.main.getTicket_enddate();

        // Станции
        String operationStation = l2.main.getSale_station(),
                     depStation = l2.main.getDeparture_station(),
                     arrStation = l2.main.getArrival_station();

        // Флаги
        Character  so = l2.main.getFlg_so(),
                  bsp = l2.main.getFlg_bsp(),
                child = l2.main.getFlg_child(),
              carryon = l2.main.getFlg_carryon(),
              onboard = l2.main.getFlg_fee_onboard(),
               twoWay = l2.main.getFlg_2wayticket();

        // Прочее
        String   carriageCode = String.valueOf(l2.main.getCarriage_code()),
                  benefitCode = l2.main.getBenefit_code(),
                        webId = l2.main.getWeb_id();
        Character paymentType = l2.main.getPaymenttype(),
                abonementType = l2.main.getAbonement_type().charAt(0),
                  carrionType = l2.main.getCarryon_type(),
                trainCategory = l2.main.getTrain_category(),
                carriageClass = l2.main.getCarriage_class();

        // Игнорировать если действие билета истекло
        return T1.builder()
                .idnum(l2.main.getIdnum())
                .request_num(l2.main.getRequest_num())
                .date_zap(l2.main.getRequest_date())
                .yymm(Transformer.yyyymm2yymm(l2.main.getYyyymm()))
                .p1("tab1")
                .p2(serial)
                .p3(Transformer.date2yyyy(operationDate))
                .p4(Transformer.date2mm(operationDate))
                .p5("17")
                .p6(nsiData.getRoad(operationStation, operationDate)) // ??
                .p7(nsiData.getRoad(operationStation, operationDate)) // ??
                .p8(operationStation)
                .p9(carriageCode)
                .p10("00")
                .p11(nsiData.getOkato(operationStation, operationDate))
//                .p12(Transformer.date2yymm(ticketBegDate))
                .p13(nsiData.getRoad(depStation, operationDate)) // !!
                .p14(Transformer.interpretDepartment(nsiData.getDepartment(depStation, operationDate))) // !!
                .p15(depStation)
                .p16(nsiData.getRegion(depStation, operationDate)) // !!
                .p17(nsiData.getOkato(depStation, operationDate)) // !!
                .p18(nsiData.getArea(depStation, operationDate)) // !!
                .p19(Transformer.interpretTrainCategory(trainCategory, '?')) // !! 2 и 3
                .p20(Transformer.interpretCarriageClass(carriageClass))
                .p21(Transformer.interpretTicketType(abonementType, carrion, onboard, twoWay))
                .p22(Transformer.interpretPassengerCategory(bsp, child, benefitCode))
                .p23('3') // ?
                .p24(benefitCode)
                .p25(Transformer.interpretPaymentType(paymentType, benefitCode, '?' , webId)) // !!
                .p26("?") // lgots.lgot
                //.p27() // берётся из функции
                //.p28() // берётся из функции
                //.p29() // берётся из функции
                .p30(Transformer.interpretOkato(nsiData.getOkato(arrStation, operationDate)))
                .p31(nsiData.getArea(arrStation, operationDate))
//                .p32()
//                .p33()
//                .p34()
//                .p35()
//                .p36()
//                .p37()
//                .p38()
//                .p39()
//                .p40()
//                .p41()
//                .p42()
//                .p43()
//                .p44()
//                .p45()
//                .p46()
//                .p47()
//                .p48()
//                .p49()
//                .p50()
//                .p51()
                .p52(Transformer.interpretDocRegistration('?', "?", l2.main.getRequest_type(), l2.main.getRequest_subtype()))
                .p53(String.valueOf(l2.main.getAgent_code()))
                .p54(arrStation)
                .p55(Transformer.interpretAbonementType(abonementType))
//                .p56() жду справочник
                .p57(Transformer.interpretCarrionType(carrionType))
                .p58(so)
                .p59(so)
                .p60("???")
//                .p61() // берётся из функции
//                .p62() // берётся из функции
//                .p63() // берётся из функции
                .build();
    }

    private T2 createT2(Level2Data.Record l2, short serial, T1 t1) {
        logger.log("\t\tprig_co22_t2");
        return T2.builder()
                .yymm(t1.getYymm())
                .p1("tab2")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
//                .p5()  // берётся из функции
//                .p6()  // берётся из функции
//                .p7()  // берётся из функции
                .build();
    }

    private T3 createT3(Level2Data.Record l2, short serial, T1 t1) {
        logger.log("\t\tprig_co22_t3");
        return T3.builder()
                .yymm(t1.getYymm())
                .p1("tab3")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
//                .p5()  // берётся из функции
//                .p6()  // берётся из функции
//                .p7()  // берётся из функции
                .build();
    }

    private T4 createT4(Level2Data.Record l2, short serial, T1 t1) {
        logger.log("\t\tprig_co22_t4");
        return T4.builder()
                .yymm(t1.getYymm())
                .p1("tab4")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
//                .p5()  // берётся из функции
//                .p6()  // берётся из функции
//                .p7()  // берётся из функции
//                .p8()  // берётся из функции
//                .p9()  // берётся из функции
                .build();
    }

    private T5 createT5(Level2Data.Record l2, T1 t1, T2 t2, T3 t3) {
        logger.log("\t\tprig_co22_t5");
        return T5.builder()
                .yymm(t1.getYymm())
                .p1("tab5")
                .p2(t1.getP5())
//                .p3()
//                .p4()
//                .p5()
                .p6(t1.getP33())
                .p7(0L) // ??
                .p8(0L) // ??
                .p9(t1.getP36())
                .p10(t1.getP44())
                .build();
    }

    private T6 createT6(Level2Data.Record l2, short serial, T1 t1) {
        logger.log("\t\tprig_co22_t6");
        return T6.builder()
                .yymm(t1.getYymm())
                .p1("tab6")
                .p2(t1.getP5())
                .p3(t1.getP2())
                .p4(serial)
//                .p5()  // берётся из функции
//                .p6()  // берётся из функции
//                .p7()  // берётся из функции
                .build();
    }
    
    private Reestr createReestr(Level2Data.Record l2) {
        logger.log("\t\tprig_lgot_reestr");
        return Reestr.builder()
                .build();
    }
    
    private Stat createStat(Level2Data.Record l2) {
        logger.log("\t\tprig_lgot_stat");
        return Stat.builder()
                .build();
    }

    @PostConstruct
    private void showSome(){

        Long lastLevel3Id = level3Data.getLastId();
        logger.log("Last level 3 id: " + lastLevel3Id);
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);
        logger.log("Level 2 data with id greater than " + lastLevel3Id + " size: " + records.size());
        logger.log("The data:");
        records.forEach((id, record) -> logger.log(id + ":\n" + record.toString()));
    }
}
