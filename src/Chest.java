import java.awt.Graphics2D;


//subclass that is representing  storage container to  allow players to store collecteditems in a fixed location for  freeing up inventory space
 
public class Chest extends WorldStructure {
    private Item[] contents; //storage for items as an array
    private boolean opened;  //open/closed state
    //own attributes except for based ones
    
    
    //constructor
    public Chest(double x, double y) {
        super(x, y, "Chest");
        this.contents = new Item[10]; //it will default storage capacity
        this.opened = false; //also close by defaut 
    }//methods with stubs
    
    //method  when a player interacts with the chest.

  @Override
  public void interact(Player player) {
   
      open(player);
  }


   // method that toggles the chest state to opened.
  public void open(Player player) {
   
  }


   //metjod to remove item from a specific slot in the chest
 
  public Item takeItem(int index, Player player) {

      return null;
  }


   //method thatchecks if the player is close enough to interact with chest
 
  @Override
  public boolean isPlayerInRange(Player player) {

      return false;
  }

 
   //method -draws chest sprite on the screen
  @Override
  public void render(Graphics2D g2) {
     
  }
}
}