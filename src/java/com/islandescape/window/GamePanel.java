package com.islandescape.window;

import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/*
    GamePanel sits inside GameWindow.
    It holds the TileMap and MapRenderer, and paints the map every frame.
    Renders the map at native resolution, then scales to fit the panel.
 */
public class GamePanel extends JPanel {

    private final TileMap map;
    private final MapRenderer renderer;
    private Player player;


    public GamePanel(TileMap map, MapRenderer renderer) {
        this.map = map;
        this.renderer = renderer;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TileMap getMap() {
        return map;
    }

    public MapRenderer getRenderer() {
        return renderer;
    }

    // Called by Swing whenever the panel needs to be drawn
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        map.renderMapComponent(renderer, g, getWidth(), getHeight(), player);
    }
}
