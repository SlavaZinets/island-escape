package com.islandescape.input;

import com.islandescape.utilities.Direction;
import com.islandescape.window.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
    GameKeyHandler listens for keyboard input and exposes the current
    movement direction so the game loop can drive Player.move(Direction).
    Convention: up is +y, down is -y, right is +x, left is -x.
 */
public class GameKeyHandler implements KeyListener {

    private boolean isPressedW = false;
    private boolean isPressedA = false;
    private boolean isPressedS = false;
    private boolean isPressedD = false;

    private final GamePanel gamePanel;

    public GameKeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
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
            case KeyEvent.VK_E:
                gamePanel.toggleInventoryScreen();
                break;
        }
        // if inventory is open, only ESC closes it
        if (gamePanel.isInventoryScreenOpen()) {
            if (key == KeyEvent.VK_ESCAPE) {
                gamePanel.toggleInventoryScreen();
            }
            return;
        }

        // hotbar slot selection when inventory is closed
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
