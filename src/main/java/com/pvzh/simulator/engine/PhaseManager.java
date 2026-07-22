package com.pvzh.simulator.engine;

import com.pvzh.simulator.model.Phase;

/**
 * State machine enforcing the 4 asymmetrical phases of a PvZ Heroes turn.
 */
public class PhaseManager {
    private Phase currentPhase;
    private int turnNumber;

    public PhaseManager() {
        this.turnNumber = 1;
        this.currentPhase = Phase.ZOMBIE_PLAY;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Advances the phase to the next step in the sequence.
     * Triggers End of Phase and Start of Phase events.
     */
    public void advancePhase(EventDispatcher dispatcher) {
        if (dispatcher != null) {
            dispatcher.triggerPhaseEnd(currentPhase);
        }

        switch (currentPhase) {
            case ZOMBIE_PLAY:
                currentPhase = Phase.PLANT_PLAY;
                break;
            case PLANT_PLAY:
                currentPhase = Phase.ZOMBIE_TRICKS;
                break;
            case ZOMBIE_TRICKS:
                currentPhase = Phase.FIGHT;
                break;
            case FIGHT:
                currentPhase = Phase.ZOMBIE_PLAY;
                turnNumber++;
                break;
        }

        if (dispatcher != null) {
            dispatcher.triggerPhaseStart(currentPhase);
        }
    }

    public boolean isPlantTurn() {
        return currentPhase == Phase.PLANT_PLAY;
    }

    public boolean isZombieTurn() {
        return currentPhase == Phase.ZOMBIE_PLAY || currentPhase == Phase.ZOMBIE_TRICKS;
    }
}
