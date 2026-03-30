package com.islandescape.map;

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
}
