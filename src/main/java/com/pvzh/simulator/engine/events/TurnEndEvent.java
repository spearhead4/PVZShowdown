package com.pvzh.simulator.engine.events;

public class TurnEndEvent implements GameEvent {
    private final int turnNumber;

    public TurnEndEvent(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() { return turnNumber; }
}
