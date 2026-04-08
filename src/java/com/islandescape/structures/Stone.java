package com.islandescape.structures;
//stone sub class of natural resource with Stone itemanme which is thematerial getting from resource after mining
public class Stone extends NaturalResource {

    public Stone(double x, double y, String name) {

        super(x, y, "Stone ", 3, "Stone");
    }

    @Override
    public void interact(double playerx, double playery) {
        //calculate distance
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));

        if (distance <= 40) {
            if (this.health > 0) {
                this.health--; //decrease the value of health whime minning
                 System.out.println("Mining:" + this.health);

                if (this.health == 0) {
                    System.out.println("You mined: " + this.itemName);
                }//show what material is mined
            }
        } else {
            System.out.println("too far");
        }

    }
}