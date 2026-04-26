package com.islandescape.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MainMenuScreen {

    public static final String[] OPTIONS = {"Start Game", "Manual", "Exit"};

    private Rectangle[] optionBounds = new Rectangle[OPTIONS.length];

    public void render(Graphics2D g2, int panelWidth, int panelHeight, int selectedIndex) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, panelWidth, panelHeight);

        g2.setColor(new Color(230, 236, 245));
        g2.setFont(new Font("SansSerif", Font.BOLD, 56));
        String title = "Island Escape";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        int titleY = panelHeight / 3;
        g2.drawString(title, panelWidth / 2 - titleWidth / 2, titleY);

        g2.setFont(new Font("SansSerif", Font.BOLD, 32));
        int optionsStartY = titleY + 80;
        int optionGap = 52;

        for (int i = 0; i < OPTIONS.length; i++) {
            boolean selected = i == selectedIndex;
            String label = (selected ? "> " : "  ") + OPTIONS[i];
            int textWidth = g2.getFontMetrics().stringWidth(label);
            int y = optionsStartY + i * optionGap;

            g2.setColor(selected ? new Color(255, 214, 102) : new Color(240, 240, 240));
            int x = panelWidth / 2 - textWidth / 2;
            g2.drawString(label, x, y);

            // Store bounds for click detection.
            optionBounds[i] = new Rectangle(x, y - 25, textWidth, 35);
        }

        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.setColor(new Color(200, 205, 220));
        String hint = "Use Up/Down to navigate and Enter to confirm";
        int hintWidth = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, panelWidth / 2 - hintWidth / 2, panelHeight - 40);
    }

    /**
     * Check if a click at (x, y) hits any menu option, return option index or -1 if no hit.
     */
    public int getOptionAt(int x, int y) {
        for (int i = 0; i < optionBounds.length; i++) {
            if (optionBounds[i] != null && optionBounds[i].contains(x, y)) {
                return i;
            }
        }
        return -1;
    }
}
