package com.islandescape.structure;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
import com.islandescape.structures.BoatWreck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class BoatWreackTest {
    @Test
    public void addPart() {
        BoatWreck boat = new BoatWreck(0, 0, "Boat Wreck Site");
        assertEquals(17, boat.getMissingParts().size());//we should have 17 part by default
        boat.addPart("Mast");
        assertEquals(16, boat.getMissingParts().size());//16 after adding the elemnt
        assertFalse(boat.getMissingParts().contains("Mast"));
    }

    @Test
    public void getMissingParts() {

    }
    @Test
    public void interact() {
        BoatWreck boat = new BoatWreck(40, 40, "Boat Wreck Site");
        boat.interact(70, 70);
        assertEquals(17, boat.getMissingParts().size());
    }
    @Test
    public void loadItems() {
        BoatWreck boat = new BoatWreck(20, 20, "Boat");
        Inventory playerInventory = new Inventory();
        Item rope = new Item(ItemType.ROPE, ItemCategory.TOOL, "Rope", "Test rope");
        playerInventory.addItem(rope);
        boat.loadItems(playerInventory);
        assertTrue(playerInventory.isEmpty(), "player inventory shjould empty");
    }
@Test
    public void depart() {


    }
}
