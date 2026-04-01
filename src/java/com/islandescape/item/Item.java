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


   //Getters
   public int getQuantity(){
      return  quantity;
   }

   public int getMaxStackSize() {
      return maxStackSize;
   }

   public ItemCategory getCategory() {
      return category;
   }

   public ItemType getType() {
      return type;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
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
   public boolean merge(Item item){
      return false;
   }
}