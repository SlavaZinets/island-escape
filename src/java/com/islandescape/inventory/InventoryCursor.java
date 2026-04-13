package com.islandescape.inventory;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.item.Item;

/*
1) Left mouse click on slot-> cursor holds all items
2) Right mouse click on slot -> cursor holds half quantity of the items in selected slot
3) (When cursor holds item) left-click on mouse -> place items in selected slot
4) (When cursor holds item) right-click on mouse -> place one item in selected slot
5) (When cursor holds item and the slot is full) right or left-click on mouse -> swap items in slot with items in cursor
 */
public class InventoryCursor {
    private Item heldItem;

    //pick all items from the slot
    public void pickUp(Inventory inventory, int slotId){
        Item slotItem = inventory.getSlot(slotId);
        if (slotItem == null) return;

        heldItem = slotItem;
        inventory.setSlot(slotId, null);
    }

    // pick half of the Items from the slot
    public void pickUpHalf(Inventory inventory, int slotId){
        Item slotItem = inventory.getSlot(slotId);
        if (slotItem == null) return;

        int total = slotItem.getQuantity();
        int takeAmount = (int) Math.ceil(total / 2.0);

        heldItem = inventory.splitSlots(slotId, takeAmount);
    }

    //place all items in slot
    public void placeAll(Inventory inventory, int slotId){
        if (heldItem == null) return;

        Item slotItem = inventory.getSlot(slotId);

        // slot is empty — place everything
        if (slotItem == null) {
            inventory.setSlot(slotId, heldItem);
            heldItem = null;
            return;
        }

        // same type — try to merge
        if (slotItem.getType() == heldItem.getType()) {
            int spaceInSlot = Item.MAX_STACK_SIZE - slotItem.getQuantity();

            if (heldItem.getQuantity() <= spaceInSlot) {
                // fits entirely
                slotItem.merge(heldItem);
                heldItem = null;
            } else {
                // partial merge — fill slot to max, keep rest on cursor
                Item partial = heldItem.split(spaceInSlot);
                slotItem.merge(partial);
            }
            return;
        }

        // different type or full — swap
        inventory.setSlot(slotId, heldItem);
        heldItem = slotItem;
    }

    // place one Item in the slot
    public void placeOne(Inventory inventory, int slotId){
        if (heldItem == null) return;

        Item slotItem = inventory.getSlot(slotId);

        // slot is empty — place one
        if (slotItem == null) {
            Item one = heldItem.split(1);
            inventory.setSlot(slotId, one);
            if (heldItem.getQuantity() <= 0) {
                heldItem = null;
            }
            return;
        }

        // same type and has space — place one
        if (slotItem.getType() == heldItem.getType()
                && slotItem.getQuantity() < Item.MAX_STACK_SIZE) {
            Item one = heldItem.split(1);
            slotItem.merge(one);
            if (heldItem.getQuantity() <= 0) {
                heldItem = null;
            }
            return;
        }

        // full or different type — swap
        inventory.setSlot(slotId, heldItem);
        heldItem = slotItem;
    }

    //get current holding Item
    public Item getHeldItem(){
        return heldItem;
    }

    // check whether the cursor is empty or not
    public boolean isEmpty(){
        return heldItem == null;
    }

    // set held item directly (used for crafted results)
    public void setHeldItem(Item item) {
        this.heldItem = item;
    }

    // clear cursor
    public void clear(){
        heldItem = null;
    }

    // Pick up all items from a crafting grid slot into the cursor (left-click)
    public void pickUpFromCraftingGrid(CraftingSystem cs, int slot) {
        if (!isEmpty()) return;
        Item taken = cs.takeOut(slot);
        if (taken != null) {
            heldItem = taken;
        }
    }

    // Pick up half items from a crafting grid slot into the cursor (right-click)
    public void pickUpHalfFromCraftingGrid(CraftingSystem cs, int slot) {
        if (!isEmpty()) return;
        Item existing = cs.getSlot(slot);
        if (existing == null) return;

        int total = existing.getQuantity();
        int takeAmount = (int) Math.ceil(total / 2.0);

        if (takeAmount >= total) {
            // Take everything
            heldItem = cs.takeOut(slot);
        } else {
            // Split — take half, leave rest in grid
            heldItem = existing.split(takeAmount);
            if (existing.getQuantity() <= 0) {
                cs.takeOut(slot);
            }
        }
    }

    // Place held item into a crafting grid slot (left-click)
    public void placeIntoCraftingGrid(CraftingSystem cs, int slot, int playerId) {
        if (heldItem == null) return;

        Item existing = cs.getSlot(slot);
        if (existing == null) {
            // Empty slot — place entire held item
            cs.placeIn(slot, heldItem, playerId);
            heldItem = null;
        } else if (existing.getType() == heldItem.getType()) {
            // Same type — try to merge
            int space = Item.MAX_STACK_SIZE - existing.getQuantity();
            if (heldItem.getQuantity() <= space) {
                // Fits entirely
                existing.merge(heldItem);
                heldItem = null;
            } else {
                // Partial merge — fill slot to max, keep rest on cursor
                Item partial = heldItem.split(space);
                existing.merge(partial);
            }
        } else {
            // Different type — swap
            cs.placeIn(slot, heldItem, playerId);
            heldItem = existing;
        }
    }

    // Place one item into a crafting grid slot (right-click)
    public void placeOneIntoCraftingGrid(CraftingSystem cs, int slot, int playerId) {
        if (heldItem == null) return;

        Item existing = cs.getSlot(slot);
        if (existing == null) {
            // Empty slot — place one
            Item one = heldItem.split(1);
            cs.placeIn(slot, one, playerId);
        } else if (existing.getType() == heldItem.getType()
                && existing.getQuantity() < Item.MAX_STACK_SIZE) {
            // Same type with space — merge one
            Item one = heldItem.split(1);
            existing.merge(one);
        } else {
            return; // Different type or full — do nothing
        }

        if (heldItem.getQuantity() <= 0) {
            heldItem = null;
        }
    }

}
