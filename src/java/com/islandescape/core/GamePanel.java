package com.islandescape.core;

import com.islandescape.entity.CraftingGrid;
import com.islandescape.entity.Item;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
	private final TileMap map;
	private final MapRenderer renderer;
	private final int nativeWidth;
	private final int nativeHeight;

	// craft state
	private final JLabel stateLabel;
	private final JLabel tickLabel;
	private final CraftingGrid craftingGrid;
	private final Map<String, BufferedImage> itemIcons;
	private final Timer gameLoopTimer;
	private BufferedImage craftingPanelBg;
	private BufferedImage craftingSlotEmpty;
	private BufferedImage craftingSlotSelected;
	private BufferedImage craftingResultFrame;
	private BufferedImage craftingArrow;
	private boolean craftingScreenOpen;
	private boolean paused;
	private int worldTick;
	private int selectedSlotIndex;
	private String currentResultItemName;
	private int craftedPlankCount;

	public GamePanel() {
		this(null, null);
	}

	public GamePanel(TileMap map, MapRenderer renderer) {
		this.map = map;
		this.renderer = renderer;

		if (map != null) {
			nativeWidth = map.getWidth() * map.getTileSize();
			nativeHeight = map.getHeight() * map.getTileSize();
		} else {
			nativeWidth = 960;
			nativeHeight = 640;
		}

		// panel base
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(960, 640));
		craftingGrid = new CraftingGrid();
		itemIcons = new HashMap<>();
		loadCraftingAssets();

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
		craftingGrid.setSlot(1, new Item("Wood"));
		refreshCraftingResult();

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

	public void moveSelection(int rowShift, int colShift) {
		if (!craftingScreenOpen) {
			return;
		}
		int row = selectedSlotIndex / 2;
		int col = selectedSlotIndex % 2;
		row = Math.max(0, Math.min(1, row + rowShift));
		col = Math.max(0, Math.min(1, col + colShift));
		selectedSlotIndex = row * 2 + col;
		repaint();
	}

	public void setItemInSelectedSlot(String itemType) {
		if (!craftingScreenOpen) {
			return;
		}
		if ("WOOD".equals(itemType)) {
			craftingGrid.setSlot(selectedSlotIndex, new Item("Wood"));
		} else if ("STONE".equals(itemType)) {
			craftingGrid.setSlot(selectedSlotIndex, new Item("Stone"));
		} else if ("CLEAR".equals(itemType)) {
			craftingGrid.setSlot(selectedSlotIndex, null);
		}
		refreshCraftingResult();
		repaint();
	}

	public void craftCurrentRecipe() {
		if (!craftingScreenOpen || !"plank".equals(currentResultItemName)) {
			return;
		}

		int removedWood = 0;
		for (int index = 0; index < craftingGrid.getSlotCount(); index++) {
			Item slotItem = craftingGrid.getSlot(index);
			if (slotItem != null && "wood".equalsIgnoreCase(slotItem.getName()) && removedWood < 2) {
				craftingGrid.setSlot(index, null);
				removedWood++;
			}
		}

		craftedPlankCount++;
		refreshCraftingResult();
		updateStateLabel();
		repaint();
	}

	public void clearCraftingGrid() {
		if (!craftingScreenOpen) {
			return;
		}
		for (int index = 0; index < craftingGrid.getSlotCount(); index++) {
			craftingGrid.setSlot(index, null);
		}
		refreshCraftingResult();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g2d = (Graphics2D) graphics;

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		if (map != null && renderer != null) {
			try {
				BufferedImage buffer = new BufferedImage(nativeWidth, nativeHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D bufferG = buffer.createGraphics();
				renderer.render(bufferG, map);
				bufferG.dispose();

				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

				double scaleX = (double) getWidth() / nativeWidth;
				double scaleY = (double) getHeight() / nativeHeight;
				double scale = Math.min(scaleX, scaleY);

				int scaledWidth = (int) (nativeWidth * scale);
				int scaledHeight = (int) (nativeHeight * scale);
				int offsetX = (getWidth() - scaledWidth) / 2;
				int offsetY = (getHeight() - scaledHeight) / 2;

				g2d.drawImage(buffer, offsetX, offsetY, scaledWidth, scaledHeight, null);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		if (craftingScreenOpen) {
			drawCraftingOverlay(g2d);
		}
	}

	private void drawCraftingOverlay(Graphics2D g2d) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, panelWidth, panelHeight);

		int panelBoxW = 820;
		int panelBoxH = 520;
		int panelX = (panelWidth - panelBoxW) / 2;
		int panelY = (panelHeight - panelBoxH) / 2;

		if (craftingPanelBg != null) {
			g2d.drawImage(craftingPanelBg, panelX, panelY, panelBoxW, panelBoxH, null);
		} else {
			g2d.setColor(new Color(150, 110, 75, 220));
			g2d.fillRoundRect(panelX, panelY, panelBoxW, panelBoxH, 24, 24);
		}

		int slotSize = 64;
		int slotGap = 12;
		int gridPixelSize = slotSize * 2 + slotGap;
		int startX = panelX + 230;
		int startY = panelY + 190;

		g2d.setColor(new Color(35, 20, 10));
		g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
		g2d.drawString("crafting", panelX + 330, panelY + 78);

		for (int index = 0; index < craftingGrid.getSlotCount(); index++) {
			int row = index / 2;
			int col = index % 2;
			int x = startX + col * (slotSize + slotGap);
			int y = startY + row * (slotSize + slotGap);

			BufferedImage slotImage = index == selectedSlotIndex ? craftingSlotSelected : craftingSlotEmpty;
			if (slotImage != null) {
				g2d.drawImage(slotImage, x, y, slotSize, slotSize, null);
			} else {
				g2d.setColor(new Color(60, 60, 60, 220));
				g2d.fillRect(x, y, slotSize, slotSize);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(x, y, slotSize, slotSize);
			}

			Item item = craftingGrid.getSlot(index);
			if (item != null) {
				BufferedImage icon = itemIcons.get(item.getName().toLowerCase());
				if (icon != null) {
					g2d.drawImage(icon, x + 16, y + 16, 32, 32, null);
				} else {
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
					g2d.drawString(item.getName(), x + 6, y + 36);
				}
			}
		}

		int arrowX = startX + gridPixelSize + 34;
		int arrowY = startY + 52;
		if (craftingArrow != null) {
			g2d.drawImage(craftingArrow, arrowX, arrowY, 48, 24, null);
		}

		int resultX = arrowX + 68;
		int resultY = startY + 32;
		if (craftingResultFrame != null) {
			g2d.drawImage(craftingResultFrame, resultX, resultY, 72, 72, null);
		} else {
			g2d.setColor(new Color(220, 220, 220, 220));
			g2d.drawRect(resultX, resultY, 72, 72);
		}

		BufferedImage plankIcon = itemIcons.get(currentResultItemName);
		if (plankIcon != null) {
			g2d.drawImage(plankIcon, resultX + 20, resultY + 20, 32, 32, null);
		}

		g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
		g2d.setColor(new Color(35, 20, 10));
		g2d.drawString("1 wood 2 stone 3 clear slot", panelX + 280, panelY + panelBoxH - 56);
		g2d.drawString("arrows move enter craft c clear all esc i close", panelX + 212, panelY + panelBoxH - 32);
	}

	private void refreshCraftingResult() {
		int woodCount = 0;
		for (int index = 0; index < craftingGrid.getSlotCount(); index++) {
			Item slotItem = craftingGrid.getSlot(index);
			if (slotItem == null) {
				continue;
			}
			if ("wood".equalsIgnoreCase(slotItem.getName())) {
				woodCount++;
			} else {
				currentResultItemName = null;
				return;
			}
		}
		currentResultItemName = woodCount == 2 ? "plank" : null;
	}

	private void loadCraftingAssets() {
		craftingPanelBg = loadImage("src/resources/ui/crafting/rafting_panel_bg.png");
		craftingSlotEmpty = loadImage("src/resources/ui/crafting/crafting_slot_empty.png");
		craftingSlotSelected = loadImage("src/resources/ui/crafting/crafting_slot_selected.png");
		craftingResultFrame = loadImage("src/resources/ui/crafting/crafting_result_frame.png");
		craftingArrow = loadImage("src/resources/ui/crafting/crafting_arrow.png");

		itemIcons.put("wood", loadImage("src/resources/items/item_wood.png"));
		itemIcons.put("stone", loadImage("src/resources/items/item_stone.png"));
		itemIcons.put("plank", loadImage("src/resources/items/item_plank.png"));
	}

	private BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException exception) {
			return null;
		}
	}

	private void updateStateLabel() {
		// show state
		stateLabel.setText(
				craftingScreenOpen
						? "Crafting: OPEN | Game: PAUSED | Planks: " + craftedPlankCount
						: "Crafting: CLOSED | Game: RUNNING | Planks: " + craftedPlankCount
		);
	}

	private void updateTickLabel() {
		tickLabel.setText("World tick: " + worldTick);
	}
}
