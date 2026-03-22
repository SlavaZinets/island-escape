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
        this.opened = false; //also closed by defaut 
    }
}