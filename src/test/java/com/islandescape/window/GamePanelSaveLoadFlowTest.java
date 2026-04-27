package com.islandescape.window;

import com.islandescape.boat.BoatWreck;
import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;
import com.islandescape.save.SaveData;
import com.islandescape.save.SaveManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GamePanelSaveLoadFlowTest {

    private static final String TMX_PATH = "src/resources/maps/IslandMap.tmx";
    private final File saveFile = new File(SaveManager.path);

    private TileMap map;

    @BeforeEach
    public void setUp() throws Exception {
        saveFile.delete();
        map = MapLoader.load(TMX_PATH);
    }

    @AfterEach
    public void tearDown() {
        saveFile.delete();
    }

    private GamePanel buildPanel(Player p1, Player p2) {
        MapRenderer renderer = new MapRenderer();
        GamePanel panel = new GamePanel(map, renderer, p1, p2);
        panel.setQuitAction(() -> {});
        panel.setBoatWreck(new BoatWreck(130, 130, "Boat"));
        return panel;
    }

    private GamePanel buildPanel() {
        Player p1 = new Player("P1", 1, 320, 192);
        Player p2 = new Player("P2", 2, 352, 192);
        return buildPanel(p1, p2);
    }

    @Test
    public void f5InPlayingWritesSaveFile() {
        GamePanel panel = buildPanel();
        panel.startNewGame();
        assertFalse(saveFile.exists());

        panel.onSaveKey();

        assertTrue(saveFile.exists());
        assertTrue(SaveManager.hasSave());
    }

    @Test
    public void onSaveKeyCapturesPlayerPositionsForLaterLoad() {
        Player p1 = new Player("P1", 1, 320, 192);
        Player p2 = new Player("P2", 2, 352, 192);
        GamePanel panel = buildPanel(p1, p2);
        panel.startNewGame();

        // Mutate positions, then save.
        p1.setPosition(150, 220);
        p2.setPosition(180, 240);
        panel.onSaveKey();

        // Mutate again, then load: positions should snap back to the saved values.
        p1.setPosition(0, 0);
        p2.setPosition(0, 0);
        panel.loadGame();

        assertEquals(150.0, p1.getX(), 0.001);
        assertEquals(220.0, p1.getY(), 0.001);
        assertEquals(180.0, p2.getX(), 0.001);
        assertEquals(240.0, p2.getY(), 0.001);
    }

    @Test
    public void loadGameEndsInPlayingStateWithPanelsClosed() {
        SaveManager.save(new SaveData());
        GamePanel panel = buildPanel();

        panel.loadGame();

        assertEquals(GameState.PLAYING, panel.getGameState());
        assertFalse(panel.isInventoryScreenOpen());
        assertFalse(panel.isBoatRepairOpen());
    }

    @Test
    public void onSaveKeyRefreshesLoadMenuAvailability() {
        GamePanel panel = buildPanel();
        assertFalse(panel.getMainMenu().isLoadEnabled());

        panel.startNewGame();
        panel.onSaveKey();

        assertTrue(panel.getMainMenu().isLoadEnabled());
    }

    @Test
    public void quitInPlayingWritesSaveFile() {
        GamePanel panel = buildPanel();
        panel.startNewGame();
        assertFalse(saveFile.exists());

        panel.quitGame();

        assertTrue(saveFile.exists());
        assertTrue(SaveManager.hasSave());
    }

    @Test
    public void quitFromMainMenuDoesNotWriteSaveFile() {
        GamePanel panel = buildPanel();
        // gameState defaults to MAIN_MENU; no session in progress yet.

        panel.quitGame();

        assertFalse(saveFile.exists());
    }

    @Test
    public void quitCapturesPlayerPositionsForLaterLoad() {
        Player p1 = new Player("P1", 1, 320, 192);
        Player p2 = new Player("P2", 2, 352, 192);
        GamePanel panel = buildPanel(p1, p2);
        panel.startNewGame();

        p1.setPosition(150, 220);
        p2.setPosition(180, 240);
        panel.quitGame();

        // Mutate again, then load: positions should snap back to the values
        // captured when the user quit.
        p1.setPosition(0, 0);
        p2.setPosition(0, 0);
        panel.loadGame();

        assertEquals(150.0, p1.getX(), 0.001);
        assertEquals(220.0, p1.getY(), 0.001);
        assertEquals(180.0, p2.getX(), 0.001);
        assertEquals(240.0, p2.getY(), 0.001);
    }

    @Test
    public void quitInInventoryOpenStateWritesSaveFile() {
        GamePanel panel = buildPanel();
        panel.startNewGame();
        // openBoatRepairScreen flips gameState to INVENTORY_OPEN.
        panel.openBoatRepairScreen();
        assertEquals(GameState.INVENTORY_OPEN, panel.getGameState());

        panel.quitGame();

        assertTrue(saveFile.exists());
    }

    @Test
    public void quitRunsQuitAction() {
        GamePanel panel = buildPanel();
        boolean[] fired = { false };
        panel.setQuitAction(() -> fired[0] = true);
        panel.startNewGame();

        panel.quitGame();

        assertTrue(fired[0]);
        assertTrue(panel.isQuitRequested());
    }

    @Test
    public void showMainMenuFromPlayingTransitionsState() {
        GamePanel panel = buildPanel();
        panel.startNewGame();

        panel.showMainMenu();

        assertEquals(GameState.MAIN_MENU, panel.getGameState());
    }

    @Test
    public void showMainMenuFromPlayingWritesSaveFile() {
        GamePanel panel = buildPanel();
        panel.startNewGame();
        assertFalse(saveFile.exists());

        panel.showMainMenu();

        assertTrue(saveFile.exists());
        assertTrue(SaveManager.hasSave());
    }

    @Test
    public void showMainMenuCapturesPlayerPositionsForLaterLoad() {
        Player p1 = new Player("P1", 1, 320, 192);
        Player p2 = new Player("P2", 2, 352, 192);
        GamePanel panel = buildPanel(p1, p2);
        panel.startNewGame();

        // Mutate positions, then bounce out to the main menu.
        p1.setPosition(150, 220);
        p2.setPosition(180, 240);
        panel.showMainMenu();

        // Mutate again; loading should snap back to the saved positions.
        p1.setPosition(0, 0);
        p2.setPosition(0, 0);
        panel.loadGame();

        assertEquals(150.0, p1.getX(), 0.001);
        assertEquals(220.0, p1.getY(), 0.001);
        assertEquals(180.0, p2.getX(), 0.001);
        assertEquals(240.0, p2.getY(), 0.001);
    }

    @Test
    public void showMainMenuFromManualDoesNotWriteSaveFile() {
        GamePanel panel = buildPanel();
        // Manual is reachable from the main menu before any session starts;
        // bouncing back to the menu must not overwrite an existing save.
        panel.showManual();
        assertEquals(GameState.MANUAL, panel.getGameState());

        panel.showMainMenu();

        assertFalse(saveFile.exists());
    }
}
