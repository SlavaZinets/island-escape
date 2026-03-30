package tests;

import classes.Direction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    private static Direction direction;

    @BeforeAll
    static void setUp() {
        direction = new Direction(3, -5);
    }



    // tests for constructor

    @Test
    void constructorSetsX() {
        assertEquals(3, direction.getX());
    }

    @Test
    void constructorSetsY() {
        assertEquals(-5, direction.getY());
    }

    @Test
    void constructorSetsDirection() {
        assertEquals(new Point(3, -5), direction.getDirection());
    }



    // tests for getters

    @Test
    void getXReturnsX() {
        assertEquals(3, direction.getX());
    }

    @Test
    void getYReturnsY() {
        assertEquals(-5, direction.getY());
    }

    @Test
    void getDirectionReturnsPoint() {
        assertEquals(new Point(3, -5), direction.getDirection());
    }



    // tests for setDirection

    @Test
    void setDirectionChangesX() {
        int prevX = direction.getX();
        direction.setDirection(7, direction.getY());
        assertEquals(7, direction.getX());
        direction.setDirection(prevX, direction.getY());
    }

    @Test
    void setDirectionChangesY() {
        int prevY = direction.getY();
        direction.setDirection(direction.getX(), 10);
        assertEquals(10, direction.getY());
        direction.setDirection(direction.getX(), prevY);
    }

    @Test
    void setDirectionChangesBoth() {
        int prevX = direction.getX();
        int prevY = direction.getY();
        direction.setDirection(4, 6);
        assertEquals(4, direction.getX());
        assertEquals(6, direction.getY());
        direction.setDirection(prevX, prevY);
    }



    // tests for normalize

    @Test
    void normalizePositiveValues() {
        Direction d = new Direction(5, 8);
        Direction normalized = d.normalize();
        assertEquals(1, normalized.getX());
        assertEquals(1, normalized.getY());
    }

    @Test
    void normalizeNegativeValues() {
        Direction d = new Direction(-3, -7);
        Direction normalized = d.normalize();
        assertEquals(-1, normalized.getX());
        assertEquals(-1, normalized.getY());
    }

    @Test
    void normalizeMixedValues() {
        Direction d = new Direction(4, -2);
        Direction normalized = d.normalize();
        assertEquals(1, normalized.getX());
        assertEquals(-1, normalized.getY());
    }

    @Test
    void normalizeZeroValues() {
        Direction d = new Direction(0, 0);
        Direction normalized = d.normalize();
        assertEquals(0, normalized.getX());
        assertEquals(0, normalized.getY());
    }

    @Test
    void normalizeAlreadyNormalized() {
        Direction d = new Direction(1, -1);
        Direction normalized = d.normalize();
        assertEquals(1, normalized.getX());
        assertEquals(-1, normalized.getY());
    }

    @Test
    void normalizeWithZeroX() {
        Direction d = new Direction(0, 9);
        Direction normalized = d.normalize();
        assertEquals(0, normalized.getX());
        assertEquals(1, normalized.getY());
    }

    @Test
    void normalizeWithZeroY() {
        Direction d = new Direction(-6, 0);
        Direction normalized = d.normalize();
        assertEquals(-1, normalized.getX());
        assertEquals(0, normalized.getY());
    }
}
