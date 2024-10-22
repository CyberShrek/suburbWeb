package org.vniizht.suburbsweb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    // Измеряет времени выполнения задачи в секундах
    public static float measureTime(Runnable task) {
        Date date = new Date();
        task.run();
        return (new Date().getTime() - date.getTime())/1000f;
    }

    // Форматирует дату в заданный формат
    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
}
