package org.vniizht.suburbsweb.service.transformation;

import java.util.Date;

public class Util {

    public static Integer yyyymm2yymm(Integer yyyymm) {
        return yyyymm - (yyyymm/1000000*1000000);
    }

    public static String date2yyyy(Date date) {
        return Integer.toString(date.getYear() + 1900);
    }

    public static String date2mm(Date date) {
        return Integer.toString(date.getMonth() + 1);
    }
}
