package com.islandescape.ui;

import com.islandescape.player.Player;
import com.islandescape.player.SurvivalStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GameOverOverlayTest {

    /*
        Drives a player's stats all the way to dead so causeOfDeath can
        observe a real isDead() == true. Mirrors the simulation pattern
        used by SurvivalGameOverTest.
     */
    private static void killPlayer(Player p) {
        SurvivalStats s = p.getSurvivalStats();
        // Drain hunger AND thirst to zero, then accumulate ticksAtZero
        // past GAME_OVER_TICKS.
        int drainTicks = (int) Math.ceil(SurvivalStats.MAX / SurvivalStats.DEPLETION_PER_TICK) + 5;
        for (int i = 0; i < drainTicks; i++) s.tickDown();
        for (int i = 0; i < SurvivalStats.GAME_OVER_TICKS + 5; i++) s.tickDown();
        assertTrue(s.isDead(), "Test setup failed — player should be dead");
    }

    @Test
    void noOneDeadReturnsNull() {
        Player p1 = new Player("Alice", 1, 0, 0);
        Player p2 = new Player("Bob",   2, 0, 0);
        // Neither has had stats touched — both are alive at MAX.
        assertNull(GameOverOverlay.causeOfDeath(p1, p2),
                "When no one is dead, no subtitle should be shown");
    }

    @Test
    void onlyPlayer1DeadShowsPlayer1Subtitle() {
        Player p1 = new Player("Alice", 1, 0, 0);
        Player p2 = new Player("Bob",   2, 0, 0);
        killPlayer(p1);

        String s = GameOverOverlay.causeOfDeath(p1, p2);
        assertEquals("Alice starved", s);
    }

    @Test
    void onlyPlayer2DeadShowsPlayer2Subtitle() {
        Player p1 = new Player("Alice", 1, 0, 0);
        Player p2 = new Player("Bob",   2, 0, 0);
        killPlayer(p2);

        String s = GameOverOverlay.causeOfDeath(p1, p2);
        assertEquals("Bob starved", s);
    }

    @Test
    void bothDeadShowsCombinedSubtitle() {
        Player p1 = new Player("Alice", 1, 0, 0);
        Player p2 = new Player("Bob",   2, 0, 0);
        killPlayer(p1);
        killPlayer(p2);

        assertEquals("Both players starved",
                GameOverOverlay.causeOfDeath(p1, p2));
    }

    @Test
    void nullPlayerCountsAsAliveNotDead() {
        // Defensive: a single-player session must not crash on a null P2.
        Player p1 = new Player("Alice", 1, 0, 0);
        // P1 still alive → no subtitle.
        assertNull(GameOverOverlay.causeOfDeath(p1, null));

        // P1 dead → subtitle shown despite null P2.
        killPlayer(p1);
        assertEquals("Alice starved", GameOverOverlay.causeOfDeath(p1, null));
    }

    @Test
    void unnamedPlayerFallsBackToIdLabel() {
        // If a player has no name set, the subtitle uses "P<id>" instead
        // of leaking a null into the string.
        Player p1 = new Player("", 1, 0, 0);
        killPlayer(p1);

        Player p2 = new Player("Bob", 2, 0, 0);
        assertEquals("P1 starved", GameOverOverlay.causeOfDeath(p1, p2));
    }
}
