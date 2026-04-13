package com.islandescape.structure;
import com.islandescape.structures.BananaTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class BananaTreeTest {
    @Test
    public void interact() {
        BananaTree banana = new BananaTree(50.0, 50.0, "Bananas Tree");
        banana.interact(50.0, 50.0);
        assertEquals(0, banana.getHealth(), "it should be collected rn");
    }
}
