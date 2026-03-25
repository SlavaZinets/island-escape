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

    //internal helper to check wether the player is physically near the table
    public boolean isPlayerNearby(double x, double y) {
// we calculate here from player to table by getting distance using the Pythagorean theorem by treating   coordinate differences as triangle sides to verify the player interaction range for it
        // сalculat horizontal and vertical distance(legs of triangle)
        double a = this.x - x;
        double b = this.y - y;

       //apply the formulae
        double distanceSquared = (a * a) + (b * b);
         //get square root inn order to have actual distance
         double distance = Math.sqrt(distanceSquared);
         //compare vlaue we got with  interaction range
        return distance <= this.interactionRange;
    }

     //drawing the crafting table sprite on screen
       @Override
    public void render(Graphics2D g2) {
    
    }
 
}