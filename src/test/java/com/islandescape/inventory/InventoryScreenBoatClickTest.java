package com.islandescape.inventory;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
import com.islandescape.ui.BoatRepairLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryScreenBoatClickTest {

    private static final int PANEL_W = 1920;
    private static final int PANEL_H = 1080;
    // Inventory slots are a fixed 64px in InventoryScreen regardless of layout.
    private static final int INV_SLOT_SIZE = 64;
    // handleClick nudges the inventory grid top by +22 (mirrors crafting path).
    private static final int GRID_TOP_NUDGE = 22;

    private Player player1;
    private Player player2;
    private InventoryCursor cursor;
    private InventoryScreen screen;
    private BoatRepairSystem brs;

    @BeforeEach
    void setUp() {
        player1 = new Player("P1", 1, 0, 0);
        player2 = new Player("P2", 2, 0, 0);
        cursor = new InventoryCursor();
        screen = new InventoryScreen(player1, player2, cursor);
        brs = new BoatRepairSystem();

        screen.setBoatRepairSystem(brs);
        screen.setBoatRepairOpen(true);
        screen.setPlayerNearBoat(true, true);
        screen.setOpen(true);
    }

    private Item item(ItemType type, int qty) {
        return new Item(type, ItemCategory.CRAFTABLE_RESOURCE, type.name(), "desc", qty);
    }

    // compute the center-pixel of a given boat slot.
    private int slotCenterX(int slotIdx, BoatRepairLayout layout) {
        int cellSize = layout.slotSize + layout.slotGap;
        return layout.repairSlotsStartX + slotIdx * cellSize + layout.slotSize / 2;
    }
    private int slotCenterY(BoatRepairLayout layout) {
        return layout.repairSlotsY + layout.slotSize / 2;
    }

    //  pixelToBoatSlot

    @Test
    public void clickCenterOfSlot0ReturnsSlot0() {
        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);

        int slot = screen.pixelToBoatSlot(
                slotCenterX(0, layout), slotCenterY(layout), PANEL_W, PANEL_H);

        assertEquals(0, slot);
    }

    @Test
    public void clickCenterOfSlot5ReturnsSlot5() {
        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);

        int slot = screen.pixelToBoatSlot(
                slotCenterX(5, layout), slotCenterY(layout), PANEL_W, PANEL_H);

        assertEquals(5, slot);
    }

    @Test
    public void clickOutsideRepairRowReturnsNeg1() {
        // Top-left corner of screen — far from any repair slot.
        int slot = screen.pixelToBoatSlot(10, 10, PANEL_W, PANEL_H);
        assertEquals(-1, slot);
    }

    @Test
    public void clickInGapBetweenSlotsReturnsNeg1() {
        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);

        // Gap between slot 0 and slot 1 (horizontal gap).
        int x = layout.repairSlotsStartX + layout.slotSize + layout.slotGap / 2;
        int y = slotCenterY(layout);

        int slot = screen.pixelToBoatSlot(x, y, PANEL_W, PANEL_H);
        assertEquals(-1, slot);
    }

    //  click routing: left/right × full/empty cursor

    @Test
    public void leftClickWithFullCursorPlacesIntoSlot() {
        // 1 plank in cursor → slot 0 (PLANK, requires 3).
        cursor.setHeldItem(item(ItemType.PLANK, 1));

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        screen.handleClick(
                slotCenterX(0, layout), slotCenterY(layout), true, PANEL_W, PANEL_H);

        assertTrue(cursor.isEmpty(), "cursor should be fully consumed");
        assertNotNull(brs.getSlot(0));
        assertEquals(ItemType.PLANK, brs.getSlot(0).getType());
        assertEquals(1, brs.getSlot(0).getQuantity());
    }

    @Test
    public void rightClickWithFullCursorPlacesOneIntoSlot() {
        // 5 fittings in cursor → slot 5 (FITTINGS, requires 2).
        cursor.setHeldItem(item(ItemType.FITTINGS, 5));

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        screen.handleClick(
                slotCenterX(5, layout), slotCenterY(layout), false, PANEL_W, PANEL_H);

        assertFalse(cursor.isEmpty(), "cursor should still hold the remainder");
        assertEquals(4, cursor.getHeldItem().getQuantity(),
                "exactly one fitting should leave the cursor");
        assertNotNull(brs.getSlot(5));
        assertEquals(1, brs.getSlot(5).getQuantity());
    }

    @Test
    public void leftClickWithEmptyCursorPicksUpFromPartialSlot() {
        // Slot 0 holds 2 planks (partial). Left-click with empty cursor.
        brs.placeIn(0, item(ItemType.PLANK, 2));
        assertTrue(cursor.isEmpty());

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        screen.handleClick(
                slotCenterX(0, layout), slotCenterY(layout), true, PANEL_W, PANEL_H);

        assertFalse(cursor.isEmpty());
        assertEquals(ItemType.PLANK, cursor.getHeldItem().getType());
        assertEquals(2, cursor.getHeldItem().getQuantity(),
                "whole partial stack should be on the cursor");
        assertNull(brs.getSlot(0), "slot must be empty after pickup");
    }

    @Test
    public void rightClickWithEmptyCursorSplitsHalfFromSlot() {
        // Slot 0 has 2 planks (partial). Right-click with empty cursor → ceil(2/2) = 1.
        brs.placeIn(0, item(ItemType.PLANK, 2));

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        screen.handleClick(
                slotCenterX(0, layout), slotCenterY(layout), false, PANEL_W, PANEL_H);

        assertFalse(cursor.isEmpty());
        assertEquals(1, cursor.getHeldItem().getQuantity(), "ceil(2/2) = 1");
        assertNotNull(brs.getSlot(0));
        assertEquals(1, brs.getSlot(0).getQuantity(), "slot should keep the remaining 1");
    }

    //  boatRepairOpen gate

    @Test
    public void boatClickDisabledWhenBoatRepairNotOpen() {
        screen.setBoatRepairOpen(false);
        cursor.setHeldItem(item(ItemType.PLANK, 1));

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        screen.handleClick(
                slotCenterX(0, layout), slotCenterY(layout), true, PANEL_W, PANEL_H);

        // Boat routing must not fire — cursor and BRS both unchanged.
        assertFalse(cursor.isEmpty(), "cursor must still hold the plank");
        assertEquals(1, cursor.getHeldItem().getQuantity());
        assertNull(brs.getSlot(0), "BRS slot must remain empty");
    }

    //  proximity blocking (inventory clicks during boat repair)

    @Test
    public void p1InventoryClicksBlockedWhenP1NotNearBoat() {
        screen.setPlayerNearBoat(false, true);

        // Put something in P1's slot 0 so pickUp would do something if it fired.
        Item stone = new Item(
                ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone", 2);
        player1.getInventory().setSlot(0, stone);

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        int x = layout.invP1Left + INV_SLOT_SIZE / 2;
        int y = layout.invAreaTop + GRID_TOP_NUDGE + INV_SLOT_SIZE / 2;

        screen.handleClick(x, y, true, PANEL_W, PANEL_H);

        assertTrue(cursor.isEmpty(), "far-player's inventory click must be ignored");
        assertNotNull(player1.getInventory().getSlot(0));
        assertEquals(ItemType.STONE, player1.getInventory().getSlot(0).getType());
    }

    @Test
    public void p2InventoryClicksBlockedWhenP2NotNearBoat() {
        screen.setPlayerNearBoat(true, false);

        Item stone = new Item(
                ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone", 2);
        player2.getInventory().setSlot(0, stone);

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        int x = layout.invP2Left + INV_SLOT_SIZE / 2;
        int y = layout.invAreaTop + GRID_TOP_NUDGE + INV_SLOT_SIZE / 2;

        screen.handleClick(x, y, true, PANEL_W, PANEL_H);

        assertTrue(cursor.isEmpty());
        assertNotNull(player2.getInventory().getSlot(0));
    }

    @Test
    public void nearPlayerInventoryClicksStillWorkWhenBoatOpen() {
        // Both players near — P1 clicks should fire normally.
        screen.setPlayerNearBoat(true, true);

        Item stone = new Item(
                ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone", 2);
        player1.getInventory().setSlot(0, stone);

        BoatRepairLayout layout = new BoatRepairLayout(PANEL_W, PANEL_H);
        int x = layout.invP1Left + INV_SLOT_SIZE / 2;
        int y = layout.invAreaTop + GRID_TOP_NUDGE + INV_SLOT_SIZE / 2;

        screen.handleClick(x, y, true, PANEL_W, PANEL_H);

        assertFalse(cursor.isEmpty(), "near player's inventory click should pick up");
        assertEquals(ItemType.STONE, cursor.getHeldItem().getType());
        assertNull(player1.getInventory().getSlot(0));
    }
}
