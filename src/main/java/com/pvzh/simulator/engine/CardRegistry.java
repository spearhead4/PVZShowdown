package com.pvzh.simulator.engine;

import com.pvzh.simulator.model.CardDefinition;
import com.pvzh.simulator.model.CardType;
import com.pvzh.simulator.model.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Acts as the global database for all loaded CardDefinitions.
 */
public class CardRegistry {
    private final List<CardDefinition> allCards = new ArrayList<>();
    private final Random random = new Random();

    public void registerCard(CardDefinition def) {
        allCards.add(def);
    }

    /**
     * Gets a random card definition matching the given criteria.
     * Often used for mechanics like Leap or Conjure.
     */
    public CardDefinition getRandomCardWithCost(Side side, CardType type, int targetCost) {
        List<CardDefinition> candidates = new ArrayList<>();
        for (CardDefinition def : allCards) {
            if (def.getSide() == side && def.getType() == type && def.getBaseCost() == targetCost) {
                candidates.add(def);
            }
        }

        if (candidates.isEmpty()) {
            return null; // Safe failure
        }

        return candidates.get(random.nextInt(candidates.size()));
    }
}
