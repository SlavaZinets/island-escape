package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemType;

public class Inventory {
    Item[] slots;
    int capacity;
    int selectedHotBarSlot;

    public Inventory(){
        capacity = 20;
        slots = new Item[20];
        selectedHotBarSlot = 0;

    }
    // add item to the array
    public boolean addItem(Item item){
        return true;
    }

    // remove item entirely
    public boolean removeItem(Item item){
        return true;
    }
    // decrease the item quantity on one for crafting purpose
    public boolean removeOne(ItemType itemType){
        return true;
    }
    public boolean isFull(){
        return true;
    }
    // returns entire inventory
    public Item[] getItems(){
        return slots;
    }
    // get slot by slotID
    public Item getSlot(int slotId){
        return null;
    }
    // set item to the slot by ID
    public void setSlot(int slotId, Item item){

    }
    // check whether there is an item in inventory in the right quantity
    public boolean hasItem(Item item, int quantity){
        return true;
    }
    public int getItemCount(Item item){
        return 0;
    }
    // Returns the bottom line in inventory, UI draws even when inventory is closed
    public Item[] getHotBarSlots(){
        return null;
    }
    // returns a selected Item in the hot bar
    public Item getActiveItem(){
        return null;
    }
    public void setSelectedHotBarSlot(int key){

    }
    public int getSelectedHotBarSlot(){
        return 0;
    }

    // swap two slots
    public void swapSlots(int slotId1, int slotId2){

    }
    //take a partial stack from a slot
    public Item splitSlots(int slotId1, int slotId2){
        return null;
    }



}
