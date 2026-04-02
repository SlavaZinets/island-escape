package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory testInventory;

    @BeforeEach
    void setUp(){
         testInventory = new Inventory();

    }
    @Test
    public void testAddItem(){
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 3);
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Banana", "Nutritious fruit", 4);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for wood extraction");
        Item fish = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 2);

        //  add Item to array
        testInventory.addItem(banana);
        // test whether the item appears in the array
        assertEquals(banana, testInventory.getItems()[0]);

        // add another Item with the same ItemType, as together quantity is less than 10 they should stack
        Item yellowBanana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Yellow banana", "Nutritious fruit", 3);
        assertEquals(yellowBanana, testInventory.getItems()[0]);

        // add Item coconut
        testInventory.addItem(coconut);
        assertEquals(coconut, testInventory.getItems()[1]);

        //add Item fish
        testInventory.addItem(fish);
        assertEquals(fish, testInventory.getItems()[2]);

        //add unstackable item
        testInventory.addItem(axe);
        assertEquals(axe, testInventory.getItems()[3]);

        // add another unstackable Item with the same ItemType
        Item axe1 = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for wood extraction");
        testInventory.addItem(axe1);
        assertEquals(axe1, testInventory.getItems()[4]);

    }

    @Test
    public void testRemoveItem(){

        // delete slot with fish
        Item fish = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 2);
        testInventory.removeItem(fish);
        assertNull(testInventory.getItems()[2]);

        //delete slot with axe
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for wood extraction");
        testInventory.removeItem(axe);
        assertNull(testInventory.getItems()[3]);

        //test whether the same ItemType in another slot is not deleted
        assertNotNull(testInventory.getItems()[4]);


    }



}
