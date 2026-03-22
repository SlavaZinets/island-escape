package com.islandescape.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameWindowTest {

    private GameWindow window;

    @BeforeEach
    public void setUp() {
        window = new GameWindow("Island Escape", 800, 600);
    }

    @AfterEach
    public void tearDown() {
        window.dispose();
    }

    // Test that the window title is set correctly
    @Test
    public void testWindowTitle() {
        assertEquals("Island Escape", window.getTitle());
    }

    // Test that the window width is set correctly
    @Test
    public void testWindowWidth() {
        assertEquals(800, window.getWindowWidth());
    }

    // Test that the window height is set correctly
    @Test
    public void testWindowHeight() {
        assertEquals(600, window.getWindowHeight());
    }

    // Test that the window is not resizable
    @Test
    public void testWindowIsNotResizable() {
        assertFalse(window.isResizable());
    }

    // Test that the window closes on exit
    @Test
    public void testDefaultCloseOperation() {
        assertEquals(javax.swing.WindowConstants.EXIT_ON_CLOSE, window.getDefaultCloseOperation());
    }
}
