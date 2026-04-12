package com.islandescape.input;

import com.islandescape.utilities.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyHandler implements KeyListener {

    private boolean isPressedW = false;
    private boolean isPressedA = false;
    private boolean isPressedS = false;
    private boolean isPressedD = false;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                isPressedW = true;
                break;
            case KeyEvent.VK_A:
                isPressedA = true;
                break;
            case KeyEvent.VK_S:
                isPressedS = true;
                break;
            case KeyEvent.VK_D:
                isPressedD = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                isPressedW = false;
                break;
            case KeyEvent.VK_A:
                isPressedA = false;
                break;
            case KeyEvent.VK_S:
                isPressedS = false;
                break;
            case KeyEvent.VK_D:
                isPressedD = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    // Current movement direction derived from currently-held keys.
    public Direction getDirection() {
        return new Direction((isPressedD ? 1 : 0) - (isPressedA ? 1 : 0), (isPressedW ? 1 : 0) - (isPressedS ? 1 : 0));
    }
}
