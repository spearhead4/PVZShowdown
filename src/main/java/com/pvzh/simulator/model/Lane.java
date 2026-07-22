package com.pvzh.simulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single lane on the board.
 * Tracks the type of lane (Heights, Ground, Water) and the entities residing in it.
 */
public class Lane {
    private final int id; // 1 to 5
    private final LaneType type;

    // Support Universal Team-Up: Maximum 2 fighters per side.
    private final List<Card> plantFighters = new ArrayList<>(2);
    private final List<Card> zombieFighters = new ArrayList<>(2);

    private Card environment;

    public Lane(int id, LaneType type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public LaneType getType() {
        return type;
    }

    public List<Card> getPlantFighters() {
        return plantFighters;
    }

    public List<Card> getZombieFighters() {
        return zombieFighters;
    }

    public Card getEnvironment() {
        return environment;
    }

    public void setEnvironment(Card environment) {
        this.environment = environment;
    }

    public void removeFighter(Card fighter) {
        plantFighters.remove(fighter);
        zombieFighters.remove(fighter);
    }

    public void addFighter(Card fighter) {
        if (!canPlayFighter(fighter)) {
            throw new IllegalArgumentException("Cannot play fighter in this lane.");
        }
        if (fighter.getOwner().getSide() == Side.PLANT) {
            plantFighters.add(fighter);
        } else {
            zombieFighters.add(fighter);
        }
    }

    /**
     * Checks if a card can be placed in this lane based on lane type and Team-Up rules.
     */
    public boolean canPlayFighter(Card card) {
        if (card.getDefinition().getType() != CardType.FIGHTER) {
            return false;
        }

        // Determine which side's lane we are checking
        List<Card> laneFighters = card.getOwner().getSide() == Side.PLANT ? plantFighters : zombieFighters;

        // Lane is full
        if (laneFighters.size() >= 2) {
            return false;
        }

        // If there is already 1 fighter, we need to check Team-Up logic
        if (laneFighters.size() == 1) {
            Card existingFighter = laneFighters.get(0);
            // Either the new card or the existing card must have Team-Up
            if (!card.hasTrait(Trait.TEAM_UP) && !existingFighter.hasTrait(Trait.TEAM_UP)) {
                return false;
            }
        }

        // Check Amphibious restriction for Water lane
        if (type == LaneType.WATER) {
            return card.hasTrait(Trait.AMPHIBIOUS);
        }

        return true;
    }
}
