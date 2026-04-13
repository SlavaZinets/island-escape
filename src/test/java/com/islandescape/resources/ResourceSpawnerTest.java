package com.islandescape.resources;

import com.islandescape.map.TileLayer;
import com.islandescape.map.TileMap;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceSpawnerTest {

    private static final int TILE_SIZE = 16;

    private TileMap buildMap(int cols, int rows) {
        return new TileMap(cols, rows, TILE_SIZE);
    }

    private void addLayer(TileMap map, String name, int[][] data) {
        map.addLayer(new TileLayer(name, data));
    }

    @Test
    public void noLayersProducesEmptyList() {
        TileMap map = buildMap(4, 4);
        List<ResourceNode> nodes = ResourceSpawner.spawnFromMap(map);
        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void singleTreeTileProducesOneTreeAtTileCenter() {
        TileMap map = buildMap(4, 4);
        int[][] trees = new int[4][4];
        trees[1][2] = 42; // row=1, col=2
        addLayer(map, "trees", trees);

        List<ResourceNode> nodes = ResourceSpawner.spawnFromMap(map);

        assertEquals(1, nodes.size());
        ResourceNode n = nodes.get(0);
        assertTrue(n instanceof Tree);
        // centroid of single tile = tile center = col*ts + ts/2, row*ts + ts/2
        assertEquals(2 * TILE_SIZE + TILE_SIZE / 2.0, n.getX(), 0.001);
        assertEquals(1 * TILE_SIZE + TILE_SIZE / 2.0, n.getY(), 0.001);
    }

    @Test
    public void twoAdjacentTreeTilesProduceTwoNodes() {
        TileMap map = buildMap(4, 4);
        int[][] trees = new int[4][4];
        trees[1][1] = 7;
        trees[1][2] = 7;
        addLayer(map, "trees", trees);

        List<ResourceNode> nodes = ResourceSpawner.spawnFromMap(map);

        assertEquals(2, nodes.size());
    }

    @Test
    public void treeAndStoneLayersAreSpawnedSeparately() {
        TileMap map = buildMap(4, 4);
        int[][] trees = new int[4][4];
        int[][] stones = new int[4][4];
        trees[0][0] = 1;
        stones[3][3] = 1;
        addLayer(map, "trees", trees);
        addLayer(map, "stones", stones);

        List<ResourceNode> nodes = ResourceSpawner.spawnFromMap(map);

        assertEquals(2, nodes.size());
        boolean hasTree = nodes.stream().anyMatch(n -> n instanceof Tree);
        boolean hasStone = nodes.stream().anyMatch(n -> n instanceof Stone);
        assertTrue(hasTree);
        assertTrue(hasStone);
    }

    @Test
    public void tileCoordIsRecordedOnNode() {
        TileMap map = buildMap(4, 4);
        int[][] trees = new int[4][4];
        trees[1][2] = 5;
        addLayer(map, "trees", trees);

        List<ResourceNode> nodes = ResourceSpawner.spawnFromMap(map);

        assertEquals(1, nodes.size());
        List<Point> coords = nodes.get(0).getTileCoords();
        assertNotNull(coords);
        assertEquals(1, coords.size());
        assertEquals(2, coords.get(0).x); // col
        assertEquals(1, coords.get(0).y); // row
    }

    @Test
    public void tiledFlipFlagsDoNotPreventDetection() {
        TileMap map = buildMap(4, 4);
        int[][] trees = new int[4][4];
        // horizontal-flip flag (0x80000000) + tile id 5
        trees[0][0] = 0x80000005;
        addLayer(map, "trees", trees);

        List<ResourceNode> nodes = ResourceSpawner.spawnFromMap(map);

        assertEquals(1, nodes.size());
    }
}
