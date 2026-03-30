package com.islandescape.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapRendererTest {

    private MapRenderer renderer;

    //set up the MapRenderer before each test
    @BeforeEach
    void setUp(){
        renderer = new MapRenderer();
    }

    @Test
    void testIsEmptyTile(){
        assertTrue(renderer.isEmpty(0));
    }

    @Test
    void testNotEmptyTile(){
        assertFalse(renderer.isEmpty(1));
    }

    @Test
    void testGetTileIdStripsFlags(){
        int rawId = 0x80000000 | 42;
        assertEquals(42, renderer.getTileId(rawId));
    }

    @Test
    void testGetTileIdNoFlags(){
        assertEquals(7, renderer.getTileId(7));
    }
}
