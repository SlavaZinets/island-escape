package com.islandescape.window;

import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;

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
    private final int nativeWidth;
    private final int nativeHeight;

    public GamePanel(TileMap map, MapRenderer renderer) {
        this.map = map;
        this.renderer = renderer;

        nativeWidth = map.getWidth() * map.getTileSize();
        nativeHeight = map.getHeight() * map.getTileSize();
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
        try {
            // Render map at native resolution onto an off-screen buffer
            BufferedImage buffer = new BufferedImage(nativeWidth, nativeHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferG = buffer.createGraphics();
            renderer.render(bufferG, map);
            bufferG.dispose();

            // Scale the buffer to fit the panel, preserving aspect ratio
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scaleX = (double) getWidth() / nativeWidth;
            double scaleY = (double) getHeight() / nativeHeight;
            double scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int) (nativeWidth * scale);
            int scaledHeight = (int) (nativeHeight * scale);
            int offsetX = (getWidth() - scaledWidth) / 2;
            int offsetY = (getHeight() - scaledHeight) / 2;

            g2.drawImage(buffer, offsetX, offsetY, scaledWidth, scaledHeight, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
