package org.vniizht.suburbsweb.service.transformation.conversion;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

public class Converter {

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(Date date, Time time, String format) {
        return new SimpleDateFormat(format).format(new Date(date.getTime() + time.getTime()));
    }


    public static String convertCarriageCode(Short carriageCode) {
        // Числовой тип исходного поля приводится к строковому. Начало строки заполняется нулями до общей длины 9 символов.
        return String.format("%09d", carriageCode);
    }

    public static String convertCarriageClass(Character carriageClass) {
        return "0" + carriageClass;
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

    public static Character convert58(String lgotInfo){
        switch (lgotInfo.charAt(9)){
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

    public static Character convert59(Character paymentType, String lgotInfo){
        if(paymentType == 'Ж' && lgotInfo.startsWith("22"))
            switch (lgotInfo.charAt(5)){
                case 'Ф':
                case 'Д': return '1';
        }
        return '0';
    }

    public static Character covertMCD(String trainNum){
//        String trimmed = trainNum.trim();
//        if(trimmed.length() == 1)
//            return trimmed.charAt(0);
//
//        return '0';

        return trainNum.isBlank() ? '0' : trainNum.trim().charAt(0);
    }

    public static long convertPassengersCount(Character ticketType, short passengersQuantity, short carrionWeight) {
        return switch (ticketType) {
            case '1', '2', '3', '4', '5' -> passengersQuantity;
            default -> carrionWeight;
        };
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
