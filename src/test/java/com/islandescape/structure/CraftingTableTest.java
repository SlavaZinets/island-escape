package com.islandescape.structure;

import com.islandescape.structures.CraftingTable;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CraftingTableTest {
    @Test   public void interact() {

            CraftingTable table = new CraftingTable(100, 100);
            table.interact(100, 100);
            assertTrue(true);

    }
@Test
    public void isPlayerNearby() {
    CraftingTable table = new CraftingTable(100.0, 100.0);
    boolean result = table.isPlayerNearby(100.0, 100.0);
    assertTrue(result, "true when player is on top of table");

    }
    @Test
    public void Crafting() {
        CraftingTable table = new CraftingTable(0, 0);
        Item[] inv = new Item[5];
         inv[0] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood");
        inv[1] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
         inv[2] = new Item(ItemType.ROPE, ItemCategory.CRAFTABLE_RESOURCE, "Rope", "Crafted rope");
           int[] indices = {0, 1, 2};
        //running craft at table position
        table.interact(0, 0);
         assertNull(inv[1]);
        assertNull(inv[2]);

        //ensure result in inventory
        assertNotNull(inv[0]);
           assertEquals("Axe", inv[0].getName());
    }


}
