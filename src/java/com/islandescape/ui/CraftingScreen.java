package com.islandescape.ui;

import com.islandescape.crafting.CraftingRecipe;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CraftingScreen {

    private static final Color P1_CURSOR_COLOR = new Color(50, 150, 255, 120);
    private static final Color P2_CURSOR_COLOR = new Color(255, 100, 50, 120);
    private static final Color TEXT_COLOR = new Color(60, 40, 20);
    private static final Color TEXT_DISABLED = new Color(150, 130, 110);
    private static final Color OVERLAY_DIM = new Color(0, 0, 0, 150);
    private static final Color OVERLAY_DISABLED = new Color(0, 0, 0, 140);

    private final UIAssets assets;
    private final SlotRenderer slotRenderer;
    private final PlayerCursor p1Cursor;
    private final PlayerCursor p2Cursor;

    public CraftingScreen() {
        this.assets = new UIAssets();
        this.slotRenderer = new SlotRenderer(assets);
        this.p1Cursor = new PlayerCursor(
                CraftingScreenLayout.INV_COLS, CraftingScreenLayout.INV_ROWS,
                CraftingScreenLayout.GRID_COLS, CraftingScreenLayout.GRID_ROWS);
        this.p2Cursor = new PlayerCursor(
                CraftingScreenLayout.INV_COLS, CraftingScreenLayout.INV_ROWS,
                CraftingScreenLayout.GRID_COLS, CraftingScreenLayout.GRID_ROWS);
    }

    // --- Input handling ---

    public void handleInput(boolean p1Action, boolean p2Action,
                            int p1DirX, int p1DirY, int p2DirX, int p2DirY,
                            boolean p1InRange, boolean p2InRange,
                            CraftingSystem cs, Inventory p1Inv, Inventory p2Inv) {
        if (p1InRange) {
            p1Cursor.move(p1DirX, p1DirY);
            if (p1Action) handleAction(p1Cursor, cs, p1Inv, 0);
        }
        if (p2InRange) {
            p2Cursor.move(p2DirX, p2DirY);
            if (p2Action) handleAction(p2Cursor, cs, p2Inv, 1);
        }
    }

    private void handleAction(PlayerCursor cursor, CraftingSystem cs,
                               Inventory inv, int playerId) {
        if (inv == null || cs == null) return;

        if (cursor.isOnInventory()) {
            // Move item from inventory to first empty grid slot
            ArrayList<Item> snapshot = inv.snapshot();
            if (cursor.getIndex() < snapshot.size()) {
                int emptySlot = cs.firstEmptySlot();
                if (emptySlot >= 0) {
                    Item removed = inv.extract(snapshot.get(cursor.getIndex()).getType(), 1);
                    if (removed != null) {
                        cs.placeIn(emptySlot, removed, playerId);
                    }
                }
            }
        } else {
            // Return grid item to player inventory
            Item taken = cs.takeOut(cursor.getIndex());
            if (taken != null) {
                inv.add(taken);
            }
        }
    }

    // --- Rendering ---

    public void render(Graphics2D g, int screenW, int screenH,
                       CraftingSystem cs, Inventory p1Inv, Inventory p2Inv,
                       boolean p1InRange, boolean p2InRange) {

        CraftingScreenLayout layout = new CraftingScreenLayout(screenW, screenH);

        drawBackground(g, screenW, screenH, layout);
        drawTitle(g, layout);
        drawCraftingGrid(g, layout, cs);
        drawArrow(g, layout);
        drawResultPreview(g, layout, cs);
        drawPlayerInventory(g, layout.p1InvX, layout.invY, p1Inv, p1InRange, "P1", layout);
        drawPlayerInventory(g, layout.p2InvX, layout.invY, p2Inv, p2InRange, "P2", layout);
        drawCursors(g, layout, p1InRange, p2InRange);
        drawHint(g, layout);
    }

    private void drawBackground(Graphics2D g, int screenW, int screenH,
                                 CraftingScreenLayout layout) {
        g.setColor(OVERLAY_DIM);
        g.fillRect(0, 0, screenW, screenH);

        BufferedImage bg = assets.getPanelBg();
        if (bg != null) {
            g.drawImage(bg, layout.panelX, layout.panelY, layout.panelW, layout.panelH, null);
        } else {
            g.setColor(new Color(139, 119, 82));
            g.fillRoundRect(layout.panelX, layout.panelY, layout.panelW, layout.panelH, 20, 20);
            g.setColor(new Color(101, 67, 33));
            g.drawRoundRect(layout.panelX, layout.panelY, layout.panelW, layout.panelH, 20, 20);
        }
    }

    private void drawTitle(Graphics2D g, CraftingScreenLayout layout) {
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.setColor(TEXT_COLOR);
        String title = "Crafting";
        int titleW = g.getFontMetrics().stringWidth(title);
        g.drawString(title, layout.centerX - titleW / 2, layout.titleY);
    }

    private void drawCraftingGrid(Graphics2D g, CraftingScreenLayout layout, CraftingSystem cs) {
        for (int row = 0; row < CraftingScreenLayout.GRID_ROWS; row++) {
            for (int col = 0; col < CraftingScreenLayout.GRID_COLS; col++) {
                int slotX = layout.craftingStartX + col * (layout.slotSize + layout.slotGap);
                int slotY = layout.gridY + row * (layout.slotSize + layout.slotGap);

                slotRenderer.drawSlot(g, slotX, slotY, layout.slotSize);

                if (cs != null) {
                    Item item = cs.getSlot(row * CraftingScreenLayout.GRID_COLS + col);
                    if (item != null) {
                        int pad = layout.slotSize / 16 + 2;
                        slotRenderer.drawItemIcon(g, item.getType(),
                                slotX + pad, slotY + pad,
                                layout.slotSize - pad * 2, item.getQuantity());
                    }
                }
            }
        }
    }

    private void drawArrow(Graphics2D g, CraftingScreenLayout layout) {
        BufferedImage arrow = assets.getArrow();
        if (arrow != null) {
            g.drawImage(arrow, layout.arrowX, layout.arrowY, layout.arrowW, layout.arrowW, null);
        } else {
            g.setColor(new Color(200, 160, 50));
            g.fillPolygon(
                    new int[]{layout.arrowX, layout.arrowX + layout.arrowW, layout.arrowX},
                    new int[]{layout.arrowY, layout.arrowY + layout.arrowW / 2, layout.arrowY + layout.arrowW},
                    3);
        }
    }

    private void drawResultPreview(Graphics2D g, CraftingScreenLayout layout, CraftingSystem cs) {
        BufferedImage frame = assets.getResultFrame();
        if (frame != null) {
            g.drawImage(frame, layout.resultX, layout.resultY,
                    layout.resultSlotSize, layout.resultSlotSize, null);
        } else {
            g.setColor(new Color(80, 60, 40));
            g.fillRect(layout.resultX, layout.resultY,
                    layout.resultSlotSize, layout.resultSlotSize);
            g.setColor(new Color(180, 140, 60));
            g.drawRect(layout.resultX, layout.resultY,
                    layout.resultSlotSize, layout.resultSlotSize);
        }

        CraftingRecipe preview = cs != null ? cs.preview() : null;
        if (preview != null) {
            int iconPad = (layout.resultSlotSize - layout.slotSize) / 2;
            slotRenderer.drawItemIcon(g, preview.getOutput(),
                    layout.resultX + iconPad, layout.resultY + iconPad,
                    layout.slotSize, preview.getOutputQty());
        }
    }

    private void drawPlayerInventory(Graphics2D g, int x, int y, Inventory inv,
                                      boolean inRange, String label,
                                      CraftingScreenLayout layout) {
        // Label
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(inRange ? TEXT_COLOR : TEXT_DISABLED);
        g.drawString(label + " Inventory", x, y + 14);
        int slotsY = y + layout.invLabelHeight;

        ArrayList<Item> items = inv != null ? inv.snapshot() : new ArrayList<>();

        for (int row = 0; row < CraftingScreenLayout.INV_ROWS; row++) {
            for (int col = 0; col < CraftingScreenLayout.INV_COLS; col++) {
                int slotX = x + col * (layout.slotSize + layout.slotGap);
                int slotY = slotsY + row * (layout.slotSize + layout.slotGap);
                int index = row * CraftingScreenLayout.INV_COLS + col;

                slotRenderer.drawSlot(g, slotX, slotY, layout.slotSize);

                if (index < items.size()) {
                    Item item = items.get(index);
                    int pad = layout.slotSize / 16 + 2;
                    slotRenderer.drawItemIcon(g, item.getType(),
                            slotX + pad, slotY + pad,
                            layout.slotSize - pad * 2, item.getQuantity());
                }
            }
        }

        // Grey-out overlay when player not in range
        if (!inRange) {
            int totalW = CraftingScreenLayout.INV_COLS * layout.slotSize
                    + (CraftingScreenLayout.INV_COLS - 1) * layout.slotGap;
            int totalH = CraftingScreenLayout.INV_ROWS * layout.slotSize
                    + (CraftingScreenLayout.INV_ROWS - 1) * layout.slotGap;
            g.setColor(OVERLAY_DISABLED);
            g.fillRect(x, slotsY, totalW, totalH);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.setColor(new Color(200, 200, 200));
            String msg = label + " not at table";
            int msgW = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, x + totalW / 2 - msgW / 2, slotsY + totalH / 2 + 5);
        }
    }

    private void drawCursors(Graphics2D g, CraftingScreenLayout layout,
                              boolean p1InRange, boolean p2InRange) {
        // Grid cursors
        if (p1InRange && p1Cursor.isOnGrid()) {
            slotRenderer.drawCursorHighlight(g, layout.craftingStartX, layout.gridY,
                    p1Cursor.getIndex(), CraftingScreenLayout.GRID_COLS,
                    P1_CURSOR_COLOR, layout.slotSize, layout.slotGap);
        }
        if (p2InRange && p2Cursor.isOnGrid()) {
            slotRenderer.drawCursorHighlight(g, layout.craftingStartX, layout.gridY,
                    p2Cursor.getIndex(), CraftingScreenLayout.GRID_COLS,
                    P2_CURSOR_COLOR, layout.slotSize, layout.slotGap);
        }

        // Inventory cursors
        int invSlotsY = layout.invY + layout.invLabelHeight;
        if (p1InRange && p1Cursor.isOnInventory()) {
            slotRenderer.drawCursorHighlight(g, layout.p1InvX, invSlotsY,
                    p1Cursor.getIndex(), CraftingScreenLayout.INV_COLS,
                    P1_CURSOR_COLOR, layout.slotSize, layout.slotGap);
        }
        if (p2InRange && p2Cursor.isOnInventory()) {
            slotRenderer.drawCursorHighlight(g, layout.p2InvX, invSlotsY,
                    p2Cursor.getIndex(), CraftingScreenLayout.INV_COLS,
                    P2_CURSOR_COLOR, layout.slotSize, layout.slotGap);
        }
    }

    private void drawHint(Graphics2D g, CraftingScreenLayout layout) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(TEXT_COLOR);
        String hint = "P1: WASD + E  |  P2: Arrows + Space  |  Enter: Craft  |  I: Close";
        int hintW = g.getFontMetrics().stringWidth(hint);
        g.drawString(hint, layout.centerX - hintW / 2, layout.hintY);
    }
}
