package com.pvzh.simulator.model;

/**
 * The source of a card being added to the hand, determining if hand size limits apply.
 */
public enum CardSource {
    /** Regular draw from the deck. Subject to the 10-card limit. */
    DRAW,
    /** Generated card. Subject to the 10-card limit. */
    CONJURE,
    /** Card returned from the board. Bypasses the 10-card limit. */
    BOUNCE,
    /** Superpower granted from a block. Subject to the 10-card limit. */
    BLOCK_REWARD
}
