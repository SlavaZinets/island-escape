package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemType;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final int DEFAULT_SLOT_COUNT = 10;

    private final int slotCount;
    private final ArrayList<Item> slots;

    public Inventory() {
        this.slotCount = DEFAULT_SLOT_COUNT;
        this.slots = new ArrayList<>();
    }

    public Inventory(int slotCount) {
        this.slotCount = slotCount;
        this.slots = new ArrayList<>();
    }

    public boolean add(Item item) {
        return false;
    }

    public Item remove(ItemType type, int qty) {
        return null;
    }

    public int countOf(ItemType type) {
        return 0;
    }

    public ArrayList<Item> snapshot() {
        return null;
    }

    public int getSlotCount() {
        return slotCount;
    }
}
