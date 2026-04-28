package com.islandescape.ui;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Visual render for the boat-repair panel. Mirrors the structure of
 * {@link CraftingScreen} — dim overlay, brown panel, title, grid row,
 * extra visuals (progress bar + "press B" prompt), hint line.
 *
 * <p>No keyboard cursor, no result slot. Partial and completed slots look
 * identical on purpose: completion is enforced functionally by
 * {@link BoatRepairSystem#placeIn}/{@link BoatRepairSystem#takeOut}.
 */
public class BoatRepairScreen {

    private static final Color TEXT_COLOR = new Color(60, 40, 20);
    private static final Color OVERLAY_DIM = new Color(0, 0, 0, 150);
    private static final Color REQ_PLACEHOLDER = new Color(255, 255, 255, 60);
    private static final Color PROGRESS_TRACK = new Color(80, 60, 40);
    private static final Color PROGRESS_FILL = new Color(90, 170, 90);
    private static final Color PROGRESS_BORDER = new Color(40, 25, 10);
    private static final Color PROMPT_COLOR = new Color(35, 90, 35);

    private final UIAssets assets;
    private final SlotRenderer slotRenderer;

    public BoatRepairScreen() {
        this.assets = new UIAssets();
        this.slotRenderer = new SlotRenderer(assets);
    }

    public void render(Graphics2D g, int screenW, int screenH, BoatRepairSystem brs) {
        BoatRepairLayout layout = new BoatRepairLayout(screenW, screenH);

        drawBackground(g, screenW, screenH, layout);
        drawTitle(g, layout);
        drawRepairSlots(g, layout, brs);
        drawProgressBar(g, layout, brs);
        drawBoardPrompt(g, layout, brs);
        drawHint(g, layout);
    }

    private void drawBackground(Graphics2D g, int screenW, int screenH, BoatRepairLayout layout) {
        g.setColor(OVERLAY_DIM);
        g.fillRect(0, 0, screenW, screenH);
        assets.drawPanel(g, layout.panelX, layout.panelY, layout.panelW, layout.panelH);
    }

    private void drawTitle(Graphics2D g, BoatRepairLayout layout) {
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.setColor(TEXT_COLOR);
        String title = "REPAIR THE BOAT";
        int titleW = g.getFontMetrics().stringWidth(title);
        g.drawString(title, layout.centerX - titleW / 2, layout.titleY);
    }

    private void drawRepairSlots(Graphics2D g, BoatRepairLayout layout, BoatRepairSystem brs) {
        if (brs == null) return;

        int cellSize = layout.slotSize + layout.slotGap;

        for (int i = 0; i < BoatRepairLayout.SLOT_COUNT; i++) {
            int slotX = layout.repairSlotsStartX + i * cellSize;
            int slotY = layout.repairSlotsY;

            // 1. Empty slot background.
            slotRenderer.drawSlot(g, slotX, slotY, layout.slotSize);

            Item deposited = brs.getSlot(i);
            ItemType required = brs.getRequiredType(i);
            int requiredQty = brs.getRequiredQty(i);

            if (deposited != null) {
                // 2a. Something deposited — draw the real item icon + current count.
                int pad = layout.slotSize / 16 + 2;
                slotRenderer.drawItemIcon(
                        g, deposited.getType(),
                        slotX + pad, slotY + pad,
                        layout.slotSize - pad * 2,
                        deposited.getQuantity());

                // Draw the "current / required" label just below the slot.
                drawSlotLabel(g, slotX, slotY, layout.slotSize,
                        deposited.getQuantity() + "/" + requiredQty);
            } else {
                // 2b. Empty slot — faint ghost icon + required qty hint.
                BufferedImage icon = assets.getItemIcon(required);
                int pad = layout.slotSize / 16 + 2;
                int iconSize = layout.slotSize - pad * 2;
                if (icon != null) {
                    Composite old = g.getComposite();
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                    g.drawImage(icon, slotX + pad, slotY + pad, iconSize, iconSize, null);
                    g.setComposite(old);
                } else {
                    // Fallback: a pale rounded rect + type abbreviation.
                    g.setColor(REQ_PLACEHOLDER);
                    g.fillRoundRect(slotX + pad + 4, slotY + pad + 4,
                            iconSize - 8, iconSize - 8, 8, 8);
                    g.setColor(TEXT_COLOR);
                    g.setFont(new Font("SansSerif", Font.BOLD, 10));
                    String abbrev = required.name().length() > 4
                            ? required.name().substring(0, 4)
                            : required.name();
                    int strW = g.getFontMetrics().stringWidth(abbrev);
                    g.drawString(abbrev,
                            slotX + layout.slotSize / 2 - strW / 2,
                            slotY + layout.slotSize / 2 + 4);
                }

                // Required quantity hint label.
                drawSlotLabel(g, slotX, slotY, layout.slotSize, "0/" + requiredQty);
            }
        }
    }

    private void drawSlotLabel(Graphics2D g, int slotX, int slotY, int slotSize, String label) {
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        g.setColor(TEXT_COLOR);
        int labelW = g.getFontMetrics().stringWidth(label);
        g.drawString(label, slotX + slotSize / 2 - labelW / 2, slotY + slotSize + 14);
    }

    private void drawProgressBar(Graphics2D g, BoatRepairLayout layout, BoatRepairSystem brs) {
        // Track
        g.setColor(PROGRESS_TRACK);
        g.fillRoundRect(layout.progressBarX, layout.progressBarY,
                layout.progressBarW, layout.progressBarH, 6, 6);

        // Fill
        double progress = brs != null ? brs.getProgress() : 0.0;
        if (progress > 0.0) {
            int fillW = (int) Math.round(layout.progressBarW * progress);
            g.setColor(PROGRESS_FILL);
            g.fillRoundRect(layout.progressBarX, layout.progressBarY,
                    fillW, layout.progressBarH, 6, 6);
        }

        // Border
        g.setColor(PROGRESS_BORDER);
        g.drawRoundRect(layout.progressBarX, layout.progressBarY,
                layout.progressBarW, layout.progressBarH, 6, 6);

        // Percentage label, centered over the bar
        int pct = (int) Math.round(progress * 100);
        String pctStr = pct + "%";
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(TEXT_COLOR);
        int pctW = g.getFontMetrics().stringWidth(pctStr);
        g.drawString(pctStr,
                layout.progressBarX + layout.progressBarW / 2 - pctW / 2,
                layout.progressBarY + layout.progressBarH - 4);
    }

    private void drawBoardPrompt(Graphics2D g, BoatRepairLayout layout, BoatRepairSystem brs) {
        if (brs == null || !brs.isFullyRepaired()) return;

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(PROMPT_COLOR);
        String prompt = "Press B to board";
        int promptW = g.getFontMetrics().stringWidth(prompt);
        // Just below the progress bar, above the inventory grids.
        int y = layout.progressBarY + layout.progressBarH + 22;
        g.drawString(prompt, layout.centerX - promptW / 2, y);
    }

    private void drawHint(Graphics2D g, BoatRepairLayout layout) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(TEXT_COLOR);
        String hint = "Left-click: place  |  Right-click: place one  |  "
                + "B: Board (when ready)  |  R / Esc: Close";
        int hintW = g.getFontMetrics().stringWidth(hint);
        g.drawString(hint, layout.centerX - hintW / 2, layout.hintY);
    }
}
