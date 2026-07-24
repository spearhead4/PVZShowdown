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

    // State management
    private CardState state = CardState.REVEALED;
    private boolean isFrozen = false;

    // We maintain a damage counter instead of mutating base health.
    // Current health = (BaseHealth modified) - damageTaken
    private int damageTaken = 0;

    // Safety flag for destruction logic queueing
    private boolean isMarkedForDestruction = false;

    // Modifier pipelines for dynamic attributes
    private final ModifierPipeline attackPipeline = new ModifierPipeline();
    private final ModifierPipeline healthPipeline = new ModifierPipeline();
    private final ModifierPipeline costPipeline = new ModifierPipeline();

    // Modifier pipeline for traits granted dynamically (like Area 22's FRENZY or persistent transformation traits)
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

    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
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

    /**
     * Applies damage to this card.
     * Handles Armor reduction, Invulnerability, and Deadly.
     */
    public void takeDamage(int amount, Card attacker) {
        if (amount <= 0 || isMarkedForDestruction) {
            return;
        }

        // Gravestones cannot be attacked/take combat damage normally
        if (this.state == CardState.GRAVESTONE) {
            return;
        }

        // Invulnerable takes 0 damage
        if (hasTrait(Trait.INVULNERABLE)) {
            return;
        }

        int armor = getTraitValue(Trait.ARMORED);
        int finalDamage = Math.max(0, amount - armor);

        if (finalDamage > 0) {
            this.damageTaken += finalDamage;

            // Check Deadly
            if (attacker != null && attacker.hasTrait(Trait.DEADLY)) {
                this.isMarkedForDestruction = true;
            }

            // Normal destruction
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
     * Dynamically determines the active value of a specific trait.
     * Combines the static blueprint value with dynamically granted modifiers.
     */
    public int getTraitValue(Trait trait) {
        int baseValue = 0;
        if (definition.getTraits() != null && definition.getTraits().containsKey(trait)) {
            baseValue = definition.getTraits().get(trait);
        }
        return baseValue + traitPipeline.getTraitValue(trait);
    }

    public boolean hasTrait(Trait trait) {
        return getTraitValue(trait) > 0;
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
