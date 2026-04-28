package com.islandescape.resources;

import com.islandescape.item.ConsumableItem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

import java.util.ArrayList;
import java.util.List;

// Tree node: player can gather it only with an axe.
public class Tree extends ResourceNode {
    
    public Tree(double x, double y) {
        super(x, y, "Tree", ItemType.AXE);
    }

    @Override
    protected List<Item> generateDrops() {
        // For MVP tree gives 3 basic materials.
        List<Item> drops = new ArrayList<>();
        drops.add(new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "Wood from tree"));
        drops.add(new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical Leaves", "Leaves from tree"));
        drops.add(new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Vines from tree"));

        if(Math.random() < 0.25) {
            drops.add(ConsumableItem.coconut(1));
        } else if(Math.random() < 0.5 &&  Math.random() > 0.25) {
            drops.add(ConsumableItem.coconut(2));
        } else if(Math.random() < 0.75 &&  Math.random() > 0.5) {
            drops.add(ConsumableItem.banana(1));
        } else {
            drops.add(ConsumableItem.banana(2));
        }


        return drops;
    }
}
