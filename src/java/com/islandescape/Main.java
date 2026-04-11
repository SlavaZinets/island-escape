package com.islandescape;

import com.islandescape.input.GameKeyHandler;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
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

		// 3. Create players
		Player player1 = new Player("Player 1", 1, 100, 100);
		Player player2 = new Player("Player 2", 2, 200, 100);

		// test items for Player 1
		player1.getInventory().addItem(new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5));
		player1.getInventory().addItem(new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 3));

		// 4. Create the panel that draws the map
		GamePanel panel = new GamePanel(map, renderer, player1, player2);

		// 5. Register key handler
		panel.setFocusable(true);
		panel.addKeyListener(new GameKeyHandler(panel));

		// 6. Create the window fullscreen and add the panel
		GameWindow window = new GameWindow("Island Escape");
		window.add(panel);
		window.setVisible(true);
		panel.requestFocusInWindow();
    }
}
