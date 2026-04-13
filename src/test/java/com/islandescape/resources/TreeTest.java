package com.islandescape.resources;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.inventory.Inventory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {

    /**
     * Test that Tree harvest returns WOOD, TROPICAL_LEAVES, and VINES
     */
    @Test
    public void testTreeHarvest_ReturnsCorrectItems() {
        Tree tree = new Tree(100, 100);
        Inventory inv = new Inventory();
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Chops trees");
        inv.addItem(axe);
        
        List<Item> harvest = tree.harvest(inv);
        
        assertNotNull(harvest, "Harvest should not be null");
        assertEquals(3, harvest.size(), "Tree should return exactly 3 items (wood, leaves, vines)");
        
        // Verify item types are present
        boolean hasWood = harvest.stream().anyMatch(item -> item.getType() == ItemType.WOOD);
        boolean hasLeaves = harvest.stream().anyMatch(item -> item.getType() == ItemType.TROPICAL_LEAVES);
        boolean hasVines = harvest.stream().anyMatch(item -> item.getType() == ItemType.VINES);
        
        assertTrue(hasWood, "Harvest should contain WOOD");
        assertTrue(hasLeaves, "Harvest should contain TROPICAL_LEAVES");
        assertTrue(hasVines, "Harvest should contain VINES");
    }

    /**
     * Test that Tree only harvests with AXE
     */
    @Test
    public void testTreeHarvest_RequiresAxe() {
        Tree tree = new Tree(100, 100);
        Inventory inv = new Inventory();
        Item pickaxe = new Item(ItemType.PICKAXE, ItemCategory.TOOL, "Pickaxe", "Mines stone");
        inv.addItem(pickaxe);
        
        List<Item> harvest = tree.harvest(inv);
        
        assertNull(harvest, "Tree should not harvest with pickaxe");
    }
}
