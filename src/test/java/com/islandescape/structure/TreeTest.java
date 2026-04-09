package com.islandescape.structure;

import com.islandescape.structures.Tree;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class TreeTest {
    @Test

    public void interact() {
        Tree tree = new Tree(50.0, 50.0, "Oak Tree");
        tree.interact(50.0, 50.0);
        tree.interact(50.0, 50.0);
        assertEquals(0, tree.getHealth(), "health should be gone");
    }
}
