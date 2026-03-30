package com.islandescape.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTest {

    // Path to the tiled map file
    private static final String TMX_PATH =
            "src/resources/maps/IslandMap.tmx";

    // The map that is loaded
    private TileMap map;

    // Load the map before each test
    @BeforeEach
    public void setUp() throws Exception {
        map = MapLoader.load(TMX_PATH);
    }

    // Test that the map is loaded correctly
    @Test
    public void testMapIsNotNull() {
        assertNotNull(map);
    }

    // Test that the map dimensions and tile size are set correctly
    @Test
    public void testMapWidth() {
        assertEquals(12, map.getWidth());
    }

    @Test
    public void testMapHeight() {
        assertEquals(10, map.getHeight());
    }

    @Test
    public void testTileSize() {
        assertEquals(64, map.getTileSize());
    }

    // Test that the correct layers are present in the map
    @Test
    public void testSeaToSandLayerExists() {
        assertNotNull(map.getLayer("seaToSand"));
    }

    @Test
    public void testSandLayerExists() {
        assertNotNull(map.getLayer("sand"));
    }

    @Test
    public void testGrassLayerExists() {
        assertNotNull(map.getLayer("Grass"));
    }

    @Test
    public void testLayerDataIsNotNull() {
        TileLayer seaToSand = map.getLayer("seaToSand");
        assertNotNull(seaToSand);
        assertTrue(seaToSand.getTileAt(0, 0) >= 0);
    }

    // Test that tilesets are loaded
    @Test
    public void testTilesetsLoaded() {
        assertFalse(map.getTilesets().isEmpty());
    }

    @Test
    public void testTilesetHasImage() {
        Tileset tileset = map.getTilesets().get(0);
        assertNotNull(tileset.getImage());
    }

    @Test
    public void testTilesetForTile() {
        Tileset tileset = map.getTilesetForTile(1);
        assertNotNull(tileset);
        assertEquals(1, tileset.getFirstgid());
    }
}
