package com.islandescape.player;

import com.islandescape.map.TileLayer;
import com.islandescape.map.TileMap;
import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerCollisionTest {

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
    public void playerBlockedByCollisionTile() {
        // 4x4 map, 64px tiles. Tree at row=1, col=0 (pixels 0-63, 64-127)
        TileMap map = buildMap(4, 4, 64);
        addLayerWithTile(map, "tree", 1, 0, 5);

        // player starts at (0, 0), moves down toward tree at row 1
        Player player = new Player("P1", 1, 0, 0);
        player.setTileMap(map);
        player.setWorldBounds(256, 256);

        // move down many times to try to reach row 1 (y=64)
        Direction down = new Direction(0, -1);
        for (int i = 0; i < 50; i++) {
            player.move(down);
        }

        // player should be stopped before entering the tree tile
        assertTrue(player.getY() < 64, "Player should not enter tree tile at row 1");
    }

    @Test
    public void playerMovesFreelWithNoCollision() {
        TileMap map = buildMap(4, 4, 64);
        addEmptyLayer(map, "tree");

        Player player = new Player("P1", 1, 64, 64);
        player.setTileMap(map);
        player.setWorldBounds(256, 256);

        double startX = player.getX();
        player.move(new Direction(1, 0)); // move right

        assertTrue(player.getX() > startX, "Player should move right on empty map");
    }


    @Test
    public void noCollisionWhenTileMapNull() {
        Player player = new Player("P1", 1, 100, 100);
        player.setWorldBounds(256, 256);
        // no setTileMap call

        double startX = player.getX();
        player.move(new Direction(1, 0));

        assertTrue(player.getX() > startX, "Player should move freely without tileMap");
    }

    @Test
    public void playerBlockedBySeaToSand() {
        TileMap map = buildMap(4, 4, 64);
        addLayerWithTile(map, "seaToSand", 1, 0, 10);

        Player player = new Player("P1", 1, 0, 0);
        player.setTileMap(map);
        player.setWorldBounds(256, 256);

        Direction down = new Direction(0, -1);
        for (int i = 0; i < 50; i++) {
            player.move(down);
        }

        assertTrue(player.getY() < 64, "Player should not enter seaToSand tile");
    }

    @Test
    public void playerWalksOnGrass() {
        TileMap map = buildMap(4, 4, 64);
        // fill grass everywhere — not a collision layer
        int[][] grassData = new int[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                grassData[row][col] = 1;
            }
        }
        map.addLayer(new TileLayer("grass", grassData));

        Player player = new Player("P1", 1, 64, 64);
        player.setTileMap(map);
        player.setWorldBounds(256, 256);

        double startX = player.getX();
        player.move(new Direction(1, 0));

        assertTrue(player.getX() > startX, "Player should walk freely on grass");
    }
}
