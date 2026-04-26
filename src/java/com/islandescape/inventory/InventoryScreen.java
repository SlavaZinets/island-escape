package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.player.Player;

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

    private boolean open;
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

        // determine player by X position
        boolean isPlayer1 = screenX < xBorder;
        Inventory inventory = isPlayer1 ? player1.getInventory() : player2.getInventory();
        int gridLeft = isPlayer1 ? getP1Left(panelW) : getP2Left(panelW);

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
    int getGridTop(int panelH) {
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

    private void drawFullInventory(Graphics2D g2d, int panelW, int panelH) {
        int p1Left = getP1Left(panelW);
        int p2Left = getP2Left(panelW);
        int gridTop = getGridTop(panelH);

        // Player 1 label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(player1.getName(), p1Left, gridTop - 8);

        // Player 1 grid
        drawPlayerGrid(g2d, player1.getInventory(), p1Left, gridTop);

        // Vertical separator
        int xBorder = getXBorder(panelW);
        g2d.setColor(new Color(200, 200, 200, 150));
        g2d.fillRect(xBorder - 1, gridTop, 2, getPlayerGridHeight());

        // Player 2 label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(player2.getName(), p2Left, gridTop - 8);

        // Player 2 grid
        drawPlayerGrid(g2d, player2.getInventory(), p2Left, gridTop);

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
        g2d.setColor(new Color(60, 60, 60, 200));
        g2d.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);
        g2d.setColor(new Color(120, 120, 120));
        g2d.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 8, 8);

        // contents
        Item item = trashBin.getItem();
        if (item != null) {
            drawItem(g2d, item, x, y);
        }
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
