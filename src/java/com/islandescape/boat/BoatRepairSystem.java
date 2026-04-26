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


    public ItemType getRequiredType(int slot) {
        if (slot < 0 || slot >= TOTAL_SLOTS) return null;
        return REQUIRED_TYPES[slot];
    }

    public int getRequiredQty(int slot) {
        if (slot < 0 || slot >= TOTAL_SLOTS) return 0;
        return REQUIRED_QTYS[slot];
    }

    public Item getSlot(int slot) {
        if (slot < 0 || slot >= TOTAL_SLOTS) return null;
        return repairedItems[slot];
    }

    // true if a slot has an item AND its quantity >= required quantity.
    public boolean isSlotComplete(int slot) {
        if (slot < 0 || slot >= TOTAL_SLOTS) return false;
        Item held = repairedItems[slot];
        return held != null && held.getQuantity() >= REQUIRED_QTYS[slot];
    }

    public int getCompletedCount() {
        int count = 0;
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (isSlotComplete(i)) count++;
        }
        return count;
    }

    public int getTotalSlots() {
        return TOTAL_SLOTS;
    }

    public double getProgress() {
        return (double) getCompletedCount() / TOTAL_SLOTS;
    }

    public boolean isFullyRepaired() {
        return getCompletedCount() == TOTAL_SLOTS;
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
        if (slot < 0 || slot >= TOTAL_SLOTS) return item;
        if (item == null) return null;
        if (item.getType() != REQUIRED_TYPES[slot]) return item;
        if (isSlotComplete(slot)) return item;

        int currentQty = repairedItems[slot] == null ? 0 : repairedItems[slot].getQuantity();
        int need = REQUIRED_QTYS[slot] - currentQty;
        int deposit = Math.min(item.getQuantity(), need);

        if (deposit <= 0) return item;


        Item deposited = item.split(deposit);

        if (repairedItems[slot] == null) {
            repairedItems[slot] = deposited;
        } else {

            repairedItems[slot].merge(deposited);
        }

        return item.getQuantity() > 0 ? item : null;
    }


    public void reset() {
    }

    // Removes and returns the item in the given slot.
    // Returns null if the slot is empty, out of range, or already complete
    // (completed deposits are permanent and cannot be taken back).
    public Item takeOut(int slot) {
        if (slot < 0 || slot >= TOTAL_SLOTS) return null;
        if (repairedItems[slot] == null) return null;
        if (isSlotComplete(slot)) return null;

        Item taken = repairedItems[slot];
        repairedItems[slot] = null;
        return taken;
    }
}
