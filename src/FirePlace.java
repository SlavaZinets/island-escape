import java.awt.Graphics2D;



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
  //stub methids for this class
    //method interacting with the fireplace (e.g lighting it)
   @Override
   public void interact(Player player) {
       
       light(player);
   }

   
    //attempting to ignite the fireplace.
   public boolean light(Player player) {
      
       return false;
   }

   
     //eturns the current status of the fire
    public boolean isLit() {
       return lit;
   }

   
      //increases the burn timer by adding fuel items
   public void addFuel(Item item) {
  
   }

   
     //updating fireplace state based on elapsed time 
     public void update(double deltaTime) {
  
   }

  
    //calculating heat intensity at a given set of coordinates
   public double getWarmthAt(double targetX, double targetY) {

       return 0.0;
   }

   
   //methid checks if the player is within range to interact with the fireplace
   @Override
   public boolean isPlayerInRange(Player player) {
       
       return false;
   }

   //drawing the fireplace and fire animations
   @Override
   public void render(Graphics2D g2) {
    
   }
}