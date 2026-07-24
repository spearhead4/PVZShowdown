package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;

public class CardFrozenEvent implements GameEvent {
    private final Card card;

    public CardFrozenEvent(Card card) {
        this.card = card;
    }

    public Card getCard() { return card; }
}
