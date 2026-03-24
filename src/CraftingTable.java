import java.awt.Graphics2D;


 //this is a subclass called crafting table that is used to enables professional item creation procceses in the game
   //providing ability for  players as a working place to combine raw resources into something useful as tools  or gear



public class CraftingTable extends WorldStructure {
    private double interactionRange; //max distance for the player to use table
//constructor with own attribte
    public CraftingTable(double x, double y) {
        super(x, y, "Crafting Table");
        this.interactionRange = 100.0; //standard interaction distance
    }
    //stub methods
    
  //method when a player interacts with crafting table


    @Override
    public void interact(double x, double y) {

    }

    //nternal helper to check wether player is physically near the table
    public boolean isPlayerNearby(double x, double y) {
     
        return false;
    }
   
     //method to check if player is within range to use the crafting table
    @Override
    public boolean isPlayerInRange(double x, double y) {
        return isPlayerNearby(0,0);
    }

    
     //drawing the crafting table sprite on screen
       @Override
    public void render(Graphics2D g2) {
    
    }
 
}