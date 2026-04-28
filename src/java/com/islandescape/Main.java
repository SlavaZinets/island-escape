package com.islandescape;

import com.islandescape.boat.BoatWreck;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.item.ConsumableItem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
import com.islandescape.player.PlayerSprite;
import com.islandescape.structures.CraftingTable;
import com.islandescape.ui.BoatRepairScreen;
import com.islandescape.ui.CraftingScreen;
import com.islandescape.window.GamePanel;
import com.islandescape.window.GameWindow;
import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) throws Exception {
		TileMap map = MapLoader.load("src/resources/maps/IslandMap.tmx");

		// 2. Create the renderer
		MapRenderer renderer = new MapRenderer();

		// 3. Create the players
		int worldW = map.getWidth() * map.getTileSize();
		int worldH = map.getHeight() * map.getTileSize();

		Player player1 = new Player("Alpha", 1, 48, 128, Color.BLUE);
		player1.setWorldBounds(worldW, worldH);
		player1.setTileMap(map);
		player1.setSprite(new PlayerSprite("src/resources/player/player_walking.png"));

		Player player2 = new Player("Romeo", 2, 80, 176, Color.RED);
		player2.setWorldBounds(worldW, worldH);
		player2.setTileMap(map);
		player2.setSprite(new PlayerSprite("src/resources/player/player_walking.png"));

		player1.getInventory().addItem(new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5));
		player1.getInventory().addItem(new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 3));
		player1.getInventory().addItem(new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Chops trees for wood", 1));
		player2.getInventory().addItem(new Item(ItemType.PICKAXE, ItemCategory.TOOL, "Pickaxe", "Mines stone", 1));

		player2.getInventory().addItem(ConsumableItem.coconut(1));
		player2.getInventory().addItem(ConsumableItem.banana(3));


		// 4. Create crafting system, crafting table, and crafting screen overlay
		CraftingSystem craftingSystem = new CraftingSystem();
		CraftingTable craftingTable = new CraftingTable(384, 160);
		CraftingScreen craftingScreen = new CraftingScreen();


		// Placed on a beach tile — hardcoded fallback so the win condition
		// never silently disables if map lookup fails.
		BoatWreck boatWreck = new BoatWreck(130, 130, "Boat");
		BoatRepairScreen boatRepairScreen = new BoatRepairScreen();

		// 5. Create the panel that draws the map
		GamePanel panel = new GamePanel(map, renderer, player1, player2);
		panel.setCraftingSystem(craftingSystem);
		panel.setCraftingTable(craftingTable);
		panel.setCraftingScreen(craftingScreen);
		panel.setBoatWreck(boatWreck);
		panel.setBoatRepairScreen(boatRepairScreen);

		// 6. Register key handler
		panel.setFocusable(true);
		panel.setKeyHandler(new GameKeyHandler(panel));

		// 7. Create the window fullscreen and add the panel.
		// Route the window X button through quitGame() so the active session
		// is saved before the JVM exits.
		GameWindow window = new GameWindow("Island Escape");
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				panel.quitGame();
			}
		});
		window.add(panel);
		window.setVisible(true);
		panel.requestFocusInWindow();

		// 8. Focus the panel so it receives key events, then start the game loop
		panel.requestFocusInWindow();
		panel.startGameLoop();
    }
}
