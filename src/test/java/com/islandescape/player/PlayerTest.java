package com.islandescape.player;

import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    private static Player player;

    @BeforeAll
    static void setUp() {
        player = new Player("Alice", 1, 10, 20);
    }



    // tests for constructor

    @Test
    void constructorSetsName() {
        assertEquals("Alice", player.getName());
    }

    @Test
    void constructorSetsId() {
        assertEquals(1, player.getId());
    }

    @Test
    void constructorSetsXPosition() {
        assertEquals(10.0, player.getX());
    }

    @Test
    void constructorSetsYPosition() {
        assertEquals(20.0, player.getY());
    }



    // tests for getters

    @Test
    void getNameReturnsName() {
        assertEquals("Alice", player.getName());
    }

    @Test
    void getIdReturnsId() {
        assertEquals(1, player.getId());
    }

    @Test
    void getXReturnsXPosition() {
        player = new Player("Alice", 1, 10, 20);
        assertEquals(10.0, player.getX());
    }

    @Test
    void getYReturnsYPosition() {
        player = new Player("Alice", 1, 10, 20);
        assertEquals(20.0, player.getY());
    }



    // tests for move

    private final double SPEED = 2.0;

    @Test
    void moveRight() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(1, 0));
        assertEquals(prevX + SPEED, player.getX());
        assertEquals(prevY, player.getY());
    }

    @Test
    void moveLeft() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(-1, 0));
        assertEquals(prevX - SPEED, player.getX());
        assertEquals(prevY, player.getY());
    }

    @Test
    void moveUp() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(0, 1));
        assertEquals(prevX, player.getX());
        assertEquals(prevY + SPEED, player.getY());
    }

    @Test
    void moveDown() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(0, -1));
        assertEquals(prevX, player.getX());
        assertEquals(prevY - SPEED, player.getY());
    }

    @Test
    void moveRightAndUp() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(1, 1));
        assertEquals(prevX + SPEED, player.getX());
        assertEquals(prevY + SPEED, player.getY());
    }

    @Test
    void moveLeftAndDown() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(-1, -1));
        assertEquals(prevX - SPEED, player.getX());
        assertEquals(prevY - SPEED, player.getY());
    }

    @Test
    void moveWithZeroDirection() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(0, 0));
        assertEquals(prevX, player.getX());
        assertEquals(prevY, player.getY());
    }

    @Test
    void moveWithNotUnitDirection() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(7, 8));
        assertEquals(prevX + SPEED, player.getX());
        assertEquals(prevY + SPEED, player.getY());
    }
}
