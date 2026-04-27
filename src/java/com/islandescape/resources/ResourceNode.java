package com.islandescape.resources;

import com.islandescape.structures.WorldStructure;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;
import com.islandescape.item.ItemCategory;
import com.islandescape.inventory.Inventory;
import com.islandescape.player.Player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// Base class for all gatherable nodes in the world.
// It keeps shared rules: required tool and drop flow.
public abstract class ResourceNode extends WorldStructure {

    private static final double GATHER_RADIUS = 32.0;
    private static final int RESPAWN_TICKS = 1000;

    protected ItemType requiredTool;
    private ArrayList<Point> tileCoords = new ArrayList<>();
    private boolean disabled = false;
    private int respawnCountdown = 0;

    public ResourceNode(double x, double y, String name, ItemType requiredTool) {
        super(x, y, name);
        this.requiredTool = requiredTool;
    }

    @Override
    public boolean isPlayerInRange(double playerX, double playerY) {
        // Player coords are top-left; shift to the player's visual center.
        double centerX = playerX + Player.WIDTH / 2.0;
        double centerY = playerY + Player.HEIGHT / 2.0;
        double dx = this.x - centerX;
        double dy = this.y - centerY;
        return (dx * dx + dy * dy) <= GATHER_RADIUS * GATHER_RADIUS;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void disable() {
        this.disabled = true;
        this.respawnCountdown = RESPAWN_TICKS;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void tick() {
        if (!disabled) return;
        respawnCountdown--;
        if (respawnCountdown <= 0) {
            disabled = false;
        }
    }

    public void setTileCoords(ArrayList<Point> coords) {
        this.tileCoords = coords;
    }

    public ArrayList<Point> getTileCoords() {
        return tileCoords;
    }

    // Main gather method used by the player.
    // If player has the correct tool, we return drops from this node.
    public List<Item> harvest(Inventory playerInventory) {
        if (hasRequiredTool(playerInventory)) {
            return generateDrops();
        }
        return null;
    }

    // Check the item the player is currently holding (active hotbar slot).
    protected boolean hasRequiredTool(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        Item held = inventory.getActiveItem();
        return held != null
                && held.getType() == requiredTool
                && held.getCategory() == ItemCategory.TOOL;
    }

    // Each concrete node defines its own drop list.
    protected abstract List<Item> generateDrops();

    // Small helpers used by tests and gameplay checks.
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
