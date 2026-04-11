package com.islandescape.input;

import com.islandescape.window.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyHandler implements KeyListener {
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

		// if inventory is open, only ESC closes it
		if (gamePanel.isInventoryScreenOpen()) {
			if (key == KeyEvent.VK_ESCAPE) {
				gamePanel.toggleInventoryScreen();
			}
			return;
		}

		// hotbar slot selection when inventory is closed
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

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}
}
