package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory testInventory;

    Inventory testInventory2;

    Inventory testInventory3;

    @BeforeEach
    void setUp(){
         testInventory = new Inventory();
         testInventory2 = new Inventory();
         fillItemsArray(testInventory2.getItems());
         testInventory3 = new Inventory();

    }

    private void fillItemsArray(Item[] items) {
        for(int i = 0; i<items.length; i++){
            items[i] = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 3);
        }
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
    @Test
    public void testRemoveOne(){
        //Remove one banana
        testInventory.removeOne(ItemType.BANANA);
        assertEquals(6, testInventory.getItems()[0].getQuantity());
        //Remove another banana
        testInventory.removeOne(ItemType.BANANA);
        assertEquals(5, testInventory.getItems()[0].getQuantity());

    }
    @Test
    public void testIsFull(){
        assertFalse(testInventory.isFull());
        assertTrue(testInventory2.isFull());
    }
    @Test
    public void testGetSlot(){
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 10);
        testInventory3.addItem(banana);
        assertEquals(banana, testInventory3.getItems()[0]);

        Item receivedSlot = testInventory3.getSlot(0);
        assertEquals(banana, receivedSlot);


    }
    @Test
    public void testSetSlot(){
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Banana", "Nutritious fruit", 4);
        assertNull(testInventory3.getItems()[2]);
        testInventory.setSlot(2,coconut);
        assertEquals(coconut, testInventory.getItems()[2]);

    }
    @Test
    public void testHasItem(){
        // put 3 Wood in slot 0 and 4 Wood in slot 5
        Item wood1 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
        Item wood2 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 4);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
        testInventory3.setSlot(0, wood1);
        testInventory3.setSlot(5, wood2);
        testInventory3.setSlot(2, axe);

        // total Wood = 3 + 4 = 7, should have 5
        assertTrue(testInventory3.hasItem(wood1, 5));

        // should have exactly 7
        assertTrue(testInventory3.hasItem(wood1, 7));

        // should NOT have 8
        assertFalse(testInventory3.hasItem(wood1, 8));

        // should have 1 Axe
        assertTrue(testInventory3.hasItem(axe, 1));

        // should NOT have 2 Axes (unstackable, only 1 exists)
        assertFalse(testInventory3.hasItem(axe, 2));

        // Stone not in inventory at all
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 3);
        assertFalse(testInventory3.hasItem(stone, 1));
    }

    @Test
    public void testGetItemCount(){
        // empty inventory should return 0
        assertEquals(0, testInventory3.getItemCount(
                new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood")));

        // put Wood in two different slots
        Item wood1 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
        Item wood2 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5);
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 2);
        testInventory3.setSlot(0, wood1);
        testInventory3.setSlot(7, wood2);
        testInventory3.setSlot(3, banana);

        // total Wood = 3 + 5 = 8
        assertEquals(8, testInventory3.getItemCount(wood1));

        // total Banana = 2
        assertEquals(2, testInventory3.getItemCount(banana));

        // Stone not present = 0
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
        assertEquals(0, testInventory3.getItemCount(stone));
    }

    @Test
    public void getSelectedHotBarSlot(){
        // default should be 0
        assertEquals(0, testInventory3.getSelectedHotBarSlot());

        // set to slot 3
        testInventory3.setSelectedHotBarSlot(3);
        assertEquals(3, testInventory3.getSelectedHotBarSlot());

        // put an item in hotbar slot 3 (index 15 + 3 = 18)
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
        testInventory3.setSlot(18, axe);

        // active item should be the axe
        assertEquals(axe, testInventory3.getActiveItem());

        // switch to slot 0, hotbar slot 0 is index 15, which is empty
        testInventory3.setSelectedHotBarSlot(0);
        assertNull(testInventory3.getActiveItem());
    }

    @Test
    public void testSwapSlots(){
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5);
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 3);
        testInventory3.setSlot(0, wood);
        testInventory3.setSlot(4, stone);

        // swap slot 0 and slot 4
        testInventory3.swapSlots(0, 4);
        assertEquals(stone, testInventory3.getSlot(0));
        assertEquals(wood, testInventory3.getSlot(4));

        // swap with an empty slot
        testInventory3.swapSlots(0, 10);  // slot 10 is null
        assertNull(testInventory3.getSlot(0));
        assertEquals(stone, testInventory3.getSlot(10));
    }

    @Test
    public void testSplitSlots(){
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 8);
        testInventory3.setSlot(2, wood);

        // split 3 from slot 2
        Item taken = testInventory3.splitSlots(2, 3);
        assertNotNull(taken);
        assertEquals(3, taken.getQuantity());
        assertEquals(5, testInventory3.getSlot(2).getQuantity());

        // split more than available — takes what's left
        Item takenAll = testInventory3.splitSlots(2, 100);
        assertNotNull(takenAll);
        assertEquals(5, takenAll.getQuantity());
        assertNull(testInventory3.getSlot(2));  // slot cleaned up

        // split from empty slot — returns null
        Item nothing = testInventory3.splitSlots(2, 3);
        assertNull(nothing);
    }





}
