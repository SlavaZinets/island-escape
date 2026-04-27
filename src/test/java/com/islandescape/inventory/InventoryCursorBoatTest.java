package com.islandescape.inventory;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryCursorBoatTest {

    private static final int PLANK_SLOT = 0;
    private static final int MAST_SLOT = 1;
    private static final int FITTINGS_SLOT = 5;

    private InventoryCursor cursor;
    private BoatRepairSystem brs;

    @BeforeEach
    public void setUp() {
        cursor = new InventoryCursor();
        brs = new BoatRepairSystem();
    }

    private Item item(ItemType type, int qty) {
        return new Item(type, ItemCategory.CRAFTABLE_RESOURCE, type.name(), "desc", qty);
    }

    // Fills PLANK slot to its required qty (3), locking it as "complete".
    private void fillPlankSlot() {
        brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 3));
    }

    //  placeIntoBoatSlot (left-click, cursor full)

    @Test
    public void placeIntoBoatSlotWithMatchingTypeDeposits() {
        cursor.setHeldItem(item(ItemType.PLANK, 1));

        cursor.placeIntoBoatSlot(brs, PLANK_SLOT);

        assertTrue(cursor.isEmpty(), "1 plank should be fully consumed (1 <= needed 3)");
        assertNotNull(brs.getSlot(PLANK_SLOT));
        assertEquals(ItemType.PLANK, brs.getSlot(PLANK_SLOT).getType());
        assertEquals(1, brs.getSlot(PLANK_SLOT).getQuantity());
    }

    @Test
    public void placeIntoBoatSlotWithWrongTypeIsNoOp() {
        cursor.setHeldItem(item(ItemType.WOOD, 5));

        cursor.placeIntoBoatSlot(brs, PLANK_SLOT);

        assertFalse(cursor.isEmpty(), "wrong-type cursor must not be consumed");
        assertEquals(ItemType.WOOD, cursor.getHeldItem().getType());
        assertEquals(5, cursor.getHeldItem().getQuantity());
        assertNull(brs.getSlot(PLANK_SLOT), "slot must remain empty on wrong-type click");
    }

    @Test
    public void placeIntoBoatSlotWithExcessKeepsRemainderOnCursor() {
        // Cursor holds 5 planks; PLANK slot needs only 3.
        cursor.setHeldItem(item(ItemType.PLANK, 5));

        cursor.placeIntoBoatSlot(brs, PLANK_SLOT);

        assertFalse(cursor.isEmpty(), "excess should remain on the cursor");
        assertEquals(ItemType.PLANK, cursor.getHeldItem().getType());
        assertEquals(2, cursor.getHeldItem().getQuantity(), "5 - 3 = 2 remaining");
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity(), "slot filled to exactly the required qty");
        assertTrue(brs.isSlotComplete(PLANK_SLOT));
    }

    @Test
    public void placeIntoBoatSlotOnCompletedSlotIsNoOp() {
        fillPlankSlot();
        cursor.setHeldItem(item(ItemType.PLANK, 2));

        cursor.placeIntoBoatSlot(brs, PLANK_SLOT);

        assertFalse(cursor.isEmpty(), "cursor must be unchanged on a completed slot");
        assertEquals(2, cursor.getHeldItem().getQuantity());
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity(), "completed slot qty must not change");
    }

    @Test
    public void placeIntoBoatSlotWithEmptyCursorIsNoOp() {
        cursor.placeIntoBoatSlot(brs, PLANK_SLOT);

        assertTrue(cursor.isEmpty());
        assertNull(brs.getSlot(PLANK_SLOT));
    }

    // placeOneIntoBoatSlot (right-click, cursor full)

    @Test
    public void placeOneIntoBoatSlotDepositsExactlyOne() {
        cursor.setHeldItem(item(ItemType.FITTINGS, 5));

        cursor.placeOneIntoBoatSlot(brs, FITTINGS_SLOT);

        assertFalse(cursor.isEmpty(), "cursor must still hold the remainder");
        assertEquals(4, cursor.getHeldItem().getQuantity(), "exactly one fitting leaves the cursor");
        assertEquals(1, brs.getSlot(FITTINGS_SLOT).getQuantity());
        assertFalse(brs.isSlotComplete(FITTINGS_SLOT), " is not yet complete");
    }

    @Test
    public void placeOneIntoBoatSlotOnCompletedSlotIsNoOp() {
        fillPlankSlot();
        cursor.setHeldItem(item(ItemType.PLANK, 2));

        cursor.placeOneIntoBoatSlot(brs, PLANK_SLOT);

        assertEquals(2, cursor.getHeldItem().getQuantity(), "cursor untouched on completed slot");
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity());
    }

    @Test
    public void placeOneIntoBoatSlotWithWrongTypeIsNoOp() {
        cursor.setHeldItem(item(ItemType.WOOD, 5));

        cursor.placeOneIntoBoatSlot(brs, MAST_SLOT);

        assertEquals(5, cursor.getHeldItem().getQuantity(), "cursor untouched on wrong type");
        assertNull(brs.getSlot(MAST_SLOT));
    }

    //pickUpFromBoatSlot (left-click, cursor empty)

    @Test
    public void pickUpFromBoatSlotOnPartialSlotTakesWholeStack() {
        brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 2));

        cursor.pickUpFromBoatSlot(brs, PLANK_SLOT);

        assertFalse(cursor.isEmpty());
        assertEquals(ItemType.PLANK, cursor.getHeldItem().getType());
        assertEquals(2, cursor.getHeldItem().getQuantity(), "whole partial stack should be picked up");
        assertNull(brs.getSlot(PLANK_SLOT), "slot must be empty after pickup");
    }

    @Test
    public void pickUpFromBoatSlotOnCompletedSlotIsNoOp() {
        fillPlankSlot();

        cursor.pickUpFromBoatSlot(brs, PLANK_SLOT);

        assertTrue(cursor.isEmpty(), "completed deposits are permanent — cannot pick up");
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity(), "slot unchanged");
    }

    @Test
    public void pickUpFromBoatSlotOnEmptySlotIsNoOp() {
        cursor.pickUpFromBoatSlot(brs, PLANK_SLOT);

        assertTrue(cursor.isEmpty());
        assertNull(brs.getSlot(PLANK_SLOT));
    }

    @Test
    public void pickUpFromBoatSlotWhenCursorFullIsNoOp() {
        brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 2));
        cursor.setHeldItem(item(ItemType.WOOD, 1));

        cursor.pickUpFromBoatSlot(brs, PLANK_SLOT);

        assertEquals(ItemType.WOOD, cursor.getHeldItem().getType(),
                "cursor must keep its original item when full");
        assertEquals(2, brs.getSlot(PLANK_SLOT).getQuantity(), "slot unchanged");
    }

    //  pickUpHalfFromBoatSlot (right-click, cursor empty)

    @Test
    public void pickUpHalfFromBoatSlotSplitsPartialStack() {
        // PLANK_SLOT requires 3; deposit 2 → partial.
        // ceil(2/2) = 1 → cursor takes 1, slot keeps 1.
        brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 2));

        cursor.pickUpHalfFromBoatSlot(brs, PLANK_SLOT);

        assertFalse(cursor.isEmpty());
        assertEquals(ItemType.PLANK, cursor.getHeldItem().getType());
        assertEquals(1, cursor.getHeldItem().getQuantity(), "ceil(2/2) = 1 should be taken");
        assertNotNull(brs.getSlot(PLANK_SLOT));
        assertEquals(1, brs.getSlot(PLANK_SLOT).getQuantity(), "slot should keep the remaining 1");
    }

    @Test
    public void pickUpHalfFromBoatSlotOnCompletedSlotIsNoOp() {
        fillPlankSlot();

        cursor.pickUpHalfFromBoatSlot(brs, PLANK_SLOT);

        assertTrue(cursor.isEmpty(), "completed deposits are permanent — cannot half-pick");
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity());
    }

    @Test
    public void pickUpHalfFromBoatSlotOnEmptySlotIsNoOp() {
        cursor.pickUpHalfFromBoatSlot(brs, PLANK_SLOT);

        assertTrue(cursor.isEmpty());
        assertNull(brs.getSlot(PLANK_SLOT));
    }
}
