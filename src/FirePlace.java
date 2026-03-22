mport java.awt.Graphics2D;



 //this class is fireplace that is used for warmth and cooking on island 
 // as it is providing  heat source to prevent player from freezing and allowing food processin
 //so in technical this class manages the fire state, burn time duration and heat radiation area

public class FirePlace extends WorldStructure {
    private boolean lit;          //current state - burning/extinguished
    private double burnTimer;     //remaining time before the fire goes out
    private double warmthRadius;  //distance at which players feel the heat
    
    //constructor
    public FirePlace(double x, double y) {
        super(x, y, "Fireplace");//base stuff
        this.lit = false;
        this.burnTimer = 0.0; //0.0 as default value 
        this.warmthRadius = 150.0; //default radius for heat radiation
    }
}