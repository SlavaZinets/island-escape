package com.islandescape.ui;

import java.awt.Graphics2D;

public class MainMenu {

    public enum MenuOption {
        NEW_GAME,
        LOAD_GAME,
        QUIT
    }

    private int selectedIndex;
    private boolean loadEnabled;

    private Runnable onNewGame;
    private Runnable onLoadGame;
    private Runnable onQuit;

    public MainMenu() {
        // stub
    }

    public MenuOption getSelected() {
        return null;
    }

    public void moveUp() {
        // stub
    }

    public void moveDown() {
        // stub
    }

    public void confirm() {

    }

    public void setLoadEnabled(boolean enabled) {

    }

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public void setOnNewGame(Runnable onNewGame) {
        this.onNewGame = onNewGame;
    }

    public void setOnLoadGame(Runnable onLoadGame) {
        this.onLoadGame = onLoadGame;
    }

    public void setOnQuit(Runnable onQuit) {
        this.onQuit = onQuit;
    }

    public void render(Graphics2D g, int screenW, int screenH) {

    }
}
