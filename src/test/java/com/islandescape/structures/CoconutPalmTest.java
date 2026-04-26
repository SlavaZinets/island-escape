package com.islandescape.structures;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.ConsumableItem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;
import com.islandescape.structures.CoconutPalm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CoconutPalmTest {

    // Returns the first non-null inventory slot, or null if empty
    private static Item firstItem(Inventory inv) {
        for (Item item : inv.getItems()) {
            if (item != null) return item;
        }
        return null;
    }

    @Test
    public void shakingTwiceExhaustsHealth() {
        CoconutPalm palm = new CoconutPalm(160.0, 160.0, "Palm");
        Inventory inv = new Inventory();

        palm.interact(160.0, 160.0, inv);
        palm.interact(160.0, 160.0, inv);

        assertEquals(0, palm.getHealth());
    }

    @Test
    public void exhaustedPalmDropsAConsumableCoconut() {
        CoconutPalm palm = new CoconutPalm(160.0, 160.0, "Palm");
        Inventory inv = new Inventory();

        palm.interact(160.0, 160.0, inv);
        palm.interact(160.0, 160.0, inv);

        Item dropped = firstItem(inv);
        assertNotNull(dropped, "Palm must drop something into inventory once health hits 0");
        assertEquals(ItemType.COCONUT, dropped.getType());
        assertTrue(dropped instanceof ConsumableItem,
                "Drop must be a ConsumableItem so the eat key can apply effects");

        ConsumableItem coconut = (ConsumableItem) dropped;
        assertTrue(coconut.getHungerEffect() > 0, "Coconut must restore hunger");
        assertTrue(coconut.getThirstEffect() > 0, "Coconut must restore thirst");
    }

    @Test
    public void notInRangeDoesNotDamagePalm() {
        CoconutPalm palm = new CoconutPalm(160.0, 160.0, "Palm");
        Inventory inv = new Inventory();

        // 200 px away — outside the 40 px range.
        palm.interact(360.0, 160.0, inv);

        assertEquals(2, palm.getHealth(), "Palm health must be untouched when player is out of range");
        assertEquals(null, firstItem(inv), "No drop when out of range");
    }
}
