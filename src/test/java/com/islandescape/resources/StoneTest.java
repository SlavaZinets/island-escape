package com.islandescape.resources;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.inventory.Inventory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoneTest {

    /**
     * Test that Stone harvest returns STONE
     */
    @Test
    public void testStoneHarvest_ReturnsStone() {
        Stone stone = new Stone(100, 100);
        Inventory inv = new Inventory();
        Item pickaxe = new Item(ItemType.PICKAXE, ItemCategory.TOOL, "Pickaxe", "Mines stone");
        inv.addItem(pickaxe);
        
        List<Item> harvest = stone.harvest(inv);
        
        assertNotNull(harvest, "Harvest should not be null");
        assertEquals(1, harvest.size(), "Stone should return exactly 1 item");
        assertEquals(ItemType.STONE, harvest.get(0).getType(), "Should return STONE item type");
    }

    /**
     * Test that Stone only harvests with PICKAXE
     */
    @Test
    public void testStoneHarvest_RequiresPickaxe() {
        Stone stone = new Stone(100, 100);
        Inventory inv = new Inventory();
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Chops trees");
        inv.addItem(axe);
        
        List<Item> harvest = stone.harvest(inv);
        
        assertNull(harvest, "Stone should not harvest with axe");
    }

    /**
     * Test that Stone position is stored correctly
     */
    @Test
    public void testStonePosition() {
        Stone stone = new Stone(250.5, 350.75);
        
        assertEquals(250.5, stone.getX(), "Stone X position should be stored");
        assertEquals(350.75, stone.getY(), "Stone Y position should be stored");
    }
}
