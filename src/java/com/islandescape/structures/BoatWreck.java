package com.islandescape.structures;

import com.islandescape.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

//this class will be structure of boatwreck wich we will need to repair
public class BoatWreck extends WorldStructure{

    private List<String> fixedParts; //making list for storing all parts crafted from player fpr repairing the boat
    private final List<String> requiredParts;//the full list of repairs needed for boat
    private List<String> boatInventory;//invertory of boat

    public BoatWreck(double x, double y, String name) {
        super(x, y, "Boat Wreck Site");

        this.requiredParts = List.of(  //initialize required repairs and their amount
                "Mast", "Frame", "Sail", "Rudder",
                "Fittings", "Fittings", "Fittings", "Fittings", "Fittings",
                "Planks", "Planks", "Planks", "Planks", "Planks", "Planks", "Planks", "Planks"
        );

        this.fixedParts = new ArrayList<>();
    }

    public void addPart(String partName) {
        int fixedCount = 0; //make count how many parts of repair part are already in the boa
        for (String p : fixedParts) {
            if (p.equals(partName)) fixedCount++;
        }
    //counting amount of parts of pepair part that is still needed from required list
        int requiredCount = 0;
        for (String p : requiredParts) {
            if (p.equals(partName)) requiredCount++;
        }

//add the part only in case if  current amount is less than  requirement
        if (fixedCount < requiredCount) {
            fixedParts.add(partName);
        }
    }//adding element to boat


    public boolean isFullyRepaired() {//check for is the repair complete fully or not
        return getMissingParts().isEmpty();//if list is empty - everything is repaired
    }

    public List<String> getMissingParts() {//get all  of remaining requird  repairs
        List<String> missing = new ArrayList<>(requiredParts);//copy of full repairs nedeed list to get the remaining parts
        for (String p : fixedParts) {
            missing.remove(p);
        }
        return missing;
    }

    @Override
    public void interact(double playerx, double playery) {
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));
//check if palyer is near the boat
        if (distance < 15.0) {
            List<String> missing = getMissingParts();
            for (String part : missing) {
                addPart(part);
            }
        }
    }
    //loads everything player needs on boat
    public void loadItems(String part) {

    }
    // method for depart the boat from island
    public boolean depart(double playerx, double playery, Inventory playerInventory) {
        if (!isFullyRepaired()) {//check that boat is fully ready
            System.out.println("Boat is not fixed");
            return false;
        }

       //check that player invertory is empmty
        if (!playerInventory.isEmpty()) {
            System.out.println("Still items in invertory");
            return false;
        }

       //calc distance
        double distance = Math.sqrt(Math.pow(this.x - playerx, 2) + Math.pow(this.y - playery, 2));

        //if distance enough - sail happen
        if (distance <= 10.0) {
            System.out.println("We sailed away");
            return true;
        }

        System.out.println("too far from boat ");
        return false;
    }
}
