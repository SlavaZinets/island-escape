package com.islandescape.player;


public class SurvivalStats {

    // Maximum value for both hunger and thirst
    public static final int MAX = 100;

    // At or below this value, the player is "low" and moves at half speed
    public static final int LOW_THRESHOLD = 20;

    public static final int GAME_OVER_TICKS = 1875;


     // Hunger and thirst depletion per tick. 0.5 per second / 62.5 fps = 0.008

    public static final double DEPLETION_PER_TICK = 0.5 / 62.5;

    private double hunger;
    private double thirst;
    private int ticksAtZero;

    public SurvivalStats() {
        this.hunger = MAX;
        this.thirst = MAX;
        this.ticksAtZero = 0;
    }

    // Decrements both stats by DEPLETION_PER_TICK; bumps ticksAtZero when starving.
    public void tickDown() {
        hunger = Math.max(0.0, hunger - DEPLETION_PER_TICK);
        thirst = Math.max(0.0, thirst - DEPLETION_PER_TICK);
        if (isStarving()) {
            ticksAtZero++;
        } else {
            ticksAtZero = 0;
        }
    }

    // Restores hunger by amount, capped at MAX. Negative amounts ignored
    public void eat(int amount) {
        if (amount <= 0) return;
        hunger = Math.min(MAX, hunger + amount);
    }

    // Restores thirst by amount, capped at MAX. Negative amounts ignored
    public void drink(int amount) {
        if (amount <= 0) return;
        thirst = Math.min(MAX, thirst + amount);
    }

    // Current hunger, floored to an int in [0, MAX].
    public int getHunger() {
        return (int) hunger;
    }

    // Current thirst, floored to an int in [0, MAX].
    public int getThirst() {
        return (int) thirst;
    }


     // Movement speed multiplier based on the worse of hunger/thirst:

    public double getSpeedMultiplier() {
        int worst = Math.min(getHunger(), getThirst());
        if (worst <= 0) return 0.25;
        if (worst <= LOW_THRESHOLD) return 0.5;
        return 1.0;
    }

    // True if hunger == 0 OR thirst == 0.
    public boolean isStarving() {
        return hunger <= 0.0 || thirst <= 0.0;
    }

    //Number of consecutive ticks the player has been starving. Resets when both stats > 0.
    public int getTicksAtZero() {
        return ticksAtZero;
    }

    // True once getTicksAtZero() ≥ GAME_OVER_TICKS  GamePanel should transition to GAME_OVER
    public boolean isDead() {
        return ticksAtZero >= GAME_OVER_TICKS;
    }
}
