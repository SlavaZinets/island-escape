package com.islandescape.ui;

import com.islandescape.item.ItemType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Reusable helpers for drawing inventory/crafting slots, item icons, and cursor highlights.
 */
public class SlotRenderer {

    private final UIAssets assets;

    public SlotRenderer(UIAssets assets) {
        this.assets = assets;
    }

    public void drawSlot(Graphics2D g, int x, int y, int size) {
        BufferedImage sprite = assets.getSlotEmpty();
        if (sprite != null) {
            g.drawImage(sprite, x, y, size, size, null);
        } else {
            g.setColor(new Color(60, 50, 40));
            g.fillRect(x, y, size, size);
            g.setColor(new Color(120, 100, 60));
            g.drawRect(x, y, size, size);
        }
    }

    public void drawItemIcon(Graphics2D g, ItemType type, int x, int y, int size, int qty) {
        BufferedImage icon = assets.getItemIcon(type);
        if (icon != null) {
            g.drawImage(icon, x, y, size, size, null);
        } else {
            g.setColor(colorFor(type));
            g.fillRoundRect(x + 4, y + 4, size - 8, size - 8, 8, 8);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            String abbrev = type.name().length() > 4 ? type.name().substring(0, 4) : type.name();
            int strW = g.getFontMetrics().stringWidth(abbrev);
            g.drawString(abbrev, x + size / 2 - strW / 2, y + size / 2 + 4);
        }

        if (qty > 1) {
            drawQuantityBadge(g, x, y, size, qty);
        }
    }

    public void drawCursorHighlight(Graphics2D g, int gridX, int gridY, int index,
                                     int cols, Color color, int slotSize, int slotGap) {
        int col = index % cols;
        int row = index / cols;
        int slotX = gridX + col * (slotSize + slotGap);
        int slotY = gridY + row * (slotSize + slotGap);

        BufferedImage selected = assets.getSlotSelected();
        if (selected != null) {
            Composite old = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g.drawImage(selected, slotX, slotY, slotSize, slotSize, null);
            g.setComposite(old);
        }

        g.setColor(color);
        g.setStroke(new BasicStroke(3));
        g.drawRect(slotX - 1, slotY - 1, slotSize + 2, slotSize + 2);
        g.setStroke(new BasicStroke(1));
    }

    private void drawQuantityBadge(Graphics2D g, int x, int y, int size, int qty) {
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        String qtyStr = String.valueOf(qty);
        int qw = g.getFontMetrics().stringWidth(qtyStr);
        int qx = x + size - qw - 2;
        int qy = y + size - 4;
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(qx - 2, qy - 11, qw + 4, 14, 4, 4);
        g.setColor(Color.WHITE);
        g.drawString(qtyStr, qx, qy);
    }

    private static Color colorFor(ItemType type) {
        switch (type) {
            case WOOD:             return new Color(139, 90, 43);
            case STONE:            return new Color(130, 130, 130);
            case VINES:            return new Color(34, 139, 34);
            case TROPICAL_LEAVES:  return new Color(0, 180, 60);
            case ROPE:             return new Color(194, 178, 128);
            case PLANKS:           return new Color(210, 170, 100);
            case AXE:              return new Color(100, 100, 160);
            case PICKAXE:          return new Color(120, 120, 170);
            case PADDLE:           return new Color(180, 140, 80);
            case FRAME:            return new Color(160, 120, 60);
            case MAST:             return new Color(170, 130, 70);
            case RUDDER:           return new Color(110, 90, 60);
            case FITTINGS:         return new Color(150, 150, 160);
            default:               return new Color(100, 100, 100);
        }
    }
}
