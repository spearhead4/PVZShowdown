package com.pvzh.simulator.engine;

import com.pvzh.simulator.engine.events.EntityTurnEndEvent;
import com.pvzh.simulator.engine.events.EntityTurnStartEvent;
import com.pvzh.simulator.engine.events.TurnEndEvent;
import com.pvzh.simulator.engine.events.TurnStartEvent;
import com.pvzh.simulator.engine.events.UnveilEvent;
import com.pvzh.simulator.model.Card;
import com.pvzh.simulator.model.CardState;
import com.pvzh.simulator.model.GameState;
import com.pvzh.simulator.model.Lane;
import com.pvzh.simulator.model.Phase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles universal Left-to-Right, Zombie-First resolution for board events,
 * and integrates with the EventManager for pub/sub mechanics.
 */
public class EventDispatcher {
    private final GameState gameState;
    private final EventManager eventManager;

    public EventDispatcher(GameState gameState, EventManager eventManager) {
        this.gameState = gameState;
        this.eventManager = eventManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Sweeps the board from Lane 1 to Lane 5.
     * Within each lane, applies the action to all Zombie fighters first, then all Plant fighters.
     */
    public void sweepBoard(Consumer<Card> zombieAction, Consumer<Card> plantAction) {
        for (Lane lane : gameState.getLanes()) {
            if (zombieAction != null) {
                // To avoid concurrent modification, iterate over a copy
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
     * Sweeps both hands, then sweeps the board Left-to-Right, Zombie-First.
     * Required for "End of Turn" transformation mechanics (e.g., Reincarnation).
     */
    public void sweepBoardAndHands(Consumer<Card> zombieAction, Consumer<Card> plantAction) {
        // Sweep Hands first (order usually doesn't matter for hands, but we can do Zombie Hand then Plant Hand)
        if (zombieAction != null) {
            for (Card zombieHandCard : new ArrayList<>(gameState.getZombiePlayer().getHand())) {
                zombieAction.accept(zombieHandCard);
            }
        }
        if (plantAction != null) {
            for (Card plantHandCard : new ArrayList<>(gameState.getPlantPlayer().getHand())) {
                plantAction.accept(plantHandCard);
            }
        }

        // Sweep Board
        sweepBoard(zombieAction, plantAction);
    }

    /**
     * Unveils all gravestones on the board. Usually called during the transition to ZOMBIE_TRICKS.
     */
    public void unveilGravestones() {
        sweepBoard(
            zombie -> {
                if (zombie.getState() == CardState.GRAVESTONE) {
                    zombie.setState(CardState.REVEALED);
                    eventManager.publish(new UnveilEvent(zombie));
                }
            },
            null // Plants do not typically have gravestones, but we could make it universal if a mod requires it
        );
    }

    /**
     * Resolves pending destructions across the entire board.
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

    public void triggerPhaseStart(Phase phase, int turnNumber) {
        if (phase == Phase.ZOMBIE_PLAY) {
            // Global event for external listeners
            eventManager.publish(new TurnStartEvent(turnNumber));

            // Sequential left-to-right + hand sweep for cards responding to turn start
            sweepBoardAndHands(
                zombie -> eventManager.publish(new EntityTurnStartEvent(zombie, turnNumber)),
                plant -> eventManager.publish(new EntityTurnStartEvent(plant, turnNumber))
            );
        }
    }

    public void triggerPhaseEnd(Phase phase, int turnNumber) {
        if (phase == Phase.FIGHT) {
            // Sequential left-to-right + hand sweep for cards responding to turn end (e.g. Reincarnation)
            sweepBoardAndHands(
                zombie -> eventManager.publish(new EntityTurnEndEvent(zombie, turnNumber)),
                plant -> eventManager.publish(new EntityTurnEndEvent(plant, turnNumber))
            );

            // Global event for external listeners
            eventManager.publish(new TurnEndEvent(turnNumber));
        }
    }
}
