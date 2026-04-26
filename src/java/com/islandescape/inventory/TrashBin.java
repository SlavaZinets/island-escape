package com.islandescape.inventory;

import com.islandescape.item.Item;

/*
    Single-slot model used by the inventory's shared trash cell.
    Holds at most one Item stack. The REMOVE button calls clear()
    to permanently destroy whatever is in the slot
 */
public class TrashBin {

    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isEmpty() {
        return item == null;
    }

    public void clear() {
        this.item = null;
    }
}
