import org.vniizht.suburbsweb.util.Util;

import java.util.Calendar;
import java.util.Date;

public class Playground {
    public static void main(String[] args) {
        Date operationDate = new Date();
        operationDate.setYear(2025 - 1900);
        operationDate.setMonth(Calendar.JANUARY);
        operationDate.setDate(31);
        System.out.println(Util.formatDate(operationDate, "yyyyMM"));
    }
}