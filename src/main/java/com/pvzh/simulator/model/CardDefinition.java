package com.pvzh.simulator.model;

import java.util.List;

/**
 * Blueprint for a card, intended to be loaded from external JSON files.
 * This ensures strict Data Decoupling.
 */
public class CardDefinition {
    private String id;
    private String name;
    private Faction faction;
    private CardType type;
    private int baseCost;
    private int baseAttack; // 0 for tricks/environments
    private int baseHealth; // 0 for tricks/environments
    private List<String> traits; // E.g., "Amphibious", "Armored 1", "Bullseye"
    private List<String> abilities; // IDs referencing ability logic scripts

    public CardDefinition() {
    }

    public CardDefinition(String id, String name, Faction faction, CardType type, int baseCost, int baseAttack, int baseHealth, List<String> traits, List<String> abilities) {
        this.id = id;
        this.name = name;
        this.faction = faction;
        this.type = type;
        this.baseCost = baseCost;
        this.baseAttack = baseAttack;
        this.baseHealth = baseHealth;
        this.traits = traits;
        this.abilities = abilities;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Faction getFaction() { return faction; }
    public CardType getType() { return type; }
    public int getBaseCost() { return baseCost; }
    public int getBaseAttack() { return baseAttack; }
    public int getBaseHealth() { return baseHealth; }
    public List<String> getTraits() { return traits; }
    public List<String> getAbilities() { return abilities; }
}
