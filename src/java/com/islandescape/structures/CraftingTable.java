package com.islandescape.structures;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

import java.awt.Graphics2D;

 //this is a subclass called crafting table that is used to enables professional item creation procceses in the game
   //providing ability for  players as a working place to combine raw resources into something useful as tools  or gear

public class CraftingTable extends WorldStructure {
    private double interactionRange; //max distance for the player to use table
//constructor with own attribte
    public CraftingTable(double x, double y) {
        super(x, y, "Crafting Table");
        this.interactionRange = 100.0; //standard interaction distance
    }

    //main method for crafting items
    public void Crafting(double playerX, double playerY, Item[] playerInventory, int[] inventoryIndices) {


        //check the distance
        if (!isPlayerNearby(playerX, playerY)) {
            System.out.println(" player is too far");
            return;
        }

        
        //transfer from inventory to 2x2 grid for craft
        Item[] grid = new Item[4];
        for (int i = 0; i < inventoryIndices.length && i < 4; i++) {
            int slotIdx = inventoryIndices[i];
            if (slotIdx >= 0 && slotIdx < playerInventory.length) {
                grid[i] = playerInventory[slotIdx];
                playerInventory[slotIdx] = null;
            }
        }

        //find matching mix from list
        Item result = craftFromMix(grid);

        //custom craft case as if  no mix  matches but the grid is not empty - create a custom item
        if (result == null && isGridNotEmpty(grid)) {
            result = createCustomItem();
        }

        // give the crafted item back to the player
        if (result != null) {
            System.out.println("Crafted: " + result.getName() + " (" + result.getCategory() + ")");

            //look for the first empty slot  in the inventory to store the result
            for (int i = 0; i < playerInventory.length; i++) {
                if (playerInventory[i] == null) {
                    playerInventory[i] = result;
                    // Item added, stop looking for slots
                    break;
                }
            }
        } else {

        }
        System.out.println("Crafting failed");
        }
    //defining the specific mixs here
    private Item craftFromMix(Item[] grid) {
        int wood = count(grid, "Wood");
        int stone = count(grid, "Stone");
        int vines = count(grid, "Vines");
        int rope = count(grid, "Rope");
        int leaves = count(grid, "Tropical Tree Leaves");
        int hide = count(grid, "Boar Hide");
        int planks = count(grid, "Planks");
        int clay = count(grid, "Clay");
        int coconut = count(grid, "Coconut");
        int shell = count(grid, "Coconut Shell");

        if (vines == 2) return new Item(ItemType.ROPE, ItemCategory.CRAFTABLE_RESOURCE, "Rope", "Crafted rope");
        if (shell == 1 && vines == 1) return new Item(ItemType.COCONUT_BOTTLE, ItemCategory.CUTLERY, "Coconut Bottle", "A bottle made from coconut");
        if (stone == 1 && wood == 1 && rope == 1) return new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
        if (stone == 2 && wood == 1 && rope == 1) return new Item(ItemType.PICKAXE, ItemCategory.TOOL, "Pickaxe", "Pickaxe for mining");
        if (wood == 2) return new Item(ItemType.PLANKS, ItemCategory.CRAFTABLE_RESOURCE, "Planks", "Wooden planks");
        if (wood == 3 && rope == 1) return new Item(ItemType.MAST, ItemCategory.CRAFTABLE_RESOURCE, "Mast", "Boat mast");
        if (planks == 2 && rope == 2) return new Item(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, "Frame", "Boat frame");
        if (planks == 2 && stone == 1 && rope == 1) return new Item(ItemType.RUDDER, ItemCategory.CRAFTABLE_RESOURCE, "Rudder", "Boat rudder");
        if (stone == 2 && rope == 1 && wood == 1) return new Item(ItemType.FITTINGS, ItemCategory.CRAFTABLE_RESOURCE, "Fittings", "Boat fittings");

        return null;
    }

    //helper method for crafting() method - unknown mix yields null
    private Item createCustomItem() {
        return null;
    }

    // helper to count items in grid
    private int count(Item[] grid, String name) {
        int c = 0;
        for (Item item : grid) {

            if (item != null && item.getName().equalsIgnoreCase(name)) {
                c++;
            }
        }
        return c;
    }

    // helper for check if grid has at least one item
    private boolean isGridNotEmpty(Item[] grid) {
        for (Item i : grid) {
            if (i != null) return true;
        }
        return false;
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