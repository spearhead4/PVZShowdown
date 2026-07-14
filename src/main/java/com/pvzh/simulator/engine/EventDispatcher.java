package com.pvzh.simulator.engine;

import com.pvzh.simulator.model.Card;
import com.pvzh.simulator.model.GameState;
import com.pvzh.simulator.model.Lane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles universal Left-to-Right, Zombie-First resolution for board events.
 */
public class EventDispatcher {
    private final GameState gameState;

    public EventDispatcher(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Sweeps the board from Lane 1 to Lane 5.
     * Within each lane, applies the action to the Zombie fighter first, then the Plant fighter.
     * Use this for AoE effects, end/start of turn triggers, etc.
     *
     * @param zombieAction The action to apply to Zombie fighters.
     * @param plantAction The action to apply to Plant fighters.
     */
    public void sweepBoard(Consumer<Card> zombieAction, Consumer<Card> plantAction) {
        for (Lane lane : gameState.getLanes()) {
            if (lane.getZombieFighter() != null && zombieAction != null) {
                zombieAction.accept(lane.getZombieFighter());
            }
            if (lane.getPlantFighter() != null && plantAction != null) {
                plantAction.accept(lane.getPlantFighter());
            }
        }
    }

    /**
     * Resolves pending destructions across the entire board.
     * Sweeps Left-to-Right, Zombie-First.
     * Triggers "When destroyed" effects and then safely removes the entity from the board.
     */
    public void resolveDestructions() {
        sweepBoard(
            zombie -> {
                if (zombie.isMarkedForDestruction()) {
                    triggerWhenDestroyed(zombie);
                    // Find which lane it is in to remove it.
                    // (Assuming 1 fighter per lane for now, otherwise we'd search deeper).
                    removeFighterFromBoard(zombie);
                }
            },
            plant -> {
                if (plant.isMarkedForDestruction()) {
                    triggerWhenDestroyed(plant);
                    removeFighterFromBoard(plant);
                }
            }
        );
    }

    private void triggerWhenDestroyed(Card card) {
        // Here we would look up abilities triggered on destruction.
        // E.g., Barrel of Deadbeards deals 1 damage to all plants and zombies.
        // We'd push those events onto an event stack or resolve them recursively.
    }

    private void removeFighterFromBoard(Card card) {
        for (Lane lane : gameState.getLanes()) {
            if (lane.getZombieFighter() == card) {
                lane.removeZombieFighter();
                break;
            } else if (lane.getPlantFighter() == card) {
                lane.removePlantFighter();
                break;
            }
        }
    }
}
