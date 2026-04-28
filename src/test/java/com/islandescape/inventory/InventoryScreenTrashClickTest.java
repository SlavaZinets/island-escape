package com.islandescape.inventory;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/*
    Unit-level tests for the new trash-cell branch in InventoryScreen.
    Pin the public contract of the hit-test helpers and verify that
    handleClick routes to the right cursor / trash operation.


 */
public class InventoryScreenTrashClickTest {

    private static final int PANEL_W = 1280;
    private static final int PANEL_H = 720;

    private Player player1;
    private Player player2;
    private InventoryCursor cursor;
    private TrashBin trashBin;
    private InventoryScreen screen;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player 1", 1, 100, 100);
        player2 = new Player("Player 2", 2, 200, 100);
        cursor = new InventoryCursor();
        trashBin = new TrashBin();
        screen = new InventoryScreen(player1, player2, cursor, trashBin);
        screen.setOpen(true);
    }

    private Item wood(int qty) {
        return new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "A piece of wood", qty);
    }

    private Item stone(int qty) {
        return new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "A solid stone", qty);
    }

    //  Hit-tests

    @Test
    public void pixelToTrashSlot_HitInsideReturnsTrue() {
        // a point at the visual center of the trash slot lands inside its bounds
        int cx = screen.getTrashSlotX(PANEL_W) + 32;
        int cy = screen.getTrashSlotY(PANEL_H) + 32;

        assertTrue(screen.pixelToTrashSlot(cx, cy, PANEL_W, PANEL_H));
    }

    @Test
    public void pixelToTrashSlot_HitOutsideReturnsFalse() {
        int x = screen.getTrashSlotX(PANEL_W);
        int y = screen.getTrashSlotY(PANEL_H);

        // 1 px above the slot — outside
        assertFalse(screen.pixelToTrashSlot(x, y - 1, PANEL_W, PANEL_H));
        // 1 px to the left — outside
        assertFalse(screen.pixelToTrashSlot(x - 1, y, PANEL_W, PANEL_H));
        // far away — outside
        assertFalse(screen.pixelToTrashSlot(0, 0, PANEL_W, PANEL_H));
    }

    @Test
    public void pixelOnRemoveButton_HitInsideReturnsTrue() {
        Rectangle r = screen.getRemoveButtonRect(PANEL_W, PANEL_H);

        // exact center of the button
        int cx = r.x + r.width / 2;
        int cy = r.y + r.height / 2;

        assertTrue(screen.pixelOnRemoveButton(cx, cy, PANEL_W, PANEL_H));
    }

    @Test
    public void pixelOnRemoveButton_HitOutsideReturnsFalse() {
        Rectangle r = screen.getRemoveButtonRect(PANEL_W, PANEL_H);

        // 1 px above the button
        assertFalse(screen.pixelOnRemoveButton(r.x, r.y - 1, PANEL_W, PANEL_H));
        // 1 px right of its right edge
        assertFalse(screen.pixelOnRemoveButton(r.x + r.width + 1, r.y, PANEL_W, PANEL_H));
        // far away
        assertFalse(screen.pixelOnRemoveButton(0, 0, PANEL_W, PANEL_H));
    }

    // Click routing

    @Test
    public void clickOnTrashSlot_RoutesToCursor_PickUp() {
        // empty cursor + trash has wood + left-click on trash slot cursor holds wood, trash empty
        Item w = wood(5);
        trashBin.setItem(w);

        int cx = screen.getTrashSlotX(PANEL_W) + 32;
        int cy = screen.getTrashSlotY(PANEL_H) + 32;
        screen.handleClick(cx, cy, true, PANEL_W, PANEL_H);

        assertSame(w, cursor.getHeldItem());
        assertTrue(trashBin.isEmpty());
    }

    @Test
    public void clickOnTrashSlot_RoutesToCursor_PlaceOne() {
        // cursor 5 wood + right-click trash slot  trash gets 1, cursor keeps 4
        Inventory inv = new Inventory();
        inv.setSlot(0, wood(5));
        cursor.pickUp(inv, 0);

        int cx = screen.getTrashSlotX(PANEL_W) + 32;
        int cy = screen.getTrashSlotY(PANEL_H) + 32;
        screen.handleClick(cx, cy, false, PANEL_W, PANEL_H);

        assertEquals(1, trashBin.getItem().getQuantity());
        assertEquals(4, cursor.getHeldItem().getQuantity());
    }

    @Test
    public void clickOnRemoveButton_ClearsTrashOnly() {
        // trash has wood, cursor has stone click REMOVE trash empty, cursor untouched
        Item w = wood(3);
        Item s = stone(2);
        trashBin.setItem(w);
        Inventory inv = new Inventory();
        inv.setSlot(0, s);
        cursor.pickUp(inv, 0);

        Rectangle r = screen.getRemoveButtonRect(PANEL_W, PANEL_H);
        screen.handleClick(r.x + r.width / 2, r.y + r.height / 2, true, PANEL_W, PANEL_H);

        assertTrue(trashBin.isEmpty());
        assertSame(s, cursor.getHeldItem());
    }
}
