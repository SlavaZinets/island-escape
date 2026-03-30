package com.islandescape.structures;

//subclass that is representing  storage container to  allow players to store collecteditems in a fixed location for  freeing up inventory space
 
public class Chest extends WorldStructure {
    private Item[] contents; //storage for items as an array
    public  boolean opened;  //open/closed state
    //own attributes except for based ones

    
    
    //constructor
    public Chest(double x, double y) {
        super(x, y, "Chest");
        this.contents = new Item[25]; //it will default storage capacity
        this.opened = false; //also close by defaut

        
        this.contents[0] = new Item("Stone", "Primary Resources");//stone
        this.contents[1] = new Item("Stone", "Primary Resources");
        this.contents[2] = new Item("Stone", "Primary Resources");
        this.contents[3] = new Item("Stone", "Primary Resources");
        this.contents[4] = new Item("Vines", "Primary Resources");//vines
        this.contents[5] = new Item("Vines", "Primary Resources");
        this.contents[6] = new Item("Vines", "Primary Resources");
        this.contents[7] = new Item("Vines", "Primary Resources");
        this.contents[8] = new Item("Vines", "Primary Resources");
        this.contents[9] = new Item("Vines", "Primary Resources");
        this.contents[10] = new Item("Banana", "Food");//food
        this.contents[11] = new Item("Banana", "Food");
        this.contents[12] = new Item("Banana", "Food");
        this.contents[13] = new Item("Banana", "Food");
        this.contents[14] = new Item("Coconut", "Food");
        this.contents[15] = new Item("Coconut", "Food");
        this.contents[16] = new Item("Tropical Tree Leaves", "Primary Resources");//leaves
        this.contents[17] = new Item("Tropical Tree Leaves", "Primary Resources");
        this.contents[18] = new Item("Tropical Tree Leaves", "Primary Resources");
        this.contents[19] = new Item("Tropical Tree Leaves", "Primary Resources");
        this.contents[20] = new Item("Wood", "Primary Resources");//wood
        this.contents[21] = new Item("Wood", "Primary Resources");
        this.contents[22] = new Item("Wood", "Primary Resources");
        this.contents[23] = new Item("Wood", "Primary Resources");
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