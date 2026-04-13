package com.islandescape.resources;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree resource node.
 * Requires AXE to harvest.
 * Returns: WOOD, TROPICAL_LEAVES, VINES
 */
public class Tree extends ResourceNode {
    
    public Tree(double x, double y) {
        super(x, y, "Tree", ItemType.AXE);
    }

    @Override
    protected List<Item> generateDrops() {
        List<Item> drops = new ArrayList<>();
        drops.add(new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "Wood from tree"));
        drops.add(new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical Leaves", "Leaves from tree"));
        drops.add(new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Vines from tree"));
        return drops;
    }
}
