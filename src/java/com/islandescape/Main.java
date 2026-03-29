package com.islandescape;

import com.islandescape.core.GamePanel;
import com.islandescape.core.GameWindow;
import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;

import javax.swing.JScrollPane;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Load the map from TMX
        TileMap map = MapLoader.load("src/resources/maps/IslandMap.tmx");

        // 2. Create the renderer and load the tileset image
        MapRenderer renderer = new MapRenderer(1, 16, 32);
        renderer.loadTileset("src/resources/tilesets/island_tileset_1.png");

        // 3. Create the panel that draws the map
        GamePanel panel = new GamePanel(map, renderer);



        // 4. Create the window fullscreen and add the panel
        GameWindow window = new GameWindow("Island Escape");
        window.add(panel);
        window.setVisible(true);
    }
}
