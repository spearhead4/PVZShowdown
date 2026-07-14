package com.pvzh.simulator.model;

public enum Phase {
    /** Zombie Phase: Play fighters/environments (No Tricks). */
    ZOMBIE_PLAY,
    /** Plant Phase: Play anything (fighters, environments, tricks). */
    PLANT_PLAY,
    /** Zombie Tricks Phase: Play Tricks using leftover Brains. */
    ZOMBIE_TRICKS,
    /** Fight Phase: Lanes resolve left to right (1 to 5). */
    FIGHT
}
