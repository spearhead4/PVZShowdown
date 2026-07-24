package com.pvzh.simulator.modifier;

import com.pvzh.simulator.model.Trait;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a pipeline of modifiers that dynamically compute an attribute's final value,
 * or aggregate active traits.
 */
public class ModifierPipeline {
    private final List<Modifier> modifiers = new ArrayList<>();

    /**
     * Adds a modifier to the pipeline and sorts it by priority.
     * @param modifier The modifier to add.
     */
    public void addModifier(Modifier modifier) {
        modifiers.add(modifier);
        modifiers.sort(Comparator.comparingInt(Modifier::getPriority));
    }

    /**
     * Removes all modifiers associated with a specific source ID.
     * Useful when a temporary effect expires or an aura leaves the board.
     * @param sourceId The ID of the source.
     */
    public void removeModifiersBySource(String sourceId) {
        modifiers.removeIf(m -> m.getSourceId().equals(sourceId));
    }

    /**
     * Computes the final value of a numeric attribute by passing it through the pipeline.
     * @param baseValue The original, unmodified value (e.g., from CardDefinition).
     * @return The final, fully modified value.
     */
    public int compute(int baseValue) {
        int currentValue = baseValue;
        for (Modifier modifier : modifiers) {
            currentValue = modifier.apply(currentValue);
        }
        return currentValue;
    }

    /**
     * Aggregates the numeric value of a granted Trait.
     * @param trait The trait to evaluate.
     * @return The total dynamically granted value for this trait.
     */
    public int getTraitValue(Trait trait) {
        int value = 0;
        for (Modifier modifier : modifiers) {
            value += modifier.getTraitValue(trait);
        }
        return value;
    }
}
