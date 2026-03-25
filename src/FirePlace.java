import java.awt.Graphics2D;



 //this class is fireplace that is used for warmth and cooking on island 
 // as it is providing  heat source to prevent player from freezing and allowing food processin
 //so in technical this class manages the fire state, burn time duration and heat radiation area

public class FirePlace extends WorldStructure {
    private boolean lit;          //current state - burning/extinguished
    double burnTimer;     //remaining time before the fire goes out
    private double warmthRadius;  //distance at which players feel the heat
    
    //constructor
    public FirePlace(double x, double y) {
        super(x, y, "Fireplace");//base stuff
        this.lit = false;
        this.burnTimer = 0.0; //0.0 as default value 
        this.warmthRadius = 150.0; //default radius for heat radiation
    }

    //method interacting with the fireplace (e.g lighting it)
    @Override
    public void interact(double x, double y) {

    }

    private boolean isPlayerNearby(double x, double y) {
        double a = this.x - x;
           double b = this.y - y;
        double distance = Math.sqrt(a * a + b * b);
         return distance <= 100.0;
    }


    //attempting to ignite the fireplace
   public boolean light(double x, double y) {
       if (isPlayerNearby(x, y)) {
           this.lit = true;
           return true;
       }
       // if plaayer far away  - return false
       return false;

   }

   
     //returns the current status of the fire
    public boolean isLit() {
       return this.lit;
   }

   
      //increases the burn timer by adding fuel items
   public void addFuel(Chest.Item item) {

   }

   
     //updating fireplace state based on elapsed time 
     public void update(double deltaTime) {
  
   }

  
    //calculating heat intensity at a given set of coordinates
   public double getWarmthAt(double targetX, double targetY) {

       return 0.0;
   }

   

}