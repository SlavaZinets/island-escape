package com.islandescape.structures;

//vines place to get vine material
public class VinesPlace extends NaturalResource {

    public VinesPlace(double x, double y, String name) {
        super(x, y, "vine", 1, "Vines");
    }

    @Override
    public void interact(double playerx, double playery) {
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));
//distance cal
        if (distance <= 40) {
            if (this.health > 0) {
                 this.health--; //
                  System.out.println("Cutting vines");

                if (this.health == 0) {
                    System.out.println("material got: " + this.itemName);
                }
            }
        } else {
            System.out.println("Cannot reach");
        }
    }
}

