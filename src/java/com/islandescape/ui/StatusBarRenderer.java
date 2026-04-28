package com.islandescape.ui;

import com.islandescape.player.SurvivalStats;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/*
    Pure renderer for the hunger/thirst HUD that sits above each player's
 */
public final class StatusBarRenderer {

    public static final int BAR_HEIGHT = 12;
    public static final int BAR_GAP = 3;

    private static final Color HUNGER_FULL = new Color(220, 140, 50);   // orange
    private static final Color THIRST_FULL = new Color(70, 160, 230);   // blue
    private static final Color LOW_TINT    = new Color(220, 60, 60);    // red — both bars when low
    private static final Color BG_FILL     = new Color(0, 0, 0, 160);
    private static final Color BORDER      = new Color(255, 255, 255, 90);
    private static final Color LABEL_COLOR = new Color(255, 255, 255, 200);

    private StatusBarRenderer() {} // utility only

    /*
        Returns the [0..1] fill fraction for a single stat value. Clamped
     */
    public static double valueToFillFraction(int value) {
        if (value <= 0) return 0.0;
        if (value >= SurvivalStats.MAX) return 1.0;
        return value / (double) SurvivalStats.MAX;
    }

    /*
        True when the bar should render in the LOW_TINT (red) — exactly
        the speed-slowdown threshold so the UI signal matches the
        gameplay consequence.
     */
    public static boolean isLow(int value) {
        return value <= SurvivalStats.LOW_THRESHOLD;
    }

    /*
        Draws both bars (hunger on top, thirst below it) ABOVE the anchor
        point.
     */
    public static void drawForPlayer(Graphics2D g, SurvivalStats stats,
                                     int anchorX, int anchorBottomY, int width) {
        if (stats == null) return;

        int thirstTop = anchorBottomY - BAR_HEIGHT;
        int hungerTop = thirstTop - BAR_GAP - BAR_HEIGHT;

        drawBar(g, anchorX, hungerTop, width, stats.getHunger(), HUNGER_FULL, "FOOD");
        drawBar(g, anchorX, thirstTop, width, stats.getThirst(), THIRST_FULL, "WATER");
    }

    /*
        One bar: rounded background, fill proportional to value, optional
        red tint when value is at-or-below LOW_THRESHOLD, white border,
        small numeric label centered inside.
     */
    private static void drawBar(Graphics2D g, int x, int y, int w, int value,
                                Color fullColor, String label) {
        // Background — always full width, dark translucent.
        g.setColor(BG_FILL);
        g.fillRoundRect(x, y, w, BAR_HEIGHT, 6, 6);

        // Filled portion.
        int filled = (int) Math.round(w * valueToFillFraction(value));
        Color barColor = isLow(value) ? LOW_TINT : fullColor;
        g.setColor(barColor);
        if (filled > 0) {
            g.fillRoundRect(x, y, filled, BAR_HEIGHT, 6, 6);
        }

        // Border.
        g.setColor(BORDER);
        g.drawRoundRect(x, y, w, BAR_HEIGHT, 6, 6);

        // Tiny label + numeric value, centered.
        g.setColor(LABEL_COLOR);
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        String text = label + " " + Math.max(0, value);
        int textW = g.getFontMetrics().stringWidth(text);
        // Drop the text 1 px below visual center to look optically balanced
        // against the small bar height.
        int textY = y + BAR_HEIGHT - 3;
        g.drawString(text, x + (w - textW) / 2, textY);
    }
}
