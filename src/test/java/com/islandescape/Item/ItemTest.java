package com.islandescape.Item;


import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class ItemTest {

    @Test
    public void testIsStackable(){
       Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 3);
       Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for stone extraction");

        assertTrue(banana.isStackable()); // true
        assertFalse(axe.isStackable()); // false
    }
    @Test
    public void testSplitItem(){
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 8);
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Banana", "Nutritious fruit", 4);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for stone extraction");

        // check cases when the amount is less than the item quantity
        Item splitBananaResult = banana.split(2);
        assertEquals(2,splitBananaResult.getQuantity()); // new split item with quantity 2
        assertEquals(6, banana.getQuantity()); // new banana quantity after split

        // check when the amount is bigger than the item quantity
        Item splitCoconutResult = coconut.split(5);
        assertEquals(4,splitCoconutResult.getQuantity()); // new split item with quantity 4,
        assertEquals(0, banana.getQuantity()); // new coconut quantity after split

        //check case when we pass an unstackable item

        assertEquals(1, axe.split(12).getQuantity());

    }
    @Test
    public void testMergeItem(){
        Item banana = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 8);
        Item coconut= new Item(ItemType.COCONUT, ItemCategory.FOOD, "Banana", "Nutritious fruit", 4);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for tree extraction");


        // check case when the quantity + merge quantity <= stackSize and the same ItemType
        Item mergeBanana1 = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 2);
        assertTrue(banana.merge(mergeBanana1)); // true


        // check case when the quantity + merge quantity > stackSize
        Item mergeBanana2 = new Item(ItemType.BANANA, ItemCategory.FOOD, "Banana", "Nutritious fruit", 8);
        assertFalse(banana.merge(mergeBanana2)); // false

        // check case when ItemType is different
        assertFalse(coconut.merge(axe)); // false

        // check case when merge two unstackable items
        Item axe1 = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for stone extraction");
        assertFalse(axe.merge(axe1));


    }


}
