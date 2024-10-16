package org.vniizht.suburbsweb.service.transformation.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.PrigCost;
import org.vniizht.suburbsweb.model.transformation.level3.co22.*;
import org.vniizht.suburbsweb.model.transformation.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.handbook.Handbook;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class PrigConversion {

    @Autowired private Logger     logger;
    @Autowired private Handbook handbook;

    public T1 getT1(Level2Data.PrigRecord l2) {

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
        Character bsp = l2.main.getFlg_bsp(),
                child = l2.main.getFlg_child(),
              carrion = l2.main.getFlg_carryon(),
              onboard = l2.main.getFlg_fee_onboard(),
               twoWay = l2.main.getFlg_2wayticket();


        // Прочее
        String   benefitCode = l2.main.getBenefit_code(),
                benefitGroupCode = l2.main.getBenefitgroup_code(),
                        webId = l2.main.getWeb_id(),
                   payagentId = l2.main.getPayagent_id();
        Character paymentType = l2.main.getPaymenttype(),
                abonementType = l2.main.getAbonement_type().charAt(0),
                abonementSubtype = l2.main.getAbonement_subtype(),
                  carrionType = l2.main.getCarryon_type(),
                trainCategory = l2.main.getTrain_category(),
                carriageClass = l2.main.getCarriage_class();

        // Конвертировано
        Character convertedTicketType = Converter.convertTicketType(abonementType, carrion, onboard, twoWay);

        // Игнорировать если действие билета истекло
        return T1.builder().key(
                T1.Key.builder()
                        .request_date(l2.main.getRequestDate())
                        .p1("tab1")
                        .p2(1)
                        .p3(Converter.date2yyyy(operationDate)).p4(Converter.date2mm(operationDate))
                        .p5("17")
                        .p6(handbook.getRoad3(operationStation, operationDate))
                        .p7(handbook.getRoad3(operationStation, operationDate))
                        .p8(Converter.convertSaleStation(l2.main.getSale_station()))
                        .p9(Converter.convertCarriageCode(l2.main.getCarriage_code()))
                        .p10(l2.main.getRegion_code())
                        .p11(Converter.convertOkato(handbook.getOkatoByStation(operationStation, operationDate)))
                        .p12(Converter.convertDepartureDate2yymm(convertedTicketType, ticketBegDate, l2.main.getYyyymm()))
                        .p15(depStation)
                        .p17(Converter.convertOkato(handbook.getOkatoByStation(depStation, operationDate)))
                        .p18(handbook.getArea(depStation, operationDate))
                        .p19(Converter.convertTrainCategory(trainCategory))
                        .p20(Converter.convertCarriageClass(carriageClass))
                        .p21(convertedTicketType)
                        .p22(Converter.convertPassengerCategory(bsp, child, benefitCode))
                        .p23('3') // к = '3' з = '1'
                        .p24(benefitGroupCode + benefitCode)
                        .p25(Converter.convertPaymentType(paymentType, handbook.getTSite(webId, operationCountry, operationDate), handbook.getPlagnVr(payagentId, operationCountry, operationDate)))
                        .p26(handbook.getGvc(benefitGroupCode, l2.main.getBenefit_code(), operationDate))
                        .p30(Converter.convertOkato(handbook.getOkatoByStation(arrStation, operationDate)))
                        .p31(handbook.getArea(arrStation, operationDate))
                        .p32((short) l2.cost.stream().mapToInt(PrigCost::getRoute_distance).sum())
                        .p52(Converter.convertDocRegistration(handbook.getTSite(webId, operationCountry, operationDate), l2.main.getRequest_subtype()))
                        .p53(String.valueOf(l2.main.getAgent_code()))
                        .p54(arrStation)
                        .p55(Converter.convertAbonementType(abonementType, abonementSubtype))
                        .p56(Converter.convertSeatStickLimit(l2.main.getSeatstick_limit(), abonementType))
                        .p57(Converter.convertCarrionType(carrionType))
                        .p58(Converter.convert58(benefitGroupCode, l2.adi.getBilgroup_code()))
                        .p59(Converter.convert59(benefitGroupCode, l2.adi.getEmployee_cat()))
                        .p60("000")
                        .p61(Converter.covertMCD(l2.main.getTrain_num()))
                        .p62((short) 0)
                        .p63(' ')
                        .list(Converter.convertPrigList(benefitGroupCode))
                        .routes(l2.cost.stream().map(PrigCost::getRoute_num).sorted().map(String::valueOf).collect(Collectors.joining(" ")))
                        .build()
                )
                .p33(Converter.convertPassengersCount(convertedTicketType, l2.main.getPass_qty(), l2.main.getCarryon_weight()))
                .p34(0L)
                .p35(0L)
                .p36(l2.main.getTariff_sum())
                .p37(0L)
                .p38(0L)
                .p39(Converter.convert39(l2.main.getOper(), l2.main.getFee_sum(), l2.main.getRefundfee_sum()))
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
                .build();
    }

    public Lgot getLgot(Level2Data.PrigRecord l2, T1 t1) {
        return Lgot.builder()
                .key(Lgot.Key.builder()
                        .p1(1)
                        .p2(handbook.getRoad2(l2.main.getSale_station(), l2.main.getOperation_date()))
                        .p3(handbook.getDepartment(l2.main.getSale_station(), l2.main.getOperation_date()))
                        .p4(switch(l2.main.getRequest_subtype() / 256){
                            case 0  -> '2';
                            case 1  -> '1';
                            default -> '0';
                        })
                        .p5(switch (new String(new char[]{l2.main.getOper(), l2.main.getOper_g()})) {
                            case "ON" -> '1';
                            case "OG" -> '2';
                            case "VN" -> '3';
                            case "OO" -> '4';
                            default   -> '0';
                        })
                        .p6(l2.main.getTrain_category())
                        .p7(t1.getKey().getP24())
                        .p8(String.valueOf(l2.main.getCarriage_code()))
                        .p9(handbook.getOkatoByRegion(l2.main.getBenefit_region(), l2.main.getOperation_date()))
                        .p10(l2.adi.getBenefit_doc())
                        .p11(l2.main.getBenefitgroup_code().equals("22")
                                ? l2.adi.getBilgroup_secur() + l2.adi.getBilgroup_code()
                                : l2.main.getBenefit_region())
                        .p12(l2.adi.getEmployee_unit())
                        .p13(l2.main.getBenefitgroup_code().equals("22")
                                ? l2.adi.getEmployee_cat()
                                : null)
                        .p14(l2.adi.getSurname() + ' ' + l2.adi.getInitials())
                        .p15(l2.adi.getDependent_surname() + ' ' + l2.adi.getDependent_initials())
                        .p16((byte) (
                                l2.main.getAbonement_type().equals("0 ")
                                        ?
                                        (l2.main.getOper_g() == 'G'
                                                ? -1 :
                                                (l2.main.getOper() == 'V'
                                                        ? 0 : 1
                                                ))
                                        : 0
                        ))
                        .p17(l2.main.getFlg_2wayticket() == '1')
                        // Такой же, как p16?
                        .p18((byte) (
                                l2.main.getAbonement_type().equals("0 ")
                                        ?
                                        (l2.main.getOper_g() == 'G'
                                                ? -1 :
                                                (l2.main.getOper() == 'V'
                                                        ? 0 : 1
                                                ))
                                        : 0
                        ))
                        .p20(switch(l2.main.getAbonement_type().trim()){
                            case "1" -> '9';
                            case "2" -> '7';
                            case "3" -> '0';
                            case "4" -> '1';
                            case "5" -> '2';
                            case "7" -> '4';
                            case "8" -> '5';
                            default -> null;
                        })
                        .p21(l2.main.getSeatstick_limit())
                        .p22(l2.main.getOperation_date())
                        .p23(l2.main.getTicket_begdate())
                        .p24(l2.main.getTicket_ser().substring(0, 2) + l2.main.getTicket_num())
                        .p25(l2.main.getDeparture_station())
                        .p26(l2.main.getArrival_station())
                        .p29(null)
                        .p30(l2.main.getServer_datetime()) // ??
                        .p31(l2.main.getServer_reqnum())
                        .p32(l2.adi.getSnils())
                        .build())
                .p19(l2.main.getPass_qty())
                .p27((int) (l2.main.getTariff_sum() * 10))
                .p28((int) (l2.main.getTariff_sum() - l2.main.getDepartment_sum()))
                .p33(l2.main.getCarryon_weight())
                .build();
    }

//    private T2 createT2(Level2Data.Record l2, short serial, T1 t1) {
//        return T2.builder()
//                .p1("tab2")
//                .p2(t1.getP5())
//                .p3(t1.getP2())
//                .p4(serial)
//                .p5("ждёт функции")
//                .p6("ждёт функции")
//                .p7((short) -1)
//                .build();
//    }
//
//    private T3 createT3(Level2Data.Record l2, short serial, T1 t1) {
//        return T3.builder()
//                .p1("tab3")
//                .p2(t1.getP5())
//                .p3(t1.getP2())
//                .p4(serial)
//                .p5("ждёт функции")
//                .p6("ждёт функции")
//                .p7((short) -1)
//                .build();
//    }
//
//    private T4 createT4(Level2Data.Record l2, short serial, T1 t1) {
//        return T4.builder()
//                .p1("tab4")
//                .p2(t1.getP5())
//                .p3(t1.getP2())
//                .p4(serial)
//                .p5("ждёт функции")
//                .p6("ждёт функции")
//                .p7(-1L)
//                .p8(-1L)
//                .p9((short) -1)
//                .build();
//    }
//
//    private T5 createT5(Level2Data.Record l2, T1 t1, T2 t2, T3 t3) {
//        return T5.builder()
//                .p1("tab5")
//                .p2(t1.getP5())
//                .p3(1)
//                .p4(1)
//                .p5(1)
//                .p6(t1.getP33())
//                .p7(0L) // ??
//                .p8(0L) // ??
//                .p9(t1.getP36())
//                .p10(t1.getP44())
//                .build();
//    }
//
//    private T6 createT6(Level2Data.Record l2, short serial, T1 t1) {
//        return T6.builder()
//                .p1("tab6")
//                .p2(t1.getP5())
//                .p3(t1.getP2())
//                .p4(serial)
//                .p5("ждёт функции")  // берётся из функции
//                .p6(-1)  // берётся из функции
//                .p7((short) -1)  // берётся из функции
//                .build();
//    }
}
