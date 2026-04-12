package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Item wood(int qty) {
        return new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", qty);
    }

    private Item stone(int qty) {
        return new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", qty);
    }

    private Item axe() {
        return new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
    }

    @Test
    public void testAddToEmptyInventory() {
        Inventory inv = new Inventory(10);
        assertTrue(inv.add(wood(3)));
        assertEquals(3, inv.countOf(ItemType.WOOD));
        assertEquals(1, inv.snapshot().size());
    }

    @Test
    public void testStackIntoExistingSlot() {
        Inventory inv = new Inventory(10);
        inv.add(wood(4));
        inv.add(wood(3));
        // 4 + 3 = 7, so it fits in one stack
        assertEquals(7, inv.countOf(ItemType.WOOD));
        assertEquals(1, inv.snapshot().size());
    }

    @Test
    public void testOverflowIntoNewSlot() {
        Inventory inv = new Inventory(10);
        inv.add(wood(8));
        inv.add(wood(5));
        // 8 + 5 = 13 → first slot tops up to 10, remainder 3 goes to new slot
        assertEquals(13, inv.countOf(ItemType.WOOD));
        assertEquals(2, inv.snapshot().size());
    }

    @Test
    public void testDifferentTypesUseSeparateSlots() {
        Inventory inv = new Inventory(10);
        inv.add(wood(3));
        inv.add(stone(2));
        assertEquals(3, inv.countOf(ItemType.WOOD));
        assertEquals(2, inv.countOf(ItemType.STONE));
        assertEquals(2, inv.snapshot().size());
    }

    @Test
    public void testUnstackableItemUsesOwnSlot() {
        Inventory inv = new Inventory(10);
        assertTrue(inv.add(axe()));
        assertTrue(inv.add(axe()));
        // axes never stack regardless of type equality
        assertEquals(2, inv.snapshot().size());
    }

    @Test
    public void testAddWhenFullReturnsFalse() {
        Inventory inv = new Inventory(2);
        assertTrue(inv.add(wood(10)));  // slot 1 maxed
        assertTrue(inv.add(wood(10)));  // slot 2 maxed
        // third add has nowhere to go
        assertFalse(inv.add(wood(1)));
        assertEquals(20, inv.countOf(ItemType.WOOD));
    }

    @Test
    public void testAddWhenPartialCapacityFillsRemainingSpace() {
        Inventory inv = new Inventory(1);
        inv.add(wood(8));
        // Adding 5 more: only 2 fit in the existing stack (cap 10), no free
        // slot for the remaining 3. Inventory takes what it can
        Item incoming = wood(5);
        assertFalse(inv.add(incoming));
        assertEquals(10, inv.countOf(ItemType.WOOD));
        assertEquals(3, incoming.getQuantity());
    }

    @Test
    public void testRemoveByType() {
        Inventory inv = new Inventory(10);
        inv.add(wood(7));
        Item removed = inv.remove(ItemType.WOOD, 3);
        assertNotNull(removed);
        assertEquals(3, removed.getQuantity());
        assertEquals(ItemType.WOOD, removed.getType());
        assertEquals(4, inv.countOf(ItemType.WOOD));
    }

    @Test
    public void testRemoveMoreThanAvailableReturnsNull() {
        Inventory inv = new Inventory(10);
        inv.add(wood(2));
        assertNull(inv.remove(ItemType.WOOD, 5));
        // inventory unchanged
        assertEquals(2, inv.countOf(ItemType.WOOD));
    }

    @Test
    public void testRemoveAcrossMultipleSlots() {
        Inventory inv = new Inventory(10);
        inv.add(wood(10));
        inv.add(wood(5)); // two slots: 10, 5
        Item removed = inv.remove(ItemType.WOOD, 12);
        assertNotNull(removed);
        assertEquals(12, removed.getQuantity());
        assertEquals(3, inv.countOf(ItemType.WOOD));
    }

    @Test
    public void testRemoveExactTotalEmptiesSlots() {
        Inventory inv = new Inventory(10);
        inv.add(wood(6));
        Item removed = inv.remove(ItemType.WOOD, 6);
        assertNotNull(removed);
        assertEquals(6, removed.getQuantity());
        assertEquals(0, inv.countOf(ItemType.WOOD));
        assertEquals(0, inv.snapshot().size());
    }

    @Test
    public void testCountOfReturnsZeroWhenMissing() {
        Inventory inv = new Inventory(10);
        inv.add(stone(4));
        assertEquals(0, inv.countOf(ItemType.WOOD));
    }

    @Test
    public void testSnapshotIsDefensiveCopy() {
        Inventory inv = new Inventory(10);
        inv.add(wood(3));
        ArrayList<Item> snap = inv.snapshot();
        snap.clear();
        assertEquals(3, inv.countOf(ItemType.WOOD));
        assertEquals(1, inv.snapshot().size());
    }
}
