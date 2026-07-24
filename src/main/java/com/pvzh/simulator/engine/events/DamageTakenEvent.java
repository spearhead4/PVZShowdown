package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;

public class DamageTakenEvent implements GameEvent {
    private final Card victim;
    private final Card attacker;
    private final int amount;

    public DamageTakenEvent(Card victim, Card attacker, int amount) {
        this.victim = victim;
        this.attacker = attacker;
        this.amount = amount;
    }

    public Card getVictim() { return victim; }
    public Card getAttacker() { return attacker; }
    public int getAmount() { return amount; }
}
