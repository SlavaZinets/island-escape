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
 //now for methods of abstract class with stub methods yet
    
    
    public void interact(double x, double y) {
     //methos when a player interacts with this structure
    }

    public abstract void interact(boolean x, boolean y);

    //checks if the layer is within valid interaction distance
    public boolean isPlayerInRange(double x, double y) {
        return false;
    }


    // handles the visual representation of the structure.
    public void render(Graphics2D g2) {
    
    }
}