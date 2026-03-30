package com.islandescape.core;

import com.islandescape.entity.CraftingGrid;
import com.islandescape.entity.Item;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel {
	// craft state
	private final JLabel stateLabel;
	private final JLabel tickLabel;
	private final CraftingGrid craftingGrid;
	private final Timer gameLoopTimer;
	private boolean craftingScreenOpen;
	private boolean paused;
	private int worldTick;

	public GamePanel() {
		// panel base
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(960, 640));
		craftingGrid = new CraftingGrid();

		// status label
		stateLabel = new JLabel();
		stateLabel.setForeground(Color.WHITE);
		add(stateLabel, BorderLayout.NORTH);

		// tick label
		tickLabel = new JLabel();
		tickLabel.setForeground(Color.WHITE);
		add(tickLabel, BorderLayout.SOUTH);

		// demo slots
		craftingGrid.setSlot(0, new Item("Wood"));
		craftingGrid.setSlot(1, new Item("Stone"));

		// loop timer
		gameLoopTimer = new Timer(1000 / 10, event -> gameTick());
		gameLoopTimer.start();

		// sync text
		updateStateLabel();
		updateTickLabel();
	}

	private void gameTick() {
		if (!paused) {
			worldTick++;
			updateTickLabel();
		}
		repaint();
	}

	public void toggleCraftingScreen() {
		// toggle screen
		craftingScreenOpen = !craftingScreenOpen;
		paused = craftingScreenOpen;
		updateStateLabel();
		repaint();
	}

	public boolean isCraftingScreenOpen() {
		return craftingScreenOpen;
	}

	public boolean isPaused() {
		return paused;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g2d = (Graphics2D) graphics;

		if (craftingScreenOpen) {
			drawCraftingOverlay(g2d);
		}
	}

	private void drawCraftingOverlay(Graphics2D g2d) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, panelWidth, panelHeight);

		int slotSize = 64;
		int slotGap = 12;
		int gridPixelSize = slotSize * 2 + slotGap;
		int startX = (panelWidth - gridPixelSize) / 2;
		int startY = (panelHeight - gridPixelSize) / 2;

		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
		g2d.drawString("Crafting 2x2", startX, startY - 16);

		for (int index = 0; index < craftingGrid.getSlotCount(); index++) {
			int row = index / 2;
			int col = index % 2;
			int x = startX + col * (slotSize + slotGap);
			int y = startY + row * (slotSize + slotGap);

			g2d.setColor(new Color(60, 60, 60, 220));
			g2d.fillRect(x, y, slotSize, slotSize);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(x, y, slotSize, slotSize);

			Item item = craftingGrid.getSlot(index);
			String text = item == null ? "-" : item.getName();
			g2d.drawString(text, x + 8, y + 36);
		}

		g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
		g2d.drawString("Press I to close", startX, startY + gridPixelSize + 22);
	}

	private void updateStateLabel() {
		// show state
		stateLabel.setText(craftingScreenOpen ? "Crafting: OPEN | Game: PAUSED" : "Crafting: CLOSED | Game: RUNNING");
	}

	private void updateTickLabel() {
		tickLabel.setText("World tick: " + worldTick);
	}
}
