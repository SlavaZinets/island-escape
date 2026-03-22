import java.awt.Graphics2D;


 //so for game we make World structure abstract base class for all of the  objects on the map of game
 //here we define base attributes and  methods for subclasses we will extend to this

public abstract class WorldStructure {
    //attributes  that we currently have 
    protected double x;    //x coordinate
    protected double y;  // y-coordinate
    protected String name;   // Display name of structure

    //constructor for/classes
    public WorldStructure(doub le x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
}