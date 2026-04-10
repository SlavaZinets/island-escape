package com.islandescape.structures;

import java.util.ArrayList;
import java.util.List;

//this class will be structure of boatwreck wich we will need to repair
public class BoatWreck extends WorldStructure{
    private List<String> fixedParts; //making list for storing all parts crafted from player fpr repairing the boat
    private final List<String> requiredParts;//the full list of repairs needed for boat
    public BoatWreck(double x, double y, String name) {
        super(x, y, "Boat Wreck Site");
        this.requiredParts = List.of(  //initialize required repairs and their amount
                "Mast", "Frame", "Sail", "Rudder",
                "Fittings", "Fittings", "Fittings", "Fittings", "Fittings",
                "Planks", "Planks", "Planks", "Planks", "Planks", "Planks", "Planks", "Planks"
        );
        this.fixedParts = new ArrayList<>();
    }
    public void addPart(String partName) {}//adding element to boat

    public boolean isFullyRepaired() {//check for is the repair complete fully or not
        return false;
    }
    public List<String> getMissingParts() {//get all  of remaining requird  repairs
        return new ArrayList<>();
    }

    @Override
    public void interact(double playerx, double playery) {}


}
