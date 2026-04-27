package com.islandescape.player;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.ConsumableItem;
import com.islandescape.item.Item;
import com.islandescape.map.TileMap;
import com.islandescape.resources.ResourceNode;
import com.islandescape.utilities.Direction;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 26;

    // Walk-cycle tuning. FRAME_COUNT must match PlayerSprite.COLS.
    // FRAMES_PER_STEP is how many game ticks a single visible frame is held for.
    private static final int FRAMES_PER_STEP = 8;
    private static final int FRAME_COUNT = 4;


    private Color nameColor;

    private String name;
    private int id;
    private Point2D.Double position;
    private final double SPEED = 2;
    private final Inventory inventory = new Inventory();
    private final SurvivalStats survivalStats = new SurvivalStats();

    private int worldWidth = 0;
    private int worldHeight = 0;
    private TileMap tileMap;

    private Facing facing = Facing.SOUTH;
    private int animationTick = 0;
    private PlayerSprite sprite = null;

    public Player(String name, int id, int x, int y, Color nameColor) {
        this.name = name;
        this.id = id;
        this.position = new Point2D.Double(x, y);
        this.nameColor = nameColor;
    }
    public Player(String name, int id, int x, int y) {
        this.name = name;
        this.id = id;
        this.position = new Point2D.Double(x, y);
    }

    public void move (Direction direction) {
        Facing newFacing = Facing.fromDirection(direction);
        if (newFacing != null) {
            facing = newFacing;
            animationTick++;
        } else {

            animationTick = 0;
        }

        double effectiveSpeed = SPEED * survivalStats.getSpeedMultiplier();
        double newX = position.getX() + direction.getX() * effectiveSpeed;
        double newY = position.getY() - direction.getY() * effectiveSpeed;

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

    public void setPosition(double x, double y) {
        position.setLocation(x, y);
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

        //show name of player above
        g.setColor(nameColor);//color for text
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        //draw name centered above player head
        g.drawString(name, x + WIDTH / 2 - g.getFontMetrics().stringWidth(name) / 2, y - 4);

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

    public SurvivalStats getSurvivalStats() {
        return survivalStats;
    }

    // Player side of gathering.
    // We only check distance here and then delegate real harvest logic to the node.
    public List<Item> farm(ResourceNode resource) {
        if (resource.isPlayerInRange(position.getX(), position.getY())) {
            return resource.harvest(inventory);
        }
        return null;
    }

    /*
        Eats whatever is in the player's currently-selected hotbar slot,
     */
    public ConsumableItem eat() {
        Item active = inventory.getActiveItem();
        if (!(active instanceof ConsumableItem)) {
            return null;
        }

        ConsumableItem food = (ConsumableItem) active;
        survivalStats.eat(food.getHungerEffect());
        survivalStats.drink(food.getThirstEffect());


        food.split(1);
        if (food.getQuantity() <= 0) {

            inventory.setSlot(inventory.getSelectedHotBarSlot(), null);
        }
        return food;
    }
}
