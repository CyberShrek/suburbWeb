package org.vniizht.suburbsweb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private final StringBuilder logBuilder = new StringBuilder();

    public void addLine(String... messages) {
        for (String line : messages) {
            System.out.println(line);
            logBuilder
                    .append(line)
                    .append("\n");
        }
    }

    public void addTimeLine(String... messages) {
        for (String line : messages) {
            addLine(new SimpleDateFormat("HH:mm:ss\t").format(new Date()) + line);
        }
    }

    public String sumUp() {
        addLine("-------------------------------------\n");
        return logBuilder.toString();
    }

    public String sumUp(String... finalMessages) {
        addLine("-------------------------------------");
        for (String message : finalMessages) {
            addLine(message);
        }
        return logBuilder
                .append("\n")
                .toString();
    }

    public String toString() {
        return logBuilder
                .toString();
    }

    public void merge(Log log) {
        logBuilder.append(log.toString());
    }
}
