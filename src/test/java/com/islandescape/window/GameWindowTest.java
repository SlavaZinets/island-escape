package com.islandescape.window;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameWindowTest {

    private GameWindow window;

    @BeforeEach
    public void setUp() {
        window = new GameWindow("Island Escape");
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

    // Test that the window is fullscreen (maximized)
    @Test
    public void testWindowIsMaximized() {
        assertEquals(javax.swing.JFrame.MAXIMIZED_BOTH, window.getExtendedState());
    }

    // Test that the window closes on exit
    @Test
    public void testDefaultCloseOperation() {
        assertEquals(javax.swing.WindowConstants.EXIT_ON_CLOSE, window.getDefaultCloseOperation());
    }
}
