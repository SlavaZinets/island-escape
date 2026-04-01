package com.islandescape.window;

import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePanelTest {

    private static final String TMX_PATH = "src/resources/maps/IslandMap.tmx";

    private GamePanel panel;

    @BeforeEach
    public void setUp() throws Exception {
        TileMap map = MapLoader.load(TMX_PATH);
        MapRenderer renderer = new MapRenderer();
        panel = new GamePanel(map, renderer);
    }

    // Panel should not be null after creation
    @Test
    public void testPanelIsNotNull() {
        assertNotNull(panel);
    }

    // Panel width should match map width * tileSize (64 * 32 = 2048)
    @Test
    public void testPanelPreferredWidth() {
        assertEquals(2048, panel.getPreferredSize().width);
    }

    // Panel height should match map height * tileSize (64 * 32 = 2048)
    @Test
    public void testPanelPreferredHeight() {
        assertEquals(2048, panel.getPreferredSize().height);
    }

    // Panel should store the TileMap
    @Test
    public void testPanelHasMap() {
        assertNotNull(panel.getMap());
    }

    // Panel should store the MapRenderer
    @Test
    public void testPanelHasRenderer() {
        assertNotNull(panel.getRenderer());
    }
}
