package com.islandescape.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ManualScreen {

    private static final String TITLE = "Game Manual";

    private static final String[] LINES = {
            "Goal:",
            "Survive on the island by collecting resources and crafting tools.",
            "",
            "Controls:",
            "Player 1 move: W A S D",
            "Player 2 move: Arrow keys",
            "Gather: G (P1), M (P2)",
            "Inventory: E",
            "Crafting screen near table: I",
            "Place/confirm in crafting: Enter",
            "",
            "Tips:",
            "Use the correct tool for each resource.",
            "Work together: one player can gather while another crafts.",
            "",
            "Press ESC to return to Main Menu"
    };

    public void render(Graphics2D g2, int panelWidth, int panelHeight) {
        int panelX = panelWidth / 8;
        int panelY = panelHeight / 10;
        int panelW = panelWidth * 3 / 4;
        int panelH = panelHeight * 4 / 5;

        g2.setColor(new Color(0, 0, 0, 185));
        g2.fillRect(0, 0, panelWidth, panelHeight);

        g2.setColor(new Color(28, 38, 48, 235));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 18, 18);
        g2.setColor(new Color(234, 236, 240));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 18, 18);

        g2.setFont(new Font("SansSerif", Font.BOLD, 36));
        int titleWidth = g2.getFontMetrics().stringWidth(TITLE);
        g2.drawString(TITLE, panelX + panelW / 2 - titleWidth / 2, panelY + 56);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
        int textX = panelX + 36;
        int textY = panelY + 102;
        int lineHeight = 34;

        for (String line : LINES) {
            g2.drawString(line, textX, textY);
            textY += lineHeight;
        }
    }
}
