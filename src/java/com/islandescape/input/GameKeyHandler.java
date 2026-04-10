package com.islandescape.input;

import com.islandescape.utilities.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
    GameKeyHandler listens for keyboard input and exposes the current
    movement direction so the game loop can drive Player.move(Direction).
 */
public class GameKeyHandler implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Current movement direction derived from currently-held keys.
    public Direction getDirection() {

        return null;
    }
}
