package com.islandescape.input;

import com.islandescape.utilities.Direction;
import com.islandescape.window.GamePanel;
import com.islandescape.window.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyHandler implements KeyListener {

    // P1 movement (WASD)
    private boolean isPressedW = false;
    private boolean isPressedA = false;
    private boolean isPressedS = false;
    private boolean isPressedD = false;

    // P2 movement (Arrow keys)
    private boolean isPressedUp = false;
    private boolean isPressedLeft = false;
    private boolean isPressedDown = false;
    private boolean isPressedRight = false;

    // Action / toggle edges (single-press: true for one poll cycle, then auto-cleared)
    private boolean p1ActionToggled = false;
    private boolean p2ActionToggled = false;
    private boolean craftScreenToggled = false;
    private boolean craftCommitted = false;

    private final GamePanel gamePanel;

    public GameKeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            // P1 movement
            case KeyEvent.VK_W: isPressedW = true; break;
            case KeyEvent.VK_A: isPressedA = true; break;
            case KeyEvent.VK_S: isPressedS = true; break;
            case KeyEvent.VK_D: isPressedD = true; break;
            // P2 movement
            case KeyEvent.VK_UP:    isPressedUp = true; break;
            case KeyEvent.VK_LEFT:  isPressedLeft = true; break;
            case KeyEvent.VK_DOWN:  isPressedDown = true; break;
            case KeyEvent.VK_RIGHT: isPressedRight = true; break;
            // P1/P2 action (interact + place-in-grid)
            case KeyEvent.VK_E:
                p1ActionToggled = true;
                // Only toggle inventory when crafting is NOT open
                // (when crafting is open, E is consumed as P1 action by the crafting screen)
                if (gamePanel.getGameState() != GameState.INVENTORY_OPEN) {
                    gamePanel.toggleInventoryScreen();
                }
                break;
            case KeyEvent.VK_SPACE: p2ActionToggled = true; break;
            // Crafting screen toggle / commit
            case KeyEvent.VK_I:     craftScreenToggled = true; break;
            case KeyEvent.VK_ENTER: craftCommitted = true; break;
        }

        // ESC closes whatever is open
        if (key == KeyEvent.VK_ESCAPE) {
            if (gamePanel.getGameState() == GameState.INVENTORY_OPEN) {
                gamePanel.closeCraftingScreen();
            } else if (gamePanel.isInventoryScreenOpen()) {
                gamePanel.toggleInventoryScreen();
            }
            return;
        }

        // Block hotbar keys while inventory/crafting is open
        if (gamePanel.isInventoryScreenOpen()) {
            return;
        }

        // Hotbar slot selection when inventory is closed
        // Player 1: keys 1-5
        if (key >= KeyEvent.VK_1 && key <= KeyEvent.VK_5 && gamePanel.getPlayer1() != null) {
            gamePanel.getPlayer1().getInventory().setSelectedHotBarSlot(15 + (key - KeyEvent.VK_1));
            return;
        }
        // Player 2: keys 6-0 (6=15, 7=16, 8=17, 9=18, 0=19)
        if (gamePanel.getPlayer2() != null) {
            if (key >= KeyEvent.VK_6 && key <= KeyEvent.VK_9) {
                gamePanel.getPlayer2().getInventory().setSelectedHotBarSlot(15 + (key - KeyEvent.VK_6));
                return;
            }
            if (key == KeyEvent.VK_0) {
                gamePanel.getPlayer2().getInventory().setSelectedHotBarSlot(19);
                return;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:     isPressedW = false; break;
            case KeyEvent.VK_A:     isPressedA = false; break;
            case KeyEvent.VK_S:     isPressedS = false; break;
            case KeyEvent.VK_D:     isPressedD = false; break;
            case KeyEvent.VK_UP:    isPressedUp = false; break;
            case KeyEvent.VK_LEFT:  isPressedLeft = false; break;
            case KeyEvent.VK_DOWN:  isPressedDown = false; break;
            case KeyEvent.VK_RIGHT: isPressedRight = false; break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    public Direction getP1Direction() {
        return new Direction(
                (isPressedD ? 1 : 0) - (isPressedA ? 1 : 0),
                (isPressedW ? 1 : 0) - (isPressedS ? 1 : 0));
    }

    public Direction getP2Direction() {
        return new Direction(
                (isPressedRight ? 1 : 0) - (isPressedLeft ? 1 : 0),
                (isPressedUp ? 1 : 0) - (isPressedDown ? 1 : 0));
    }

    // Kept for backwards compatibility with existing callers.
    public Direction getDirection() {
        return getP1Direction();
    }

    public boolean consumeCraftScreenToggle() {
        boolean val = craftScreenToggled;
        craftScreenToggled = false;
        return val;
    }

    public boolean consumeCraftCommit() {
        boolean val = craftCommitted;
        craftCommitted = false;
        return val;
    }

    public boolean consumeP1Action() {
        boolean val = p1ActionToggled;
        p1ActionToggled = false;
        return val;
    }

    public boolean consumeP2Action() {
        boolean val = p2ActionToggled;
        p2ActionToggled = false;
        return val;
    }
}
