package com.islandescape.item;

import java.util.Set;

public class Item {
   private ItemType type;
   private ItemCategory category;
   private String name;
   private String description;
   private int quantity;
   private static final int MAX_STACK_SIZE = 10;
   private static final Set<ItemCategory> UNSTACKABLE = Set.of(
           ItemCategory.TOOL, ItemCategory.CUTLERY
   );

   public Item(ItemType type, ItemCategory category, String name, String description, int quantity) {
      this.type = type;
      this.category = category;
      this.name = name;
      this.description = description;
      this.quantity = quantity;
   }

   public Item(ItemType type, ItemCategory category, String name, String description) {
      this(type, category, name, description, 1);
   }

   //Getters
   public int getQuantity() {
      return quantity;
   }

   public int getMaxStackSize() {
      return MAX_STACK_SIZE;
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

   public boolean isStackable() {
      return !UNSTACKABLE.contains(this.category);
   }

   public boolean hasSpace(Item item) {
      return isStackable() && quantity + item.quantity <= MAX_STACK_SIZE;
   }

   public Item split(int amount) {
      if (quantity <= 0 || amount <= 0) {
         return null;
      }
      int actual = Math.min(amount, quantity);
      quantity -= actual;
      return new Item(type, category, name, description, actual);
   }

   public boolean merge(Item item) {
      if (item == null || item.quantity <= 0) {
         return false;
      }
      if (this.type == item.type && isStackable()
              && this.quantity + item.quantity <= MAX_STACK_SIZE) {
         this.quantity += item.quantity;
         item.quantity = 0;
         return true;
      }
      return false;
   }
}