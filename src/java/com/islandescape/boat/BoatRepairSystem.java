package com.islandescape.boat;

import com.islandescape.item.Item;
import com.islandescape.item.ItemType;


public class BoatRepairSystem {

    public static final int TOTAL_SLOTS = 6;

    //  required ItemType
    private static final ItemType[] REQUIRED_TYPES = {
            ItemType.PLANK,
            ItemType.MAST,
            ItemType.FRAME,
            ItemType.SAIL,
            ItemType.RUDDER,
            ItemType.FITTINGS
    };

    // required quantity
    private static final int[] REQUIRED_QTYS = {
            3, 1, 1, 1, 1, 2
    };



    private final Item[] repairedItems = new Item[TOTAL_SLOTS];

    public BoatRepairSystem() {

    }

    public ItemType getRequiredType(int slot) {

        return null;
    }

    public int getRequiredQty(int slot) {

        return 0;
    }

    public Item getSlot(int slot) {

        return null;
    }

    // true iff slot has an item AND its quantity >= required quantity.
    public boolean isSlotComplete(int slot) {

        return false;
    }

    public int getCompletedCount() {

        return 0;
    }

    public int getTotalSlots() {

        return 0;
    }

    public double getProgress() {

        return 0.0;
    }

    public boolean isFullyRepaired() {

        return false;
    }


    // Attempts to deposit the given item into the target slot.
    //
    // Rejects (returns the input unchanged) if:
    //   slot index is out of range
    //   item is null
    //   item type does not match REQUIRED_TYPES[slot]
    //   slot is already complete
    //
    // Otherwise deposits min(item.quantity, requiredQty - currentQty);
    // excess (if any) is returned so the caller can keep it on the cursor.
    // Returns null if the whole item was consumed.
    public Item placeIn(int slot, Item item) {
        // stub
        return item;
    }


    // Removes and returns the item in the given slot.
    // Returns null if the slot is empty, out of range, or already complete
    // (completed deposits are permanent and cannot be taken back).
    public Item takeOut(int slot) {
        // stub
        return null;
    }
}
