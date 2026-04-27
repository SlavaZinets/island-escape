package com.islandescape.input;

import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.window.GamePanel;
import com.islandescape.window.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameKeyHandlerMenuTest {

    private static final String TMX_PATH = "src/resources/maps/IslandMap.tmx";

    private GameKeyHandler handler;
    private Component source;
    private GameState[] stateHolder;

    @BeforeEach
    public void setUp() throws Exception {
        TileMap map = MapLoader.load(TMX_PATH);
        MapRenderer renderer = new MapRenderer();
        stateHolder = new GameState[] { GameState.MAIN_MENU };
        GamePanel fake = new GamePanel(map, renderer, null, null) {
            @Override
            public GameState getGameState() {
                return stateHolder[0];
            }
        };
        handler = new GameKeyHandler(fake);
        source = new JPanel();
    }

    private KeyEvent press(int keyCode) {
        return new KeyEvent(source, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    // --- in MAIN_MENU, the navigation keys flip their edge fields ---

    @Test
    public void downKeySetsMenuDownInMainMenu() {
        handler.keyPressed(press(KeyEvent.VK_DOWN));
        assertTrue(handler.consumeMenuDown());
    }

    @Test
    public void upKeySetsMenuUpInMainMenu() {
        handler.keyPressed(press(KeyEvent.VK_UP));
        assertTrue(handler.consumeMenuUp());
    }

    @Test
    public void enterKeySetsMenuConfirmInMainMenu() {
        handler.keyPressed(press(KeyEvent.VK_ENTER));
        assertTrue(handler.consumeMenuConfirm());
    }

    // --- consume edges are one-shot ---

    @Test
    public void menuDownConsumeIsOneShot() {
        handler.keyPressed(press(KeyEvent.VK_DOWN));
        assertTrue(handler.consumeMenuDown());
        assertFalse(handler.consumeMenuDown());
    }

    @Test
    public void menuUpConsumeIsOneShot() {
        handler.keyPressed(press(KeyEvent.VK_UP));
        assertTrue(handler.consumeMenuUp());
        assertFalse(handler.consumeMenuUp());
    }

    @Test
    public void menuConfirmConsumeIsOneShot() {
        handler.keyPressed(press(KeyEvent.VK_ENTER));
        assertTrue(handler.consumeMenuConfirm());
        assertFalse(handler.consumeMenuConfirm());
    }

    @Test
    public void menuConsumesStartFalse() {
        assertFalse(handler.consumeMenuUp());
        assertFalse(handler.consumeMenuDown());
        assertFalse(handler.consumeMenuConfirm());
    }

    // --- in PLAYING, the same keys do NOT set menu edges ---

    @Test
    public void downKeyDoesNotSetMenuDownInPlaying() {
        stateHolder[0] = GameState.PLAYING;
        handler.keyPressed(press(KeyEvent.VK_DOWN));
        assertFalse(handler.consumeMenuDown());
    }

    @Test
    public void upKeyDoesNotSetMenuUpInPlaying() {
        stateHolder[0] = GameState.PLAYING;
        handler.keyPressed(press(KeyEvent.VK_UP));
        assertFalse(handler.consumeMenuUp());
    }

    @Test
    public void enterKeyDoesNotSetMenuConfirmInPlaying() {
        stateHolder[0] = GameState.PLAYING;
        handler.keyPressed(press(KeyEvent.VK_ENTER));
        assertFalse(handler.consumeMenuConfirm());
    }

    // --- in MAIN_MENU, navigation keys should NOT also drive movement ---
    // (the plan says: "block other branches in that state")

    @Test
    public void arrowKeyDoesNotMoveP2InMainMenu() {
        handler.keyPressed(press(KeyEvent.VK_DOWN));
        assertEquals(0, handler.getP2Direction().getX());
        assertEquals(0, handler.getP2Direction().getY());
    }

    @Test
    public void wKeyDoesNotMoveP1InMainMenu() {
        handler.keyPressed(press(KeyEvent.VK_W));
        assertEquals(0, handler.getP1Direction().getX());
        assertEquals(0, handler.getP1Direction().getY());
    }
}
