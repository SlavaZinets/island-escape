package com.islandescape.core;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class GamePanel extends JPanel {
	// Craft state
	private final JLabel stateLabel;
	private boolean craftingScreenOpen;

	public GamePanel() {
		// Panel base
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(960, 640));

		// Status label
		stateLabel = new JLabel();
		stateLabel.setForeground(Color.WHITE);
		add(stateLabel, BorderLayout.NORTH);

		// Sync text
		updateStateLabel();
	}

	public void toggleCraftingScreen() {
		// Toggle screen
		craftingScreenOpen = !craftingScreenOpen;
		updateStateLabel();
		repaint();
	}

	public boolean isCraftingScreenOpen() {
		return craftingScreenOpen;
	}

	private void updateStateLabel() {
		// Show state
		stateLabel.setText(craftingScreenOpen ? "Crafting: OPEN (press I)" : "Crafting: CLOSED (press I)");
	}
}
