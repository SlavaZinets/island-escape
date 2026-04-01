package com.islandescape.inventory;

import com.islandescape.item.Item;

public class Inventory {
    Item[] slots;
    int capacity;


    public boolean addItem(Item item){
        return true;
    }
    public boolean removeItem(Item item){
        return true;
    }
    public boolean isFull(){
        return true;
    }
    public Item[] getItems(){
        return slots;
    }
    public boolean hasItem(Item item, int amount){
        return true;
    }
    public int getItemCount(Item item){
        return 0;
    }

}
