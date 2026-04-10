package com.islandescape.structures;
import java.util.ArrayList;
import java.util.List;
//this class will be structure of boatwreck wich we will need to repair
public class BoatWreck extends WorldStructure{
    private List<String> fixedParts; //making the list for storing all parts crafted from player fpr repairing the boat
    private final List<String> requiredParts;//the full list of repairs needed for boat
    public BoatWreck(double x, double y, String name, List<String> requiredParts) {
        super(x, y, "Boat Wreck Site");
        this.requiredParts = requiredParts;
        this.fixedParts = new ArrayList<>();
    }
    public void addPart(String partName) {}//adding element to boat

    public boolean isFullyRepaired() {//check for is the repair complete fully or not
        return false;
    }
    @Override
    public void interact(double playerx, double playery) {}


}
