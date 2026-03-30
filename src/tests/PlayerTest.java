package tests;

import classes.Player;
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
}
