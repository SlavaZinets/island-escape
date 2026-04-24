package com.islandescape.inventory;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.crafting.CraftingRecipe;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.item.Item;
import com.islandescape.player.Player;
import com.islandescape.ui.BoatRepairLayout;
import com.islandescape.ui.CraftingScreenLayout;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    private static final int SEPARATOR_WIDTH = 30;

    private final Player player1;
    private final Player player2;
    private final InventoryCursor cursor;
    private final Map<String, BufferedImage> itemIcons;

    private CraftingSystem craftingSystem;
    private BoatRepairSystem boatRepairSystem;

    private boolean open;
    private boolean craftingOpen;
    private boolean boatRepairOpen;
    private boolean p1NearTable = true;
    private boolean p2NearTable = true;
    private boolean p1NearBoat = true;
    private boolean p2NearBoat = true;
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

    public void setCraftingOpen(boolean craftingOpen) {
        this.craftingOpen = craftingOpen;
    }

    public void setCraftingSystem(CraftingSystem craftingSystem) {
        this.craftingSystem = craftingSystem;
    }

    public void setPlayerNearTable(boolean p1Near, boolean p2Near) {
        this.p1NearTable = p1Near;
        this.p2NearTable = p2Near;
    }

    // --- Boat repair extensions (stubs — wired up in Step 10) ---

    public void setBoatRepairSystem(BoatRepairSystem boatRepairSystem) {
        this.boatRepairSystem = boatRepairSystem;
    }

    public void setBoatRepairOpen(boolean boatRepairOpen) {
        this.boatRepairOpen = boatRepairOpen;
    }

    public void setPlayerNearBoat(boolean p1Near, boolean p2Near) {
        this.p1NearBoat = p1Near;
        this.p2NearBoat = p2Near;
    }

    // Determine which boat-repair slot was clicked, or -1 if none
    int pixelToBoatSlot(int px, int py, int panelW, int panelH) {
        return -1;
    }

    public void updateMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }



    public void handleClick(int screenX, int screenY, boolean isLeftClick, int panelW, int panelH) {
        if (!open) return;

        // When boat repair is open, check boat-repair slots first
        if (boatRepairOpen && boatRepairSystem != null) {
            int boatSlot = pixelToBoatSlot(screenX, screenY, panelW, panelH);
            if (boatSlot >= 0) {
                handleBoatRepairClick(boatSlot, isLeftClick, screenX, panelW, panelH);
                return;
            }
        }

        // When crafting is open, check crafting areas first
        if (craftingOpen && craftingSystem != null) {
            // Check result slot — click to pick up crafted item
            if (isClickOnResultSlot(screenX, screenY, panelW, panelH)) {
                handleResultSlotClick();
                return;
            }

            // Check crafting grid slots
            int craftSlot = pixelToCraftingSlot(screenX, screenY, panelW, panelH);
            if (craftSlot >= 0) {
                handleCraftingGridClick(craftSlot, isLeftClick, screenX, panelW, panelH);
                return;
            }
        }

        int xBorder;
        int gridTop;
        int p1Left;
        int p2Left;

        if (craftingOpen) {
            CraftingScreenLayout layout = new CraftingScreenLayout(panelW, panelH);
            xBorder = layout.invXBorder;
            gridTop = layout.invAreaTop + 22;
            p1Left = layout.invP1Left;
            p2Left = layout.invP2Left;
        } else {
            xBorder = getXBorder(panelW);
            gridTop = getGridTop(panelH);
            p1Left = getP1Left(panelW);
            p2Left = getP2Left(panelW);
        }

        // determine player by X position
        boolean isPlayer1 = screenX < xBorder;

        // Block inventory clicks for players not near the crafting table
        if (craftingOpen) {
            if (isPlayer1 && !p1NearTable) return;
            if (!isPlayer1 && !p2NearTable) return;
        }

        Inventory inventory = isPlayer1 ? player1.getInventory() : player2.getInventory();
        int gridLeft = isPlayer1 ? p1Left : p2Left;

        int slotIndex = pixelToSlot(screenX, screenY, gridLeft, gridTop);
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

    // Determine which crafting grid slot was clicked, or -1 if none
    int pixelToCraftingSlot(int px, int py, int panelW, int panelH) {
        CraftingScreenLayout layout = new CraftingScreenLayout(panelW, panelH);

        int relX = px - layout.craftingStartX;
        int relY = py - layout.gridY;
        if (relX < 0 || relY < 0) return -1;

        int cellSize = layout.slotSize + layout.slotGap;
        int col = relX / cellSize;
        int row = relY / cellSize;

        if (col >= CraftingScreenLayout.GRID_COLS || row >= CraftingScreenLayout.GRID_ROWS) return -1;

        // Check click is inside the slot, not in the gap
        if (relX % cellSize >= layout.slotSize) return -1;
        if (relY % cellSize >= layout.slotSize) return -1;

        return row * CraftingScreenLayout.GRID_COLS + col;
    }

    // Handle a click on a boat-repair slot.
    private void handleBoatRepairClick(int slot, boolean isLeftClick, int screenX, int panelW, int panelH) {

    }

    // Handle a click on a crafting grid slot — mirrors inventory click behavior
    private void handleCraftingGridClick(int craftSlot, boolean isLeftClick, int screenX, int panelW, int panelH) {
        CraftingScreenLayout layout = new CraftingScreenLayout(panelW, panelH);
        int playerId = screenX < layout.invXBorder ? 0 : 1;

        if (cursor.isEmpty()) {
            if (isLeftClick) {
                cursor.pickUpFromCraftingGrid(craftingSystem, craftSlot);
            } else {
                cursor.pickUpHalfFromCraftingGrid(craftingSystem, craftSlot);
            }
        } else {
            if (isLeftClick) {
                cursor.placeIntoCraftingGrid(craftingSystem, craftSlot, playerId);
            } else {
                cursor.placeOneIntoCraftingGrid(craftingSystem, craftSlot, playerId);
            }
        }
    }

    // Check if click is on the crafting result slot
    private boolean isClickOnResultSlot(int px, int py, int panelW, int panelH) {
        CraftingScreenLayout layout = new CraftingScreenLayout(panelW, panelH);
        return px >= layout.resultX && px <= layout.resultX + layout.resultSlotSize
                && py >= layout.resultY && py <= layout.resultY + layout.resultSlotSize;
    }

    // Click on result slot — craft the item and put it on the cursor
    private void handleResultSlotClick() {
        if (!cursor.isEmpty()) return; // cursor must be empty to pick up result

        CraftingRecipe preview = craftingSystem.preview();
        if (preview == null) return; // no valid recipe

        Item result = craftingSystem.craft(null);
        if (result != null) {
            cursor.setHeldItem(result);
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

    // --- Layout calculations (side-by-side) ---

    private int getOneGridWidth() {
        return COLS * SLOT_SIZE + (COLS - 1) * SLOT_GAP;
    }

    private int getPlayerGridHeight() {
        return ROWS_PER_PLAYER * SLOT_SIZE + (ROWS_PER_PLAYER - 1) * SLOT_GAP;
    }

    private int getTotalWidth() {
        return getOneGridWidth() * 2 + SEPARATOR_WIDTH;
    }

    // Player 1 grid left X
    int getP1Left(int panelW) {
        return (panelW - getTotalWidth()) / 2;
    }

    // Player 2 grid left X
    int getP2Left(int panelW) {
        return getP1Left(panelW) + getOneGridWidth() + SEPARATOR_WIDTH;
    }

    // Vertical X border between the two grids
    int getXBorder(int panelW) {
        return getP1Left(panelW) + getOneGridWidth() + SEPARATOR_WIDTH / 2;
    }

    // Both grids share the same top Y
    // When crafting is open, push inventory to bottom half of screen
    int getGridTop(int panelH) {
        if (craftingOpen) {
            int craftingPanelBottom = (int) (panelH * 0.45) + (panelH - (int)(panelH * 0.45)) / 2;
            return craftingPanelBottom + 20;
        }
        return (panelH - getPlayerGridHeight()) / 2;
    }



    public void renderInventoryComponent(Graphics g, int panelW, int panelH) {
        Graphics2D g2d = (Graphics2D) g;
        if (open) {
            drawFullInventory(g2d, panelW, panelH);
        } else {
            drawHotbar(g2d, panelW, panelH);
        }
    }

    // Draw held item on cursor — call LAST in paint order so it's always on top
    public void drawHeldItem(Graphics2D g2d) {
        if (!cursor.isEmpty()) {
            drawItem(g2d, cursor.getHeldItem(), mouseX - SLOT_SIZE / 2, mouseY - SLOT_SIZE / 2);
        }
    }

    private void drawFullInventory(Graphics2D g2d, int panelW, int panelH) {
        int p1Left, p2Left, gridTop, xBorder;

        if (craftingOpen) {
            CraftingScreenLayout layout = new CraftingScreenLayout(panelW, panelH);
            p1Left = layout.invP1Left;
            p2Left = layout.invP2Left;
            gridTop = layout.invAreaTop + 22;
            xBorder = layout.invXBorder;
        } else {
            p1Left = getP1Left(panelW);
            p2Left = getP2Left(panelW);
            gridTop = getGridTop(panelH);
            xBorder = getXBorder(panelW);
        }

        boolean p1Blocked = craftingOpen && !p1NearTable;
        boolean p2Blocked = craftingOpen && !p2NearTable;

        // Player 1 label
        g2d.setColor(p1Blocked ? new Color(120, 100, 80) : new Color(60, 40, 20));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(player1.getName(), p1Left, gridTop - 8);

        // Player 1 grid
        drawPlayerGrid(g2d, player1.getInventory(), p1Left, gridTop, p1Blocked);

        // Vertical separator
        g2d.setColor(new Color(101, 67, 33, 150));
        g2d.fillRect(xBorder - 1, gridTop, 2, getPlayerGridHeight());

        // Player 2 label
        g2d.setColor(p2Blocked ? new Color(120, 100, 80) : new Color(60, 40, 20));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(player2.getName(), p2Left, gridTop - 8);

        // Player 2 grid
        drawPlayerGrid(g2d, player2.getInventory(), p2Left, gridTop, p2Blocked);
    }

    private void drawPlayerGrid(Graphics2D g2d, Inventory inventory, int gridLeft, int gridTop, boolean blocked) {
        int cellSize = SLOT_SIZE + SLOT_GAP;

        for (int row = 0; row < ROWS_PER_PLAYER; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = gridLeft + col * cellSize;
                int y = gridTop + row * cellSize;
                int slotIndex = row * COLS + col;

                // slot background — dimmer when blocked
                g2d.setColor(blocked ? new Color(40, 40, 40, 160) : new Color(60, 60, 60, 200));
                g2d.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
                g2d.setColor(blocked ? new Color(80, 80, 80) : new Color(120, 120, 120));
                g2d.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);

                // highlight hotbar row (only if not blocked)
                if (!blocked && row == 3) {
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

        // Dark overlay on top of blocked grid
        if (blocked) {
            int gridW = COLS * cellSize - SLOT_GAP;
            int gridH = ROWS_PER_PLAYER * cellSize - SLOT_GAP;
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(gridLeft - 4, gridTop - 4, gridW + 8, gridH + 8, 8, 8);
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
        int p1HotbarX = panelW / 2 - hotbarWidth - 120;
        drawHotbarRow(g2d, player1.getInventory(), p1HotbarX, hotbarY);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString(player1.getName(), p1HotbarX, hotbarY - 6);

        // Player 2 hotbar — right side
        int p2HotbarX = panelW / 2 + 120;
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
