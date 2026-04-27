package com.islandescape.Item;

import com.islandescape.item.ConsumableItem;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ConsumableItemTest {

    @Test
    void coconutRestoresBothHungerAndThirst() {
        ConsumableItem c = ConsumableItem.coconut(1);

        assertNotNull(c);
        assertEquals(ItemType.COCONUT, c.getType());
        assertEquals(ItemCategory.FOOD, c.getCategory());
        assertTrue(c.getHungerEffect() > 0, "coconut must restore some hunger");
        assertTrue(c.getThirstEffect() > 0, "coconut must restore some thirst");
    }

    @Test
    void bananaRestoresOnlyHunger() {
        ConsumableItem b = ConsumableItem.banana(1);

        assertEquals(ItemType.BANANA, b.getType());
        assertTrue(b.getHungerEffect() > 0, "banana must restore some hunger");
        assertEquals(0, b.getThirstEffect(), "banana must not restore thirst");
    }

    @Test
    void waterRestoresOnlyThirst() {
        ConsumableItem w = ConsumableItem.water(1);

        assertEquals(ItemType.WATER, w.getType());
        assertEquals(0, w.getHungerEffect(), "water must not restore hunger");
        assertTrue(w.getThirstEffect() > 0, "water must restore some thirst");
    }

    @Test
    void factoriesProduceFreshInstancesEachCall() {
        ConsumableItem a = ConsumableItem.coconut(1);
        ConsumableItem b = ConsumableItem.coconut(1);

        // Mutating one (e.g. via Item.split or merge) must not affect the other.
        // Sanity-check that they are distinct objects.
        assertTrue(a != b, "factories must return new instances each call");
    }


}
