package com.islandescape.player;

import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(prevY - SPEED, player.getY());
    }

    @Test
    void moveDown() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(0, -1));
        assertEquals(prevX, player.getX());
        assertEquals(prevY + SPEED, player.getY());
    }

    @Test
    void moveRightAndUp() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(1, 1));
        assertEquals(prevX + SPEED, player.getX());
        assertEquals(prevY - SPEED, player.getY());
    }

    @Test
    void moveLeftAndDown() {
        double prevX = player.getX();
        double prevY = player.getY();
        player.move(new Direction(-1, -1));
        assertEquals(prevX - SPEED, player.getX());
        assertEquals(prevY + SPEED, player.getY());
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
        assertEquals(prevY - SPEED, player.getY());
    }



    // tests for world-border clamping
    // Player size is 60x100 (see Player.WIDTH / Player.HEIGHT), so with a
    // 500x500 world the right edge is at x=440 and the bottom edge at y=400.
    private static final int WORLD_SIZE = 500;
    private static final int MAX_X = WORLD_SIZE - 60;  // worldWidth - Player.WIDTH
    private static final int MAX_Y = WORLD_SIZE - 100; // worldHeight - Player.HEIGHT

    @Test
    void clampsAtLeftEdge() {
        Player p = new Player("Bob", 2, 0, 200);
        p.setWorldBounds(WORLD_SIZE, WORLD_SIZE);
        p.move(new Direction(-1, 0));
        assertEquals(0.0, p.getX());
        assertEquals(200.0, p.getY());
    }

    @Test
    void clampsAtTopEdge() {
        // World-space up is +y; in screen coords that means y = 0 is the top.
        Player p = new Player("Bob", 2, 200, 0);
        p.setWorldBounds(WORLD_SIZE, WORLD_SIZE);
        p.move(new Direction(0, 1));
        assertEquals(200.0, p.getX());
        assertEquals(0.0, p.getY());
    }

    @Test
    void clampsAtRightEdge() {
        Player p = new Player("Bob", 2, MAX_X, 200);
        p.setWorldBounds(WORLD_SIZE, WORLD_SIZE);
        p.move(new Direction(1, 0));
        assertEquals((double) MAX_X, p.getX());
        assertEquals(200.0, p.getY());
    }

    @Test
    void clampsAtBottomEdge() {
        // World-space down is -y; in screen coords that pins y at worldHeight - HEIGHT.
        Player p = new Player("Bob", 2, 200, MAX_Y);
        p.setWorldBounds(WORLD_SIZE, WORLD_SIZE);
        p.move(new Direction(0, -1));
        assertEquals(200.0, p.getX());
        assertEquals((double) MAX_Y, p.getY());
    }

    @Test
    void slidesAlongTopWall() {
        // Diagonal up-right into the top wall: y is blocked, x still advances.
        Player p = new Player("Bob", 2, 200, 0);
        p.setWorldBounds(WORLD_SIZE, WORLD_SIZE);
        p.move(new Direction(1, 1));
        assertEquals(200.0 + SPEED, p.getX());
        assertEquals(0.0, p.getY());
    }

    @Test
    void slidesAlongRightWall() {
        // Diagonal down-right into the right wall: x is blocked, y still advances.
        // Direction(1, -1) is right + world-space down, which in screen coords is y+.
        Player p = new Player("Bob", 2, MAX_X, 200);
        p.setWorldBounds(WORLD_SIZE, WORLD_SIZE);
        p.move(new Direction(1, -1));
        assertEquals((double) MAX_X, p.getX());
        assertEquals(200.0 + SPEED, p.getY());
    }

    @Test
    void noClampWhenBoundsUnset() {
        // Without setWorldBounds, move behaves exactly as before — no clamping.
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(-1, 1)); // left + world-space up = x-, y-
        assertEquals(-SPEED, p.getX());
        assertEquals(-SPEED, p.getY());
    }
}
