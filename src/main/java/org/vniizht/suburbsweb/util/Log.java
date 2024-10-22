package org.vniizht.suburbsweb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private final StringBuilder logBuilder = new StringBuilder();

    public void log(String message) {
        message = new SimpleDateFormat("HH:mm:ss\t").format(new Date()) + message;
        System.out.println(message);
        logBuilder.append(message).append("\n");
    }

    public String collect() {
        return logBuilder.toString();
    }
}
