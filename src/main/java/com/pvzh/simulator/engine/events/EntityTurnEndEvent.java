package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;

/**
 * Dispatched sequentially to specific cards during the L-to-R board sweep.
 */
public class EntityTurnEndEvent implements GameEvent {
    private final Card card;
    private final int turnNumber;

    public EntityTurnEndEvent(Card card, int turnNumber) {
        this.card = card;
        this.turnNumber = turnNumber;
    }

    public Card getCard() { return card; }
    public int getTurnNumber() { return turnNumber; }
}
