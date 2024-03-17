package com.example.criptoElmo;

import java.time.Instant;

public class PrimoInteiroState {
    private static String gson;
    private static Instant lastUpdate = Instant.now();
    private static final long UPDATE_INTERVAL = 10 * 60 * 1000; // 10 minutos em milissegundos

    public static synchronized void updateJson(String json) {
        gson = json;
        lastUpdate = Instant.now();
    }

    public static synchronized String getCurrentStateJson() {
        return gson;
    }

    public static synchronized boolean isUpdateRequired() {
        return Instant.now().minusMillis(UPDATE_INTERVAL).isAfter(lastUpdate);
    }
}
