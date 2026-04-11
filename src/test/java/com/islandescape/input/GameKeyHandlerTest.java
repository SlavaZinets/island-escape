package com.islandescape.input;

import com.islandescape.utilities.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameKeyHandlerTest {

    private GameKeyHandler handler;
    private Component source;

    @BeforeEach
    void setUp() {
        handler = new GameKeyHandler();
        source = new JPanel();
    }

    private KeyEvent press(int keyCode) {
        return new KeyEvent(source, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private KeyEvent release(int keyCode) {
        return new KeyEvent(source, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    // test situation when no keys are pressed
    @Test
    void testNoKeysPressed() {
        Direction direction = handler.getDirection();

        assertEquals(0, direction.getX());
        assertEquals(0, direction.getY());
    }



    // test situation when one key is pressed
    @Test
    void testWPressed() {
        handler.keyPressed(press(KeyEvent.VK_W));
        assertEquals(0, handler.getDirection().getX());
        assertEquals(1, handler.getDirection().getY());
    }

    @Test
    void testSPressed() {
        handler.keyPressed(press(KeyEvent.VK_S));
        assertEquals(0, handler.getDirection().getX());
        assertEquals(-1, handler.getDirection().getY());
    }

    @Test
    void testAPressed() {
        handler.keyPressed(press(KeyEvent.VK_A));
        assertEquals(-1, handler.getDirection().getX());
        assertEquals(0, handler.getDirection().getY());
    }

    @Test
    void testDPressed() {
        handler.keyPressed(press(KeyEvent.VK_D));
        assertEquals(1, handler.getDirection().getX());
        assertEquals(0, handler.getDirection().getY());
    }



    // test situations when two diagonal keys are pressed
    @Test
    void testWAndDPressed() {
        handler.keyPressed(press(KeyEvent.VK_W));
        handler.keyPressed(press(KeyEvent.VK_D));
        assertEquals(1, handler.getDirection().getX());
        assertEquals(1, handler.getDirection().getY());
    }

    @Test
    void testAAndSPressed() {
        handler.keyPressed(press(KeyEvent.VK_S));
        handler.keyPressed(press(KeyEvent.VK_A));
        assertEquals(-1, handler.getDirection().getX());
        assertEquals(-1, handler.getDirection().getY());
    }

    @Test
    void testWAndAPressed() {
        handler.keyPressed(press(KeyEvent.VK_W));
        handler.keyPressed(press(KeyEvent.VK_A));
        assertEquals(-1, handler.getDirection().getX());
        assertEquals(1, handler.getDirection().getY());
    }

    @Test
    void testDAndSPressed() {
        handler.keyPressed(press(KeyEvent.VK_D));
        handler.keyPressed(press(KeyEvent.VK_S));
        assertEquals(1, handler.getDirection().getX());
        assertEquals(-1, handler.getDirection().getY());
    }



    // test situations when opposite keys are pressed
    @Test
    void testWAndSPressed() {
        handler.keyPressed(press(KeyEvent.VK_W));
        handler.keyPressed(press(KeyEvent.VK_S));
        assertEquals(0, handler.getDirection().getY());
    }

    @Test
    void testDAndAPressed() {
        handler.keyPressed(press(KeyEvent.VK_A));
        handler.keyPressed(press(KeyEvent.VK_D));
        assertEquals(0, handler.getDirection().getX());
    }



    // test situations when keys are released
    @Test
    void testWReleased() {
        handler.keyPressed(press(KeyEvent.VK_W));
        handler.keyReleased(release(KeyEvent.VK_W));
        assertEquals(0, handler.getDirection().getY());
        assertEquals(0, handler.getDirection().getX());
    }

    @Test
    void testDStaysPressed() {
        handler.keyPressed(press(KeyEvent.VK_W));
        handler.keyPressed(press(KeyEvent.VK_D));
        handler.keyReleased(release(KeyEvent.VK_W));
        assertEquals(1, handler.getDirection().getX());
        assertEquals(0, handler.getDirection().getY());
    }

    @Test
    void testUnpressedButtonReleased() {
        handler.keyReleased(release(KeyEvent.VK_W));
        assertEquals(0, handler.getDirection().getX());
        assertEquals(0, handler.getDirection().getY());
    }



    // test situations when unsupported key is pressed

    @Test
    void testWrongButtonPressed() {
        handler.keyPressed(press(KeyEvent.VK_J));
        assertEquals(0, handler.getDirection().getX());
        assertEquals(0, handler.getDirection().getY());
    }



    // test that keyTyped events are ignored
    @Test
    void testKeyTypedIsIgnored() {
        KeyEvent typed = new KeyEvent(source, KeyEvent.KEY_TYPED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'w');
        handler.keyTyped(typed);
        assertEquals(0, handler.getDirection().getX());
        assertEquals(0, handler.getDirection().getY());
    }
}
