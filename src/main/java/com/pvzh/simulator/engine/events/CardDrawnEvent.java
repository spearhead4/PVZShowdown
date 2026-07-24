package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;
import com.pvzh.simulator.model.Player;

public class CardDrawnEvent implements GameEvent {
    private final Player player;
    private final Card card;

    public CardDrawnEvent(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    public Player getPlayer() { return player; }
    public Card getCard() { return card; }
}
