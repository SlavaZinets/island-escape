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
           this.burnTimer = 50.0;//amount of time it will light from start
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
   public void addFuel(Item item) {
       if (!this.lit) {
           System.out.println("light fire first");
           return;
       }//check if no lit - message

            //check the given item is not null
       if (item != null) {
           String itemName = item.getName().toLowerCase();

           if (itemName.contains("wood") || itemName.contains("stick")) {
               this.burnTimer += 100.0;  //wood gives extra 100
               System.out.println("+100s");
           }
           else if (itemName.contains("coal") || itemName.contains("fuel")) {
               this.burnTimer += 300.0; //coal/fuel - 300 to burntimer
               System.out.println("+300s");
           }
           else {
               //if something else thrown  +10
               this.burnTimer += 10.0;
               System.out.println("+10s");
           }
       }
   }

   
     //updating fireplace state based on elapsed time 
     public void update(double deltaTime) {

             // check if  fire is currently burning
             if (this.lit) {
                 //subtract the elapsed time from  timer
                 this.burnTimer -= deltaTime;

                 //if fuel  completely burned out
                 if (this.burnTimer <= 0) {
                     this.lit = false;
                     this.burnTimer = 0.0; //reset to avoid negative values
                     System.out.println("fire is out");
                 }

         }
   }

  
    //calculating heat intensity at a given set of coordinates
   public double getWarmthAt(double targetX, double targetY) {

       // if the fire is not lit - no heat
       if (!this.lit) {
           return 0.0;
       }

       // calculating distance between the fireplace and the player here target x -y by using  Phytagorous theorem
       double dx = this.x - targetX;
       double dy = this.y - targetY;
       double distance = Math.sqrt(dx * dx + dy * dy);

       //if target is within enought  warmth radius
       if (distance <= this.warmthRadius) {
           double intensity = 1.0 - (distance / this.warmthRadius);
           return 100.0 * intensity; // Returns warmth value from 0 to 100
       }

       return 0.0;

   }
    public Item cook(Item food) {

        if (this.lit && food != null) {

            //chhange name to cooked state
            String newName = food.getName().replace("Raw", "Cooked");
            return new Item(newName, "Custom");
        }
        return null;
    }

    public double getLightRadius() {
        return this.lit ? 200.0 : 0.0;
    }

}