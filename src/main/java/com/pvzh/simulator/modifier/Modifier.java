package com.pvzh.simulator.modifier;

/**
 * A modifier that can alter an attribute (like attack or health) dynamically.
 * Priority determines the order in which modifiers are applied (e.g., set to 1, then +2).
 */
public interface Modifier {
    /**
     * Applies the modification to the current value.
     * @param currentValue The value after previous modifiers have been applied.
     * @return The newly modified value.
     */
    int apply(int currentValue);

    /**
     * @return The priority of this modifier. Lower values execute earlier.
     * Standard addition/subtraction should typically run before min/max clamps.
     */
    int getPriority();

    /**
     * @return A unique identifier or source for this modifier, allowing it to be removed.
     */
    String getSourceId();
}
