package com.islandescape;

import com.islandescape.input.GameKeyHandler;
import com.islandescape.player.Player;
import com.islandescape.player.PlayerSprite;
import com.islandescape.window.GamePanel;
import com.islandescape.window.GameWindow;
import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;

public class Main {
	public static void main(String[] args) throws Exception {
		TileMap map = MapLoader.load("src/resources/maps/IslandMap.tmx");

		// 2. Create the renderer
		MapRenderer renderer = new MapRenderer();

		// 3. Create the player
		Player player = new Player("Player1", 1, 0, 0);
		player.setWorldBounds(map.getWidth() * map.getTileSize(), map.getHeight() * map.getTileSize());
		player.setSprite(new PlayerSprite("src/resources/player/player_walking.png"));

		// 4. Create the panel that draws the map
		GamePanel panel = new GamePanel(map, renderer);
		panel.setPlayer(player);

		// 5. Create the key handler and attach it to the panel
		GameKeyHandler keyHandler = new GameKeyHandler();
		panel.setKeyHandler(keyHandler);

		// 6. Create the window fullscreen and add the panel
		GameWindow window = new GameWindow("Island Escape");
		window.add(panel);
		window.setVisible(true);

		// 7. Focus the panel so it receives key events, then start the game loop
		panel.requestFocusInWindow();
		panel.startGameLoop();
    }
}
