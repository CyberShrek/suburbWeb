package org.vniizht.suburbsweb.service.transformation;

import java.util.Date;

public class Transformer {

    public static Integer yyyymm2yymm(Integer yyyymm) {
        return yyyymm - (yyyymm/1000000*1000000);
    }

    public static String date2yyyy(Date date) {
        return Integer.toString(date.getYear() + 1900);
    }

    public static String date2yy(Date date) {
        return Integer.toString(date.getYear() % 100);
    }

    public static String date2yymm(Date date) {
        return date2yy(date) + date2mm(date);
    }

    public static String date2mm(Date date) {
        return Integer.toString(date.getMonth() + 1);
    }

    public static String interpretDepartment(String department) {
        return department.substring(1);
    }

    public static String interpretOkato(String okato) {
        return okato.substring(0, 5); // ???
    }

    public static Character interpretTrainCategory(Character category, Character prod) {
//                case.
//         when train_category='С' then '6'
//         when train_category='7' then '5'
//         when train_category='А' then '8'
//         when train_category='Г' then '9'
//         when train_category='Л' then 'Л'
//         when train_category='Б' then '7'
//         when train_category in('1','М') then '4'
//         when prod in('i','m') then '4'  else '1' end
        switch (category){
            case 'С': category = '6'; break;
            case '7': category = '5'; break;
            case 'А': category = '8'; break;
            case 'Г': category = '9'; break;
            case 'Б': category = '7'; break;
            case '1':
            case 'М': category = '4'; break;
            default:  category = '1';
        }
        return category == '1' && (prod == 'i' || prod == 'm')
                ? '4' : category;
    }

    public static String interpretCarriageClass(Character carriageClass) {
        return "0" + carriageClass;
    }

    public static Character interpretTicketType(Character carrionType,
                                                Character abonementType,
                                                Character twoWay) {
        if(abonementType == '0') {
            if(carrionType == ' ')
                return (twoWay == '1') ? '3' : '2';
            else
                return '6';
        }


        return '0';
    }

    public static Character interpretPassengerCategory(Character bsp,
                                                       Character child,
                                                       String benefitCode) {
        return    bsp   == '1' ? '4'
                : child == '1' ? '2'
                : benefitCode.equals("0") ? '1'
                : '3';
    }

    public static Character interpretPaymentType(Character paymentType,
                                                 String benefitCode,
                                                 Character prod,
                                                 String webId) {
        return '?';
    }

    public static Character interpretDocRegistration(Character flgRuch,
                                                     String tsite,
                                                     short requestType,
                                                     short requestSubtype) {
        return flgRuch == '0' ? '1'
                : tsite != null ? tsite.charAt(0)
                : requestSubtype == 10 || requestSubtype == 20 || requestSubtype == 25 ? '4'
                : requestType != 64 && requestSubtype >= 200 && requestSubtype <= 299 ? '2'
                : '5';
    }

    public static Character interpretAbonementType(Character abonementType) {
        return abonementType == '3' ? '1'
                : abonementType == '5' || abonementType == '6' ? '2'
                : abonementType == '7' || abonementType == '8' ? '3'
                : abonementType == '4' ? '1'
                : abonementType == '1' ? '5'
                : abonementType == '2' ? '4'
                : abonementType;
    }

    public static String interpretAbonementValidity(Character abonementType,
                                                    short srok_mon,
                                                    k_pas,
                                                    srok_bil) {
        return abonementType == '1' ? '4' + k_pas * 2
                : abonementType > '1' && srok_bil < 10 ? '10' + srok_bil
                : abonementType > '1' ? '1' + srok_bil
                : "000";
    }

    public static Character interpretCarrionType(Character carrionType){
        switch (carrionType){
            case 'Ж': carrionType = '1'; break;
            case 'Т': carrionType = '2'; break;
            case 'В': carrionType = '3'; break;
            case 'Р': carrionType = '4';
        }
        return carrionType;
    }
}
