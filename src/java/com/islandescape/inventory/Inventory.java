package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemType;

public class Inventory {
    Item[] slots;
    Item[] hotBarSlots;
    int capacity;

    int selectedHotBarSlot;

    public Inventory(){
        capacity = 15;
        slots = new Item[15];
        hotBarSlots = new Item[5];
        selectedHotBarSlot = 0;

    }
    // add item to the array
    public boolean addItem(Item newItem){
        if (newItem == null) return false;

        for (Item hotBarSlot : hotBarSlots) {
            if (hotBarSlot != null && hotBarSlot.hasSpace(newItem)) {
                hotBarSlot.merge(newItem);
                return true;
            }
        }

        for (Item slot : slots) {
            if (slot != null && slot.hasSpace(newItem)) {
                slot.merge(newItem);
                return true;
            }
        }



        for (int i = 0; i < hotBarSlots.length; i++) {
            if (hotBarSlots[i] == null) {
                hotBarSlots[i] = newItem;
                return true;
            }
        }
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                slots[i] = newItem;
                return true;
            }
        }


        return false; // full
    }

    // remove a specific quantity of an ItemType from inventory
    public boolean remove(ItemType type, int quantity) {
        if (getItemCountByType(type) < quantity) {
            return false;
        }

        int remaining = quantity;
        for (int i = 0; i < slots.length && remaining > 0; i++) {
            if (slots[i] != null && slots[i].getType() == type) {
                if (slots[i].getQuantity() <= remaining) {
                    remaining -= slots[i].getQuantity();
                    slots[i] = null;
                } else {
                    slots[i].split(remaining);
                    remaining = 0;
                }
            }
        }
        for (int i = 0; i < hotBarSlots.length && remaining > 0; i++) {
            if (hotBarSlots[i] != null && hotBarSlots[i].getType() == type) {
                if (hotBarSlots[i].getQuantity() <= remaining) {
                    remaining -= hotBarSlots[i].getQuantity();
                    hotBarSlots[i] = null;
                } else {
                    hotBarSlots[i].split(remaining);
                    remaining = 0;
                }
            }
        }
        return true;
    }
    public boolean isFull(){
        for(int i = 0; i<slots.length; i++){
            if(slots[i] == null) return false;
        }
        for(int j =0; j<hotBarSlots.length; j++){
            if(hotBarSlots[j] == null) return false;
        }
        return true;
    }
    // returns entire inventory
    public Item[] getItems(){
        Item[] all = new Item[slots.length + hotBarSlots.length];
        for (int i = 0; i < slots.length; i++) {
            all[i] = slots[i];
        }
        for (int i = 0; i < hotBarSlots.length; i++) {
            all[slots.length + i] = hotBarSlots[i];
        }
        return all;
    }
    // get slot by slotID
    public Item getSlot(int slotIndex){
        if(slotIndex < 15){
            return slots[slotIndex];
        }
        return hotBarSlots[slotIndex - 15];
    }
    // set item to the slot by ID
    public void setSlot(int slotIndex, Item item){
        if(slotIndex < 15){
            slots[slotIndex] = item;
        } else {
            hotBarSlots[slotIndex - 15] = item;
        }
    }
    // check whether there is an item in inventory in the right quantity
    public boolean hasItem(Item item, int quantity){
        return true;
    }
    public int getItemCount(Item item){
        if (item == null) return 0;
        return getItemCountByType(item.getType());
    }
    private int getItemCountByType(ItemType type) {
        int total = 0;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i].getType() == type) {
                total += slots[i].getQuantity();
            }
        }
        for (int i = 0; i < hotBarSlots.length; i++) {
            if (hotBarSlots[i] != null && hotBarSlots[i].getType() == type) {
                total += hotBarSlots[i].getQuantity();
            }
        }
        return total;
    }
    // Returns the bottom line in inventory, UI draws even when inventory is closed
    public Item[] getHotBarSlots(){
        return hotBarSlots;
    }
    // returns a selected Item in the hot bar
    public Item getActiveItem(){
        return hotBarSlots[selectedHotBarSlot];
    }
    public void setSelectedHotBarSlot(int slotIndex){
        if (slotIndex >= 15 && slotIndex < 20) {
            selectedHotBarSlot = slotIndex - 15;
        }
    }
    public int getSelectedHotBarSlot(){
        return 15 + selectedHotBarSlot;
    }

    // swap two slots
    public void swapSlots(int slotId1, int slotId2){
        Item temp = getSlot(slotId1);
        setSlot(slotId1, getSlot(slotId2));
        setSlot(slotId2, temp);
    }
    //take a partial stack from a slot
    public Item splitSlots(int slotId, int amount) {
        Item source = getSlot(slotId);
        if (source == null) {
            return null;
        }
        Item taken = source.split(amount);
        if (source.getQuantity() <= 0) {
            setSlot(slotId, null);
        }
        return taken;
    }



}
