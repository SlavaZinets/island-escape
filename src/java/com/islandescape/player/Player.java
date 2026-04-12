package com.islandescape.player;

import com.islandescape.inventory.Inventory;
import com.islandescape.map.TileMap;
import com.islandescape.utilities.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 100;

    // Walk-cycle tuning. FRAME_COUNT must match PlayerSprite.COLS.
    // FRAMES_PER_STEP is how many game ticks a single visible frame is held for.
    private static final int FRAMES_PER_STEP = 8;
    private static final int FRAME_COUNT = 6;

    private String name;
    private int id;
    private Point position;
    private final double SPEED = 2;
    private final Inventory inventory = new Inventory();

    private int worldWidth = 0;
    private int worldHeight = 0;
    private TileMap tileMap;

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
        } else {

            animationTick = 0;
        }

        double newX = position.getX() + direction.getX() * SPEED;
        double newY = position.getY() - direction.getY() * SPEED;

        if (worldWidth > 0 && worldHeight > 0) {
            newX = Math.max(0, Math.min(newX, worldWidth - WIDTH));
            newY = Math.max(0, Math.min(newY, worldHeight - HEIGHT));
        }


        if (tileMap != null) {
            double currentX = position.getX();
            double currentY = position.getY();

            if (tileMap.isBlocked(newX, currentY, WIDTH, HEIGHT)) {
                newX = currentX;
            }

            if (tileMap.isBlocked(newX, newY, WIDTH, HEIGHT)) {
                newY = currentY;
            }
        }

        position.setLocation(newX, newY);
    }

    public void setWorldBounds(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
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
        return (animationTick / FRAMES_PER_STEP) % FRAME_COUNT;
    }

    public void setSprite(PlayerSprite sprite) {
        this.sprite = sprite;
    }

    public void renderPlayer(Graphics2D g) {
        int x = (int) position.getX();
        int y = (int) position.getY();

        if (sprite == null) {
            g.setColor(Color.RED);
            g.fillRect(x, y, WIDTH, HEIGHT);
            return;
        }

        int row = facing.getSpriteRow();
        int col = getFrameIndex();
        BufferedImage frame = facing.isFlipped()
                ? sprite.getFlippedFrame(row, col)
                : sprite.getFrame(row, col);
        g.drawImage(frame, x, y, WIDTH, HEIGHT, null);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
