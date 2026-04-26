package com.islandescape.structures;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.ConsumableItem;

//class for bananas
public class BananaTree extends NaturalResource {

    public BananaTree(double x, double y, String name) {
        super(x, y, name, 1, "Banana");
    }

    @Override
    public void interact(double playerx, double playery, Inventory inventory) {
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));

        if (distance <= 40) {
            if (this.health > 0) {
                this.health--;
                System.out.println("collect bananas" + this.health);

                if (this.health == 0) {
                    // Drop a ConsumableItem so the player can later eat it with the F/. key.
                    inventory.addItem(ConsumableItem.banana(1));
                    System.out.println("You got: " + this.itemName);
                }
            }
        } else {
            System.out.println("Too far from bananas");
        }
    }
}
