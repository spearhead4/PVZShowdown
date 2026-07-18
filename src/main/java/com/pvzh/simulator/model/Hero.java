package com.pvzh.simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a Player's Hero, their health, block meter, and superpower pool.
 */
public class Hero {
    private final Side side;
    private final List<HeroClass> classes;
    private int maxHealth;
    private int currentHealth;
    private int blockMeter;

    // The pool of superpowers available to be blocked/drawn (usually 4 unique cards)
    private final List<CardDefinition> superpowerPool;

    private final Random random = new Random();

    public Hero(Side side, List<HeroClass> classes, List<CardDefinition> superpowerPool) {
        this.side = side;
        this.classes = new ArrayList<>(classes);
        this.maxHealth = 20;
        this.currentHealth = 20;
        this.blockMeter = 0;
        this.superpowerPool = new ArrayList<>(superpowerPool);
    }

    public Side getSide() {
        return side;
    }

    public List<HeroClass> getClasses() {
        return classes;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void heal(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.min(this.maxHealth, this.currentHealth + amount);
        }
    }

    public int getBlockMeter() {
        return blockMeter;
    }

    /**
     * Applies damage to the hero, processing Block Meter RNG and logic.
     * @param amount The amount of incoming damage.
     * @param blockRewardCallback A callback invoked if the hero successfully blocks and is rewarded a superpower.
     *                            The callback handles adding the card to the player's hand.
     */
    public void takeDamage(int amount, Consumer<CardDefinition> blockRewardCallback) {
        if (amount <= 0) {
            return;
        }

        // Check if block meter is already full (should not happen if it resets, but safety first)
        if (blockMeter >= 8) {
            // Cannot block if already full before the hit? Actually in PvZH, it triggers immediately at 8.
            // If it's somehow stuck at 8, damage goes through unblocked.
            // In PvZH, if you have 10 cards, you burn the block but meter resets.
            // So blockMeter should always be < 8 when taking damage.
        }

        // Step 1: Generate RNG for the Block Meter (1, 2, or 3 charges)
        // Note: In some scenarios like Bullseye, this step is skipped. Assuming standard damage here.
        int charges = random.nextInt(3) + 1; // Generates 1, 2, or 3
        this.blockMeter += charges;

        // Step 2: Check for Block
        if (this.blockMeter >= 8) {
            // Damage is immediately cancelled
            this.blockMeter = 0; // Reset meter

            // Give a random superpower from the pool, if any remain
            if (!superpowerPool.isEmpty()) {
                int powerIndex = random.nextInt(superpowerPool.size());
                CardDefinition powerReward = superpowerPool.remove(powerIndex);

                // Use the callback to let the Player class attempt to add it to the hand
                if (blockRewardCallback != null) {
                    blockRewardCallback.accept(powerReward);
                }
            }
        } else {
            // Step 3: If no Block occurred, apply the damage
            this.currentHealth -= amount;
        }
    }
}
