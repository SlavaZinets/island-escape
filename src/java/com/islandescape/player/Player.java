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

    private int worldWidth = 0;
    private int worldHeight = 0;

    private Facing facing = Facing.SOUTH;
    private int animationTick = 0;
    private PlayerSprite sprite = null;

    public Player(String name, int id, int x, int y) {
        this.name = name;
        this.id = id;
        this.position = new Point(x, y);
    }

    public void move (Direction direction) {
        Facing newFacing = Facing.fromDirection(direction);
        if (newFacing != null) {
            facing = newFacing;
            animationTick++;
        }

        double newX = position.getX() + direction.getX() * SPEED;
        double newY = position.getY() - direction.getY() * SPEED;

        if (worldWidth > 0 && worldHeight > 0) {
            newX = Math.max(0, Math.min(newX, worldWidth - WIDTH));
            newY = Math.max(0, Math.min(newY, worldHeight - HEIGHT));
        }

        position.setLocation(newX, newY);
    }

    public void setWorldBounds(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
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

    public Facing getFacing() {
        return facing;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public int getFrameIndex() {
        return 0;
    }

    public void setSprite(PlayerSprite sprite) {
        this.sprite = sprite;
    }

    public void renderPlayer(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect((int) position.getX(), (int) position.getY(), WIDTH, HEIGHT);
    }

}
