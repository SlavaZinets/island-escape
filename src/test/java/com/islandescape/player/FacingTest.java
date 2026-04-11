package com.islandescape.player;

import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FacingTest {

    @Test
    void testFromDirectionSouth() {
        assertEquals(Facing.SOUTH, Facing.fromDirection(new Direction(0, -1)));
    }

    @Test
    void testFromDirectionNorth() {
        assertEquals(Facing.NORTH, Facing.fromDirection(new Direction(0, 1)));
    }

    @Test
    void testFromDirectionEast() {
        assertEquals(Facing.EAST, Facing.fromDirection(new Direction(1, 0)));
    }

    @Test
    void testFromDirectionWest() {
        assertEquals(Facing.WEST, Facing.fromDirection(new Direction(-1, 0)));
    }

    @Test
    void testFromZeroDirectionReturnsNull() {
        assertNull(Facing.fromDirection(new Direction(0, 0)));
    }

    @Test
    void testDiagonalPrefersHorizontal() {
        assertEquals(Facing.EAST, Facing.fromDirection(new Direction(1, 1)));
        assertEquals(Facing.EAST, Facing.fromDirection(new Direction(1, -1)));
        assertEquals(Facing.WEST, Facing.fromDirection(new Direction(-1, 1)));
        assertEquals(Facing.WEST, Facing.fromDirection(new Direction(-1, -1)));
    }


    @Test
    void testSpriteRowMapping() {
        assertEquals(0, Facing.SOUTH.getSpriteRow());
        assertEquals(1, Facing.EAST.getSpriteRow());
        assertEquals(2, Facing.NORTH.getSpriteRow());
        assertEquals(1, Facing.WEST.getSpriteRow());
    }

    @Test
    void testWestIsFlipped() {
        assertTrue(Facing.WEST.isFlipped());
        assertFalse(Facing.EAST.isFlipped());
        assertFalse(Facing.SOUTH.isFlipped());
        assertFalse(Facing.NORTH.isFlipped());
    }
}
