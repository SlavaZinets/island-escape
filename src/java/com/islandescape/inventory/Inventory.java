package com.islandescape.inventory;

import com.islandescape.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
       private List<Item> items;

    public Inventory() {
          this.items = new ArrayList<>();
    }
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public List<Item> getItems() {
        return items;
    }
    public void clear() {
        items.clear();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }

}
