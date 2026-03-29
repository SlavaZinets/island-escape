package com.islandescape.core;

import javax.swing.JFrame;

/*
    Sets up a fullscreen undecorated window with title and close behavior.
 */
public class GameWindow extends JFrame {

    public GameWindow(String title) {
        setTitle(title);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
