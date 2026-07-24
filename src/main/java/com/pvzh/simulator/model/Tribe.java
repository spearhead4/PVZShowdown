package com.pvzh.simulator.model;

/**
 * Represents the sub-type of a card, which can trigger specific synergies.
 * A card may belong to multiple tribes.
 */
public enum Tribe {
    // Plant Tribes
    PEA,
    FLOWER,
    NUT,
    MUSHROOM,
    ROOT,
    BERRY,
    LEAFY,
    PINECLONE, // Specialized but standard in PvZH
    BEAN,
    TREE,
    SEED,
    CACTUS,
    CORN,
    ANIMAL,
    MOSS,
    SQUASH,
    TRICK, // Some tricks have tribes, though typically they are just CardType.TRICK

    // Zombie Tribes
    GARGANTUAR,
    PROFESSIONAL,
    PET,
    IMP,
    SPORTS,
    SCIENCE,
    DANCING,
    PIRATE,
    MUSTACHE,
    MONSTER,
    HISTORY,
    PARTY,
    CLOCK,
    BARREL,
    GOURMET
}
