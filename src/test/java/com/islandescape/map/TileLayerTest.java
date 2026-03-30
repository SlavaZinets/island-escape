package com.islandescape.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TileLayerTest {

    // Test that a layer can be added to a map
    @Test
    public void testAddedLayerCanBeRetrievedByName(){
        TileMap map = new TileMap(64, 64, 32);
        TileLayer layer = new TileLayer("DeepWater", new int[64][64]);
        map.addLayer(layer);
        assertNotNull(map.getLayer("DeepWater"));
    }

    // Test that a null is returned if a layer is not found
    @Test
    public void testGetLayerReturnsNullForUnknownName(){
        TileMap map = new TileMap(64, 64, 32);
        assertNull(map.getLayer("NonExistent"));
    }

    // Test that the correct layer is returned
    @Test
    public void testGetLayerReturnsCorrectLayerName(){
        TileMap map = new TileMap(64, 64, 32);
        TileLayer layer = new TileLayer("Water", new int[64][64]);
        map.addLayer(layer);
        assertEquals("Water", map.getLayer("Water").getName());
    }
}
