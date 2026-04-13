package com.islandescape.ui;

import com.islandescape.crafting.CraftingRecipe;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CraftingScreen {

    private static final Color TEXT_COLOR = new Color(60, 40, 20);
    private static final Color OVERLAY_DIM = new Color(0, 0, 0, 150);

    private final UIAssets assets;
    private final SlotRenderer slotRenderer;

    public CraftingScreen() {
        this.assets = new UIAssets();
        this.slotRenderer = new SlotRenderer(assets);
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

    private void drawHint(Graphics2D g, CraftingScreenLayout layout) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(TEXT_COLOR);
        String hint = "Left-click: place/take items  |  Right-click: place/take one  |  I / Esc: Close";
        int hintW = g.getFontMetrics().stringWidth(hint);
        g.drawString(hint, layout.centerX - hintW / 2, layout.hintY);
    }
}
