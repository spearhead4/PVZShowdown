package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Player;

public class GameOverEvent implements GameEvent {
    private final Player losingPlayer;
    private final String reason;

    public GameOverEvent(Player losingPlayer, String reason) {
        this.losingPlayer = losingPlayer;
        this.reason = reason;
    }

    public Player getLosingPlayer() { return losingPlayer; }
    public String getReason() { return reason; }
}
