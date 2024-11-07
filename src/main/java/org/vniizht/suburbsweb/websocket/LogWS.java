package org.vniizht.suburbsweb.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/logs")
public class LogWS {

    private final static Map<String, Session> sessions = new HashMap<>();
    private final static Map<String, String> lastMessages = new HashMap<>();

    @OnOpen
    public void subscribeSession(Session session) {
        sessions.put(session.getId(), session);
        lastMessages.forEach((key, value) -> sendToSession(session, key, value));
    }

    @OnClose
    public void unsubscribeSession(Session session) {
        sessions.remove(session.getId());
    }

    // spread basic log text to all clients
    public  static void spreadLog(String log) {
        spread("log", log);
    }

    // spread progress value from 0 to 100 to all clients
    public static void spreadProgress(int progress) {
        spread("progress", String.valueOf(progress));
    }

    private static void spread(String key, String value) {
        if(lastMessages.containsKey(key) && lastMessages.get(key).equals(value)) return;
        lastMessages.put(key, value);
        for (Session session : sessions.values())
            sendToSession(session, key, value);
    }
    private static void sendToSession(Session session, String key, String value) {
        session.getAsyncRemote().sendText(key + ":::" + value);
    }
}