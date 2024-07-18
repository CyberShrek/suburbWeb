package org.vniizht.suburbsweb.service.transformation;

import java.util.Date;

public class Converter {

    public static Integer yyyymm2yymm(Integer yyyymm) {
        return yyyymm - (yyyymm/10000*10000);
    }

    public static String date2yyyy(Date date) {
        return Integer.toString(date.getYear());
    }

    public static String date2yy(Date date) {
        int year = date.getYear() % 100;
        return (year < 10 ? "0" : "") + year;
    }

    public static String date2yymm(Date date) {
        return date2yy(date) + date2mm(date);
    }

    public static String date2mm(Date date) {
        int month = date.getMonth() + 1;
        return (month < 10 ? "0" : "") + month;
    }

    public static String convertCarriageCode(Short carriageCode) {
        // Числовой тип исходного поля приводится к строковому. Остаток строки заполняется нулями до длины строки 9
        return String.format("%-9d", carriageCode).replace(' ', '0');
    }

    public static String convertDepartment(String department) {
        return department.substring(1);
    }

    public static String convertOkato(String okato) {
        return okato.substring(0, 5);
    }

    public static Character convertTrainCategory(Character category) {
        switch (category){
            case 'С': return '6'; // скорые пригородные поезда типа «Спутник» (7ХХХ)
            case '7': return '5'; // скорые пригородные поезда без предоставления мест (7ХХХ)
            case 'А': return '8'; // рельсовые автобусы 6000-е
            case 'Б': return '7'; // рельсовые автобусы 7000-е
            case 'Г': return '9'; // городские линии
            case '1':
            case 'М': return '4'; // скорые пригородные с предоставлением мест (7XXX(8xx-c АМГ))
            default:  return '1'; // пригородные пассажирские
        }
    }

    public static String convertCarriageClass(Character carriageClass) {
        return "0" + carriageClass;
    }

    public static Character convertTicketType(
                                                Character abonementType,
                                                Character carrion,
                                                Character onboard,
                                                Character twoWay) {

        // Квитанция за оформление в поезде
        if(onboard == '1')
            return '8';

        // Перевозочный документ (для багажа)
        if(carrion == '1')
            return '6';

        // Билет выходного дня
        if(abonementType == '5' || abonementType == '6')
            return '4';

        // Разовый
        if(abonementType == '0')
            return (twoWay == '1') ? '3' : '2'; // “туда+обратно” или только “туда”

        // Абонементный
        return '5';
    }

    public static Character convertPassengerCategory(Character bsp,
                                                     Character child,
                                                     String benefitCode) {
        return    bsp   == '1' ? '4'            // Бесплатный
                : child == '1' ? '2'            // Детский
                : benefitCode.equals("00") ? '1' // Полный
                : '3';                          // Льготный
    }

    public static Character convertPaymentType(Character  paymentType,
                                               String     siteType,
                                               String     plagnVr) {

        // Электронный кошелёк
        if(siteType.equals("09") && plagnVr.equals("6 "))
            return '4';

        switch (paymentType){
            case '8': return '3'; // Банковские карты
            case '9': return '1'; // Льготные
            case '1':
            case '3': return '2'; // Наличные
            case '6': return '5'; // Безнал для юр. лиц
        }

        // Интернет
        return '6';
    }

    public static String convertSeatStickLimit(Short seatStickLimit, Character abonementType) {
        if (seatStickLimit == 0 || seatStickLimit == 10 || seatStickLimit == 11 || abonementType == '0')
            return "000";

        char type;
        switch (abonementType) {
            // Месячный
            case '3':
            case '5':
            case '7': type = '0'; break;

            // Посуточный
            case '2':
            case '4':
            case '6':
            case '8': type = '1'; break;

            // Количество поездок
            default: type = '4'; break;
        }
        return type + String.format("%02d", seatStickLimit);
    }

    public static Character convertDocRegistration(String tsite,
                                                   short requestSubtype) {
        if(!tsite.equals("  "))
            return String.valueOf(Integer.parseInt(tsite.trim())).charAt(0);

        switch (requestSubtype / 256){
            case 0: return '3';
            case 1: return '2';
        }
        return  '5';
    }

    public static Character convertAbonementType(Character abonementType) {
        switch (abonementType){
            case '1': return '5'; // билет на количество поездок
            case '2': return '4'; // билет на определенные даты
            case '3':
            case '4': return '1'; // билет «ежедневно»
            case '5':
            case '6': return '2'; // билет «выходного дня»
            case '7':
            case '8': return '3'; // билет «рабочего дня»
        }
        return abonementType;
    }

    public static String convertDepartureDate2yymm(Character interpretedTicketType,
                                                   Date ticketBegDate,
                                                   Integer yyyymm){
        // Для разового билета и абонементов - yymm даты начала действия
        switch (interpretedTicketType){
            case '2':
            case '3':
            case '5': return date2yymm(ticketBegDate);
        }
        // Во всем остальном - yymm
        return String.valueOf(yyyymm2yymm(yyyymm));
    }

    public static String convertDocRegistrationType(Character ruch){
        // web_id
        return "";
    }

    public static Character convertCarrionType(Character carrionType){
        switch (carrionType){
            case 'Ж': return '1'; // живность
            case 'Т': return '2'; // телевизор
            case 'В': return '3'; // велосипед
            case 'Р': return '4'; // излишний вес ручной клади
        }
        return carrionType;
    }

    public static Character convert58(String benefitGroupCode, String bilGroupCode){
        if(!benefitGroupCode.equals("22"))
            return null;

        if(Integer.parseInt(bilGroupCode) > 4)
            return '1';

        return '0';
    }

    public static Character convert59(String benefitGroupCode, Character employeeCategory){
        if(!benefitGroupCode.equals("22"))
            return null;

        if(employeeCategory == 'Ф')
            return '1';

        return '0';
    }

    public static Character covertMCD(String trainNum){
        String trimmed = trainNum.trim();
        if(trimmed.length() == 1)
            return trimmed.charAt(0);

        return '0';
    }

    public static long convertPassengersCount(Character ticketType, short passengersQuantity, short carrionWeight) {
        switch (ticketType){
            case '1':
            case '2':
            case '3':
            case '4':
            case '5': return passengersQuantity;
        }
        return carrionWeight;
    }

    public static long convertDocumentsCount(Character operType,
                                             Character operCancelType,
                                             short passengersQuantity) {
        // Гашение = -pass_qty, Возврат = 0, остальное = +pass_qty
        if (operType == 'V')
            return 0;

        if (operCancelType == 'G')
            return -passengersQuantity;

        return passengersQuantity;
    }
}
