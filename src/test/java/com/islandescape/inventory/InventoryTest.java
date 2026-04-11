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
         // fill both arrays directly since getItems() returns a copy
         fillItemsArray(testInventory2.slots);
         fillItemsArray(testInventory2.hotBarSlots);
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
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "Nutritious fruit", 4);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for wood extraction");
        Item fish = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 2);

        // add Item to inventory — goes to hotbar first (slot 15 in combined view)
        testInventory.addItem(banana);
        assertEquals(banana, testInventory.getSlot(15));

        // add another Banana — should merge into existing stack (3+3=6)
        Item yellowBanana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Yellow banana", "Nutritious fruit", 3);
        testInventory.addItem(yellowBanana);
        assertEquals(6, testInventory.getSlot(15).getQuantity());

        // add Item coconut — different type, goes to next hotbar slot
        testInventory.addItem(coconut);
        assertEquals(coconut, testInventory.getSlot(16));

        // add Item fish
        testInventory.addItem(fish);
        assertEquals(fish, testInventory.getSlot(17));

        // add unstackable item
        testInventory.addItem(axe);
        assertEquals(axe, testInventory.getSlot(18));

        // add another unstackable Item with the same ItemType — cannot merge, new slot
        Item axe1 = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for wood extraction");
        testInventory.addItem(axe1);
        assertEquals(axe1, testInventory.getSlot(19));

    }

    @Test
    public void testRemove(){
        // setup: slot[0] = 3 Wood, slot[4] = 7 Wood
        Item wood1 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
        Item wood2 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 7);
        testInventory3.setSlot(0, wood1);
        testInventory3.setSlot(4, wood2);

        // remove 1 — takes from first stack
        assertTrue(testInventory3.remove(ItemType.WOOD, 1));
        assertEquals(2, testInventory3.getSlot(0).getQuantity());
        assertEquals(7, testInventory3.getSlot(4).getQuantity());

        // remove 4 — empties first stack (2), takes 2 from second
        assertTrue(testInventory3.remove(ItemType.WOOD, 4));
        assertNull(testInventory3.getSlot(0));  // 2 Wood gone, slot cleared
        assertEquals(5, testInventory3.getSlot(4).getQuantity());  // 7 - 2 = 5

        // remove more than available — should fail and not remove anything
        assertFalse(testInventory3.remove(ItemType.WOOD, 100));
        assertEquals(5, testInventory3.getSlot(4).getQuantity());  // unchanged

        // remove across main and hotbar
        Item hotbarWood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
        testInventory3.getHotBarSlots()[0] = hotbarWood;
        // total = 5 (main) + 3 (hotbar) = 8, remove 7
        assertTrue(testInventory3.remove(ItemType.WOOD, 7));
        assertNull(testInventory3.getSlot(4));  // 5 taken, slot cleared
        assertEquals(1, testInventory3.getHotBarSlots()[0].getQuantity());  // 3 - 2 = 1

        // remove item type that doesn't exist
        assertFalse(testInventory3.remove(ItemType.STONE, 1));
    }
    @Test
    public void testIsFull(){
        assertFalse(testInventory.isFull());
        // testInventory2 was filled in setUp
        assertTrue(testInventory2.isFull());
    }
    @Test
    public void testGetSlot(){
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 10);
        testInventory3.setSlot(3, banana);

        Item receivedSlot = testInventory3.getSlot(3);
        assertEquals(banana, receivedSlot);

        // test hotbar slot access
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
        testInventory3.setSlot(16, axe);
        assertEquals(axe, testInventory3.getSlot(16));
    }
    @Test
    public void testSetSlot(){
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "Nutritious fruit", 4);
        assertNull(testInventory3.getSlot(2));
        testInventory3.setSlot(2, coconut);
        assertEquals(coconut, testInventory3.getSlot(2));
    }
    @Test
    public void testHasItem(){
        // put 3 Wood in main slot 0 and 4 Wood in main slot 5
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

        // hasItem should also count items in hotbar
        Item hotbarWood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
        testInventory3.getHotBarSlots()[0] = hotbarWood;
        // total Wood now = 3 + 4 (main) + 3 (hotbar) = 10
        assertTrue(testInventory3.hasItem(wood1, 10));
        assertFalse(testInventory3.hasItem(wood1, 11));
    }

    @Test
    public void testGetItemCount(){
        // empty inventory should return 0
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood");
        assertEquals(0, testInventory3.getItemCount(wood));

        // put Wood in two different main slots
        Item wood1 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
        Item wood2 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5);
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 2);
        testInventory3.setSlot(0, wood1);
        testInventory3.setSlot(7, wood2);
        testInventory3.setSlot(3, banana);

        // total Wood in main = 3 + 5 = 8
        assertEquals(8, testInventory3.getItemCount(wood1));

        // total Banana = 2
        assertEquals(2, testInventory3.getItemCount(banana));

        // Stone not present = 0
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone");
        assertEquals(0, testInventory3.getItemCount(stone));

        // add Wood to hotbar — should be counted too
        Item hotbarWood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 2);
        testInventory3.getHotBarSlots()[1] = hotbarWood;
        // total Wood = 3 + 5 (main) + 2 (hotbar) = 10
        assertEquals(10, testInventory3.getItemCount(wood1));
    }

    @Test
    public void getSelectedHotBarSlot(){
        // default should be 15 (global index of hotbar slot 0)
        assertEquals(15, testInventory3.getSelectedHotBarSlot());

        // set to hotbar slot 3 (global index 18)
        testInventory3.setSelectedHotBarSlot(18);
        assertEquals(18, testInventory3.getSelectedHotBarSlot());

        // put an item in hotbar slot 3
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
        testInventory3.getHotBarSlots()[3] = axe;

        // active item should be the axe
        assertEquals(axe, testInventory3.getActiveItem());

        // switch to hotbar slot 0 (global index 15), which is empty
        testInventory3.setSelectedHotBarSlot(15);
        assertNull(testInventory3.getActiveItem());
    }

    @Test
    public void testSwapSlots(){
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 5);
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 3);

        // swap two main slots
        testInventory3.setSlot(0, wood);
        testInventory3.setSlot(4, stone);
        testInventory3.swapSlots(0, 4);
        assertEquals(stone, testInventory3.getSlot(0));
        assertEquals(wood, testInventory3.getSlot(4));

        // swap main slot with an empty main slot
        testInventory3.swapSlots(0, 10);  // slot 10 is null
        assertNull(testInventory3.getSlot(0));
        assertEquals(stone, testInventory3.getSlot(10));

        // swap main slot with hotbar slot (index 15 = hotbar[0])
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for chopping");
        testInventory3.getHotBarSlots()[0] = axe;
        testInventory3.swapSlots(4, 15); // main slot 4 (wood) ↔ hotbar slot 0 (axe)
        assertEquals(axe, testInventory3.getSlot(4));
        assertEquals(wood, testInventory3.getSlot(15));
    }

    @Test
    public void testSplitSlots(){
        // split from main slot
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

        // split from hotbar slot (index 16 = hotbar[1])
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 6);
        testInventory3.getHotBarSlots()[1] = stone;
        Item hotbarTaken = testInventory3.splitSlots(16, 2);
        assertNotNull(hotbarTaken);
        assertEquals(2, hotbarTaken.getQuantity());
        assertEquals(4, testInventory3.getSlot(16).getQuantity());
    }





}
