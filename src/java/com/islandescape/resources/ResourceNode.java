package com.islandescape.resources;

import com.islandescape.structures.WorldStructure;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;
import com.islandescape.item.ItemCategory;
import com.islandescape.inventory.Inventory;

import java.util.List;

/**
 * Abstract base class for all harvestable resource nodes.
 * Extends WorldStructure to reuse position and range checking.
 */
public abstract class ResourceNode extends WorldStructure {
    protected ItemType requiredTool;

    public ResourceNode(double x, double y, String name, ItemType requiredTool) {
        super(x, y, name);
        this.requiredTool = requiredTool;
    }

    /**
     * Attempt to harvest this resource node.
     * Returns a list of items if successful (player has correct tool),
     * or null if harvest fails (missing tool).
     */
    public List<Item> harvest(Inventory playerInventory) {
        if (hasRequiredTool(playerInventory)) {
            return generateDrops();
        }
        return null;
    }

    /**
     * Check if the player's inventory contains the required tool.
     */
    protected boolean hasRequiredTool(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        
        // Check all items in inventory for the required tool
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

    /**
     * Generate the drops for this resource node.
     * Implemented by subclasses.
     */
    protected abstract List<Item> generateDrops();

    /**
     * Helper method to get X position.
     */
    public double getX() {
        return x;
    }

    /**
     * Helper method to get Y position.
     */
    public double getY() {
        return y;
    }
}
