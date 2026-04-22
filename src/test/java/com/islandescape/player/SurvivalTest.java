package com.islandescape.player;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class SurvivalTest {
    @Test
    void testConsume() {
        SurvivalStats stats = new SurvivalStats();
        stats.hunger = 50.0;
        Item banana = new Item(
                ItemType.BANANA,
                ItemCategory.FOOD,
                "Banan",
                "fruit"
        );
        stats.consume(banana);
        assertEquals(70.0, stats.hunger, 0.1);
    }
}
