package com.islandescape.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Full-screen "YOU ESCAPED" overlay shown when the game reaches GAME_WON
 */
public class WinOverlay {

    private static final Color DIM = new Color(0, 0, 0, 210);
    private static final Color TITLE_COLOR = new Color(255, 215, 80);
    private static final Color SUBTITLE_COLOR = new Color(230, 230, 230);

    private static final String TITLE_TEXT = "YOU ESCAPED";
    private static final String SUBTITLE_TEXT = "Both survivors made it off the island";

    public void render(Graphics2D g, int panelW, int panelH) {

        g.setColor(DIM);
        g.fillRect(0, 0, panelW, panelH);


        g.setFont(new Font("SansSerif", Font.BOLD, 72));
        int titleW = g.getFontMetrics().stringWidth(TITLE_TEXT);
        g.setColor(TITLE_COLOR);
        g.drawString(TITLE_TEXT, panelW / 2 - titleW / 2, panelH / 2);


        g.setFont(new Font("SansSerif", Font.PLAIN, 22));
        int subW = g.getFontMetrics().stringWidth(SUBTITLE_TEXT);
        g.setColor(SUBTITLE_COLOR);
        g.drawString(SUBTITLE_TEXT, panelW / 2 - subW / 2, panelH / 2 + 40);
    }
}
