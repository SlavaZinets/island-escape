package com.islandescape.input;

import com.islandescape.core.GamePanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyHandler extends KeyAdapter {
	// target panel
	private final GamePanel gamePanel;
	public GameKeyHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyPressed(KeyEvent event) {
		// toggle by i
		if (event.getKeyCode() == KeyEvent.VK_I) {
			gamePanel.toggleCraftingScreen();
			return;
		}

		if (!gamePanel.isCraftingScreenOpen()) {
			return;
		}

		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT -> gamePanel.moveSelection(0, -1);
			case KeyEvent.VK_RIGHT -> gamePanel.moveSelection(0, 1);
			case KeyEvent.VK_UP -> gamePanel.moveSelection(-1, 0);
			case KeyEvent.VK_DOWN -> gamePanel.moveSelection(1, 0);
			case KeyEvent.VK_1 -> gamePanel.setItemInSelectedSlot("WOOD");
			case KeyEvent.VK_2 -> gamePanel.setItemInSelectedSlot("STONE");
			case KeyEvent.VK_3 -> gamePanel.setItemInSelectedSlot("CLEAR");
			case KeyEvent.VK_ENTER -> gamePanel.craftCurrentRecipe();
			default -> {
			}
		}
	}
}
