package com.islandescape.map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    TileMap holds map dimensions, tile size and a collection of layers and tilesets
 */
public class TileMap {

    private final int width;
    private final int height;
    private final int tileSize;
    private final Map<String, TileLayer> layers = new HashMap<>();
    private final List<Tileset> tilesets = new ArrayList<>();



    public TileMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void addLayer(TileLayer layer) {
        layers.put(layer.getName(), layer);
    }

    public TileLayer getLayer(String name) {
        return layers.get(name);
    }

    public void addTileset(Tileset tileset) {
        tilesets.add(tileset);
    }

    public List<Tileset> getTilesets() {
        return tilesets;
    }

    // Find the tileset that contains the given tile ID
    public Tileset getTilesetForTile(int tileId) {
        Tileset result = null;
        for (Tileset ts : tilesets) {
            if (ts.getFirstgid() <= tileId) {
                if (result == null || ts.getFirstgid() > result.getFirstgid()) {
                    result = ts;
                }
            }
        }
        return result;
    }
    public void renderMapComponent(MapRenderer renderer, Graphics g, int screenWidth, int screenHeight) {

        int nativeWidth = this.getWidth() * this.getTileSize();
        int nativeHeight = this.getHeight() * this.getTileSize();

        try {
            // Render map at native resolution onto an off-screen buffer
            BufferedImage buffer = new BufferedImage(nativeWidth, nativeHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferG = buffer.createGraphics();
            renderer.render(bufferG, this);
            bufferG.dispose();

            // Scale the buffer to fit the panel, preserving aspect ratio
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scaleX = (double) screenWidth / nativeWidth;
            double scaleY = (double) screenHeight / nativeHeight;
            double scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int) (nativeWidth * scale);
            int scaledHeight = (int) (nativeHeight * scale);
            int offsetX = (screenWidth - scaledWidth) / 2;
            int offsetY = (screenHeight - scaledHeight) / 2;

            g2.drawImage(buffer, offsetX, offsetY, scaledWidth, scaledHeight, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
