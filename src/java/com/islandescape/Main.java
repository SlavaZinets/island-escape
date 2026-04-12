package com.islandescape;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.inventory.Inventory;
import com.islandescape.player.Player;
import com.islandescape.player.PlayerSprite;
import com.islandescape.structures.CraftingTable;
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

		// 3. Create the players
		int worldW = map.getWidth() * map.getTileSize();
		int worldH = map.getHeight() * map.getTileSize();

		Player player1 = new Player("Player1", 0, 0, 0);
		player1.setWorldBounds(worldW, worldH);
		player1.setSprite(new PlayerSprite("src/resources/player/player_walking.png"));

		Player player2 = new Player("Player2", 1, 200, 0);
		player2.setWorldBounds(worldW, worldH);
		player2.setSprite(new PlayerSprite("src/resources/player/player_walking.png"));

		// 4. Create inventories, crafting system, and crafting table
		Inventory p1Inv = new Inventory();
		Inventory p2Inv = new Inventory();
		CraftingSystem craftingSystem = new CraftingSystem();
		CraftingTable craftingTable = new CraftingTable(0, 0);

		// 5. Create the panel that draws the map
		GamePanel panel = new GamePanel(map, renderer);
		panel.setPlayer(player1);
		panel.setPlayer2(player2);
		panel.setInventories(p1Inv, p2Inv);
		panel.setCraftingSystem(craftingSystem);
		panel.setCraftingTable(craftingTable);

		// 6. Create the key handler and attach it to the panel
		GameKeyHandler keyHandler = new GameKeyHandler();
		panel.setKeyHandler(keyHandler);

		// 7. Create the window fullscreen and add the panel
		GameWindow window = new GameWindow("Island Escape");
		window.add(panel);
		window.setVisible(true);

		// 8. Focus the panel so it receives key events, then start the game loop
		panel.requestFocusInWindow();
		panel.startGameLoop();
    }
}
