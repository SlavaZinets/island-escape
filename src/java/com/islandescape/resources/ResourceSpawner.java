package com.islandescape.resources;

import com.islandescape.map.TileLayer;
import com.islandescape.map.TileMap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

// Walks tile layers and emits one ResourceNode per non-zero tile.
public final class ResourceSpawner {

    private static final int MASK_TILE_ID = 0x1FFFFFFF;

    private ResourceSpawner() {}

    public static ArrayList<ResourceNode> spawnFromMap(TileMap map) {
       return null;
    }

    private static ArrayList<ResourceNode> spawnTrees(TileMap map) {
        return null;
    }

    private static ArrayList<ResourceNode> spawnStones(TileMap map) {
        return null;
    }
}
