package com.islandescape.structures;//subclass that is representing  storage container to  allow players to store collecteditems in a fixed location for  freeing up inventory space


import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

public class Chest extends WorldStructure {
    private Item[] contents; //storage for items as an array
    public  boolean opened;  //open/closed state
    //own attributes except for based ones

    
    
    //constructor
    public Chest(double x, double y) {
        super(x, y, "Chest");
        this.contents = new Item[25]; //it will default storage capacity
        this.opened = false; //also close by defaut

        
        this.contents[0] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
        this.contents[1] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
        this.contents[2] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
        this.contents[3] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
        this.contents[4] = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines");
        this.contents[5] = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines");
        this.contents[6] = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines");
        this.contents[7] = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines");
        this.contents[8] = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines");
        this.contents[9] = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines");
        this.contents[10] = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit");
        this.contents[11] = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit");
        this.contents[12] = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit");
        this.contents[13] = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit");
        this.contents[14] = new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "A hard coconut");
        this.contents[15] = new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "A hard coconut");
        this.contents[16] = new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical Tree Leaves", "Large tropical leaves");
        this.contents[17] = new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical Tree Leaves", "Large tropical leaves");
        this.contents[18] = new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical Tree Leaves", "Large tropical leaves");
        this.contents[19] = new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical Tree Leaves", "Large tropical leaves");
        this.contents[20] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood");
        this.contents[21] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood");
        this.contents[22] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood");
        this.contents[23] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood");
    }
    
    //method when a player interacts with the chest.

    @Override
    public void interact(double playerx, double playery) {
        if (isPlayerInRange(playerx, playery)) {
            this.opened = !this.opened;  // toggle boolean uopdate instead of if else
        }else{
                System.out.println("Too far away");
            }
    }




   // method that toggles the chest state to opened.
  public void open(double playerx, double playery) {
      if (isPlayerInRange(playerx, playery)) {
          this.opened = true;
      }
  }


   //method to remove item from a specific slot in the chest
 
  public Item takeItem(double playerx, double playery) {
      //player cant take items if the chest is curently closed
        if (!isPlayerInRange(playerx, playery) || !opened) {
          return null;
      }
      //get  through the array to find first non-null item to take it
      for(int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                 Item itemToReturn = contents[i];
               contents[i] = null; //remove the item from the chest slot - signigy is taken
                return itemToReturn;
          }
      }
      return null; //return null if no items remaining
  }

    public boolean addItem(Item item) {
          if (item == null) {
             return false;
        }

        //adding to chest
        for (int i = 0; i < this.contents.length; i++) {
            if (this.contents[i] == null) {
                  this.contents[i] = item;
                  System.out.println(item.getName() + " added to chest " + i);
                return true;
            }
        }

          System.out.println("Chest is full");
        return false;
    }



}