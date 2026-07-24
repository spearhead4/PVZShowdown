package com.pvzh.simulator.engine.events;

import com.pvzh.simulator.model.Card;

public class BonusAttackEvent implements GameEvent {
    private final Card attacker;

    public BonusAttackEvent(Card attacker) {
        this.attacker = attacker;
    }

    public Card getAttacker() { return attacker; }
}
