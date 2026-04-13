package com.islandescape.resources;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.inventory.Inventory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceNodeTest {

    @Test
    public void testIsPlayerInRange_PlayerClose() {
        ResourceNode node = new Tree(100, 100);

        // Player at (110, 110) - distance ~14 units, in range (16)
        assertTrue(node.isPlayerInRange(110, 110),
                   "Player should be in range when within 16 px");
    }

    @Test
    public void testIsPlayerInRange_PlayerFar() {
        ResourceNode node = new Tree(100, 100);

        // Player at (200, 200) - distance ~141 units, out of range (16)
        assertFalse(node.isPlayerInRange(200, 200),
                    "Player should be out of range when far from resource node");
    }

    @Test
    public void testIsPlayerInRange_JustOutsideGatherRadius() {
        ResourceNode node = new Tree(100, 100);

        // 17 px to the right of the node center -> out of range (16)
        assertFalse(node.isPlayerInRange(117, 100),
                    "Player 17 px away should be out of gather range");
    }

    @Test
    public void testNewNodeIsNotDisabled() {
        ResourceNode node = new Tree(100, 100);
        assertFalse(node.isDisabled(), "New node should not be disabled");
    }

    @Test
    public void testDisableMarksNodeDisabled() {
        ResourceNode node = new Tree(100, 100);
        node.disable();
        assertTrue(node.isDisabled(), "disable() should mark node as disabled");
    }

    @Test
    public void testCanHarvest_WithCorrectTool() {
        ResourceNode tree = new Tree(100, 100);
        Inventory inv = new Inventory();
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Chops trees");
        inv.addItem(axe);
        
        // Should be able to harvest with correct tool
        List<Item> harvest = tree.harvest(inv);
        assertNotNull(harvest, "Harvest should return items list with correct tool");
        assertTrue(harvest.size() > 0, "Harvest should return at least one item");
    }

    @Test
    public void testCanHarvest_WithoutCorrectTool() {
        ResourceNode tree = new Tree(100, 100);
        Inventory inv = new Inventory();
        // No axe in inventory
        
        // Should not be able to harvest without tool
        List<Item> harvest = tree.harvest(inv);
        assertNull(harvest, "Harvest should return null without correct tool");
    }

    @Test
    public void testStoneRequiresPickaxe() {
        ResourceNode stone = new Stone(100, 100);
        Inventory inv = new Inventory();
        Item pickaxe = new Item(ItemType.PICKAXE, ItemCategory.TOOL, "Pickaxe", "Mines stone");
        inv.addItem(pickaxe);
        
        // Should be able to harvest with correct tool
        List<Item> harvest = stone.harvest(inv);
        assertNotNull(harvest, "Stone harvest should work with pickaxe");
        assertTrue(harvest.size() > 0, "Stone harvest should return items");
    }

    @Test
    public void testStoneRequiresPickaxe_NoTool() {
        ResourceNode stone = new Stone(100, 100);
        Inventory inv = new Inventory();
        // No pickaxe in inventory
        
        // Should not be able to harvest
        List<Item> harvest = stone.harvest(inv);
        assertNull(harvest, "Stone harvest should return null without pickaxe");
    }
}
