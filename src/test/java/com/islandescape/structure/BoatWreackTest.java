package com.islandescape.structure;

import com.islandescape.structures.BoatWreck;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class BoatWreackTest {
    @Test
    public void addPart() {
        BoatWreck boat = new BoatWreck(0, 0, "Boat Wreck Site");
        assertEquals(17, boat.getMissingParts().size());//we should have 17 part by default
        boat.addPart("Mast");
        assertEquals(16, boat.getMissingParts().size());//16 after adding the elemnt
        assertFalse(boat.getMissingParts().contains("Mast"));
    }

    @Test
    public void getMissingParts() {

    }
    @Test
    public void interact() {}
}
