package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.player.Player;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventoryScreen {

    private static final int COLS = 5;
    private static final int ROWS_PER_PLAYER = 4; // 3 main + 1 hotbar
    private static final int SLOT_SIZE = 64;
    private static final int SLOT_GAP = 12;
    private static final int SEPARATOR_HEIGHT = 20;

    private final Player player1;
    private final Player player2;
    private final InventoryCursor cursor;
    private final Map<String, BufferedImage> itemIcons;

    private boolean open;
    private int mouseX;
    private int mouseY;

    public InventoryScreen(Player player1, Player player2, InventoryCursor cursor) {
        this.player1 = player1;
        this.player2 = player2;
        this.cursor = cursor;
        this.itemIcons = new HashMap<>();
        loadItemIcons();
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void updateMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }



    public void handleClick(int bufferX, int bufferY, boolean isLeftClick) {
        if (!open) return;

        int gridLeft = calcGridLeft(bufferX, bufferY);
        int p1Top = calcP1Top(bufferX, bufferY);
        int yBorder = calcYBorder(bufferX, bufferY);

        // determine player
        boolean isPlayer1 = bufferY < yBorder;
        Inventory inventory = isPlayer1 ? player1.getInventory() : player2.getInventory();
        int gridTop = isPlayer1 ? p1Top : calcP2Top(bufferX, bufferY);

        int slotIndex = pixelToSlot(bufferX, bufferY, gridLeft, gridTop);
        if (slotIndex < 0) return;

        if (cursor.isEmpty()) {
            if (isLeftClick) {
                cursor.pickUp(inventory, slotIndex);
            } else {
                cursor.pickUpHalf(inventory, slotIndex);
            }
        } else {
            if (isLeftClick) {
                cursor.placeAll(inventory, slotIndex);
            } else {
                cursor.placeOne(inventory, slotIndex);
            }
        }
    }

    // Overloaded version using panel dimensions for layout calculation
    public void handleClick(int bufferX, int bufferY, boolean isLeftClick, int panelW, int panelH) {
        if (!open) return;

        int gridLeft = getGridLeft(panelW);
        int p1Top = getP1Top(panelH);
        int yBorder = getYBorder(panelH);
        int p2Top = getP2Top(panelH);

        boolean isPlayer1 = bufferY < yBorder;
        Inventory inventory = isPlayer1 ? player1.getInventory() : player2.getInventory();
        int gridTop = isPlayer1 ? p1Top : p2Top;

        int slotIndex = pixelToSlot(bufferX, bufferY, gridLeft, gridTop);
        if (slotIndex < 0) return;

        if (cursor.isEmpty()) {
            if (isLeftClick) {
                cursor.pickUp(inventory, slotIndex);
            } else {
                cursor.pickUpHalf(inventory, slotIndex);
            }
        } else {
            if (isLeftClick) {
                cursor.placeAll(inventory, slotIndex);
            } else {
                cursor.placeOne(inventory, slotIndex);
            }
        }
    }

    private int pixelToSlot(int px, int py, int gridLeft, int gridTop) {
        int cellSize = SLOT_SIZE + SLOT_GAP;

        int relX = px - gridLeft;
        int relY = py - gridTop;

        if (relX < 0 || relY < 0) return -1;

        int col = relX / cellSize;
        int row = relY / cellSize;

        if (col >= COLS || row >= ROWS_PER_PLAYER) return -1;

        // check click is inside the slot, not in the gap
        if (relX % cellSize >= SLOT_SIZE) return -1;
        if (relY % cellSize >= SLOT_SIZE) return -1;

        return row * COLS + col;
    }

    // --- Layout calculations ---

    private int getGridWidth() {
        return COLS * SLOT_SIZE + (COLS - 1) * SLOT_GAP;
    }

    private int getPlayerGridHeight() {
        return ROWS_PER_PLAYER * SLOT_SIZE + (ROWS_PER_PLAYER - 1) * SLOT_GAP;
    }

    private int getTotalHeight() {
        return getPlayerGridHeight() * 2 + SEPARATOR_HEIGHT;
    }

    int getGridLeft(int panelW) {
        return (panelW - getGridWidth()) / 2;
    }

    int getP1Top(int panelH) {
        return (panelH - getTotalHeight()) / 2;
    }

    int getP2Top(int panelH) {
        return getP1Top(panelH) + getPlayerGridHeight() + SEPARATOR_HEIGHT;
    }

    int getYBorder(int panelH) {
        return getP1Top(panelH) + getPlayerGridHeight() + SEPARATOR_HEIGHT / 2;
    }

    // Fallback versions using stored mouse coordinates (not recommended — use panelW/H versions)
    private int calcGridLeft(int bx, int by) { return 0; }
    private int calcP1Top(int bx, int by) { return 60; }
    private int calcP2Top(int bx, int by) { return calcP1Top(bx, by) + getPlayerGridHeight() + SEPARATOR_HEIGHT; }
    private int calcYBorder(int bx, int by) { return calcP1Top(bx, by) + getPlayerGridHeight() + SEPARATOR_HEIGHT / 2; }

    // --- Rendering ---

    public void drawFullInventory(Graphics2D g2d, int panelW, int panelH) {
        int gridLeft = getGridLeft(panelW);
        int p1Top = getP1Top(panelH);
        int p2Top = getP2Top(panelH);

        // Player 1 label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(player1.getName(), gridLeft, p1Top - 8);

        // Player 1 grid
        drawPlayerGrid(g2d, player1.getInventory(), gridLeft, p1Top);

        // Separator
        int yBorder = getYBorder(panelH);
        g2d.setColor(new Color(200, 200, 200, 150));
        g2d.fillRect(gridLeft, yBorder - 1, getGridWidth(), 2);

        // Player 2 label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(player2.getName(), gridLeft, p2Top - 8);

        // Player 2 grid
        drawPlayerGrid(g2d, player2.getInventory(), gridLeft, p2Top);

        // Held item on cursor
        if (!cursor.isEmpty()) {
            drawItem(g2d, cursor.getHeldItem(), mouseX - SLOT_SIZE / 2, mouseY - SLOT_SIZE / 2);
        }
    }

    private void drawPlayerGrid(Graphics2D g2d, Inventory inventory, int gridLeft, int gridTop) {
        int cellSize = SLOT_SIZE + SLOT_GAP;

        for (int row = 0; row < ROWS_PER_PLAYER; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = gridLeft + col * cellSize;
                int y = gridTop + row * cellSize;
                int slotIndex = row * COLS + col;

                // slot background
                g2d.setColor(new Color(60, 60, 60, 200));
                g2d.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
                g2d.setColor(new Color(120, 120, 120));
                g2d.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);

                // highlight hotbar row
                if (row == 3) {
                    int selectedGlobal = inventory.getSelectedHotBarSlot();
                    int selectedLocal = selectedGlobal - 15;
                    if (col == selectedLocal) {
                        g2d.setColor(new Color(255, 255, 100, 120));
                        g2d.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
                        g2d.setColor(new Color(255, 255, 100));
                        g2d.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
                    }
                }

                // draw item
                Item item = inventory.getSlot(slotIndex);
                if (item != null) {
                    drawItem(g2d, item, x, y);
                }
            }
        }
    }

    private void drawItem(Graphics2D g2d, Item item, int x, int y) {
        BufferedImage icon = itemIcons.get(item.getName().toLowerCase());
        if (icon != null) {
            g2d.drawImage(icon, x + 16, y + 16, 32, 32, null);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2d.drawString(item.getName(), x + 4, y + SLOT_SIZE / 2);
        }

        // quantity
        if (item.getQuantity() > 1) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            String qty = String.valueOf(item.getQuantity());
            g2d.drawString(qty, x + SLOT_SIZE - 8 - g2d.getFontMetrics().stringWidth(qty), y + SLOT_SIZE - 6);
        }
    }

    public void drawHotbar(Graphics2D g2d, int panelW, int panelH) {
        int cellSize = SLOT_SIZE + SLOT_GAP;
        int hotbarWidth = COLS * SLOT_SIZE + (COLS - 1) * SLOT_GAP;
        int hotbarY = panelH - SLOT_SIZE - 20;

        // Player 1 hotbar — left side
        int p1HotbarX = panelW / 2 - hotbarWidth - 30;
        drawHotbarRow(g2d, player1.getInventory(), p1HotbarX, hotbarY);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString(player1.getName(), p1HotbarX, hotbarY - 6);

        // Player 2 hotbar — right side
        int p2HotbarX = panelW / 2 + 30;
        drawHotbarRow(g2d, player2.getInventory(), p2HotbarX, hotbarY);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString(player2.getName(), p2HotbarX, hotbarY - 6);
    }

    private void drawHotbarRow(Graphics2D g2d, Inventory inventory, int startX, int startY) {
        int cellSize = SLOT_SIZE + SLOT_GAP;
        int selectedLocal = inventory.getSelectedHotBarSlot() - 15;

        for (int col = 0; col < COLS; col++) {
            int x = startX + col * cellSize;
            int slotIndex = 15 + col; // hotbar slots

            // slot background
            g2d.setColor(new Color(60, 60, 60, 200));
            g2d.fillRoundRect(x, startY, SLOT_SIZE, SLOT_SIZE, 8, 8);
            g2d.setColor(new Color(120, 120, 120));
            g2d.drawRoundRect(x, startY, SLOT_SIZE, SLOT_SIZE, 8, 8);

            // selected highlight
            if (col == selectedLocal) {
                g2d.setColor(new Color(255, 255, 100, 120));
                g2d.fillRoundRect(x, startY, SLOT_SIZE, SLOT_SIZE, 8, 8);
                g2d.setColor(new Color(255, 255, 100));
                g2d.drawRoundRect(x, startY, SLOT_SIZE, SLOT_SIZE, 8, 8);
            }

            // draw item
            Item item = inventory.getSlot(slotIndex);
            if (item != null) {
                drawItem(g2d, item, x, startY);
            }
        }
    }

    // Return held item to player1's inventory on close
    public void returnHeldItem() {
        if (!cursor.isEmpty()) {
            Item held = cursor.getHeldItem();
            if (!player1.getInventory().addItem(held)) {
                player2.getInventory().addItem(held);
            }
            cursor.clear();
        }
    }

    private void loadItemIcons() {
        String[] names = {"wood", "stone", "banana", "coconut", "fish", "axe", "pickaxe",
                "rope", "vines", "plank", "paddle", "fishing_rod", "coconut_shell",
                "coconut_bottle", "tropical_leaves", "mast", "frame", "sail", "rudder", "fittings"};
        for (String name : names) {
            BufferedImage img = loadImage("src/resources/items/item_" + name + ".png");
            if (img != null) {
                itemIcons.put(name, img);
            }
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            return null;
        }
    }
}
