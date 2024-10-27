package org.vniizht.suburbsweb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private final StringBuilder logBuilder = new StringBuilder();

    public void addLine(String message) {
        message = new SimpleDateFormat("HH:mm:ss\t").format(new Date()) + message;
        System.out.println(message);
        logBuilder.append(message).append("\n");
    }

    public String sumUp(String finalMessage) {
        return logBuilder
                .append("-------------------------------------\n")
                .append(finalMessage)
                .append("\n\n")
                .toString();
    }
}
