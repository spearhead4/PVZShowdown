package com.pvzh.simulator.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Dual-tier logging system for the game.
 * Public logs are broadcasted to players.
 * Debug logs are for internal trace and modding analysis.
 */
public class GameLogger {
    private final List<String> publicLogs = new ArrayList<>();
    private final List<String> debugLogs = new ArrayList<>();

    public void logPublic(String message) {
        publicLogs.add(message);
        // Depending on backend structure, this could also push to WebSockets here.
        System.out.println("[PUBLIC] " + message);
    }

    public void logDebug(String message) {
        debugLogs.add(message);
        System.out.println("[DEBUG] " + message);
    }

    public List<String> getPublicLogs() {
        return publicLogs;
    }

    public List<String> getDebugLogs() {
        return debugLogs;
    }
}
