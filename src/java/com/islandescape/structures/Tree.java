package com.islandescape.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tree extends NaturalResource{

    public Tree(double x, double y, String name) {
        super(x, y, "tree", 2, "Tree");
    }

    @Override
    public void interact(double playerx, double playery) {

    }
}
