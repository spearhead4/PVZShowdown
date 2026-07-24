package com.pvzh.simulator.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A perfectly frozen snapshot of a Card's state at a specific millisecond.
 * Crucial for simultaneous combat resolution logic.
 */
public class CardSnapshot {
    private final Card cardRef;
    private final Side side;
    private final int currentHealth;
    private final int attack;
    private final int cost;
    private final Map<Trait, Integer> traits = new HashMap<>();

    public CardSnapshot(Card card) {
        this.cardRef = card;
        this.side = card.getOwner().getSide();
        this.currentHealth = card.getCurrentHealth();
        this.attack = card.getAttack();
        this.cost = card.getCost();

        // Capture all active traits dynamically
        for (Trait trait : Trait.values()) {
            int val = card.getTraitValue(trait);
            if (val > 0) {
                this.traits.put(trait, val);
            }
        }
    }

    public Card getCardRef() { return cardRef; }
    public Side getSide() { return side; }
    public int getCurrentHealth() { return currentHealth; }
    public int getAttack() { return attack; }
    public int getCost() { return cost; }

    public int getTraitValue(Trait trait) {
        return traits.getOrDefault(trait, 0);
    }

    public boolean hasTrait(Trait trait) {
        return getTraitValue(trait) > 0;
    }
}
