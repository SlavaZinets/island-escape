package com.islandescape.inventory;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.crafting.CraftingRecipe;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.item.Item;
import com.islandescape.player.Player;
import com.islandescape.ui.BoatRepairLayout;
import com.islandescape.ui.CraftingScreenLayout;
import com.islandescape.ui.StatusBarRenderer;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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

    // Trash cluster sits below the grids
    private static final int TRASH_GAP_BELOW_GRID = 30;
    private static final int REMOVE_BUTTON_WIDTH = 120;
    private static final int REMOVE_BUTTON_HEIGHT = 32;
    private static final int REMOVE_BUTTON_GAP_BELOW_SLOT = 12;

    private final Player player1;
    private final Player player2;
    private final InventoryCursor cursor;
    private final TrashBin trashBin;
    private final Map<String, BufferedImage> itemIcons;
    private BufferedImage slotEmpty;
    private BufferedImage slotSelected;

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
        this(player1, player2, cursor, new TrashBin());
    }

    public InventoryScreen(Player player1, Player player2, InventoryCursor cursor, TrashBin trashBin) {
        this.player1 = player1;
        this.player2 = player2;
        this.cursor = cursor;
        this.trashBin = trashBin;
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

    // Boat repair

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
        BoatRepairLayout layout = new BoatRepairLayout(panelW, panelH);

        int relX = px - layout.repairSlotsStartX;
        int relY = py - layout.repairSlotsY;
        if (relX < 0 || relY < 0) return -1;

        // One row only — click must be within the slot height vertically.
        if (relY >= layout.slotSize) return -1;

        int cellSize = layout.slotSize + layout.slotGap;
        int col = relX / cellSize;
        if (col >= BoatRepairLayout.SLOT_COUNT) return -1;

        // Click must be inside the slot horizontally, not in the gap between slots.
        if (relX % cellSize >= layout.slotSize) return -1;

        return col;
    }

    public void updateMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }



    public void handleClick(int screenX, int screenY, boolean isLeftClick, int panelW, int panelH) {
        if (!open) return;

        // REMOVE button — destroys whatever is in the trash slot, leaves cursor untouched
        if (pixelOnRemoveButton(screenX, screenY, panelW, panelH)) {
            trashBin.clear();
            return;
        }

        //  Trash slot — same cursor slot routing as a normal slot, just on the bin
        if (pixelToTrashSlot(screenX, screenY, panelW, panelH)) {
            if (cursor.isEmpty()) {
                if (isLeftClick) {
                    cursor.pickUpFromTrash(trashBin);
                } else {
                    cursor.pickUpHalfFromTrash(trashBin);
                }
            } else {
                if (isLeftClick) {
                    cursor.placeAllInTrash(trashBin);
                } else {
                    cursor.placeOneInTrash(trashBin);
                }
            }
            return;
        }

        // 3. Player grids
        int xBorder = getXBorder(panelW);
        int gridTop = getGridTop(panelH);

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


        int p1Left;
        int p2Left;

        if (boatRepairOpen) {
            BoatRepairLayout layout = new BoatRepairLayout(panelW, panelH);
            xBorder = layout.invXBorder;
            gridTop = layout.invAreaTop + 22;
            p1Left = layout.invP1Left;
            p2Left = layout.invP2Left;
        } else if (craftingOpen) {
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

        // Block inventory clicks for players not near the active structure.
        if (boatRepairOpen) {
            if (isPlayer1 && !p1NearBoat) return;
            if (!isPlayer1 && !p2NearBoat) return;
        } else if (craftingOpen) {
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
        if (cursor.isEmpty()) {
            if (isLeftClick) {
                cursor.pickUpFromBoatSlot(boatRepairSystem, slot);
            } else {
                cursor.pickUpHalfFromBoatSlot(boatRepairSystem, slot);
            }
        } else {
            if (isLeftClick) {
                cursor.placeIntoBoatSlot(boatRepairSystem, slot);
            } else {
                cursor.placeOneIntoBoatSlot(boatRepairSystem, slot);
            }
        }
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

    // Trash cluster geometry (centered below both grids)

    int getTrashSlotX(int panelW) {
        return panelW / 2 - SLOT_SIZE / 2;
    }

    int getTrashSlotY(int panelH) {
        return getGridTop(panelH) + getPlayerGridHeight() + TRASH_GAP_BELOW_GRID;
    }

    Rectangle getRemoveButtonRect(int panelW, int panelH) {
        int x = panelW / 2 - REMOVE_BUTTON_WIDTH / 2;
        int y = getTrashSlotY(panelH) + SLOT_SIZE + REMOVE_BUTTON_GAP_BELOW_SLOT;
        return new Rectangle(x, y, REMOVE_BUTTON_WIDTH, REMOVE_BUTTON_HEIGHT);
    }

    boolean pixelToTrashSlot(int px, int py, int panelW, int panelH) {
        int x = getTrashSlotX(panelW);
        int y = getTrashSlotY(panelH);
        return px >= x && px < x + SLOT_SIZE
                && py >= y && py < y + SLOT_SIZE;
    }

    boolean pixelOnRemoveButton(int px, int py, int panelW, int panelH) {
        return getRemoveButtonRect(panelW, panelH).contains(px, py);
    }

    // expose the trash bin for tests + external inspection
    public TrashBin getTrashBin() {
        return trashBin;
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

        if (boatRepairOpen) {
            BoatRepairLayout layout = new BoatRepairLayout(panelW, panelH);
            p1Left = layout.invP1Left;
            p2Left = layout.invP2Left;
            gridTop = layout.invAreaTop + 22;
            xBorder = layout.invXBorder;
        } else if (craftingOpen) {
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
            drawInventoryPanel(g2d, panelW, panelH, p1Left, gridTop);
        }

        boolean p1Blocked = (craftingOpen && !p1NearTable) || (boatRepairOpen && !p1NearBoat);
        boolean p2Blocked = (craftingOpen && !p2NearTable) || (boatRepairOpen && !p2NearBoat);

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

        // Shared trash slot + REMOVE button (centered below both grids)
        drawTrashSlot(g2d, panelW, panelH);
        drawRemoveButton(g2d, panelW, panelH);

        // Held item on cursor — draw last so it floats above the trash UI
        if (!cursor.isEmpty()) {
            drawItem(g2d, cursor.getHeldItem(), mouseX - SLOT_SIZE / 2, mouseY - SLOT_SIZE / 2);
        }
    }
    private void drawTrashSlot(Graphics2D g2d, int panelW, int panelH) {
        int x = getTrashSlotX(panelW);
        int y = getTrashSlotY(panelH);

        // "TRASH" label, same style as the player labels above each grid
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString("TRASH", x, y - 8);

        // slot background — same look as a normal inventory slot
        drawSlotCell(g2d, x, y, false);

        // contents
        Item item = trashBin.getItem();
        if (item != null) {
            drawItem(g2d, item, x, y);
        }
    }

    // Draws a single inventory cell using crafting_slot_empty.png (or
    // crafting_slot_selected.png when selected). Falls back to the original
    // dark rounded rect if the sprite is missing.
    private void drawSlotCell(Graphics2D g2d, int x, int y, boolean selected) {
        BufferedImage sprite = selected ? slotSelected : slotEmpty;
        if (sprite != null) {
            g2d.drawImage(sprite, x, y, SLOT_SIZE, SLOT_SIZE, null);
            return;
        }
        // Fallback
        g2d.setColor(selected ? new Color(255, 255, 100, 120) : new Color(60, 60, 60, 200));
        g2d.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
        g2d.setColor(selected ? new Color(255, 255, 100) : new Color(120, 120, 120));
        g2d.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
    }

    // Brown rounded panel behind the inventory grids (only when inventory is
    // the sole open screen — otherwise the crafting/boat-repair panel
    // already provides a background).
    private void drawInventoryPanel(Graphics2D g2d, int panelW, int panelH, int p1Left, int gridTop) {
        int totalW = getTotalWidth();
        int gridH = getPlayerGridHeight();
        int trashY = getTrashSlotY(panelH);
        Rectangle btn = getRemoveButtonRect(panelW, panelH);

        int padX = 50;
        int padTop = 50;
        int padBottom = 30;

        int rectX = p1Left - padX;
        int rectY = gridTop - padTop;
        int rectW = totalW + padX * 2;
        int rectBottom = btn.y + btn.height + padBottom;
        int rectH = rectBottom - rectY;

        g2d.setColor(new Color(139, 119, 82));
        g2d.fillRoundRect(rectX, rectY, rectW, rectH, 20, 20);
        g2d.setColor(new Color(101, 67, 33));
        g2d.drawRoundRect(rectX, rectY, rectW, rectH, 20, 20);
    }

    private void drawRemoveButton(Graphics2D g2d, int panelW, int panelH) {
        Rectangle r = getRemoveButtonRect(panelW, panelH);

        // red-tinted fill + lighter red border
        g2d.setColor(new Color(180, 50, 50, 220));
        g2d.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawRoundRect(r.x, r.y, r.width, r.height, 8, 8);

        // centered white "REMOVE" label
        g2d.setColor(Color.WHITE);
        Font font = new Font("SansSerif", Font.BOLD, 14);
        g2d.setFont(font);
        String label = "REMOVE";
        int textW = g2d.getFontMetrics().stringWidth(label);
        int textX = r.x + (r.width - textW) / 2;
        int textY = r.y + (r.height + g2d.getFontMetrics().getAscent()) / 2 - 2;
        g2d.drawString(label, textX, textY);
    }
    private void drawPlayerGrid(Graphics2D g2d, Inventory inventory, int gridLeft, int gridTop, boolean blocked) {
        int cellSize = SLOT_SIZE + SLOT_GAP;
        int selectedLocal = inventory.getSelectedHotBarSlot() - 15;

        for (int row = 0; row < ROWS_PER_PLAYER; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = gridLeft + col * cellSize;
                int y = gridTop + row * cellSize;
                int slotIndex = row * COLS + col;

                // slot background — selected hotbar cell uses the selected
                // sprite, everything else uses the empty sprite.
                boolean isSelectedHotbar = !blocked && row == 3 && col == selectedLocal;
                drawSlotCell(g2d, x, y, isSelectedHotbar);

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
        BufferedImage icon = itemIcons.get(item.getType().name().toLowerCase());
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

    /*
        Draws the hunger/thirst HUD above each player's hotbar. No-ops when
        the full inventory is open — the bars only belong on top of the
        hotbar layout (PLAYING and GAME_OVER), not when slot grids are
        spread across the screen. Geometry mirrors drawHotbar() exactly so
        the bars line up perfectly with the hotbar slots underneath.
     */
    public void drawStatusBars(Graphics2D g2d, int panelW, int panelH) {
        if (open) return; // hidden during INVENTORY_OPEN / E-toggle full grid

        int hotbarWidth = COLS * SLOT_SIZE + (COLS - 1) * SLOT_GAP;
        int hotbarY = panelH - SLOT_SIZE - 20;

        // The player name sits at hotbarY - 6. The bar stack ends just
        // above the name with a 4 px breather so it doesn't crowd the text.
        int barStackBottom = hotbarY - 6 - 12 - 4;

        int p1HotbarX = panelW / 2 - hotbarWidth - 120;
        int p2HotbarX = panelW / 2 + 120;

        if (player1 != null) {
            StatusBarRenderer.drawForPlayer(g2d, player1.getSurvivalStats(),
                    p1HotbarX, barStackBottom, hotbarWidth);
        }
        if (player2 != null) {
            StatusBarRenderer.drawForPlayer(g2d, player2.getSurvivalStats(),
                    p2HotbarX, barStackBottom, hotbarWidth);
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

            // slot background — selected uses crafting_slot_selected.png
            drawSlotCell(g2d, x, startY, col == selectedLocal);

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
        String[] names = {"wood", "stone", "banana", "coconut", "axe", "pickaxe",
                "rope", "vines", "plank", "paddle", "tropical_leaves", "mast", "frame", "sail", "rudder", "fittings"};
        for (String name : names) {
            BufferedImage img = loadImage("src/resources/items/item_" + name + ".png");
            if (img != null) {
                itemIcons.put(name, img);
            }
        }
        slotEmpty    = loadImage("src/resources/ui/crafting/crafting_slot_empty.png");
        slotSelected = loadImage("src/resources/ui/crafting/crafting_slot_selected.png");
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            return null;
        }
    }
}
