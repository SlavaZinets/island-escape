package com.islandescape.input;

import com.islandescape.core.GamePanel;
import com.islandescape.inventory.InventoryScreen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InventoryMouseHandler extends MouseAdapter {

    private final InventoryScreen inventoryScreen;
    private final GamePanel gamePanel;

    public InventoryMouseHandler(InventoryScreen inventoryScreen, GamePanel gamePanel) {
        this.inventoryScreen = inventoryScreen;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!inventoryScreen.isOpen()) return;

        boolean isLeftClick = e.getButton() == MouseEvent.BUTTON1;
        boolean isRightClick = e.getButton() == MouseEvent.BUTTON3;
        if (!isLeftClick && !isRightClick) return;

        // inventory is drawn in screen/panel space, so use raw screen coords
        inventoryScreen.handleClick(e.getX(), e.getY(), isLeftClick,
                gamePanel.getWidth(), gamePanel.getHeight());
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
