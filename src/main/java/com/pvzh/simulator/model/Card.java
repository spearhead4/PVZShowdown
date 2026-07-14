package com.pvzh.simulator.model;

import com.pvzh.simulator.modifier.ModifierPipeline;

import java.util.UUID;

/**
 * An active instance of a card on the board or in a hand.
 * Computes its attributes dynamically via Modifier Pipelines.
 */
public class Card {
    private final String instanceId; // Unique ID for this instance
    private final CardDefinition definition; // Static blueprint
    private final Player owner;

    // We maintain a damage counter instead of mutating base health.
    // Current health = (BaseHealth modified) - damageTaken
    private int damageTaken = 0;

    // Safety flag for destruction logic queueing
    private boolean isMarkedForDestruction = false;

    // Modifier pipelines for dynamic attributes
    private final ModifierPipeline attackPipeline = new ModifierPipeline();
    private final ModifierPipeline healthPipeline = new ModifierPipeline();
    private final ModifierPipeline costPipeline = new ModifierPipeline();

    // Modifier pipeline for traits granted dynamically (like Area 22's FRENZY)
    private final ModifierPipeline traitPipeline = new ModifierPipeline();

    public Card(CardDefinition definition, Player owner) {
        this.instanceId = UUID.randomUUID().toString();
        this.definition = definition;
        this.owner = owner;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public CardDefinition getDefinition() {
        return definition;
    }

    public Player getOwner() {
        return owner;
    }

    // Dynamic Getters

    public int getCost() {
        return Math.max(0, costPipeline.compute(definition.getBaseCost()));
    }

    public int getAttack() {
        // Attack cannot drop below 0
        return Math.max(0, attackPipeline.compute(definition.getBaseAttack()));
    }

    public int getMaxHealth() {
        // Max health cannot be less than 1 (if it's a fighter)
        return Math.max(1, healthPipeline.compute(definition.getBaseHealth()));
    }

    public int getCurrentHealth() {
        return Math.max(0, getMaxHealth() - damageTaken);
    }

    public void takeDamage(int amount) {
        if (amount > 0) {
            this.damageTaken += amount;

            if (getCurrentHealth() <= 0) {
                this.isMarkedForDestruction = true;
            }
        }
    }

    public void heal(int amount) {
        if (amount > 0) {
            this.damageTaken = Math.max(0, this.damageTaken - amount);
        }
    }

    public boolean isMarkedForDestruction() {
        return isMarkedForDestruction;
    }

    public void markForDestruction() {
        this.isMarkedForDestruction = true;
    }

    /**
     * Dynamically determines if this card has a specific trait.
     * Checks both the static blueprint and any active modifiers in the trait pipeline.
     */
    public boolean hasTrait(Trait trait) {
        if (definition.getTraits() != null && definition.getTraits().contains(trait)) {
            return true;
        }
        return traitPipeline.hasActiveTrait(trait);
    }

    public ModifierPipeline getAttackPipeline() {
        return attackPipeline;
    }

    public ModifierPipeline getHealthPipeline() {
        return healthPipeline;
    }

    public ModifierPipeline getCostPipeline() {
        return costPipeline;
    }

    public ModifierPipeline getTraitPipeline() {
        return traitPipeline;
    }
}
