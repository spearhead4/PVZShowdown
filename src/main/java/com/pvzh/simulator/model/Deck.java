package com.pvzh.simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player's deck of cards.
 * Stores CardDefinitions (blueprints) which are instantiated into active Cards when drawn.
 */
public class Deck {
    private final List<CardDefinition> cards = new ArrayList<>();

    public Deck(List<CardDefinition> cards) {
        if (cards != null) {
            this.cards.addAll(cards);
        }
    }

    public List<CardDefinition> getCards() {
        return cards;
    }

    /**
     * Draws the top card from the deck.
     * @return The CardDefinition, or null if the deck is empty.
     */
    public CardDefinition draw() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0); // Top of the deck is index 0
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }
}
