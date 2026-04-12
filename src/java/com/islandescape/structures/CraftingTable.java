package com.islandescape.structures;

import java.awt.Graphics2D;

 //this is a subclass called crafting table that is used to enables professional item creation procceses in the game
   //providing ability for  players as a working place to combine raw resources into something useful as tools  or gear

public class CraftingTable extends WorldStructure {
    private double interactionRange; //max distance for the player to use table
//constructor with own attribte
    public CraftingTable(double x, double y) {
        super(x, y, "Crafting Table");
        this.interactionRange = 50.0; //interaction distance
    }

//method when a player interacts with crafting table
    @Override
    public void interact(double playerx, double playery) {
        if (isPlayerNearby(playerx, playery)) {
            System.out.println("Opening crafting table");
        } else {
            System.out.println("player is too far");
        }
    }

    //internal helper to check wether the player is physically near the table
    public boolean isPlayerNearby(double playerx, double playery) {
// we calculate here from player to table by getting distance using the Pythagorean theorem by treating   coordinate differences as triangle sides to verify the player interaction range for it
        // сalculat horizontal and vertical distance(legs of triangle)
        double a = this.x - playerx;
        double b = this.y - playery;

       //apply the formulae
        double distanceSquared = (a * a) + (b * b);
         //get square root inn order to have actual distance
         double distance = Math.sqrt(distanceSquared);
         //compare vlaue we got with  interaction range
        return distance <= this.interactionRange;
    }

     //drawing the crafting table sprite on screen
       @Override
    public void render(Graphics2D g2) {
    
    }


}