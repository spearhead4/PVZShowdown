package com.pvzh.simulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Player in the game.
 * Manages the Hero, their Hand, and resources.
 */
public class Player {
    private final Side side;
    private final Hero hero;

    private final List<Card> hand = new ArrayList<>();

    private int maxResources; // Suns for plants, Brains for zombies
    private int currentResources;

    public Player(Side side, Hero hero) {
        this.side = side;
        this.hero = hero;
        this.maxResources = 0;
        this.currentResources = 0;
    }

    public Side getSide() {
        return side;
    }

    public Hero getHero() {
        return hero;
    }

    public int getMaxResources() {
        return maxResources;
    }

    public void setMaxResources(int maxResources) {
        this.maxResources = maxResources;
    }

    public int getCurrentResources() {
        return currentResources;
    }

    public void setCurrentResources(int currentResources) {
        this.currentResources = currentResources;
    }

    public void spendResources(int amount) {
        this.currentResources -= amount;
    }

    public List<Card> getHand() {
        return hand;
    }

    /**
     * Attempts to add a card to the player's hand, respecting the 10-card limit
     * based on the source of the card.
     * @param card The card to add.
     * @param source How the card was acquired.
     * @return true if the card was successfully added, false if it was burned due to hand size limit.
     */
    public boolean addCardToHand(Card card, CardSource source) {
        if (source == CardSource.BOUNCE) {
            // BOUNCE bypasses the hand size limit
            hand.add(card);
            return true;
        } else {
            // DRAW, CONJURE, BLOCK_REWARD are subject to the 10-card limit
            if (hand.size() >= 10) {
                // The card is burned
                return false;
            } else {
                hand.add(card);
                return true;
            }
        }
    }

    /**
     * Helper method to process damage directed at the Player.
     * Handles the Block Meter interception and properly adds the superpower reward to the hand.
     */
    public void takeDamage(int amount) {
        // Standard damage (can be blocked)
        hero.takeDamage(amount, powerRewardDefinition -> {
            // Callback invoked if blocked and superpower is granted
            Card powerCard = new Card(powerRewardDefinition, this);
            boolean added = addCardToHand(powerCard, CardSource.BLOCK_REWARD);
            // If added == false, the superpower was burned due to a full hand.
        });
    }
}
