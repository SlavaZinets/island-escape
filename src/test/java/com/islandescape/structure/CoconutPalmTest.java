package com.islandescape.structure;
import com.islandescape.structures.CoconutPalm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CoconutPalmTest {
    @Test
    public void interact() {
        CoconutPalm palm = new CoconutPalm(160.0, 160.0, "Palm");

        palm.interact(160.0, 160.0);
        palm.interact(160.0, 160.0);
        assertEquals(0, palm.getHealth());
    }
}
