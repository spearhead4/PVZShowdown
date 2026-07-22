package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;

public class UnveilEvent implements GameEvent {
    private final Card card;

    public UnveilEvent(Card card) {
        this.card = card;
    }

    public Card getCard() { return card; }
}
