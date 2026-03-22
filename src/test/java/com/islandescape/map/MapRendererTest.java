package com.islandescape.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapRendererTest {

    private MapRenderer renderer;

    //set up the MapRenderer before each test
    @BeforeEach
    void setUp(){
        renderer = new MapRenderer(1,6,32);
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
    void testNinthTileSourceX(){
        assertEquals(64, renderer.getTileSourceX(9));
    }
    @Test
    void testNinthTileSourceY(){
        assertEquals(32, renderer.getTileSourceY(9));
    }
    @Test
    void testEleventhTileSourceX(){
        assertEquals(128, renderer.getTileSourceX(11));
    }
    @Test
    void testEleventhSourceY(){
        assertEquals(32, renderer.getTileSourceY(11));
    }
    @Test
    void testSecondTileSourceX(){
        assertEquals(32, renderer.getTileSourceX(2));
    }
    @Test
    void testSecondTileSourceY(){
        assertEquals(0, renderer.getTileSourceY(2));
    }

}
