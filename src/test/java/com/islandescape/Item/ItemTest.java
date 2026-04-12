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
       Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 3);
       Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for stone extraction");

        assertTrue(wood.isStackable()); // true
        assertFalse(axe.isStackable()); // false
    }
    @Test
    public void testSplitItem(){
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 8);
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 4);
        Item vines = new Item(ItemType.VINES, ItemCategory.PRIMARY_RESOURCE, "Vines", "Flexible vines", 10);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for stone extraction");


        // check cases when the amount is less than the item quantity
        Item splitWoodResult = wood.split(2);
        assertEquals(2,splitWoodResult.getQuantity()); // new split item with quantity 2
        assertEquals(6, wood.getQuantity()); // new wood quantity after split

        // check when the amount is bigger than the item quantity
        Item splitStoneResult = stone.split(5);
        assertEquals(4,splitStoneResult.getQuantity()); // new split item with quantity 4,
        assertEquals(0, stone.getQuantity()); // new stone quantity after split

        //check case when we pass an unstackable item

        assertEquals(1, axe.split(12).getQuantity());

        //check case when amount equals to 0
        assertNull(vines.split(0));

    }
    @Test
    public void testMergeItem(){
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 8);
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", 4);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for tree extraction");


        // check case when the quantity + merge quantity <= stackSize and the same ItemType
        Item mergeWood1 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 2);
        assertTrue(wood.merge(mergeWood1)); // true


        // check case when the quantity + merge quantity > stackSize
        Item mergeWood2 = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", 8);
        assertFalse(wood.merge(mergeWood2)); // false

        // check case when ItemType is different
        assertFalse(stone.merge(axe)); // false

        // check case when merge two unstackable items
        Item axe1 = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Axe for stone extraction");
        assertFalse(axe.merge(axe1));


    }


}
