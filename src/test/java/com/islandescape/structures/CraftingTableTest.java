package com.islandescape.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CraftingTableTest {

    @Test
    public void testInteract() {
        CraftingTable table = new CraftingTable(100, 100);
        table.interact(100, 100);
        assertTrue(true);
    }

    @Test
    public void testIsPlayerNearby() {
        CraftingTable table = new CraftingTable(100.0, 100.0);
        assertTrue(table.isPlayerNearby(100.0, 100.0), "true when player is on top of table");
    }

    @Test
    public void testPlayerTooFar() {
        CraftingTable table = new CraftingTable(100.0, 100.0);
        assertFalse(table.isPlayerNearby(300.0, 300.0), "false when player is far away");
    }
}
