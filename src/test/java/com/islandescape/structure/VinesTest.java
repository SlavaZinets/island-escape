package com.islandescape.structure;

import com.islandescape.structures.VinesPlace;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VinesTest {
    
    @Test
    public void interact() {
        VinesPlace vines = new VinesPlace(100.0, 100.0, "Vines on tree");
        vines.interact(100.0, 100.0);
        assertEquals(0, vines.getHealth());
    }
}
