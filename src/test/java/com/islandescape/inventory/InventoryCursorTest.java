package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class InventoryCursorTest {
    Inventory inventory;
    InventoryCursor inventoryCursor;

    @BeforeEach
    void setUp(){
        inventory = new Inventory();
        inventoryCursor = new InventoryCursor();

    }
    private void fillItemsArray(Item[] items) {
        for(int i = 0; i<items.length; i++){
            items[i] = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 3);
        }
    }
    @Test
    public void testPickUp(){
        // test whether the item picked correctly
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 10);
        inventory.setSlot(0, banana);
        inventoryCursor.pickUp(inventory, 0);
        assertEquals(banana, inventoryCursor.getHeldItem());
        // check whether the slot after picking item is empty
        assertNull(inventory.getSlot(0));
    }
    @Test
    public void testPickUpHalf(){
        // pickUpHalf with odd quantity: ceil(9/2) = 5 taken, 4 left in slot
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 9);
        inventory.setSlot(0, banana);
        inventoryCursor.pickUpHalf(inventory, 0);
        assertEquals(5, inventoryCursor.getHeldItem().getQuantity());
        assertEquals(4, inventory.getSlot(0).getQuantity());

        // place cursor back to clear it
        inventoryCursor.placeAll(inventory, 1);

        // pickUpHalf with even quantity: ceil(6/2) = 3 taken, 3 left in slot
        Item coconut = new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "Coconut", 6);
        inventory.setSlot(2, coconut);
        inventoryCursor.pickUpHalf(inventory, 2);
        assertEquals(3, inventoryCursor.getHeldItem().getQuantity());
        assertEquals(3, inventory.getSlot(2).getQuantity());

        // place cursor back to clear it
        inventoryCursor.placeAll(inventory, 3);

        // pickUpHalf with quantity 1: ceil(1/2) = 1 taken, slot becomes empty
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 1);
        inventory.setSlot(4, stone);
        inventoryCursor.pickUpHalf(inventory, 4);
        assertEquals(1, inventoryCursor.getHeldItem().getQuantity());
        assertNull(inventory.getSlot(4));
    }
    @Test
    public void testPlaceAllItems(){
        // first add the new Item, and check whether cursor holds it
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for wood extraction");
        inventory.setSlot(3, axe);
        inventoryCursor.pickUp(inventory, 3);
        assertEquals(axe, inventoryCursor.getHeldItem());

        //Case 1 when the target slot is empty
        //Place the item at index 10
        inventoryCursor.placeAll(inventory, 10);
        assertEquals(axe, inventory.getSlot(10));
        //Check whether the cursor does not hold any item
        assertNull(inventoryCursor.getHeldItem());

        //Case 2 when the target slot is mot empty
        // and if the target slot item has the same Item type as the item that is held in the cursor
        // and when the sum of item quantity in target slot + quantity of item in the cursor is less than MaxStackSize

        Item fish = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 3); //cursor item
        inventory.setSlot(8,fish);
        Item fish1 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 5);// target slot
        inventory.setSlot(9, fish1);

        // pick item at index 8
        inventoryCursor.pickUp(inventory, 8);
        // check whether item picked correctly
        assertEquals(fish, inventoryCursor.getHeldItem());

        inventoryCursor.placeAll(inventory, 9);
        // check if items merged correctly
        assertEquals(8, inventory.getSlot(9).getQuantity());
        // check whether the cursor became 0
        assertNull(inventoryCursor.getHeldItem());
        //Case 3 when the target slot is mot empty
        // and if the target slot item has the same Item type as the item that is held in the cursor
        // and when the sum of item quantity in target slot + quantity of item in the cursor is bigger than MaxStackSize
        Item fish2 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 11); //cursor item
        inventory.setSlot(10,fish2);
        Item fish3 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 5);// target slot
        inventory.setSlot(11, fish3);

        // pick item at index 10
        inventoryCursor.pickUp(inventory, 10);
        // check whether item picked correctly
        assertEquals(fish2, inventoryCursor.getHeldItem());

        inventoryCursor.placeAll(inventory, 11);
        assertEquals(Item.MAX_STACK_SIZE, inventory.getSlot(11).getQuantity());
        assertEquals((11 + 5) - Item.MAX_STACK_SIZE, inventoryCursor.getHeldItem().getQuantity());
        //Case 3 when the target slot is mot empty
        // and if the target slot item has the different Item type as the item that is held in the cursor
        Item fish4 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 11); //cursor item
        inventory.setSlot(12,fish4);
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 9);
        inventory.setSlot(13,banana);

        inventoryCursor.pickUp(inventory, 12);
        // check whether item picked correctly
        assertEquals(fish4, inventoryCursor.getHeldItem());

        inventoryCursor.placeAll(inventory, 13);
        assertEquals(fish4, inventory.getSlot(13));
        assertEquals(banana, inventoryCursor.getHeldItem());

    }
    @Test
    public void testPlaceOne(){
        //Case 1 if the target slot is empty
        Item fish = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 3); //cursor item
        inventory.setSlot(1,fish);

        // pick item at index 1
        inventoryCursor.pickUp(inventory, 1);
        // check whether item picked correctly
        assertEquals(fish, inventoryCursor.getHeldItem());

        //make sure that next slot is null
        inventory.setSlot(2, null);

        inventoryCursor.placeOne(inventory, 2);

        assertEquals(1, inventory.getSlot(2).getQuantity());
        assertEquals(2, inventoryCursor.getHeldItem().getQuantity());

        //case 2 when the target slot is not empty and has the same Item type
        // and target slot quantity + 1 is less or equal than MAX_STACK_SIZE

        Item fish1 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 8); //cursor item
        inventory.setSlot(3,fish1);
        Item fish2 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 7);// target slot
        inventory.setSlot(4, fish2);


        inventoryCursor.pickUp(inventory, 3);
        // check whether item picked correctly
        assertEquals(fish1, inventoryCursor.getHeldItem());

        inventoryCursor.placeOne(inventory, 4);
        assertEquals(8, inventory.getSlot(4).getQuantity());
        assertEquals(7, inventoryCursor.getHeldItem().getQuantity());

        //case 3 when the target slot is not empty and has the same Item type
        // and target slot quantity + 1 is bigger than MAX_STACK_SIZE

        Item fish3 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 1); //cursor item
        inventory.setSlot(5,fish3);
        Item fish4 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 10);// target slot
        inventory.setSlot(6, fish4);


        inventoryCursor.pickUp(inventory, 5);
        // check whether item picked correctly
        assertEquals(fish3, inventoryCursor.getHeldItem());

        inventoryCursor.placeOne(inventory, 6);
        assertEquals(fish3, inventory.getSlot(6));
        assertEquals(fish4, inventoryCursor.getHeldItem());

        //case 4 when the target slot is not empty and has the different Item type
        // and target slot quantity + 1 is bigger than MAX_STACK_SIZE

        Item fish5 = new Item(ItemType.FISH , ItemCategory.FOOD, "Fish", "Fish", 1); //cursor item
        inventory.setSlot(7,fish5);
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Coconut", "Nutritious fruit", 4);
        inventory.setSlot(8, coconut);


        inventoryCursor.pickUp(inventory, 7);
        // check whether item picked correctly
        assertEquals(fish5, inventoryCursor.getHeldItem());

        inventoryCursor.placeOne(inventory, 8);
        assertEquals(fish5, inventory.getSlot(8));
        assertEquals(coconut, inventoryCursor.getHeldItem());


    }
}
