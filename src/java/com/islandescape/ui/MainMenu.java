package com.islandescape.ui;

import java.awt.Graphics2D;

public class MainMenu {

    public enum MenuOption {
        NEW_GAME,
        LOAD_GAME,
        QUIT
    }

    private int selectedIndex = 0;
    private boolean loadEnabled = true;

    // runnable interfaces for functions which will be called when the option is selected
    private Runnable onNewGame;
    private Runnable onLoadGame;
    private Runnable onQuit;

    public MainMenu() {
    }

    public MenuOption getSelected() {
        return MenuOption.values()[selectedIndex];
    }

    public void moveUp() {
        changeOption(-1);
    }

    public void moveDown() {
        changeOption(1);
    }

    //
    private void changeOption(int delta) {
        MenuOption[] options = MenuOption.values();
        int n = options.length;

        // do while to skip load game option if there are no saved game
        do {
            selectedIndex = (selectedIndex + delta) % n;
        } while (!loadEnabled && options[selectedIndex] == MenuOption.LOAD_GAME);
    }

    public void confirm() {
        switch (getSelected()) {
            case NEW_GAME:
                if (onNewGame != null) onNewGame.run();
                break;
            case LOAD_GAME:
                if (loadEnabled && onLoadGame != null) onLoadGame.run();
                break;
            case QUIT:
                if (onQuit != null) onQuit.run();
                break;
        }
    }

    public void setLoadEnabled(boolean enabled) {
        this.loadEnabled = enabled;
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
