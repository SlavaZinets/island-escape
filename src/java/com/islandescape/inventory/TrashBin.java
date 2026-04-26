package com.islandescape.inventory;

import com.islandescape.item.Item;

/*
    Single-slot model used by the inventory's shared trash cell.
    Holds at most one Item stack. The REMOVE button calls clear()
    to permanently destroy whatever is in the slot
 */
public class TrashBin {

    public Item getItem() {
        return null;
    }

    public void setItem(Item item) {

    }

    public boolean isEmpty() {
        return true;
    }

    public void clear() {

    }
}
