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
        if (item == null || item.getQuantity() <= 0) {
            return false;
        }

        // For unstackable items each unit needs its own slot
        if (!item.isStackable()) {
            if (slots.size() < slotCount) {
                slots.add(item);
                return true;
            }
            return false;
        }

        // Fill existing stacks of the same type
        for (Item slot : slots) {
            if (slot.getType() == item.getType() && slot.hasSpace()) {
                int space = slot.getMaxStackSize() - slot.getQuantity();
                int transfer = Math.min(space, item.getQuantity());
                Item chunk = item.split(transfer);
                slot.merge(chunk);
                if (item.getQuantity() <= 0) {
                    return true;
                }
            }
        }

        // Overflow into new slots
        while (item.getQuantity() > 0 && slots.size() < slotCount) {
            int transfer = Math.min(item.getMaxStackSize(), item.getQuantity());
            Item newSlot = item.split(transfer);
            slots.add(newSlot);
        }

        return item.getQuantity() <= 0;
    }

    public Item remove(ItemType type, int qty) {
        if (qty <= 0 || countOf(type) < qty) {
            return null;
        }

        int remaining = qty;
        Item template = null;

        for (int i = slots.size() - 1; i >= 0 && remaining > 0; i--) {
            Item slot = slots.get(i);
            if (slot.getType() == type) {
                if (template == null) {
                    template = slot;
                }
                int take = Math.min(remaining, slot.getQuantity());
                slot.split(take);
                remaining -= take;
                if (slot.getQuantity() <= 0) {
                    slots.remove(i);
                }
            }
        }

        return new Item(template.getType(), template.getCategory(),
                template.getName(), template.getDescription(), qty);
    }

    public int countOf(ItemType type) {
        int total = 0;
        for (Item slot : slots) {
            if (slot.getType() == type) {
                total += slot.getQuantity();
            }
        }
        return total;
    }

    public ArrayList<Item> snapshot() {
        return new ArrayList<>(slots);
    }

    public int getSlotCount() {
        return slotCount;
    }
}
