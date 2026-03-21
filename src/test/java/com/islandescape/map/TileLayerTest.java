package com.islandescape.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TileLayerTest {
    @Test
    public void testAddedLayerCanBeRetrievedByName(){
        TileMap map = new TileMap(64, 64, 32);
        TileLayer layer = new TileLayer("DeepWater", new int[64][64]);
        map.addLayer(layer);
        assertNotNull(map.getLayer("DeepWater"));
    }

    @Test
    public void testGetLayerReturnsNullForUnknownName(){
        TileMap map = new TileMap(64, 64, 32);
        assertNull(map.getLayer("NonExistent"));
    }

    @Test
    public void testGetLayerReturnsCorrectLayerName(){
        TileMap map = new TileMap(64, 64, 32);
        TileLayer layer = new TileLayer("Water", new int[64][64]);
        map.addLayer(layer);
        assertEquals("Water", map.getLayer("Water").getName());
    }
}
