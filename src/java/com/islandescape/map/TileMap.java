package com.islandescape.map;

import com.islandescape.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
    TileMap holds map dimensions, tile size and a collection of layers and tilesets
 */
public class TileMap {

    private static final String[] WALKABLE_LAYERS = {
        "ground(surface)", "ground(borders)", "bridges"
    };
    private static final String[] BLOCKING_LAYERS = {
        "ground(cliffs)", "trees", "stones"
    };
    private static final int MASK_TILE_ID = 0x1FFFFFFF;

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

    public boolean isBlocked(double x, double y, int playerWidth, int playerHeight) {
        int colStart = (int) (x / tileSize);
        int colEnd = (int) ((x + playerWidth - 1) / tileSize);
        int rowStart = (int) (y / tileSize);
        int rowEnd = (int) ((y + playerHeight - 1) / tileSize);

        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                if (row < 0 || row >= height || col < 0 || col >= width) return true;

                for (String layerName : BLOCKING_LAYERS) {
                    TileLayer layer = getLayer(layerName);
                    if (layer == null) continue;
                    int tileId = layer.getTileAt(row, col) & MASK_TILE_ID;
                    if (tileId != 0) return true;
                }

                boolean walkable = false;
                for (String layerName : WALKABLE_LAYERS) {
                    TileLayer layer = getLayer(layerName);
                    if (layer == null) continue;
                    int tileId = layer.getTileAt(row, col) & MASK_TILE_ID;
                    if (tileId != 0) { walkable = true; break; }
                }
                if (!walkable) return true;
            }
        }
        return false;
    }

    public void renderMapComponent(MapRenderer renderer, Graphics g, int screenWidth, int screenHeight,
                                   Player player1, Player player2, Set<Point> disabledResourceTiles) {

        int nativeWidth = this.getWidth() * this.getTileSize();
        int nativeHeight = this.getHeight() * this.getTileSize();

        try {
            // Render map at native resolution onto an off-screen buffer
            BufferedImage buffer = new BufferedImage(nativeWidth, nativeHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferG = buffer.createGraphics();
            renderer.render(bufferG, this, disabledResourceTiles);

            // Render players on the same buffer so they scale with the map
            if (player1 != null) {
                player1.renderPlayer(bufferG);
            }
            if (player2 != null) {
                player2.renderPlayer(bufferG);
            }

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
