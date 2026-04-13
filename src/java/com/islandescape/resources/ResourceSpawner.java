package com.islandescape.resources;

import com.islandescape.map.TileLayer;
import com.islandescape.map.TileMap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Walks tile layers and emits one ResourceNode per non-zero tile.
public final class ResourceSpawner {

    private static final int MASK_TILE_ID = 0x1FFFFFFF;

    private ResourceSpawner() {}

    public static Set<Point> disabledTileCoords(List<ResourceNode> nodes) {
        return null;
    }

    public static ArrayList<ResourceNode> spawnFromMap(TileMap map) {
        ArrayList<ResourceNode> nodes = new ArrayList<>();
        nodes.addAll(spawnTrees(map));
        nodes.addAll(spawnStones(map));
        return nodes;
    }

    private static ArrayList<ResourceNode> spawnTrees(TileMap map) {
        ArrayList<ResourceNode> out = new ArrayList<>();
        TileLayer layer = map.getLayer("trees");
        if (layer == null) return out;

        int width = map.getWidth();
        int height = map.getHeight();
        int tileSize = map.getTileSize();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((layer.getTileAt(row, col) & MASK_TILE_ID) == 0) continue;

                double cx = col * tileSize + tileSize / 2.0;
                double cy = row * tileSize + tileSize / 2.0;
                Tree node = new Tree(cx, cy);
                node.setTileCoords(Collections.singletonList(new Point(col, row)));
                out.add(node);
            }
        }
        return out;
    }

    private static ArrayList<ResourceNode> spawnStones(TileMap map) {
        ArrayList<ResourceNode> out = new ArrayList<>();
        TileLayer layer = map.getLayer("stones");
        if (layer == null) return out;

        int width = map.getWidth();
        int height = map.getHeight();
        int tileSize = map.getTileSize();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((layer.getTileAt(row, col) & MASK_TILE_ID) == 0) continue;

                double cx = col * tileSize + tileSize / 2.0;
                double cy = row * tileSize + tileSize / 2.0;
                Stone node = new Stone(cx, cy);
                node.setTileCoords(Collections.singletonList(new Point(col, row)));
                out.add(node);
            }
        }
        return out;
    }
}
