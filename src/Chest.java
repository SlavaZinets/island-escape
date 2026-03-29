//subclass that is representing  storage container to  allow players to store collecteditems in a fixed location for  freeing up inventory space
 
public class Chest extends WorldStructure {
    private Item[] contents; //storage for items as an array
    public  boolean opened;  //open/closed state
    //own attributes except for based ones

    
    
    //constructor
    public Chest(double x, double y) {
        super(x, y, "Chest");
        this.contents = new Item[10]; //it will default storage capacity
        this.opened = false; //also close by defaut
        this.contents[0] = new Item("stone", "Custom");//put the element into chest to test
    }//methods with stubs
    
    //method  when a player interacts with the chest.

    @Override
    public void interact(double x, double y) {
        if (this.opened == true) {

            this.opened = false;
        } else {

            this.opened = true;
        }
    }




   // method that toggles the chest state to opened.
  public void open(double x, double y) {
      this.opened = true;
  }


   //method to remove item from a specific slot in the chest
 
  public Item takeItem(double x, double y) {
      //player cant take items if the chest is curently closed
        if (!opened) {
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