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
        assertEquals(64, map.getWidth());
    }

    @Test
    public void testMapHeight() {
        assertEquals(64, map.getHeight());
    }

    @Test
    public void testTileSize() {
        assertEquals(32, map.getTileSize());
    }

    // Test that the correct layers are present in the map
    @Test
    public void testDeepWaterLayerExists() {
        assertNotNull(map.getLayer("DeepWater"));
    }

    @Test
    public void testWaterLayerExists() {
        assertNotNull(map.getLayer("Water"));
    }

    @Test
    public void testSandLayerExists() {
        assertNotNull(map.getLayer("Sand"));
    }

    @Test
    public void testJungleLayerExists() {
        assertNotNull(map.getLayer("Jungle"));
    }

    @Test
    public void testLayerDataIsNotNull() {
        TileLayer deepWater = map.getLayer("DeepWater");
        assertNotNull(deepWater);
        // tile at (0,0) in DeepWater should be a valid tile id (>= 0)
        assertTrue(deepWater.getTileAt(0, 0) >= 0);
    }
}
