package com.islandescape.structures;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.ConsumableItem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;
import com.islandescape.structures.BananaTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BananaTreeTest {

    private static Item firstItem(Inventory inv) {
        for (Item item : inv.getItems()) {
            if (item != null) return item;
        }
        return null;
    }

    @Test
    public void singleInteractExhaustsHealth() {
        BananaTree banana = new BananaTree(50.0, 50.0, "Bananas Tree");
        Inventory inv = new Inventory();

        banana.interact(50.0, 50.0, inv);

        assertEquals(0, banana.getHealth(), "Banana tree starts at health 1; one interact empties it");
    }

    @Test
    public void exhaustedTreeDropsAConsumableBanana() {
        BananaTree banana = new BananaTree(50.0, 50.0, "Bananas Tree");
        Inventory inv = new Inventory();

        banana.interact(50.0, 50.0, inv);

        Item dropped = firstItem(inv);
        assertNotNull(dropped, "Banana tree must drop something into inventory once health hits 0");
        assertEquals(ItemType.BANANA, dropped.getType());
        assertTrue(dropped instanceof ConsumableItem,
                "Drop must be a ConsumableItem so the eat key can apply effects");

        ConsumableItem b = (ConsumableItem) dropped;
        assertTrue(b.getHungerEffect() > 0, "Banana must restore hunger");
        assertEquals(0, b.getThirstEffect(), "Banana must not restore thirst");
    }

    @Test
    public void notInRangeDoesNotDamageTree() {
        BananaTree banana = new BananaTree(50.0, 50.0, "Bananas Tree");
        Inventory inv = new Inventory();

        // 200 px away — outside the 40 px range.
        banana.interact(250.0, 50.0, inv);

        assertEquals(1, banana.getHealth(), "Tree health must be untouched when player is out of range");
        assertEquals(null, firstItem(inv), "No drop when out of range");
    }
}
