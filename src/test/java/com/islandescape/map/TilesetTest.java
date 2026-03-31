package com.islandescape.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TilesetTest {

    private Tileset tileset;

    @BeforeEach
    void setUp() {
        // firstgid=1, 4 columns, 64x64 tiles, 36 tiles total
        tileset = new Tileset(1, "test", 64, 64, 4, 36);
    }

    @Test
    void testContainsTileInRange() {
        assertTrue(tileset.containsTile(1));
        assertTrue(tileset.containsTile(36));
    }

    @Test
    void testDoesNotContainTileOutOfRange() {
        assertFalse(tileset.containsTile(0));
        assertFalse(tileset.containsTile(37));
    }

    @Test
    void testFirstTileSourcePosition() {
        assertEquals(0, tileset.getTileSourceX(1));
        assertEquals(0, tileset.getTileSourceY(1));
    }

    @Test
    void testSecondTileSourcePosition() {
        assertEquals(64, tileset.getTileSourceX(2));
        assertEquals(0, tileset.getTileSourceY(2));
    }

    @Test
    void testFifthTileWrapsToNextRow() {
        assertEquals(0, tileset.getTileSourceX(5));
        assertEquals(64, tileset.getTileSourceY(5));
    }

    @Test
    void testMultipleTilesetsSecondTileset() {
        // Second tileset starts at gid 37
        Tileset second = new Tileset(37, "test2", 32, 32, 8, 20);
        assertTrue(second.containsTile(37));
        assertTrue(second.containsTile(56));
        assertFalse(second.containsTile(36));
        assertEquals(0, second.getTileSourceX(37));
        assertEquals(0, second.getTileSourceY(37));
    }
}
