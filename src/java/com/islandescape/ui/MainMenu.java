package com.islandescape.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MainMenu {

    public enum MenuOption {
        NEW_GAME,
        LOAD_GAME,
        MANUAL,
        QUIT
    }

    private int selectedIndex = 0;
    private boolean loadEnabled = true;

    // runnable interfaces for functions which will be called when the option is selected
    private Runnable onNewGame;
    private Runnable onLoadGame;
    private Runnable onManual;
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

        // do while to skip load game option if there are no saved game.
        // Math.floorMod handles the negative wraparound for moveUp from index 0.
        do {
            selectedIndex = Math.floorMod(selectedIndex + delta, n);
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
            case MANUAL:
                if (onManual != null) onManual.run();
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

    public void setOnManual(Runnable onManual) {
        this.onManual = onManual;
    }

    public void setOnQuit(Runnable onQuit) {
        this.onQuit = onQuit;
    }

    private static final Color DIM = new Color(0, 0, 0, 210);
    private static final Color TITLE_COLOR = new Color(255, 215, 80);
    private static final Color SELECTED_COLOR = new Color(255, 215, 80);
    private static final Color NORMAL_COLOR = new Color(230, 230, 230);
    private static final Color DISABLED_COLOR = new Color(120, 120, 120);

    private static final String TITLE_TEXT = "ISLAND ESCAPE";

    public void render(Graphics2D g, int screenW, int screenH) {
        g.setColor(DIM);
        g.fillRect(0, 0, screenW, screenH);

        g.setFont(new Font("SansSerif", Font.BOLD, 72));
        int titleW = g.getFontMetrics().stringWidth(TITLE_TEXT);
        g.setColor(TITLE_COLOR);
        g.drawString(TITLE_TEXT, screenW / 2 - titleW / 2, screenH / 3);

        MenuOption[] options = MenuOption.values();
        int optionFontSize = 36;
        int spacing = 60;
        int firstY = screenH / 2 + 20;

        for (int i = 0; i < options.length; i++) {
            MenuOption option = options[i];
            boolean isSelected = (i == selectedIndex);
            boolean isDisabledLoad = (option == MenuOption.LOAD_GAME) && !loadEnabled;

            Color color;
            int style;
            if (isDisabledLoad) {
                color = DISABLED_COLOR;
                style = Font.PLAIN;
            } else if (isSelected) {
                color = SELECTED_COLOR;
                style = Font.BOLD;
            } else {
                color = NORMAL_COLOR;
                style = Font.PLAIN;
            }

            g.setFont(new Font("SansSerif", style, optionFontSize));
            String label = labelFor(option);
            int w = g.getFontMetrics().stringWidth(label);
            g.setColor(color);
            g.drawString(label, screenW / 2 - w / 2, firstY + i * spacing);
        }
    }

    private static String labelFor(MenuOption option) {
        switch (option) {
            case NEW_GAME:  return "New Game";
            case LOAD_GAME: return "Load Game";
            case MANUAL:    return "Manual";
            case QUIT:      return "Quit";
            default:        return option.name();
        }
    }
}
