package com.islandescape.input;

import com.islandescape.core.GamePanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyHandler extends KeyAdapter {
	// Target panel
	private final GamePanel gamePanel;
	public GameKeyHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyPressed(KeyEvent event) {
		// Toggle by I
		if (event.getKeyCode() == KeyEvent.VK_I) {
			gamePanel.toggleCraftingScreen();
		}
	}
}
