package com.islandescape.resources;

import com.islandescape.structures.WorldStructure;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;
import com.islandescape.item.ItemCategory;
import com.islandescape.inventory.Inventory;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

// Base class for all gatherable nodes in the world.
// It keeps shared rules: required tool and drop flow.
public abstract class ResourceNode extends WorldStructure {

    private static final double GATHER_RADIUS = 16.0;

    protected ItemType requiredTool;
    private List<Point> tileCoords = Collections.emptyList();
    private boolean disabled = false;

    public ResourceNode(double x, double y, String name, ItemType requiredTool) {
        super(x, y, name);
        this.requiredTool = requiredTool;
    }

    @Override
    public boolean isPlayerInRange(double playerX, double playerY) {
        double dx = this.x - playerX;
        double dy = this.y - playerY;
        return (dx * dx + dy * dy) <= GATHER_RADIUS * GATHER_RADIUS;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void disable() {
        this.disabled = true;
    }

    public void setTileCoords(List<Point> coords) {
        this.tileCoords = coords;
    }

    public List<Point> getTileCoords() {
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
