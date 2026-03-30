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
		}
	}
}
