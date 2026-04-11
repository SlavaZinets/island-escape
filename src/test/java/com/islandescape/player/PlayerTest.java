package com.islandescape.player;

import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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



    // tests for facing tracking

    @Test
    void defaultFacingIsSouth() {
        Player p = new Player("Bob", 2, 0, 0);
        assertEquals(Facing.SOUTH, p.getFacing());
    }

    @Test
    void moveEastSetsFacingEast() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(1, 0));
        assertEquals(Facing.EAST, p.getFacing());
    }

    @Test
    void moveWestSetsFacingWest() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(-1, 0));
        assertEquals(Facing.WEST, p.getFacing());
    }

    @Test
    void moveNorthSetsFacingNorth() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(0, 1));
        assertEquals(Facing.NORTH, p.getFacing());
    }

    @Test
    void moveSouthSetsFacingSouth() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(0, -1));
        assertEquals(Facing.SOUTH, p.getFacing());
    }

    @Test
    void zeroMoveKeepsPreviousFacing() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(1, 0));          // facing becomes EAST
        p.move(new Direction(0, 0));          // no input, so should keep EAST
        assertEquals(Facing.EAST, p.getFacing());
    }

    @Test
    void diagonalMoveUpdatesFacingViaHorizontalRule() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(1, 1));
        assertEquals(Facing.EAST, p.getFacing());

        p.move(new Direction(-1, -1));
        assertEquals(Facing.WEST, p.getFacing());
    }



    // tests for animation tick

    @Test
    void nonZeroMoveIncrementsAnimationTick() {
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(1, 0));
        assertEquals(1, p.getAnimationTick());

        for (int i = 0; i < 4; i++) {
            p.move(new Direction(1, 0));
        }
        assertEquals(5, p.getAnimationTick());
    }

    @Test
    void zeroMoveResetsAnimationTick() {
        // Standing still must snap the walk cycle back to the first frame,
        // so an idle player is drawn in a neutral pose rather than holding
        // a mid-stride frame from whenever they last stopped.
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(1, 0));
        p.move(new Direction(1, 0));
        assertEquals(2, p.getAnimationTick());

        p.move(new Direction(0, 0));
        assertEquals(0, p.getAnimationTick());
        assertEquals(0, p.getFrameIndex());
    }

    @Test
    void animationTickIncrementsAcrossDirections() {
        // Changing direction should not reset the tick — the walk cycle keeps flowing
        // even as facing flips. Otherwise pressing A then D would stutter the animation.
        Player p = new Player("Bob", 2, 0, 0);
        p.move(new Direction(1, 0));
        p.move(new Direction(-1, 0));
        p.move(new Direction(0, 1));
        assertEquals(3, p.getAnimationTick());
    }

    // tests for frame index and rendering

    private static final int FRAMES_PER_STEP = 8;
    private static final int FRAME_COUNT = 6;

    private static Player movedBy(int ticks) {
        Player p = new Player("Bob", 2, 0, 0);
        for (int i = 0; i < ticks; i++) {
            p.move(new Direction(1, 0));
        }
        return p;
    }

    @Test
    void frameIndexAdvancesAsTickGrows() {
        assertEquals(1, movedBy(FRAMES_PER_STEP).getFrameIndex());
        assertEquals(2, movedBy(FRAMES_PER_STEP * 2).getFrameIndex());
        assertEquals(5, movedBy(FRAMES_PER_STEP * 5).getFrameIndex());
    }

    @Test
    void frameIndexHoldsWithinAStep() {
        assertEquals(0, movedBy(FRAMES_PER_STEP - 1).getFrameIndex());
        assertEquals(1, movedBy(FRAMES_PER_STEP + 1).getFrameIndex());
    }

    @Test
    void frameIndexWrapsAfterFullCycle() {
        assertEquals(0, movedBy(FRAMES_PER_STEP * FRAME_COUNT).getFrameIndex());
    }
}
