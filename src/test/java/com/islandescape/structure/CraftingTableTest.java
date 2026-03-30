package com.islandescape.structure;

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
         inv[0] = new Item("Wood", "Materials");
        inv[1] = new Item("Stone", "Materials");
         inv[2] = new Item("Rope", "Materials");
           int[] indices = {0, 1, 2};
        //running craft at table position
        table.Crafting(0, 0, inv, indices);
         assertNull(inv[1]);
        assertNull(inv[2]);

        //ensure result in inventory
        assertNotNull(inv[0]);
           assertEquals("Axe", inv[0].getName());
    }


}
