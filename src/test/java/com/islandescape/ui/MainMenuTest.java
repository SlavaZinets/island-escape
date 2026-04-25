package com.islandescape.ui;

import com.islandescape.ui.MainMenu.MenuOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuTest {

    private MainMenu menu;

    @BeforeEach
    public void setUp() {
        menu = new MainMenu();
    }

    @Test
    public void initialSelectionIsNewGame() {
        assertEquals(MenuOption.NEW_GAME, menu.getSelected());
    }

    @Test
    public void moveDownAdvancesThroughOptions() {
        menu.moveDown();
        assertEquals(MenuOption.LOAD_GAME, menu.getSelected());
        menu.moveDown();
        assertEquals(MenuOption.QUIT, menu.getSelected());
    }

    @Test
    public void moveDownTransitionsFromQuitToNewGame() {
        menu.moveDown();
        menu.moveDown();
        menu.moveDown();
        assertEquals(MenuOption.NEW_GAME, menu.getSelected());
    }

    @Test
    public void moveUpFromNewGameTransitionsToQuit() {
        menu.moveUp();
        assertEquals(MenuOption.QUIT, menu.getSelected());
    }

    @Test
    public void moveUpAdvancesThroughOptions() {
        menu.moveUp();
        menu.moveUp();
        assertEquals(MenuOption.LOAD_GAME, menu.getSelected());
        menu.moveUp();
        assertEquals(MenuOption.NEW_GAME, menu.getSelected());
    }

    @Test
    public void moveDownSkipsLoadGameWhenDisabled() {
        menu.setLoadEnabled(false);
        menu.moveDown();
        assertEquals(MenuOption.QUIT, menu.getSelected());
    }

    @Test
    public void moveUpSkipsLoadGameWhenDisabled() {
        menu.setLoadEnabled(false);
        menu.moveDown();
        assertEquals(MenuOption.QUIT, menu.getSelected());
        menu.moveUp();
        assertEquals(MenuOption.NEW_GAME, menu.getSelected());
    }

    @Test
    public void confirmInvokesNewGameHook() {
        AtomicBoolean fired = new AtomicBoolean(false);
        menu.setOnNewGame(() -> fired.set(true));
        menu.confirm();
        assertTrue(fired.get());
    }

    @Test
    public void confirmInvokesLoadGameHookWhenEnabled() {
        AtomicBoolean fired = new AtomicBoolean(false);
        menu.setOnLoadGame(() -> fired.set(true));
        menu.moveDown();
        menu.confirm();
        assertTrue(fired.get());
    }

    @Test
    public void confirmInvokesQuitHook() {
        AtomicBoolean fired = new AtomicBoolean(false);
        menu.setOnQuit(() -> fired.set(true));
        menu.moveDown();
        menu.moveDown();
        menu.confirm();
        assertTrue(fired.get());
    }

    @Test
    public void confirmDoesNotFireOtherHooks() {
        AtomicBoolean loadFired = new AtomicBoolean(false);
        AtomicBoolean quitFired = new AtomicBoolean(false);
        menu.setOnLoadGame(() -> loadFired.set(true));
        menu.setOnQuit(() -> quitFired.set(true));
        menu.confirm();
        assertFalse(loadFired.get());
        assertFalse(quitFired.get());
    }

    @Test
    public void isLoadEnabledReflectsSetter() {
        menu.setLoadEnabled(true);
        assertTrue(menu.isLoadEnabled());
        menu.setLoadEnabled(false);
        assertFalse(menu.isLoadEnabled());
    }
}
