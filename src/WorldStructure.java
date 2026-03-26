import java.awt.Graphics2D;


 //so for game we make World structure abstract base class for all of the  objects on the map of game
 //here we define base attributes and  methods for subclasses we will extend to this

public abstract class WorldStructure {
    //attributes  that we currently have 
    protected double x;    //x coordinate
    protected double y;  // y-coordinate
    protected String name;   // Display name of structure

    //constructor for/classes
    public WorldStructure(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    
    public void interact(double x, double y) {
        double distance = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));

        // check if player is within interaction range
        if (distance <= 40) {
            //close enough - interaction go
            System.out.println("interact happening with " + name);

        } else {
               // player too far
            System.out.println("too far from " + name + " (distance: " + distance +  ")");

         }
    }



    //checks if the layer is within valid interaction distance
    public boolean isPlayerInRange(double x, double y) {
        return false;
    }


    // handles the visual representation of the structure
    public void render(Graphics2D g2) {
    
    }
}