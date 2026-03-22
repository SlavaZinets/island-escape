package com.islandescape.map;

import java.util.HashMap;
import java.util.Map;

/*
    TileMap holds map dimensions, tile size and a collection of layers
 */
public class TileMap {

    private final int width;
    private final int height;
    private final int tileSize;
    private final Map<String, TileLayer> layers = new HashMap<>();

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
}
