package com.islandescape.ui;

import com.islandescape.player.SurvivalStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class StatusBarRendererTest {

    private static final double EPS = 1e-9;

    @Test
    void fullStatRendersAsCompletelyFilled() {
        assertEquals(1.0, StatusBarRenderer.valueToFillFraction(SurvivalStats.MAX), EPS);
    }

    @Test
    void emptyStatRendersAsCompletelyEmpty() {
        assertEquals(0.0, StatusBarRenderer.valueToFillFraction(0), EPS);
    }

    @Test
    void halfStatRendersAsHalfFilled() {
        // MAX is 100; 50 → 0.5
        assertEquals(0.5, StatusBarRenderer.valueToFillFraction(SurvivalStats.MAX / 2), EPS);
    }

    @Test
    void overflowIsClampedToFullBar() {
        // Defensive: stats should never exceed MAX, but if they do the bar
        // must NOT render past 100%.
        assertEquals(1.0, StatusBarRenderer.valueToFillFraction(SurvivalStats.MAX + 50), EPS);
    }

    @Test
    void negativeIsClampedToEmptyBar() {
        assertEquals(0.0, StatusBarRenderer.valueToFillFraction(-10), EPS);
    }

    @Test
    void lowFlagMatchesSpeedThreshold() {
        // The "red bar" cutoff must be the same threshold that triggers
        // the half-speed penalty in SurvivalStats — visual and gameplay
        // signals must agree.
        assertTrue(StatusBarRenderer.isLow(SurvivalStats.LOW_THRESHOLD),
                "Exactly at the threshold counts as LOW (matches getSpeedMultiplier <= LOW_THRESHOLD)");
        assertTrue(StatusBarRenderer.isLow(SurvivalStats.LOW_THRESHOLD - 1));
        assertTrue(StatusBarRenderer.isLow(0),
                "Empty stat is the strongest LOW signal");
    }

    @Test
    void healthyStatIsNotLow() {
        assertFalse(StatusBarRenderer.isLow(SurvivalStats.LOW_THRESHOLD + 1),
                "One unit above the threshold is healthy — bar must NOT tint red");
        assertFalse(StatusBarRenderer.isLow(SurvivalStats.MAX),
                "A full stat is healthy");
    }
}
