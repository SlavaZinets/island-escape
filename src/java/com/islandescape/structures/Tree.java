package com.islandescape.structures;


import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

public class Tree extends NaturalResource{

    public Tree(double x, double y, String name) {
        super(x, y, name, 2, "Wood", "Tropical leaves");
    }

    @Override
    public void interact(double playerx, double playery, Inventory inventory) {

//distance cal
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));

        if (distance <= 40) {
            if (this.health > 0) {
                this.health--;
                System.out.println("Mining:  " + this.health);

                if (this.health == 0) {
                    Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "wood");
                    inventory.addItem(wood);
                    Item leaves = new Item(ItemType.TROPICAL_LEAVES, ItemCategory.PRIMARY_RESOURCE, "Tropical leaves", "leaves");
                    inventory.addItem(leaves);

                    System.out.println("Done: ");
                    System.out.println("You got: " + this.itemName);
                    System.out.println(" and  " + this.secondItem);
                }
            }
        } else {
            System.out.println("too far");
        }
    }
}
