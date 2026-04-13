package com.islandescape.structures;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

//class for Coconat palms to get coconuts
public class CoconutPalm extends NaturalResource{
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
                    Item coconut = new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "coconut");
                    inventory.addItem(coconut);
                    System.out.println("you gathered: ");
                    System.out.println("  " + this.itemName);

                }
            }
        } else {
            System.out.println("Too far to cocounts");
        }
    }

}
