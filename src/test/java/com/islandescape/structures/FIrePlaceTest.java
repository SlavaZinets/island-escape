package com.islandescape.structures;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class FIrePlaceTest {
    @Test
    public void interact() {
        FirePlace fire = new FirePlace(100, 100);
         fire.interact(100, 100);
        assertTrue(fire.isLit(), "Fire suppouse to lit");
    }

    @Test
    public void light() {
        FirePlace fire = new FirePlace(100, 100);
        boolean success = fire.light(130, 130);
        assertTrue(success, "works if near the fireplace");
    }
    @Test
    public void addFuel() {
        FirePlace fire = new FirePlace(100, 100);
         fire.light(100, 100);
         double startTime = 100.0;
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood stick", "A wooden stick");
        fire.addFuel(wood);
        assertTrue(fire.burnTimer > startTime, "Timer suppose to increase");
    }
    @Test
    public void update() {
        FirePlace fire = new FirePlace(100, 100);
        fire.light(100, 100);
        fire.update(10.0);
        assertEquals(40.0, fire.burnTimer, 0.1);
    }


    @Test
    public void getWarmthAt() {
        FirePlace fire = new FirePlace(100, 100);
        fire.light(100, 100);
        double warmth = fire.getWarmthAt(240, 100);
        assertTrue(warmth > 0, " warmer near the fireplace");

    }
    @Test
    public void cook() {
        FirePlace fire = new FirePlace(100, 100);
        Item rawFood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Raw Wood", "A raw piece");
        fire.light(100, 100);
        Item result = fire.cook(rawFood); //cookin
        //if result null or no match for name - fails
        assertNotNull(result);
        assertEquals("Cooked Wood", result.getName());
    }

}
