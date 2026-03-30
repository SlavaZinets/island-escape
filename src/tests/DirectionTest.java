package tests;

import classes.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {



    // tests for constructor

    @Test
    void constructorSetsX() {
        Direction direction = new Direction(0, 0);
        assertEquals(0, direction.getX());
    }

    @Test
    void constructorSetsY() {
        Direction direction = new Direction(0, 0);
        assertEquals(0, direction.getY());
    }

    @Test
    void constructorNormalizesPositiveX() {
        Direction d = new Direction(7, 0);
        assertEquals(1, d.getX());
    }

    @Test
    void constructorNormalizesNegativeX() {
        Direction d = new Direction(-9, 0);
        assertEquals(-1, d.getX());
    }

    @Test
    void constructorNormalizesPositiveY() {
        Direction d = new Direction(0, 7);
        assertEquals(1, d.getY());
    }

    @Test
    void constructorNormalizesNegativeY() {
        Direction d = new Direction(0, -9);
        assertEquals(-1, d.getY());
    }

    @Test
    void constructorNormalizesZero() {
        Direction d = new Direction(0, 0);
        assertEquals(0, d.getX());
        assertEquals(0, d.getY());
    }



    // tests for setX

    @Test
    void setXChangesX() {
        Direction direction = new Direction(0, 0);
        direction.setX(7);
        assertEquals(1, direction.getX());
    }

    @Test
    void setXDoesNotChangeY() {
        Direction direction = new Direction(0, 0);
        direction.setX(7);
        assertEquals(0, direction.getY());
    }



    // tests for setY

    @Test
    void setYChangesY() {
        Direction direction = new Direction(0, 0);
        direction.setY(10);
        assertEquals(1, direction.getY());
    }

    @Test
    void setYDoesNotChangeX() {
        Direction direction = new Direction(0, 0);
        direction.setY(10);
        assertEquals(0, direction.getX());
    }



    // tests for setDirection
    @Test
    void setDirectionChangesXAndY() {
        Direction direction = new Direction(0, 0);
        direction.setDirection(4, 6);
        assertEquals(1, direction.getX());
        assertEquals(1, direction.getY());
    }
}
