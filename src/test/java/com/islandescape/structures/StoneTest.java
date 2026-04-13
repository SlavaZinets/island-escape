package com.islandescape.structure;

import com.islandescape.structures.Stone;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoneTest {

    @Test
    public void interact() {
        Stone stone = new Stone(100.0, 100.0, "Large Rock");
        stone.interact(100.0, 100.0);
        assertEquals(2, stone.getHealth(), "health of stone should be 2");
    }

}
