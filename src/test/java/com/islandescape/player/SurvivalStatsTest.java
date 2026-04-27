package com.islandescape.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SurvivalStatsTest {

    //Number of ticks needed to drop a stat by exactly 1 unit
    private static int ticksForDrop(int unitsToDrop) {
        return (int) Math.round(unitsToDrop / SurvivalStats.DEPLETION_PER_TICK);
    }

    // Calls tickDown() the requested number of times
    private static void tickDownTimes(SurvivalStats s, int n) {
        for (int i = 0; i < n; i++) {
            s.tickDown();
        }
    }

    @Test
    void newStatsStartFullAndAlive() {
        SurvivalStats s = new SurvivalStats();

        assertEquals(SurvivalStats.MAX, s.getHunger());
        assertEquals(SurvivalStats.MAX, s.getThirst());
        assertFalse(s.isStarving());
        assertEquals(0, s.getTicksAtZero());
        assertFalse(s.isDead());
        assertEquals(1.0, s.getSpeedMultiplier(), 0.0001);
    }

    @Test
    void tickDownDepletesBothHungerAndThirst() {
        SurvivalStats s = new SurvivalStats();

        // Drop by exactly 1 unit.
        tickDownTimes(s, ticksForDrop(1));

        assertEquals(SurvivalStats.MAX - 1, s.getHunger());
        assertEquals(SurvivalStats.MAX - 1, s.getThirst());
    }

    @Test
    void tickDownDoesNotDriveStatsBelowZero() {
        SurvivalStats s = new SurvivalStats();

        // Tick way past empty.
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 5000);

        assertEquals(0, s.getHunger());
        assertEquals(0, s.getThirst());
    }

    @Test
    void eatRestoresHungerOnly() {
        SurvivalStats s = new SurvivalStats();
        tickDownTimes(s, ticksForDrop(40));   // hunger and thirst now 60

        s.eat(20);

        assertEquals(80, s.getHunger());
        assertEquals(60, s.getThirst()); // thirst unaffected
    }

    @Test
    void eatCapsAtMax() {
        SurvivalStats s = new SurvivalStats();
        tickDownTimes(s, ticksForDrop(10));   // hunger == 90

        s.eat(50);

        assertEquals(SurvivalStats.MAX, s.getHunger());
    }

    @Test
    void eatIgnoresNegativeAmounts() {
        SurvivalStats s = new SurvivalStats();
        tickDownTimes(s, ticksForDrop(20));   // hunger == 80

        s.eat(-30);

        assertEquals(80, s.getHunger());
    }

    @Test
    void drinkRestoresThirstOnly() {
        SurvivalStats s = new SurvivalStats();
        tickDownTimes(s, ticksForDrop(40));   // hunger and thirst now 60

        s.drink(25);

        assertEquals(60, s.getHunger());     // hunger unaffected
        assertEquals(85, s.getThirst());
    }

    @Test
    void drinkCapsAtMax() {
        SurvivalStats s = new SurvivalStats();
        tickDownTimes(s, ticksForDrop(5));    // thirst == 95

        s.drink(50);

        assertEquals(SurvivalStats.MAX, s.getThirst());
    }

    @Test
    void speedMultiplierIsOneWhenBothStatsAboveLowThreshold() {
        SurvivalStats s = new SurvivalStats();
        // Drop slightly so we're not at MAX but still above LOW_THRESHOLD.
        tickDownTimes(s, ticksForDrop(10));

        assertEquals(1.0, s.getSpeedMultiplier(), 0.0001);
    }

    @Test
    void speedMultiplierIsHalfWhenAStatDropsToLowZone() {
        SurvivalStats s = new SurvivalStats();
        // Drop hunger and thirst into the (0, LOW_THRESHOLD] band.
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX - SurvivalStats.LOW_THRESHOLD + 5));

        assertTrue(s.getHunger() <= SurvivalStats.LOW_THRESHOLD && s.getHunger() > 0,
                "expected hunger to be in low zone but was " + s.getHunger());
        assertEquals(0.5, s.getSpeedMultiplier(), 0.0001);
    }

    @Test
    void speedMultiplierIsQuarterWhenAStatHitsZero() {
        SurvivalStats s = new SurvivalStats();
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 10);

        assertEquals(0, s.getHunger());
        assertEquals(0.25, s.getSpeedMultiplier(), 0.0001);
    }

    @Test
    void isStarvingTrueOnlyWhenAStatHitsZero() {
        SurvivalStats s = new SurvivalStats();
        assertFalse(s.isStarving());

        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX - 1)); // both at 1
        assertFalse(s.isStarving());

        tickDownTimes(s, ticksForDrop(2)); // pushed to 0
        assertTrue(s.isStarving());
    }

    @Test
    void ticksAtZeroAccumulatesOnlyWhileStarving() {
        SurvivalStats s = new SurvivalStats();

        // Tick down while NOT starving — counter must stay at 0.
        tickDownTimes(s, ticksForDrop(50));
        assertEquals(0, s.getTicksAtZero(),
                "ticksAtZero must not increment while stats are positive");

        // Drain to zero, then tick a known number of times.
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 10);
        int beforeZeroTicks = s.getTicksAtZero();
        tickDownTimes(s, 100);
        int afterZeroTicks = s.getTicksAtZero();

        assertEquals(100, afterZeroTicks - beforeZeroTicks,
                "ticksAtZero must increment by exactly the number of starving ticks");
    }

    @Test
    void ticksAtZeroResetsWhenEatingBackAboveZero() {
        SurvivalStats s = new SurvivalStats();

        // Drain hunger to 0 while keeping thirst positive enough to be sole starver.
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 10);
        tickDownTimes(s, 50);
        assertTrue(s.getTicksAtZero() > 0);

        // Eat AND drink to push both above zero.
        s.eat(50);
        s.drink(50);

        // Tick once more while not starving — counter resets.
        s.tickDown();

        assertEquals(0, s.getTicksAtZero(),
                "ticksAtZero must reset once player is no longer starving");
        assertFalse(s.isStarving());
    }

    @Test
    void isDeadOnceTicksAtZeroReachesGameOverTicks() {
        SurvivalStats s = new SurvivalStats();

        // Drain to empty.
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 5);
        assertFalse(s.isDead());

        // Stay starving until the threshold.
        tickDownTimes(s, SurvivalStats.GAME_OVER_TICKS);

        assertTrue(s.isDead(),
                "isDead must be true once ticksAtZero ≥ GAME_OVER_TICKS");
    }
}
