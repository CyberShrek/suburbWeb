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
        return switch (oper) {
            case 'O' -> feeSum;
            case 'V' -> refundfeeSum;
            default -> 0L;
        };
    }

    public static Long convert39(Float sumNde, Short sumCode) {
        return switch (sumCode) {
            case 104, 105, 106 -> (long) Math.round(sumNde);
            default -> 0L;
        };
    }

    public static Long convert40(Float sumNde, Short sumCode) {
//        if (sumCode == 101)
//            return (long) Math.round(sumNde);
//        else
//            return 0L;

        return sumCode == 101 ? (long) Math.round(sumNde) : 0L;
    }

    public static Long convert44(Float sumNde, Short sumCode, Character paymentType) {
//        switch (sumCode) {
//            case 101:
//            case 116: switch (paymentType) {
//                case 'Б':
//                case 'В':
//                case 'Ж':
//                case '9': return (long) Math.round(sumNde);
//            }
//        }
//        return 0L;

        return switch (sumCode) {
            case 101, 116 -> switch (paymentType) {
                case 'Б', 'В', 'Ж', '9' -> (long) Math.round(sumNde);
                default -> 0L;
            };
            default -> 0L;
        };
    }

    public static Long convert47(Float sumNde, Short sumCode, Character paymentType) {
//        switch (sumCode) {
//            case 104:
//            case 105:
//            case 106: switch (paymentType) {
//                case 'Б':
//                case 'В':
//                case 'Ж':
//                case '9': return (long) Math.round(sumNde);
//            }
//        }
//        return 0L;

        return switch (sumCode) {
            case 104, 105, 106 -> switch (paymentType) {
                case 'Б', 'В', 'Ж', '9' -> (long) Math.round(sumNde);
                default -> 0L;
            };
            default -> 0L;
        };
    }

    public static Long convert48(Float sumNde, Short sumCode, Character paymentType) {
//        if (sumCode == 101) {
//            switch (paymentType) {
//                case 'Б':
//                case 'В':
//                case 'Ж':
//                case '9': return (long) Math.round(sumNde);
//            }
//        }
//        return 0L;

        return sumCode == 101 ? switch (paymentType) {
            case 'Б', 'В', 'Ж', '9' -> (long) Math.round(sumNde);
            default -> 0L;
        } : 0L;
    }

    public static Character convert59(Character paymentType, String lgotInfo){
        if(paymentType == 'Ж' && lgotInfo.startsWith("22"))
            switch (lgotInfo.charAt(5)){
                case 'Ф':
                case 'Д': return '1';
        }
        return '0';
    }

    public static long convertPassengersCount(Character ticketType, short passengersQuantity, short carrionWeight) {
        return switch (ticketType) {
            case '1', '2', '3', '4', '5' -> passengersQuantity;
            default -> carrionWeight;
        };
    }

    public static String convertPrigList(String benefitGroupCode) {
        return "R064" + (benefitGroupCode.equals("22") ? 'Z' : 'G');
    }

    public static String convertPassList(Character paymentType, String lgotInfo) {
        return "R800" + (paymentType == 'Ж' && lgotInfo.startsWith("22") ? 'Z' : 'G');
    }
}
