package com.islandescape.player;

import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PlayerSurvivalIntegrationTest {

    // Player.SPEED is private; this constant mirrors it for assertions
    private static final double SPEED = 2.0;

    // Calls tickDown() the requested number of times
    private static void tickDownTimes(SurvivalStats s, int n) {
        for (int i = 0; i < n; i++) {
            s.tickDown();
        }
    }

    // Number of ticks needed to drop a stat by exactly N units.
    private static int ticksForDrop(int unitsToDrop) {
        return (int) Math.round(unitsToDrop / SurvivalStats.DEPLETION_PER_TICK);
    }

    @Test
    void newPlayerHasFreshSurvivalStats() {
        Player p = new Player("Tester", 1, 100, 100);

        SurvivalStats s = p.getSurvivalStats();
        assertNotNull(s, "Player must own a SurvivalStats instance");
        assertEquals(SurvivalStats.MAX, s.getHunger());
        assertEquals(SurvivalStats.MAX, s.getThirst());
        assertEquals(1.0, s.getSpeedMultiplier(), 0.0001);
    }

    @Test
    void differentPlayersHaveIndependentSurvivalStats() {
        Player a = new Player("A", 1, 0, 0);
        Player b = new Player("B", 2, 0, 0);

        // Drain only A.
        tickDownTimes(a.getSurvivalStats(), ticksForDrop(40));

        assertEquals(SurvivalStats.MAX - 40, a.getSurvivalStats().getHunger());
        assertEquals(SurvivalStats.MAX, b.getSurvivalStats().getHunger(),
                "Player B's stats must not be affected by ticking Player A");
    }

    @Test
    void movementIsFullSpeedWhenStatsAreFull() {
        Player p = new Player("Tester", 1, 100, 100);

        double startX = p.getX();
        p.move(new Direction(1, 0));

        assertEquals(SPEED, p.getX() - startX, 0.0001,
                "At full stats, multiplier=1.0 → move covers exactly SPEED pixels");
    }

    @Test
    void movementIsHalvedWhenStatIsInLowZone() {
        Player p = new Player("Tester", 1, 100, 100);
        SurvivalStats s = p.getSurvivalStats();

        // Drain hunger into the low zone (≤ LOW_THRESHOLD, > 0).
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX - SurvivalStats.LOW_THRESHOLD + 5));
        assertTrue(s.getHunger() <= SurvivalStats.LOW_THRESHOLD && s.getHunger() > 0);
        assertEquals(0.5, s.getSpeedMultiplier(), 0.0001);

        double startX = p.getX();
        p.move(new Direction(1, 0));

        assertEquals(SPEED * 0.5, p.getX() - startX, 0.0001,
                "In the low zone the player moves at half speed");
    }

    @Test
    void movementIsQuarterWhenStatHitsZero() {
        Player p = new Player("Tester", 1, 100, 100);
        SurvivalStats s = p.getSurvivalStats();

        // Drain past empty.
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 10);
        assertEquals(0, s.getHunger());
        assertEquals(0.25, s.getSpeedMultiplier(), 0.0001);

        double startX = p.getX();
        p.move(new Direction(1, 0));

        assertEquals(SPEED * 0.25, p.getX() - startX, 0.0001,
                "When starving the player moves at quarter speed");
    }

    @Test
    void eatingRestoresFullSpeed() {
        Player p = new Player("Tester", 1, 100, 100);
        SurvivalStats s = p.getSurvivalStats();

        // Drain to empty so multiplier is 0.25 (and thirst will follow).
        tickDownTimes(s, ticksForDrop(SurvivalStats.MAX) + 10);
        assertEquals(0.25, s.getSpeedMultiplier(), 0.0001);

        // Restore both stats well above LOW_THRESHOLD.
        s.eat(80);
        s.drink(80);
        assertEquals(1.0, s.getSpeedMultiplier(), 0.0001);

        double startX = p.getX();
        p.move(new Direction(1, 0));

        assertEquals(SPEED, p.getX() - startX, 0.0001,
                "After eating and drinking, full speed is restored");
    }
}
