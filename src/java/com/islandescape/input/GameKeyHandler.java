package com.islandescape.input;

import com.islandescape.core.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyHandler implements KeyListener {
	// target panel
	private final GamePanel gamePanel;
	public GameKeyHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();

		// toggle inventory by E
		if (key == KeyEvent.VK_E) {
			gamePanel.toggleInventoryScreen();
			return;
		}

		// toggle crafting by I
		if (key == KeyEvent.VK_I) {
			gamePanel.toggleCraftingScreen();
			return;
		}

		// if inventory is open, block other keys
		if (gamePanel.isInventoryScreenOpen()) {
			if (key == KeyEvent.VK_ESCAPE) {
				gamePanel.toggleInventoryScreen();
			}
			return;
		}

		// hotbar slot selection when nothing is open
		if (!gamePanel.isCraftingScreenOpen() && !gamePanel.isInventoryScreenOpen()) {
			// Player 1: keys 1-5
			if (key >= KeyEvent.VK_1 && key <= KeyEvent.VK_5 && gamePanel.getPlayer1() != null) {
				gamePanel.getPlayer1().getInventory().setSelectedHotBarSlot(15 + (key - KeyEvent.VK_1));
				return;
			}
			// Player 2: keys 6-0 (6=15, 7=16, 8=17, 9=18, 0=19)
			if (gamePanel.getPlayer2() != null) {
				if (key >= KeyEvent.VK_6 && key <= KeyEvent.VK_9) {
					gamePanel.getPlayer2().getInventory().setSelectedHotBarSlot(15 + (key - KeyEvent.VK_6));
					return;
				}
				if (key == KeyEvent.VK_0) {
					gamePanel.getPlayer2().getInventory().setSelectedHotBarSlot(19);
					return;
				}
			}
		}

		if (!gamePanel.isCraftingScreenOpen()) {
			return;
		}

		switch (key) {
			case KeyEvent.VK_ESCAPE -> gamePanel.toggleCraftingScreen();
			case KeyEvent.VK_LEFT -> gamePanel.moveSelection(0, -1);
			case KeyEvent.VK_RIGHT -> gamePanel.moveSelection(0, 1);
			case KeyEvent.VK_UP -> gamePanel.moveSelection(-1, 0);
			case KeyEvent.VK_DOWN -> gamePanel.moveSelection(1, 0);
			case KeyEvent.VK_C -> gamePanel.clearCraftingGrid();
			case KeyEvent.VK_ENTER -> gamePanel.craftCurrentRecipe();
			default -> {
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}
}
