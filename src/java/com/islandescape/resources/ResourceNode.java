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

    protected ItemType requiredTool;
    private ArrayList<Point> tileCoords = new ArrayList<>();
    private boolean disabled = false;

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

    // Simple check: does inventory contain the tool this node needs?
    protected boolean hasRequiredTool(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        
        // We scan all inventory slots and stop at first valid tool.
        Item[] allItems = inventory.getItems();
        for (Item item : allItems) {
            if (item != null 
                    && item.getType() == requiredTool
                    && item.getCategory() == ItemCategory.TOOL) {
                return true;
            }
        }
        return false;
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
