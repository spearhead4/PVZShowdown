package com.pvzh.simulator.engine.events;

public class TurnStartEvent implements GameEvent {
    private final int turnNumber;

    public TurnStartEvent(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() { return turnNumber; }
}
