package com.islandescape.resources;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

import java.util.ArrayList;
import java.util.List;

// Stone node: player can gather it only with a pickaxe.
public class Stone extends ResourceNode {
    
    public Stone(double x, double y) {
        super(x, y, "Stone", ItemType.PICKAXE);
    }

    @Override
    protected List<Item> generateDrops() {
        // For now stone gives one stone item in MVP.
        List<Item> drops = new ArrayList<>();
        drops.add(new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone from mining"));
        return drops;
    }
}
