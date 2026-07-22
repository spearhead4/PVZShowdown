package com.pvzh.simulator.engine;

import com.pvzh.simulator.model.Card;
import com.pvzh.simulator.model.GameState;
import com.pvzh.simulator.model.Lane;
import com.pvzh.simulator.model.Phase;

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
     * Within each lane, applies the action to all Zombie fighters first, then all Plant fighters.
     */
    public void sweepBoard(Consumer<Card> zombieAction, Consumer<Card> plantAction) {
        for (Lane lane : gameState.getLanes()) {
            if (zombieAction != null) {
                // To avoid concurrent modification, iterate over a copy if actions can remove entities.
                for (Card zombie : new ArrayList<>(lane.getZombieFighters())) {
                    zombieAction.accept(zombie);
                }
            }
            if (plantAction != null) {
                for (Card plant : new ArrayList<>(lane.getPlantFighters())) {
                    plantAction.accept(plant);
                }
            }
        }
    }

    /**
     * Resolves pending destructions across the entire board.
     * Sweeps Left-to-Right, Zombie-First.
     */
    public void resolveDestructions() {
        sweepBoard(
            zombie -> {
                if (zombie.isMarkedForDestruction()) {
                    triggerWhenDestroyed(zombie);
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
    }

    private void removeFighterFromBoard(Card card) {
        for (Lane lane : gameState.getLanes()) {
            lane.removeFighter(card);
        }
    }

    // Phase Transition Hooks

    public void triggerPhaseStart(Phase phase) {
        // E.g., Start of Turn effects in Phase.ZOMBIE_PLAY
    }

    public void triggerPhaseEnd(Phase phase) {
        // E.g., End of Turn effects at the end of Phase.FIGHT
    }
}
