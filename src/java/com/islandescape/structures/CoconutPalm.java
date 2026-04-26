package com.islandescape.structures;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.ConsumableItem;

//class for Coconat palms to get coconuts
public class CoconutPalm extends NaturalResource {
    public CoconutPalm(double x, double y, String name) {
        super(x, y, name, 2, "Coconut");
    }

    @Override
    public void interact(double playerx, double playery, Inventory inventory) {
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));

        if (distance <= 40) {
            if (this.health > 0) {
                this.health--;
                System.out.println("Shaking the palm" + this.health);

                if (this.health == 0) {
                    // Drop a ConsumableItem so the player can later eat it with the F/. key.
                    inventory.addItem(ConsumableItem.coconut(1));
                    System.out.println("you gathered: ");
                    System.out.println("  " + this.itemName);
                }
            }
        } else {
            System.out.println("Too far to cocounts");
        }
    }
}
