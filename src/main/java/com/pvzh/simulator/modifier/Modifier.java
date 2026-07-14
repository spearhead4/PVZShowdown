package com.pvzh.simulator.modifier;

import com.pvzh.simulator.model.Trait;

/**
 * A modifier that can alter an attribute (like attack or health) dynamically,
 * or grant specific Traits (like FRENZY).
 * Priority determines the order in which modifiers are applied:
 * - Priority 10: Base stat overrides (e.g., "Attack becomes 3")
 * - Priority 20: Multipliers or specific scales.
 * - Priority 50: Standard Additions/Subtractions (e.g., "+2/+2")
 */
public interface Modifier {
    /**
     * Applies the modification to the current numeric value.
     * @param currentValue The value after previous modifiers have been applied.
     * @return The newly modified value.
     */
    default int apply(int currentValue) {
        return currentValue;
    }

    /**
     * @return true if this modifier explicitly grants the specified Trait, false otherwise.
     */
    default boolean grantsTrait(Trait trait) {
        return false;
    }

    /**
     * @return The priority of this modifier. Lower values execute earlier.
     */
    int getPriority();

    /**
     * @return A unique identifier or source for this modifier, allowing it to be removed.
     */
    String getSourceId();
}
