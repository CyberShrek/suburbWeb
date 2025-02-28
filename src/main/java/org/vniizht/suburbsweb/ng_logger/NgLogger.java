package org.vniizht.suburbsweb.ng_logger;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

/**
 * @author Alexander Ilyin
 * <p>
 * Пишет логи в БД
 */
@Service
public class NgLogger {

    @Autowired
    private NgLoggerJdbc jdbc;

    private String processId = "0";
    private boolean wasError = false;

    public void initProcessStart() throws UnknownHostException {
        jdbc.addProcess();
        processId = jdbc.getLastProcessId();
    }

    public void writeInfo(String message) {
        writeLog("I", message);
    }

    public void writeWarning(String message) {
        writeLog("W", message);
    }

    public void writeError(String message) {
        writeLog("E", message);
        wasError = true;
    }

    public void writeFatalError(String message) {
        writeLog("F", message);
        wasError = true;
    }

    public void initProcessEnd() {
        jdbc.endProcess(processId, wasError);
        processId = "0";
        wasError = false;
    }

    @SneakyThrows
    private void writeLog(String code, String message) {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        jdbc.insertLog(NgLog.builder()
                .messageCode(code)
                .messageText(message)
                .processName(traceElement.getMethodName())
                .build(), processId, wasError ? 1 : 0);
    }
}