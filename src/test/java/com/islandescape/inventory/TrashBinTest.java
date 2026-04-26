package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrashBinTest {

    private TrashBin trashBin;

    @BeforeEach
    void setUp() {
        trashBin = new TrashBin();
    }

    @Test
    public void newTrashBinIsEmpty() {
        // a fresh bin holds nothing and reports empty
        assertNull(trashBin.getItem());
        assertTrue(trashBin.isEmpty());
    }

    @Test
    public void setItemThenGetItemReturnsSameInstance() {
        // putting an item into the bin makes it retrievable and non-empty
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5);
        trashBin.setItem(wood);

        assertSame(wood, trashBin.getItem());
        assertFalse(trashBin.isEmpty());
    }

    @Test
    public void clearMakesItEmptyAgain() {
        // after clear() the bin is back to its initial empty state
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 3);
        trashBin.setItem(stone);

        trashBin.clear();

        assertNull(trashBin.getItem());
        assertTrue(trashBin.isEmpty());
    }
}
