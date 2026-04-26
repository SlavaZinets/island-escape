package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
    Tests for the four trash-bin variants on InventoryCursor:
        pickUpFromTrash, pickUpHalfFromTrash, placeAllInTrash, placeOneInTrash.


 */
public class InventoryCursorTrashTest {

    private TrashBin trashBin;
    private InventoryCursor cursor;

    @BeforeEach
    void setUp() {
        trashBin = new TrashBin();
        cursor = new InventoryCursor();
    }

    private Item wood(int qty) {
        return new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", qty);
    }

    private Item stone(int qty) {
        return new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", qty);
    }

    //  pickUpFromTrash

    @Test
    public void pickUpFromTrashEmpty_NoOp() {
        // empty trash → cursor still empty afterwards
        cursor.pickUpFromTrash(trashBin);

        assertTrue(cursor.isEmpty());
        assertTrue(trashBin.isEmpty());
    }

    @Test
    public void pickUpFromTrash_FullStackTransfersToCursor() {
        // trash has 5 wood, cursor empty → cursor holds the same 5 wood, trash empty
        Item w = wood(5);
        trashBin.setItem(w);

        cursor.pickUpFromTrash(trashBin);

        assertSame(w, cursor.getHeldItem());
        assertEquals(5, cursor.getHeldItem().getQuantity());
        assertTrue(trashBin.isEmpty());
    }

    //  pickUpHalfFromTrash
    @Test
    public void pickUpHalfFromTrash_OddStackTakesCeil() {
        // trash has 5 wood → cursor takes ceil(5/2) = 3, trash keeps 2
        trashBin.setItem(wood(5));

        cursor.pickUpHalfFromTrash(trashBin);

        assertEquals(3, cursor.getHeldItem().getQuantity());
        assertEquals(2, trashBin.getItem().getQuantity());
    }

    @Test
    public void pickUpHalfFromTrash_EmptiesWhenSingleton() {
        // trash has 1 wood → cursor takes 1, trash becomes empty
        trashBin.setItem(wood(1));

        cursor.pickUpHalfFromTrash(trashBin);

        assertEquals(1, cursor.getHeldItem().getQuantity());
        assertTrue(trashBin.isEmpty());
    }

    // placeAllInTrash

    @Test
    public void placeAllInTrash_EmptyBin() {
        // cursor 5 wood + empty trash → trash has the stack, cursor empty
        Item w = wood(5);
        cursor.pickUpFromTrash(trashBin); // cursor empty, this is a no-op
        // seed cursor manually via inventory (use a dummy inventory)
        Inventory inv = new Inventory();
        inv.setSlot(0, w);
        cursor.pickUp(inv, 0);

        cursor.placeAllInTrash(trashBin);

        assertSame(w, trashBin.getItem());
        assertEquals(5, trashBin.getItem().getQuantity());
        assertTrue(cursor.isEmpty());
    }

    @Test
    public void placeAllInTrash_SameTypeMerges() {
        // cursor 3 wood + trash 4 wood → trash 7 wood, cursor empty
        trashBin.setItem(wood(4));
        Inventory inv = new Inventory();
        inv.setSlot(0, wood(3));
        cursor.pickUp(inv, 0);

        cursor.placeAllInTrash(trashBin);

        assertEquals(7, trashBin.getItem().getQuantity());
        assertTrue(cursor.isEmpty());
    }

    @Test
    public void placeAllInTrash_OverflowKeepsRemainderOnCursor() {
        // cursor (max - 1) wood + trash (max - 3) wood → trash maxed, cursor 2
        // parametrised on MAX_STACK_SIZE so the test holds even if the constant changes
        int max = Item.MAX_STACK_SIZE;
        int cursorQty = max - 1;
        int trashQty = max - 3;
        trashBin.setItem(wood(trashQty));
        Inventory inv = new Inventory();
        inv.setSlot(0, wood(cursorQty));
        cursor.pickUp(inv, 0);

        cursor.placeAllInTrash(trashBin);

        assertEquals(max, trashBin.getItem().getQuantity());
        // cursor keeps cursorQty - (max - trashQty) = (max - 1) - 3 = max - 4
        assertEquals(cursorQty - (max - trashQty), cursor.getHeldItem().getQuantity());
    }

    @Test
    public void placeAllInTrash_DifferentTypeSwaps() {
        // cursor 3 stone + trash 4 wood → trash gets stone, cursor gets wood
        Item w = wood(4);
        Item s = stone(3);
        trashBin.setItem(w);
        Inventory inv = new Inventory();
        inv.setSlot(0, s);
        cursor.pickUp(inv, 0);

        cursor.placeAllInTrash(trashBin);

        assertSame(s, trashBin.getItem());
        assertSame(w, cursor.getHeldItem());
    }

    //  placeOneInTrash
    @Test
    public void placeOneInTrash_EmptyBinDeposits1() {
        // cursor 5 wood + empty trash → trash 1 wood, cursor 4 wood
        Inventory inv = new Inventory();
        inv.setSlot(0, wood(5));
        cursor.pickUp(inv, 0);

        cursor.placeOneInTrash(trashBin);

        assertEquals(1, trashBin.getItem().getQuantity());
        assertEquals(4, cursor.getHeldItem().getQuantity());
    }

    @Test
    public void placeOneInTrash_SameTypeMerges1() {
        // cursor 5 wood + trash 4 wood → trash 5, cursor 4
        trashBin.setItem(wood(4));
        Inventory inv = new Inventory();
        inv.setSlot(0, wood(5));
        cursor.pickUp(inv, 0);

        cursor.placeOneInTrash(trashBin);

        assertEquals(5, trashBin.getItem().getQuantity());
        assertEquals(4, cursor.getHeldItem().getQuantity());
    }

    @Test
    public void placeOneInTrash_DifferentTypeSwaps() {
        // cursor 1 stone + trash 1 wood → trash 1 stone, cursor 1 wood
        Item w = wood(1);
        Item s = stone(1);
        trashBin.setItem(w);
        Inventory inv = new Inventory();
        inv.setSlot(0, s);
        cursor.pickUp(inv, 0);

        cursor.placeOneInTrash(trashBin);

        assertSame(s, trashBin.getItem());
        assertSame(w, cursor.getHeldItem());
    }
}
