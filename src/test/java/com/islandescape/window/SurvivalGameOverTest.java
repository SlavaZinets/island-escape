package com.islandescape.window;

import com.islandescape.player.Player;
import com.islandescape.player.SurvivalStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SurvivalGameOverTest {

    // Number of ticks needed to drop a stat by exactly N units
    private static int ticksForDrop(int unitsToDrop) {
        return (int) Math.round(unitsToDrop / SurvivalStats.DEPLETION_PER_TICK);
    }

    /**
     * Simulates the per-frame loop in GamePanel
     * player's survival stats and return whether the game should transition
     * to GAME_OVER. Stops as soon as the player dies or the budget runs out.
     */
    private static GameState simulateGameLoop(Player player, int maxTicks) {
        for (int i = 0; i < maxTicks; i++) {
            player.getSurvivalStats().tickDown();
            if (player.getSurvivalStats().isDead()) {
                return GameState.GAME_OVER;
            }
        }
        return GameState.PLAYING;
    }

    @Test
    void gameOverEnumValueExists() {
        // Compile-time guard: the enum value must be reachable.
        GameState s = GameState.GAME_OVER;
        assertNotNull(s);

        // Runtime guard: it's distinct from PLAYING and INVENTORY_OPEN.
        assertEquals("GAME_OVER", s.name());
    }

    @Test
    void wellFedPlayerNeverTriggersGameOver() {
        Player p = new Player("Tester", 1, 0, 0);

        // Tick aggressively, but feed/drink every once in a while to keep
        // both stats above LOW_THRESHOLD.
        for (int i = 0; i < 50_000; i++) {
            p.getSurvivalStats().tickDown();
            if (i % 1000 == 0) {
                p.getSurvivalStats().eat(50);
                p.getSurvivalStats().drink(50);
            }
            assertFalse(p.getSurvivalStats().isDead(),
                    "A well-fed player must never reach the death threshold");
        }
    }

    @Test
    void partialStarvationBelowThresholdDoesNotKill() {
        Player p = new Player("Tester", 1, 0, 0);

        // Drain past empty so isStarving() is true.
        for (int i = 0; i < ticksForDrop(SurvivalStats.MAX) + 10; i++) {
            p.getSurvivalStats().tickDown();
        }
        assertTrue(p.getSurvivalStats().isStarving());

        // Tick MUCH less than GAME_OVER_TICKS — player is suffering but alive.
        for (int i = 0; i < SurvivalStats.GAME_OVER_TICKS / 4; i++) {
            p.getSurvivalStats().tickDown();
        }

        assertFalse(p.getSurvivalStats().isDead(),
                "Starving for less than GAME_OVER_TICKS must not be fatal");
    }

    @Test
    void simulatedLoopTransitionsToGameOverWhenStarvingTooLong() {
        Player p = new Player("Tester", 1, 0, 0);

        // Generous budget: enough to drain to zero AND stay there past the threshold.
        int budget = ticksForDrop(SurvivalStats.MAX) + SurvivalStats.GAME_OVER_TICKS + 100;
        GameState result = simulateGameLoop(p, budget);

        assertEquals(GameState.GAME_OVER, result,
                "Player who never eats should trigger GAME_OVER within the budget");
    }

    @Test
    void eatingMidStarvationResetsTheDeathTimer() {
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();

        // Drain past empty so the death timer starts.
        for (int i = 0; i < ticksForDrop(SurvivalStats.MAX) + 10; i++) {
            s.tickDown();
        }
        // Stay starving for half the threshold.
        for (int i = 0; i < SurvivalStats.GAME_OVER_TICKS / 2; i++) {
            s.tickDown();
        }
        assertTrue(s.getTicksAtZero() > 0);
        assertFalse(s.isDead());

        // Eat AND drink — both stats jump above zero.
        s.eat(80);
        s.drink(80);

        // Continue ticking — but multiplier=1, stats high, ticksAtZero stays at 0.
        // Tick enough that, *had the timer not reset*, the player would have died.
        for (int i = 0; i < SurvivalStats.GAME_OVER_TICKS; i++) {
            s.tickDown();
            assertFalse(s.isDead(),
                    "Death timer must reset on recovery — well-fed player can't die");
        }
    }

    @Test
    void oneStatAtZeroIsEnoughToKill() {
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();

        // Drain both. While ticking, periodically top up thirst — keep ONLY
        // thirst above zero. Hunger will sit at zero accumulating ticks.
        // Eventually hunger-alone runs the timer to GAME_OVER_TICKS.
        for (int i = 0; i < ticksForDrop(SurvivalStats.MAX) + 10; i++) {
            s.tickDown();
            if (i % 100 == 0) s.drink(2); // small top-up so thirst > 0
        }

        // Now hunger is 0, thirst is positive. Continue ticking and topping
        // up only thirst. ticksAtZero must keep growing because hunger == 0.
        int extra = SurvivalStats.GAME_OVER_TICKS + 200;
        for (int i = 0; i < extra; i++) {
            s.tickDown();
            if (i % 50 == 0) s.drink(2);
        }

        assertEquals(0, s.getHunger(), "Hunger should still be at zero");
        assertTrue(s.getThirst() > 0, "Thirst was kept positive throughout");
        assertTrue(s.isDead(),
                "A single stat at zero for long enough must trigger death — not just both stats");
    }
}
