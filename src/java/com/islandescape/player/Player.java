package com.islandescape.player;

import com.islandescape.utilities.Direction;

import java.awt.*;

public class Player {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 100;

    private String name;
    private int id;
    private Point position;
    private final double SPEED = 2;

    public Player(String name, int id, int x, int y) {
        this.name = name;
        this.id = id;
        this.position = new Point(x, y);
    }

    public void move (Direction direction) {
        // Direction uses world-space convention (+y = up), but Swing screen coords grow downward, so flip y.
        position.setLocation(position.getX() + direction.getX() * SPEED, position.getY() - direction.getY() * SPEED);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void renderPlayer(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect((int) position.getX(), (int) position.getY(), WIDTH, HEIGHT);
    }

}
