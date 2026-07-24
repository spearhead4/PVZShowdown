package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;

public class CardPlayedEvent implements GameEvent {
    private final Card card;
    private final int laneId; // 0 if trick/environment played globally

    public CardPlayedEvent(Card card, int laneId) {
        this.card = card;
        this.laneId = laneId;
    }

    public Card getCard() { return card; }
    public int getLaneId() { return laneId; }
}
