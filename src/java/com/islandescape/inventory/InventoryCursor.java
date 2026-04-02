package com.islandescape.inventory;

import com.islandescape.item.Item;

/*
1) Left mouse click on slot-> cursor holds all items
2) Right mouse click on slot -> cursor holds half quantity of the items in selected slot
3) (When cursor holds item) left-click on mouse -> place items in selected slot
4) (When cursor holds item) right-click on mouse -> place one item in selected slot
5) (When cursor holds item and the slot is full) right or left-click on mouse -> swap items in slot with items in cursor
 */
public class InventoryCursor {
    Item heldItem;

    //pick all items from the slot
    public void pickUp(Inventory inventory, int slotId){

    }
    // pick half if the Items from the slot
    public void pickUpHalf(Inventory inventory, int slotId){

    }
    //place all item in slot
    public void placeAll(Inventory inventory, int slotId){

    }
    // place one Item in the slot
    public void placeOne(Inventory inventory, int slotId){

    }
    //get current holding Item
    public Item getHeldItem(){
        return null;
    }
    // check whether the cursor is empty or not
    public boolean isEmpty(){
        return true;
    }
    // clear cursor
    public void clear(){

    }

}
