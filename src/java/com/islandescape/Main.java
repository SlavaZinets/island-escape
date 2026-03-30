package com.islandescape;

import com.islandescape.core.GamePanel;
import com.islandescape.input.GameKeyHandler;
import javax.swing.SwingUtilities;
import com.islandescape.window.GameWindow;
import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				TileMap map = MapLoader.load("src/resources/maps/IslandMap.tmx");
				MapRenderer renderer = new MapRenderer();

				GamePanel panel = new GamePanel(map, renderer);

				GameWindow window = new GameWindow("Island Escape");
				window.add(panel);

				panel.setFocusable(true);
				panel.addKeyListener(new GameKeyHandler(panel));

				window.setVisible(true);
				panel.requestFocusInWindow();
			} catch (Exception exception) {
				throw new RuntimeException("Failed to start game", exception);
			}
		});
    }
}
