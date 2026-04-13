package com.islandescape.input;

import com.islandescape.inventory.InventoryScreen;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InventoryMouseHandler extends MouseAdapter {

    private final InventoryScreen inventoryScreen;
    private final JPanel panel;

    public InventoryMouseHandler(InventoryScreen inventoryScreen, JPanel panel) {
        this.inventoryScreen = inventoryScreen;
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!inventoryScreen.isOpen()) return;

        boolean isLeftClick = e.getButton() == MouseEvent.BUTTON1;
        boolean isRightClick = e.getButton() == MouseEvent.BUTTON3;
        if (!isLeftClick && !isRightClick) return;

        inventoryScreen.handleClick(e.getX(), e.getY(), isLeftClick,
                panel.getWidth(), panel.getHeight());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        inventoryScreen.updateMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        inventoryScreen.updateMouse(e.getX(), e.getY());
    }
}
