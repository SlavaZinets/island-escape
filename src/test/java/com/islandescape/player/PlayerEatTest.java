package com.islandescape.player;

import com.islandescape.item.ConsumableItem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PlayerEatTest {

    /*
        Drains both stats below MAX so eat()/drink() effects are observable
        (otherwise restoration is clipped at MAX and the deltas vanish).
     */
    private static void drainTo(SurvivalStats s, int targetHunger, int targetThirst) {
        // tickDown depletes both stats by DEPLETION_PER_TICK each call.
        // Just tick enough times to land at-or-below the target.
        int ticks = (int) Math.ceil(
                Math.max(SurvivalStats.MAX - targetHunger, SurvivalStats.MAX - targetThirst)
                        / SurvivalStats.DEPLETION_PER_TICK);
        for (int i = 0; i < ticks; i++) {
            s.tickDown();
        }
    }

    @Test
    void eatWithEmptyInventoryReturnsNull() {
        Player p = new Player("Tester", 1, 0, 0);
        assertNull(p.eat(),
                "Empty inventory must produce no consumable result");
    }

    @Test
    void eatWithNoConsumableReturnsNull() {
        Player p = new Player("Tester", 1, 0, 0);
        // A plain (non-consumable) Item — should be ignored by eat().
        p.getInventory().addItem(new Item(
                ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "Plain log"));

        assertNull(p.eat(),
                "Non-consumable items must not be eaten");
    }

    @Test
    void eatingCoconutRestoresHungerAndThirst() {
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();
        drainTo(s, 50, 50);

        int hungerBefore = s.getHunger();
        int thirstBefore = s.getThirst();

        p.getInventory().addItem(ConsumableItem.coconut());
        ConsumableItem eaten = p.eat();

        assertNotNull(eaten, "Coconut should have been consumed");
        assertEquals(ItemType.COCONUT, eaten.getType());
        assertTrue(s.getHunger() > hungerBefore,
                "Coconut must restore hunger");
        assertTrue(s.getThirst() > thirstBefore,
                "Coconut must restore thirst");
    }

    @Test
    void eatingBananaRestoresHungerOnly() {
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();
        drainTo(s, 50, 50);

        int hungerBefore = s.getHunger();
        int thirstBefore = s.getThirst();

        p.getInventory().addItem(ConsumableItem.banana());
        ConsumableItem eaten = p.eat();

        assertNotNull(eaten);
        assertEquals(ItemType.BANANA, eaten.getType());
        assertTrue(s.getHunger() > hungerBefore, "Banana must restore hunger");
        assertEquals(thirstBefore, s.getThirst(),
                "Banana has thirstEffect=0 so thirst must be unchanged");
    }

    @Test
    void eatingWaterRestoresThirstOnly() {
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();
        drainTo(s, 50, 50);

        int hungerBefore = s.getHunger();
        int thirstBefore = s.getThirst();

        p.getInventory().addItem(ConsumableItem.water());
        ConsumableItem eaten = p.eat();

        assertNotNull(eaten);
        assertEquals(ItemType.WATER, eaten.getType());
        assertEquals(hungerBefore, s.getHunger(),
                "Water has hungerEffect=0 so hunger must be unchanged");
        assertTrue(s.getThirst() > thirstBefore,
                "Water must restore thirst");
    }

    @Test
    void eatingDecrementsStackByOne() {
        Player p = new Player("Tester", 1, 0, 0);
        // Build a stack of 3 coconuts in one slot.
        ConsumableItem stack = new ConsumableItem(
                ItemType.COCONUT, ItemCategory.FOOD,
                "Coconut", "Stack",
                3, 15, 8);
        p.getInventory().addItem(stack);

        ConsumableItem eaten = p.eat();
        assertNotNull(eaten);
        assertEquals(ItemType.COCONUT, eaten.getType());

        // Two coconuts must remain in the inventory.
        int remaining = 0;
        for (Item it : p.getInventory().getItems()) {
            if (it != null && it.getType() == ItemType.COCONUT) {
                remaining += it.getQuantity();
            }
        }
        assertEquals(2, remaining,
                "Eating once must reduce a 3-stack to 2 coconuts");
    }

    @Test
    void eatingLastOfStackClearsTheSlot() {
        Player p = new Player("Tester", 1, 0, 0);
        p.getInventory().addItem(ConsumableItem.banana()); // qty 1

        assertNotNull(p.eat(), "First eat consumes the lone banana");

        // Inventory must contain no BANANA at all afterwards.
        for (Item it : p.getInventory().getItems()) {
            if (it != null) {
                assertTrue(it.getType() != ItemType.BANANA,
                        "All bananas should be gone — slot must be cleared");
            }
        }
    }

    @Test
    void activeSlotWithNonConsumableDoesNotEatLaterFood() {
        // Eat now ONLY consumes the active hotbar slot. If the player has
        // a non-consumable selected, eat is a no-op even when food sits
        // elsewhere in the inventory.
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();
        drainTo(s, 50, 50);
        int thirstBefore = s.getThirst();

        // Wood lands in hotbar[0] (the default active slot); water goes to
        // hotbar[1]. Default selectedHotBarSlot = 0 → active is Wood.
        p.getInventory().addItem(new Item(
                ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "Plain log"));
        p.getInventory().addItem(ConsumableItem.water());

        assertNull(p.eat(),
                "Active slot holds Wood — eat must not reach the water in the next slot");
        assertEquals(thirstBefore, s.getThirst(),
                "No food eaten → thirst unchanged");
    }

    @Test
    void switchingHotBarSlotPicksDifferentConsumable() {
        // Demonstrates the new contract: the player chooses what to eat
        // by selecting the hotbar slot that holds it.
        Player p = new Player("Tester", 1, 0, 0);
        SurvivalStats s = p.getSurvivalStats();
        drainTo(s, 50, 50);

        // Banana → hotbar[0] (logical slot 15). Water → hotbar[1] (slot 16).
        p.getInventory().addItem(ConsumableItem.banana());
        p.getInventory().addItem(ConsumableItem.water());

        // Select the WATER slot, then eat → water should be consumed.
        p.getInventory().setSelectedHotBarSlot(16);

        int thirstBefore = s.getThirst();
        ConsumableItem eaten = p.eat();

        assertNotNull(eaten);
        assertEquals(ItemType.WATER, eaten.getType(),
                "Active slot was the water slot — water must be consumed, not the banana");
        assertTrue(s.getThirst() > thirstBefore);

        // Banana must still be sitting in slot 15 untouched.
        Item banana = p.getInventory().getSlot(15);
        assertNotNull(banana, "Banana slot must NOT have been touched");
        assertEquals(ItemType.BANANA, banana.getType());
    }
}
