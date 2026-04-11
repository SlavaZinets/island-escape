package com.islandescape.player;

import com.islandescape.inventory.Inventory;
import com.islandescape.utilities.Direction;

import java.awt.Point;

public class Player {

    private String name;
    private int id;
    private Point position;
    private final double SPEED = 2;
    private final Inventory inventory = new Inventory();

    public Player(String name, int id, int x, int y) {
        this.name = name;
        this.id = id;
        this.position = new Point(x, y);
    }

    public void move (Direction direction) {
        position.setLocation(position.getX() + direction.getX() * SPEED, position.getY() + direction.getY() * SPEED);
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

    public Inventory getInventory() {
        return inventory;
    }
}
