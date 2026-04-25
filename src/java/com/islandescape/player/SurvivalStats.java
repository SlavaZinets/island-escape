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

    }

    // Decrements both stats by DEPLETION_PER_TICK; bumps ticksAtZero when starving.
    public void tickDown() {

    }

    // Restores hunger by amount, capped at MAX. Negative amounts ignored
    public void eat(int amount) {

    }

    // Restores thirst by amount, capped at MAX. Negative amounts ignored
    public void drink(int amount) {
        // Stub: no-op until Step 3.
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
        // Stub: returns 1.0 until Step 3.
        return 1.0;
    }

    // True if hunger == 0 OR thirst == 0.
    public boolean isStarving() {

        return false;
    }

    //Number of consecutive ticks the player has been starving. Resets when both stats > 0.
    public int getTicksAtZero() {
        return ticksAtZero;
    }

    // True once getTicksAtZero() ≥ GAME_OVER_TICKS  GamePanel should transition to GAME_OVER
    public boolean isDead() {

        return false;
    }
}
