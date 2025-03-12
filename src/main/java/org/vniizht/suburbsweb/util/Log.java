package org.vniizht.suburbsweb.util;

import org.vniizht.suburbsweb.ng_logger.NgLogger;
import org.vniizht.suburbsweb.websocket.LogWS;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private final NgLogger ngLogger;
    private final StringBuilder logBuilder = new StringBuilder();

    public Log(NgLogger ngLogger) throws UnknownHostException {
        this.ngLogger = ngLogger;
        ngLogger.initProcessStart();
    }

    public void addLine(String... messages) {
        addLine(true, messages);
    }

    private void addLine(boolean withNG, String... messages) {
        for (String line : messages) {
            System.out.println(line);
            if (withNG) ngLogger.writeInfo(line);
            logBuilder
                    .append(line)
                    .append("\n");
        }
        LogWS.spreadLog(logBuilder.toString());
    }

    public void addTimeLine(String... messages) {
        for (String line : messages) {
            addLine(false, new SimpleDateFormat("HH:mm:ss\t").format(new Date()) + line);
            ngLogger.writeInfo(line);
        }
    }

    public String sumUp() {
        addLine(false, "-------------------------------------\n");
        return logBuilder.toString();
    }

    public String sumUp(String... finalMessages) {
        addLine(false, "-------------------------------------");
        for (String message : finalMessages) {
            addLine(message);
        }
        return logBuilder
                .append("\n")
                .toString();
    }

    public void error(String message) {
        ngLogger.writeError(message);
        sumUp(message);
    }

    public String toString() {
        return logBuilder
                .toString();
    }

    public void finish(String... finalMessages) {
        sumUp(finalMessages);
        ngLogger.initProcessEnd();
    }
}
