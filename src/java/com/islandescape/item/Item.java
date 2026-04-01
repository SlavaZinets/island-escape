package com.islandescape.item;

import com.islandescape.player.Player;

public class Item {
   ItemType type;
   ItemCategory category;
   String name;
   String description;
   int quantity;
   final int maxStackSize = 15;

   public Item(ItemType type, ItemCategory category, String name, String description, int quantity) {
      this.type = type;
      this.category = category;
      this.name = name;
      this.description = description;
      this.quantity = quantity;
   }
   public Item(ItemType type, ItemCategory category, String name, String description) {
      this.type = type;
      this.category = category;
      this.name = name;
      this.description = description;
      quantity = 1;
   }
   public boolean use(Player player) {
       return false;
   }
   public boolean isStackable() {
       return true;
   }
   public Item split(int amount) {
       return null;
   }
   public void merge(Item item){

   }




}