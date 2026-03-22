package com.islandescape.core;

import javax.swing.JFrame;
import java.awt.Dimension;

/*
    Sets up the window with a fixed size, title, and close behavior.
 */
public class GameWindow extends JFrame {

    private final int windowWidth;
    private final int windowHeight;

    public GameWindow(String title, int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;

        setTitle(title);
        setPreferredSize(new Dimension(width, height));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // center on screen
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }
}
