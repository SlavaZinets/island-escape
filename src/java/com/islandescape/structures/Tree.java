package com.islandescape.structures;



public class Tree extends NaturalResource{

    public Tree(double x, double y, String name) {
        super(x, y, "tree", 2, "Tree");
    }

    @Override
    public void interact(double playerx, double playery) {
//distance cal
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));

        if (distance <= 40) {
            if (this.health > 0) {
                this.health--;
                System.out.println("Mining:  " + this.health);

                if (this.health == 0) {
                    System.out.println("Done: here is " + this.itemName);
                }
            }
        } else {
            System.out.println("too far");
        }
    }
}
