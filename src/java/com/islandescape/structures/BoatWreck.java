package com.islandescape.structures;
import java.util.ArrayList;
import java.util.List;
//this class will be structure of boatwreck wich we will need to repair
public class BoatWreck extends WorldStructure{
    private List<String> fixedParts; //making the list for storing all parts crafted from player fpr repairing the boat
    public BoatWreck(double x, double y, String name) {
        super(x, y, "Boat Wreck Site");
        this.fixedParts = new ArrayList<>();
    }
    @Override
    public void interact(double playerx, double playery) {}


}
