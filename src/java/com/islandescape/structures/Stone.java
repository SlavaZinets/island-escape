package com.islandescape.structures;
//stone sub class of natural resource with Stone itemanme which is thematerial getting from resource after mining
public class Stone extends NaturalResource {

    public Stone(double x, double y, String name) {

        super(x, y, "Stone ", 3, "Stone");
    }

    @Override
    public void interact(double playerx, double playery) {

    }
}