package com.islandescape.ui;

import com.islandescape.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/*
    Full-screen GAME_OVER overlay. Painted last in GamePanel.paintComponent
    when gameState == GAME_OVER, so it covers everything underneath.

    Visual: translucent black wash + centered red "YOU DIDN'T MAKE IT"
    headline + a smaller subtitle naming the player(s) who starved.
 */
public final class GameOverOverlay {

    private static final Color WASH       = new Color(0, 0, 0, 200);
    private static final Color HEADLINE   = new Color(220, 50, 50);
    private static final Color HEADLINE_SHADOW = new Color(60, 0, 0);
    private static final Color SUBTITLE   = new Color(230, 230, 230);

    private static final String HEADLINE_TEXT = "YOU DIDN'T MAKE IT";

    private GameOverOverlay() {} // utility only

    /*
        Returns the subtitle text describing which player(s) died. Pure —
        no Graphics, fully unit-testable.

        Rules:
          - Both dead       → "Both players starved"
          - Only P1 dead    → "P1 starved"
          - Only P2 dead    → "P2 starved"
          - Neither dead OR a player is null → null  (caller skips the subtitle)
     */
    public static String causeOfDeath(Player p1, Player p2) {
        boolean p1Dead = p1 != null && p1.getSurvivalStats().isDead();
        boolean p2Dead = p2 != null && p2.getSurvivalStats().isDead();

        if (p1Dead && p2Dead) return "Both players starved";
        if (p1Dead)           return playerLabel(p1) + " starved";
        if (p2Dead)           return playerLabel(p2) + " starved";
        return null;
    }

    private static String playerLabel(Player p) {
        // Prefer the player's name; fall back to a stable ID label so the
        // subtitle never collapses into "null starved" if name is unset.
        String name = p.getName();
        return (name == null || name.isEmpty()) ? ("P" + p.getId()) : name;
    }

    /*
        Paints the full overlay:
          1. Translucent black wash over the whole panel.
          2. Centered red headline ("YOU DIDN'T MAKE IT") with a dark
             drop-shadow offset 3 px right/down for legibility on busy
             map backgrounds.
          3. Subtitle below the headline naming the player(s) who starved
             (skipped if causeOfDeath returns null — defensive).
     */
    public static void render(Graphics2D g, int panelW, int panelH, Player p1, Player p2) {
        // Save the existing rendering hint so we don't permanently mutate
        // the Graphics2D state after this paint.
        Object oldAA = g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Wash.
        g.setColor(WASH);
        g.fillRect(0, 0, panelW, panelH);

        // 2. Headline.
        Font headlineFont = new Font("SansSerif", Font.BOLD, Math.max(48, panelW / 18));
        g.setFont(headlineFont);
        int headlineW = g.getFontMetrics().stringWidth(HEADLINE_TEXT);
        int headlineX = (panelW - headlineW) / 2;
        int headlineY = panelH / 2;

        g.setColor(HEADLINE_SHADOW);
        g.drawString(HEADLINE_TEXT, headlineX + 3, headlineY + 3);
        g.setColor(HEADLINE);
        g.drawString(HEADLINE_TEXT, headlineX, headlineY);

        // 3. Subtitle (only when we have something to say).
        String subtitle = causeOfDeath(p1, p2);
        if (subtitle != null) {
            Font subFont = new Font("SansSerif", Font.PLAIN, Math.max(20, panelW / 50));
            g.setFont(subFont);
            int subW = g.getFontMetrics().stringWidth(subtitle);
            int subX = (panelW - subW) / 2;
            int subY = headlineY + headlineFont.getSize() / 2 + subFont.getSize() + 12;

            g.setColor(SUBTITLE);
            g.drawString(subtitle, subX, subY);
        }

        // Restore hint.
        if (oldAA != null) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldAA);
        }
    }
}
