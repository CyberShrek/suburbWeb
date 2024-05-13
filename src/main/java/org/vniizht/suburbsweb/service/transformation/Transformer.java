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
        //        case.
        // when train_category='С' then '6'
        // when train_category='7' then '5'
        // when train_category='А' then '8'
        // when train_category='Г' then '9'
        // when train_category='Л' then 'Л'
        // when train_category='Б' then '7'
        // when train_category in('1','М') then '4'
        // when prod in('i','m') then '4'  else '1' end
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

    public static Character interpretTicketType(Character sbor,
                                                Character bag,
                                                Character prod,
                                                Character abonementType,
                                                Character twoWay,
                                                Character workDay) {
        // case
        // when flg_bil_sbor='S' then '8'
        // when flg_bag='1' then '6'
        // when prod in('i','m') then '1'
        // when abonement_type='0' and flg_tuda_obr='2' then '3'
        // when abonement_type='0' and flg_tuda_obr='1' then '2'
        // when flg_rab_day='1' then '4'
        // when flg_bag='0' then '5'  else '0' end
        return   sbor == 'S' ? '8'
                : bag == '1' ? '6'
                : prod == 'i' || prod == 'm' ? '1'
                : abonementType == '0' && twoWay == '2' ? '3'
                : abonementType == '0' && twoWay == '1' ? '2'
                : workDay == '1' ? '4'
                : bag == '0' ? '5'
                : '0';
    }

    public static Character interpretPassengerCategory(Character bsp,
                                                       Character child,
                                                       String benefitCode) {
        // case
        // when flg_bsp='1' then '4'
        // when flg_child='1' then '2'
        // when kod_lgt=0 then '1' else '3' end
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
        // case when flg_ruch='0' then '1'
        // when tsite is not null then cast(cast(tsite as smallint) as char(1))
        // when request_subtype in(10,20,25) then '4'
        // when request_type!=64 and request_subtype>=200 and request_subtype<=299 then '2' else '5' end
        return flgRuch == '0' ? '1'
                : tsite != null ? tsite.charAt(0)
                : requestSubtype == 10 || requestSubtype == 20 || requestSubtype == 25 ? '4'
                : requestType != 64 && requestSubtype >= 200 && requestSubtype <= 299 ? '2'
                : '5';
    }

    public static Character interpretAbonementType(Character abonementType) {
        // case when ABONEMENT_TYPE='3' then '1'
        // when ABONEMENT_TYPE in('5','6') then '2'
        //  when ABONEMENT_TYPE in('7','8') then '3'
        // when ABONEMENT_TYPE='4' then '1'
        // when ABONEMENT_TYPE='1' then '5'
        // when ABONEMENT_TYPE='2' then '4'
        // else ABONEMENT_TYPE end
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
        // case
        // when srok_mon>0 and srok_mon<10 then '00'||cast(srok_mon as char(1))
        // when srok_mon>9 then '0'||cast(srok_mon as char(12))
        // when ABONEMENT_TYPE='1' then '4'||cast(k_pas*2 as char(2))
        // when ABONEMENT_TYPE>'1' and srok_bil<10 then '10'||cast(srok_bil as char(2))
        // when ABONEMENT_TYPE>'1' then '1'||cast(srok_bil as char(2))
        // else '000' end
        return abonementType == '1' ? '4' + k_pas * 2
                : abonementType > '1' && srok_bil < 10 ? '10' + srok_bil
                : abonementType > '1' ? '1' + srok_bil
                : "000";
    }

    public static Character interpretLuggageType(Character carriageType){
        // case
        // when bag_vid='Ж' then '1'
        // when bag_vid='Т' then '2'
        // when bag_vid='В' then '3'
        // when bag_vid='Р' then '4'
        // else  bag_vid end
        switch (carriageType){
            case 'Ж': carriageType = '1'; break;
            case 'Т': carriageType = '2'; break;
            case 'В': carriageType = '3'; break;
            case 'Р': carriageType = '4';
        }
        return carriageType;
    }
}
