package com.pvzh.simulator.model;

/**
 * Represents a single lane on the board.
 * Tracks the type of lane (Heights, Ground, Water) and the entities residing in it.
 */
public class Lane {
    private final int id; // 1 to 5
    private final LaneType type;

    private Card plantFighter;
    private Card zombieFighter;
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

    public Card getPlantFighter() {
        return plantFighter;
    }

    public void setPlantFighter(Card plantFighter) {
        this.plantFighter = plantFighter;
    }

    public Card getZombieFighter() {
        return zombieFighter;
    }

    public void setZombieFighter(Card zombieFighter) {
        this.zombieFighter = zombieFighter;
    }

    public Card getEnvironment() {
        return environment;
    }

    public void setEnvironment(Card environment) {
        this.environment = environment;
    }

    public void removePlantFighter() {
        this.plantFighter = null;
    }

    public void removeZombieFighter() {
        this.zombieFighter = null;
    }

    /**
     * Checks if a card can be placed in this lane based on lane type and traits.
     */
    public boolean canPlayFighter(Card card) {
        if (card.getDefinition().getType() != CardType.FIGHTER) {
            return false;
        }

        // Check if lane is already occupied by the same faction
        if (card.getOwner().getFaction() == Faction.PLANT && plantFighter != null) {
            // Note: Team-Up logic would require modifying this to hold a List<Card> or primary/secondary fighters.
            // For now, keeping it simple as 1 fighter per side.
            return false;
        }
        if (card.getOwner().getFaction() == Faction.ZOMBIE && zombieFighter != null) {
            return false;
        }

        // Check Amphibious restriction for Water lane
        if (type == LaneType.WATER) {
            return card.getDefinition().getTraits().contains("Amphibious");
        }

        return true;
    }
}
