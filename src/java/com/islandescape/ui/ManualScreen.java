package com.islandescape.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ManualScreen {

    private static final String TITLE = "Game Manual";

    private static final String[] LINES = {
            "Goal:",
            "Survive, gather resources, repair the boat wreck, and escape together.",
            "",
            "Player 1:",
            "Move: W A S D     Gather: G     Eat / drink: F",
            "Open inventory: E     Hotbar slots: 1 2 3 4 5",
            "",
            "Player 2:",
            "Move: Arrow keys     Gather: M     Eat / drink: .",
            "Hotbar slots: 6 7 8 9 0",
            "",
            "Shared:",
            "Crafting screen (near table): I     Confirm craft: Enter",
            "Boat repair (near wreck): R     Board boat: B",
            "Save game: F5     Close panel: Esc",
            "",
            "Press ESC to return to Main Menu"
    };

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
