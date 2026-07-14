package com.pvzh.simulator.model;

public class Player {
    private final Faction faction;
    private int health;
    private int blockMeter;
    private int maxResources; // Suns for plants, Brains for zombies
    private int currentResources;

    public Player(Faction faction) {
        this.faction = faction;
        this.health = 20; // Default hero health
        this.blockMeter = 0;
        this.maxResources = 0;
        this.currentResources = 0;
    }

    public Faction getFaction() {
        return faction;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
    }

    public int getBlockMeter() {
        return blockMeter;
    }

    public void addBlockMeterCharges(int charges) {
        this.blockMeter += charges;
    }

    public void resetBlockMeter() {
        this.blockMeter = 0;
    }

    public int getMaxResources() {
        return maxResources;
    }

    public void setMaxResources(int maxResources) {
        this.maxResources = maxResources;
    }

    public int getCurrentResources() {
        return currentResources;
    }

    public void setCurrentResources(int currentResources) {
        this.currentResources = currentResources;
    }

    public void spendResources(int amount) {
        this.currentResources -= amount;
    }
}
