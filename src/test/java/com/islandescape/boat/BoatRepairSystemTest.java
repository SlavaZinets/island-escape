package com.islandescape.boat;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoatRepairSystemTest {


    private static final int PLANK_SLOT = 0;
    private static final int MAST_SLOT = 1;
    private static final int FRAME_SLOT = 2;
    private static final int SAIL_SLOT = 3;
    private static final int RUDDER_SLOT = 4;
    private static final int FITTINGS_SLOT = 5;

    private BoatRepairSystem brs;

    @BeforeEach
    public void setUp() {
        brs = new BoatRepairSystem();
    }

    private Item item(ItemType type, int qty) {
        return new Item(type, ItemCategory.CRAFTABLE_RESOURCE, type.name(), "desc", qty);
    }

    private Item item(ItemType type) {
        return item(type, 1);
    }

    // Fills the PLANK slot to its required quantity (3). Used by several tests.
    private void fillPlankSlot() {
        brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 3));
    }

    //

    @Test
    public void placeInCorrectTypeAccepts() {
        Item plank = item(ItemType.PLANK, 1);
        Item remainder = brs.placeIn(PLANK_SLOT, plank);

        // Plank fully consumed (1 <= required 3).
        assertNull(remainder, "1 plank placed into a PLANK slot needing 3 should be fully consumed");
        assertNotNull(brs.getSlot(PLANK_SLOT), "slot should now hold the deposited item");
        assertEquals(ItemType.PLANK, brs.getSlot(PLANK_SLOT).getType());
        assertEquals(1, brs.getSlot(PLANK_SLOT).getQuantity());
        assertFalse(brs.isSlotComplete(PLANK_SLOT), "1/3 should not be complete");
    }

    @Test
    public void placeInWrongTypeReturnsItemUnchanged() {
        Item wood = item(ItemType.WOOD, 5);
        Item result = brs.placeIn(PLANK_SLOT, wood);



        assertEquals(5, result.getQuantity(), "rejected item quantity must be untouched");
        assertNull(brs.getSlot(PLANK_SLOT), "slot must remain empty after wrong-type deposit");
    }

    @Test
    public void placeInCompleteSlotReturnsItemUnchanged() {

        fillPlankSlot();
        assertTrue(brs.isSlotComplete(PLANK_SLOT), "precondition: plank slot is complete");

        // try to add more planks.
        Item extra = item(ItemType.PLANK, 2);
        Item result = brs.placeIn(PLANK_SLOT, extra);


        assertEquals(2, result.getQuantity(), "rejected quantity must be untouched");
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity(),
                "completed slot quantity must not increase past its requirement");
    }

    @Test
    public void placeInExcessReturnsRemainder() {
        // Cursor has 5 planks; PLANK slot needs only 3.
        Item stack = item(ItemType.PLANK, 5);
        Item remainder = brs.placeIn(PLANK_SLOT, stack);

        assertNotNull(remainder, "excess should be returned");
        assertEquals(2, remainder.getQuantity(), "remainder should be 5 - 3 = 2");
        assertEquals(ItemType.PLANK, remainder.getType());
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity(), "slot must contain exactly the required 3");
        assertTrue(brs.isSlotComplete(PLANK_SLOT));
    }

    //

    @Test
    public void takeOutFromCompletedSlotReturnsNull() {
        fillPlankSlot();
        assertTrue(brs.isSlotComplete(PLANK_SLOT));

        Item taken = brs.takeOut(PLANK_SLOT);

        assertNull(taken, "completed deposits are permanent takeOut must return null");
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity());
    }

    @Test
    public void takeOutFromPartialSlotReturnsItem() {
        // Partial deposit: 2 of 3 planks.
        brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 2));
        assertFalse(brs.isSlotComplete(PLANK_SLOT));

        Item taken = brs.takeOut(PLANK_SLOT);

        assertNotNull(taken);
        assertEquals(ItemType.PLANK, taken.getType());
        assertEquals(2, taken.getQuantity());
        assertNull(brs.getSlot(PLANK_SLOT), "slot should be empty after takeOut");
    }

    // incremental accumulation

    @Test
    public void incrementalDepositsAccumulate() {
        // 1 + 1 + 1 = 3, reaching the requirement for PLANK.
        Item r1 = brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 1));
        Item r2 = brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 1));
        Item r3 = brs.placeIn(PLANK_SLOT, item(ItemType.PLANK, 1));

        assertNull(r1);
        assertNull(r2);
        assertNull(r3);
        assertEquals(3, brs.getSlot(PLANK_SLOT).getQuantity());
        assertTrue(brs.isSlotComplete(PLANK_SLOT), "slot should flip to complete when qty meets requirement");
    }

    //  progress / completion counting

    @Test
    public void progressMatchesCompletedCount() {
        assertEquals(0, brs.getCompletedCount(), "no deposits -> 0 completed");
        assertEquals(0.0, brs.getProgress(), 0.0001);

        // Complete the 1-qty slots: MAST, FRAME.
        brs.placeIn(MAST_SLOT, item(ItemType.MAST, 1));
        brs.placeIn(FRAME_SLOT, item(ItemType.FRAME, 1));

        assertEquals(2, brs.getCompletedCount());
        assertEquals(2.0 / 6.0, brs.getProgress(), 0.0001);
        assertFalse(brs.isFullyRepaired());

        // Finish everything.
        brs.placeIn(SAIL_SLOT, item(ItemType.SAIL, 1));
        brs.placeIn(RUDDER_SLOT, item(ItemType.RUDDER, 1));
        brs.placeIn(FITTINGS_SLOT, item(ItemType.FITTINGS, 2));
        fillPlankSlot();

        assertEquals(6, brs.getCompletedCount());
        assertEquals(1.0, brs.getProgress(), 0.0001);
        assertTrue(brs.isFullyRepaired());
    }
}
