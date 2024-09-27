package org.vniizht.suburbsweb.util;

public class Log {

    private StringBuilder logBuilder = new StringBuilder();

    public void log(String message) {
        System.out.println(message);
        logBuilder.append(message).append("\n");
    }

    public String collect() {
        return logBuilder.toString();
    }
}
