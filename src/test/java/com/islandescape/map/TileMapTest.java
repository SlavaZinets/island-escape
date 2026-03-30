package com.islandescape.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TileMapTest {
    // Test that the map dimensions and tile size are set correctly
    @Test
    public void testMapHeight(){
        TileMap map = new TileMap(10,10,10);
        assertEquals(10,map.getHeight());
    }

    @Test
    public void testMapWidth(){
        TileMap map = new TileMap(14,10,10);
        assertEquals(14,map.getWidth());
    }


    @Test
    public void testTileSize(){
        TileMap map = new TileMap(14,10,21);
        assertEquals(21,map.getTileSize());
    }


}
