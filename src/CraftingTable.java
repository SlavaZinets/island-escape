import java.awt.Graphics2D;


 //this is a subclass called crafting table that is used to enables professional item creation procceses in the game
   //providing ability for  players as a working palce to combine raw resources into something useful as tools  or gear



public class CraftingTable extends WorldStructure {
    private double interactionRange; //max distance for the player to use table
//constructor with own attribte
    public CraftingTable(double x, double y) {
        super(x, y, "Crafting Table");
        this.interactionRange = 100.0; //standard interaction distance
    }
    //stub methods
    
  //method when a player interacts with rafting table
    @Override
    public void interact(Player player) {
        
    }

     //nternal helper to check wether player is physically near the table
    public boolean isPlayerNearby(Player player) {
     
        return false;
    }
   
     //method to check if player is within range to use the crafting table
    @Override
    public boolean isPlayerInRange(Player player) {
        return isPlayerNearby(player);
    }

    
     //drawing the crafting table sprite on screen
       @Override
    public void render(Graphics2D g2) {
    
    }
 
}