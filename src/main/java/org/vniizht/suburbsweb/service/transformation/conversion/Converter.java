package org.vniizht.suburbsweb.service.transformation.conversion;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

public class Converter {

    public static String p1() {
        return "tab1";
    }

    public static Integer yyyymm2yymm(Integer yyyymm) {
        return yyyymm - (yyyymm/10000*10000);
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(Date date, Time time, String format) {
        return new SimpleDateFormat(format).format(new Date(date.getTime() + time.getTime()));
    }

    public static String convertSaleStation(String saleStation) {
        // Начало строки заполняется двумя нулями (00). Затем sale_station.
        return "00" + saleStation;
    }

    public static String convertCarriageCode(Short carriageCode) {
        // Числовой тип исходного поля приводится к строковому. Начало строки заполняется нулями до общей длины 9 символов.
        return String.format("%09d", carriageCode);
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

    public static Character convertPassengerCategory(List<Boolean> fTick,
                                                     String benefitCode) {
        return    fTick.get(2) ? '2'                               // Детский
                : !benefitCode.equals("000") || fTick.get(4) ? '3' // Льготный
                : fTick.get(1) ? '1'                               // Полный
                : '4';                                             // Бесплатный
    }

    public static Character convertPaymentType(Character  paymentType) {

        switch (paymentType){
            case '8': return '3'; // Банковские карты
            case '9':
            case 'В':
            case 'Б':return '1'; // Льготные
            case '1':
            case '3': return '2'; // Наличные
            case '6': return '5'; // Безнал для юр. лиц
        }

        // Электронный кошелёк
        return '4';
    }

    public static Character convertPaymentType(Character  paymentType,
                                               String     siteType,
                                               String     plagnVr) {

        // Электронный кошелёк
        if(siteType.equals("09") && plagnVr.equals("6 "))
            return '4';

        return switch (paymentType) {
            case '8'      -> '3';   // Банковские карты
            case '9'      -> '1';   // Льготные
            case '1', '3' -> '2';   // Наличные
            case '6'      -> '5';   // Безнал для юр. лиц
            default       -> '6';   // Интернет
        };

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

    public static Character convertAbonementType(Character abonementType, Character abonementSubtype) {
        switch (abonementType){
            case '1': return '5'; // билет на количество поездок
            case '2':
                switch (abonementSubtype){
                    case '0': return '4'; // билет на определенные даты
                    case '1': return '6'; // билет на определенные нечетные даты
                    case '2': return '7'; // билет на определенные четные даты
                }
            case '3':
            case '4': return '1'; // билет «ежедневно»
            case '5':
            case '6': return '2'; // билет «выходного дня»
            case '7':
            case '8': return '3'; // билет «рабочего дня»
        }
        return 0;
    }

    public static String convertBenefitCode(String benefitCode, Character paymenttype, Short militaryCode, String lgot_info) {
        if(paymenttype == 'В')
            return "21" + String.format("%02d", militaryCode);

        if(lgot_info != null && !benefitCode.equals("000") && !benefitCode.equals("013"))
            return lgot_info.substring(1, 4);

        return null;
    }

    public static Short convertSeasonTicketCode(Character abonementType){
        switch (abonementType){
            case '1': return 9; // билет на количество поездок
            case '2': return 7; // билет на определенные даты
            case '3': return 1; // билет «ежедневно» (помесячный)
            case '4': return 2; // билет «ежедневно» (посуточный)
            case '5': return 8; // билет «выходного дня»
            case '7': return 3; // билет «рабочего дня» (помесячный)
            case '8': return 4; // билет «рабочего дня» (посуточный)
        }
        return 0;
    }

    public static String convertDepartureDate2yymm(Character interpretedTicketType,
                                                   Date ticketBegDate,
                                                   Integer yyyymm){
        // Для разового билета и абонементов - yymm даты начала действия
        switch (interpretedTicketType){
            case '2':
            case '3':
            case '5': return formatDate(ticketBegDate, "yyMM");
        }
        // Во всем остальном - yymm
        return String.valueOf(yyyymm2yymm(yyyymm));
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

    public static Long convert39(Character oper, Long feeSum, Long refundfeeSum) {
        switch (oper) {
            case 'O': return feeSum;
            case 'V': return refundfeeSum;
            default: return 0L;
        }
    }

    public static Long convert39(Float sumNde, Short sumCode) {
        switch (sumCode) {
            case 104:
            case 105:
            case 106: return (long) Math.round(sumNde);
            default: return 0L;
        }
    }

    public static Long convert40(Float sumNde, Short sumCode) {
        if (sumCode == 101)
            return (long) Math.round(sumNde);
        else
            return 0L;
    }

    public static Long convert44(Float sumNde, Short sumCode, Character paymentType) {
        switch (sumCode) {
            case 101:
            case 116: switch (paymentType) {
                case 'Б':
                case 'В':
                case 'Ж':
                case '9': return (long) Math.round(sumNde);
            }
        }
        return 0L;
    }

    public static Long convert47(Float sumNde, Short sumCode, Character paymentType) {
        switch (sumCode) {
            case 104:
            case 105:
            case 106: switch (paymentType) {
                case 'Б':
                case 'В':
                case 'Ж':
                case '9': return (long) Math.round(sumNde);
            }
        }
        return 0L;
    }

    public static Long convert48(Float sumNde, Short sumCode, Character paymentType) {
        if (sumCode == 101) {
            switch (paymentType) {
                case 'Б':
                case 'В':
                case 'Ж':
                case '9': return (long) Math.round(sumNde);
            }
        }
        return 0L;
    }

    public static Character convert58(String benefitGroupCode, String bilGroupCode){
        if(!benefitGroupCode.equals("22"))
            return null;

        if(Integer.parseInt(bilGroupCode) > 4)
            return '1';

        return '0';
    }

    public static Character convert58(String lgotInfo){
        switch (lgotInfo.charAt(10)){
            case '0':
            case '1':
            case '2':
            case '3':
            case '4': return '0';
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': return '1';
        }
        return null;
    }

    public static Character convert59(String benefitGroupCode, Character employeeCategory){
        if(!benefitGroupCode.equals("22"))
            return null;

        if(employeeCategory == 'Ф')
            return '1';

        return '0';
    }

    public static Character convert59(Character paymentType, String lgotInfo){
        if(paymentType == 'Ж' && lgotInfo.startsWith("22"))
            switch (lgotInfo.charAt(5)){
                case 'Ф':
                case 'Д': return '1';
        }
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
        if(operCancelType == 'N') switch (operType) {
            case 'O': return passengersQuantity;
            case 'V': return -passengersQuantity;
        }
        return 0;
    }

    public static String convertPrigList(String benefitGroupCode) {
        return "R064" + (benefitGroupCode.equals("22") ? 'Z' : 'G');
    }

    public static String convertPassList(Character paymentType, String lgotInfo) {
        return "R800" + (paymentType == 'Ж' && lgotInfo.startsWith("22") ? 'Z' : 'G');
    }
}
