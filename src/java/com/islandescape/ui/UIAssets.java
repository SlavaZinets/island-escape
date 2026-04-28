package com.islandescape.ui;

import com.islandescape.item.ItemType;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Loads and holds all sprite assets used by the crafting / inventory UI.
 * Constructed once and shared across screens.
 */
public class UIAssets {

    private final BufferedImage panelBg;
    private final BufferedImage slotEmpty;
    private final BufferedImage slotSelected;
    private final BufferedImage arrow;
    private final BufferedImage resultFrame;
    private final Map<ItemType, BufferedImage> itemIcons;

    public UIAssets() {
        String base = "src/resources/ui/crafting/";
        panelBg      = loadImage(base + "crafting_panel_bg-removebg-preview.png");
        slotEmpty    = loadImage(base + "crafting_slot_empty.png");
        slotSelected = loadImage(base + "crafting_slot_selected.png");
        arrow        = loadImage(base + "crafting_arrow.png");
        resultFrame  = loadImage(base + "crafting_result_frame.png");

        itemIcons = loadItemIcons();
    }

    private Map<ItemType, BufferedImage> loadItemIcons() {
        EnumMap<ItemType, BufferedImage> icons = new EnumMap<>(ItemType.class);
        String itemBase = "src/resources/items/";

        for (ItemType type : ItemType.values()) {
            String fileName = "item_" + type.name().toLowerCase() + ".png";
            BufferedImage icon = loadImage(itemBase + fileName);
            // PLANK enum now matches file name, no fallback needed
            if (icon != null) {
                icons.put(type, icon);
            }
        }

        return Collections.unmodifiableMap(icons);
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            return null;
        }
    }

    public BufferedImage getPanelBg()     { return panelBg; }

    // Draws a brown rounded panel — the shared panel background used by
    // crafting, boat-repair, and inventory screens.
    public void drawPanel(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(139, 119, 82));
        g.fillRoundRect(x, y, w, h, 20, 20);
        g.setColor(new Color(101, 67, 33));
        g.drawRoundRect(x, y, w, h, 20, 20);
    }

    public BufferedImage getSlotEmpty()   { return slotEmpty; }
    public BufferedImage getSlotSelected(){ return slotSelected; }
    public BufferedImage getArrow()       { return arrow; }
    public BufferedImage getResultFrame() { return resultFrame; }
    public BufferedImage getItemIcon(ItemType type) { return itemIcons.get(type); }
}
