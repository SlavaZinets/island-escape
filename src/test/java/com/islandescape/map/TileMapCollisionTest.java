package com.islandescape.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileMapCollisionTest {

    private TileMap buildMap(int cols, int rows, int tileSize) {
        return new TileMap(cols, rows, tileSize);
    }

    private void addEmptyLayer(TileMap map, String name) {
        int[][] data = new int[map.getHeight()][map.getWidth()];
        map.addLayer(new TileLayer(name, data));
    }

    private void addLayerWithTile(TileMap map, String name, int row, int col, int tileId) {
        int[][] data = new int[map.getHeight()][map.getWidth()];
        data[row][col] = tileId;
        map.addLayer(new TileLayer(name, data));
    }

    @Test
    public void emptyMapNotBlocked() {
        TileMap map = buildMap(4, 4, 64);
        addEmptyLayer(map, "seaToSand");
        addEmptyLayer(map, "tree");
        addEmptyLayer(map, "stones");

        assertFalse(map.isBlocked(64, 64, 60, 100));
    }

    @Test
    public void collisionLayerBlocks() {
        TileMap map = buildMap(4, 4, 64);
        addLayerWithTile(map, "tree", 1, 1, 5);

        assertTrue(map.isBlocked(64, 64, 60, 100));
    }

    @Test
    public void walkableLayerDoesNotBlock() {
        TileMap map = buildMap(4, 4, 64);
        addLayerWithTile(map, "grass", 1, 1, 5);

        assertFalse(map.isBlocked(64, 64, 60, 100));
    }

    @Test
    public void outOfBoundsDoesNotCrash() {
        TileMap map = buildMap(4, 4, 64);
        addEmptyLayer(map, "tree");

        assertFalse(map.isBlocked(-100, -100, 60, 100));
    }

    @Test
    public void playerOverlappingMultipleTilesDetectsCollision() {
        TileMap map = buildMap(4, 4, 64);
        // player at (50, 50) with 60x100 overlaps cols 0-1, rows 0-2
        // place a stone at row=1, col=1
        addLayerWithTile(map, "stones", 1, 1, 3);

        assertTrue(map.isBlocked(50, 50, 60, 100));
    }

    @Test
    public void flippedTileStillBlocks() {
        TileMap map = buildMap(4, 4, 64);
        // 0x80000005 = horizontal flip flag + tile ID 5
        addLayerWithTile(map, "tree", 0, 0, 0x80000005);

        assertTrue(map.isBlocked(0, 0, 60, 100));
    }

    @Test
    public void multipleCollisionLayersChecked() {
        TileMap map = buildMap(4, 4, 64);
        addEmptyLayer(map, "tree");
        addLayerWithTile(map, "bonfire", 1, 1, 7);

        assertTrue(map.isBlocked(64, 64, 60, 100));
    }
}
