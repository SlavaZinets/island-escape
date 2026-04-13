package com.islandescape.inventory;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryCursorCraftingTest {

    private Inventory inventory;
    private InventoryCursor cursor;
    private CraftingSystem cs;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        cursor = new InventoryCursor();
        cs = new CraftingSystem();
    }

    private Item wood(int qty) {
        return new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "Wood", qty);
    }

    private Item stone(int qty) {
        return new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone", qty);
    }

    // --- pickUpFromCraftingGrid ---

    @Test
    public void pickUpFromEmptyCraftingSlotDoesNothing() {
        cursor.pickUpFromCraftingGrid(cs, 0);
        assertTrue(cursor.isEmpty());
    }

    @Test
    public void pickUpFromCraftingSlotMovesItemToCursor() {
        cs.placeIn(0, wood(3), 0);
        cursor.pickUpFromCraftingGrid(cs, 0);

        assertFalse(cursor.isEmpty());
        assertEquals(ItemType.WOOD, cursor.getHeldItem().getType());
        assertEquals(3, cursor.getHeldItem().getQuantity());
        assertNull(cs.getSlot(0));
    }

    @Test
    public void pickUpFromCraftingSlotWhenCursorNotEmptyDoesNothing() {
        // Cursor already holds something
        inventory.setSlot(0, stone(2));
        cursor.pickUp(inventory, 0);

        cs.placeIn(1, wood(1), 0);
        cursor.pickUpFromCraftingGrid(cs, 1);

        // Cursor still holds original stone
        assertEquals(ItemType.STONE, cursor.getHeldItem().getType());
        // Crafting slot still has the wood
        assertNotNull(cs.getSlot(1));
    }

    // --- placeIntoCraftingGrid (left-click) ---

    @Test
    public void placeIntoCraftingGridEmptySlot() {
        inventory.setSlot(0, wood(5));
        cursor.pickUp(inventory, 0);

        cursor.placeIntoCraftingGrid(cs, 0, 0);

        assertTrue(cursor.isEmpty());
        assertNotNull(cs.getSlot(0));
        assertEquals(ItemType.WOOD, cs.getSlot(0).getType());
        assertEquals(5, cs.getSlot(0).getQuantity());
    }

    @Test
    public void placeIntoCraftingGridOccupiedSlotSwaps() {
        cs.placeIn(0, stone(2), 1);

        inventory.setSlot(0, wood(3));
        cursor.pickUp(inventory, 0);

        cursor.placeIntoCraftingGrid(cs, 0, 0);

        // Cursor now holds the stone that was in the grid
        assertFalse(cursor.isEmpty());
        assertEquals(ItemType.STONE, cursor.getHeldItem().getType());
        assertEquals(2, cursor.getHeldItem().getQuantity());
        // Grid now has the wood
        assertEquals(ItemType.WOOD, cs.getSlot(0).getType());
        assertEquals(3, cs.getSlot(0).getQuantity());
    }

    @Test
    public void placeIntoCraftingGridWhenCursorEmptyDoesNothing() {
        cs.placeIn(0, wood(1), 0);
        cursor.placeIntoCraftingGrid(cs, 0, 0);

        // Grid unchanged
        assertNotNull(cs.getSlot(0));
        assertTrue(cursor.isEmpty());
    }

    // --- placeOneIntoCraftingGrid (right-click) ---

    @Test
    public void placeOneIntoCraftingGridEmptySlot() {
        inventory.setSlot(0, wood(5));
        cursor.pickUp(inventory, 0);

        cursor.placeOneIntoCraftingGrid(cs, 0, 0);

        // One placed in grid, 4 remain on cursor
        assertNotNull(cs.getSlot(0));
        assertEquals(1, cs.getSlot(0).getQuantity());
        assertEquals(4, cursor.getHeldItem().getQuantity());
    }

    @Test
    public void placeOneIntoCraftingGridLastItemClearsCursor() {
        inventory.setSlot(0, wood(1));
        cursor.pickUp(inventory, 0);

        cursor.placeOneIntoCraftingGrid(cs, 0, 0);

        assertNotNull(cs.getSlot(0));
        assertEquals(1, cs.getSlot(0).getQuantity());
        assertTrue(cursor.isEmpty());
    }

    @Test
    public void placeOneIntoCraftingGridOccupiedSlotDoesNothing() {
        cs.placeIn(0, stone(1), 1);

        inventory.setSlot(0, wood(3));
        cursor.pickUp(inventory, 0);

        cursor.placeOneIntoCraftingGrid(cs, 0, 0);

        // Grid unchanged — still stone
        assertEquals(ItemType.STONE, cs.getSlot(0).getType());
        // Cursor unchanged — still wood x3
        assertEquals(ItemType.WOOD, cursor.getHeldItem().getType());
        assertEquals(3, cursor.getHeldItem().getQuantity());
    }
}
