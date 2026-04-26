package com.islandescape.input;

import com.islandescape.ui.MainMenuScreen;
import com.islandescape.window.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenuMouseHandler implements MouseListener {

    private final MainMenuScreen mainMenuScreen;
    private final GamePanel gamePanel;

    public MainMenuMouseHandler(MainMenuScreen mainMenuScreen, GamePanel gamePanel) {
        this.mainMenuScreen = mainMenuScreen;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int optionIndex = mainMenuScreen.getOptionAt(e.getX(), e.getY());
        if (optionIndex >= 0) {
            gamePanel.selectMainMenuOption(optionIndex);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
