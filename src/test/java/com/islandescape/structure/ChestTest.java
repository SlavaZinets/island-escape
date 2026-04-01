package com.islandescape.structure;

import com.islandescape.structures.Chest;
import com.islandescape.item.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChestTest {

    @Test
    public void interact() {
        Chest chest = new Chest(10.0, 10.0);
        chest.interact(5.0, 5.0);


        assertTrue(chest.opened, "chest had to open");

    }


    @Test
    public void takeItem() {
        Chest chest = new Chest(10.0, 10.0);
        chest.open(10.0, 10.0);
        assertNotNull(chest.takeItem(5.0, 5.0));

    }
    @Test
    public void addItem() {
        Chest chest = new Chest(100, 100);
        Item wood = new Item("wood", "Custom");
        assertTrue(chest.addItem(wood), "item addded");
    }

}
