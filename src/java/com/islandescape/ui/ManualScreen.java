package com.islandescape.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ManualScreen {

    private static final String TITLE = "Game Manual";

    private static final String[] CONTROLS = {
            "Goal:",
            "Survive, gather resources, repair the boat,",
            "and board it together to escape.",
            "Movement:",
            "  Player 1: W A S D | Player 2: Arrow keys",
            "Action / place item in slot:",
            "  Player 1: E   (also opens inventory in-game)",
            "  Player 2: Space",
            "Gather resource (near a tree / stone / vines):",
            "  Player 1: G | Player 2: M",
            "Eat or drink first consumable in hotbar:",
            "  Player 1: F | Player 2: .",
            "Hotbar slot select:",
            "  Player 1: 1 2 3 4 5",
            "  Player 2: 6 7 8 9 0",
            "Open crafting screen (near table):  I",
            "Confirm craft: Enter",
            "Open boat repair screen (near wreck): R",
            "Board the boat (when fully repaired): B",
            "Save game: F5",
            "Close panel / back to main menu:    Esc",
    };

    private static final String[] CRAFTS = {
            "Crafts (place ingredients in the 2x2 grid",
            "near the crafting table):",
            "",
            "  Rope = 2 Vines",
            "  Axe = 1 Stone + 1 Wood + 1 Rope",
            "  Pickaxe = 2 Stone + 1 Wood + 1 Rope",
            "  Plank x3 = 2 Wood",
            "  Paddle = 2 Plank",
            "  Frame = 2 Plank + 2 Rope",
            "  Mast = 3 Wood + 1 Rope",
            "  Rudder = 2 Plank + 1 Stone + 1 Rope",
            "  Fittings = 2 Wood + 1 Stone + 1 Rope",
            "  Sail = 2 Tropical Leaves + 1 Wood + 1 Rope",
            "Boat repair slots (deposit at the wreck):",
            "  3 Plank, 1 Mast, 1 Frame, 1 Sail,",
            "  1 Rudder, 2 Fittings",
    };

    private static final String FOOTER = "Press ESC to return to Main Menu";

    public void render(Graphics2D g2, int panelWidth, int panelHeight) {
        int panelX = panelWidth / 8;
        int panelY = panelHeight / 10;
        int panelW = panelWidth * 3 / 4;
        int panelH = panelHeight * 4 / 5;

        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRect(0, 0, panelWidth, panelHeight);

        g2.setColor(new Color(28, 38, 48, 235));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 18, 18);
        g2.setColor(new Color(234, 236, 240));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 18, 18);

        g2.setFont(new Font("SansSerif", Font.BOLD, 36));
        int titleWidth = g2.getFontMetrics().stringWidth(TITLE);
        g2.drawString(TITLE, panelX + panelW / 2 - titleWidth / 2, panelY + 56);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        int leftX = panelX + 36;
        int rightX = panelX + panelW / 2 + 18;
        int textY = panelY + 100;
        int lineHeight = 28;

        int rows = Math.max(CONTROLS.length, CRAFTS.length);
        for (int i = 0; i < rows; i++) {
            if (i < CONTROLS.length) {
                g2.drawString(CONTROLS[i], leftX, textY + i * lineHeight);
            }
            if (i < CRAFTS.length) {
                g2.drawString(CRAFTS[i], rightX, textY + i * lineHeight);
            }
        }

        g2.setFont(new Font("SansSerif", Font.ITALIC, 18));
        int footerWidth = g2.getFontMetrics().stringWidth(FOOTER);
        g2.drawString(FOOTER, panelX + panelW / 2 - footerWidth / 2, panelY + panelH - 24);
    }
}
