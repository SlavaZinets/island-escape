package com.islandescape.input;

import com.islandescape.utilities.Direction;

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

    // Action keys (hold-style)
    private boolean isPressedE = false;
    private boolean isPressedSpace = false;

    // Toggle keys (single-press: true for one poll cycle, then auto-cleared)
    private boolean inventoryToggled = false;
    private boolean craftCommitted = false;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            // P1 movement
            case KeyEvent.VK_W:     isPressedW = true; break;
            case KeyEvent.VK_A:     isPressedA = true; break;
            case KeyEvent.VK_S:     isPressedS = true; break;
            case KeyEvent.VK_D:     isPressedD = true; break;
            // P2 movement
            case KeyEvent.VK_UP:    isPressedUp = true; break;
            case KeyEvent.VK_LEFT:  isPressedLeft = true; break;
            case KeyEvent.VK_DOWN:  isPressedDown = true; break;
            case KeyEvent.VK_RIGHT: isPressedRight = true; break;
            // Actions
            case KeyEvent.VK_E:     isPressedE = true; break;
            case KeyEvent.VK_SPACE: isPressedSpace = true; break;
            // Toggles (single-press)
            case KeyEvent.VK_I:     inventoryToggled = true; break;
            case KeyEvent.VK_ENTER: craftCommitted = true; break;
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
            case KeyEvent.VK_E:     isPressedE = false; break;
            case KeyEvent.VK_SPACE: isPressedSpace = false; break;
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

    public boolean isP1Action() {
        return isPressedE;
    }

    public boolean isP2Action() {
        return isPressedSpace;
    }

    public boolean consumeInventoryToggle() {
        boolean val = inventoryToggled;
        inventoryToggled = false;
        return val;
    }

    public boolean consumeCraftCommit() {
        boolean val = craftCommitted;
        craftCommitted = false;
        return val;
    }
}
