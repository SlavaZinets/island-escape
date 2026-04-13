package com.islandescape.ui;

import com.islandescape.crafting.CraftingRecipe;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;

import java.awt.*;
import java.awt.image.BufferedImage;

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
                CraftingScreenLayout.GRID_COLS, CraftingScreenLayout.GRID_ROWS,
                CraftingScreenLayout.GRID_COLS, CraftingScreenLayout.GRID_ROWS);
        this.p2Cursor = new PlayerCursor(
                CraftingScreenLayout.GRID_COLS, CraftingScreenLayout.GRID_ROWS,
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

        // Return grid item to player inventory
        Item taken = cs.takeOut(cursor.getIndex());
        if (taken != null) {
            inv.add(taken);
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

    }

    private void drawHint(Graphics2D g, CraftingScreenLayout layout) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(TEXT_COLOR);
        String hint = "P1: WASD + E  |  P2: Arrows + Space  |  Enter: Craft  |  I: Close";
        int hintW = g.getFontMetrics().stringWidth(hint);
        g.drawString(hint, layout.centerX - hintW / 2, layout.hintY);
    }
}
