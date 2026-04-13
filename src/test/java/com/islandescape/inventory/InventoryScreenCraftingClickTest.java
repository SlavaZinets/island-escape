package com.islandescape.inventory;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
import com.islandescape.ui.CraftingScreenLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryScreenCraftingClickTest {

    private static final int PANEL_W = 1920;
    private static final int PANEL_H = 1080;

    private Player player1;
    private Player player2;
    private InventoryCursor cursor;
    private InventoryScreen screen;
    private CraftingSystem cs;

    @BeforeEach
    void setUp() {
        player1 = new Player("P1", 1, 0, 0);
        player2 = new Player("P2", 2, 0, 0);
        cursor = new InventoryCursor();
        screen = new InventoryScreen(player1, player2, cursor);
        cs = new CraftingSystem();

        screen.setCraftingSystem(cs);
        screen.setCraftingOpen(true);
        screen.setOpen(true);
    }

    // --- pixelToCraftingSlot ---

    @Test
    public void clickInsideCraftingGridReturnsSlot() {
        CraftingScreenLayout layout = new CraftingScreenLayout(PANEL_W, PANEL_H);

        // Click center of slot (0,0)
        int x = layout.craftingStartX + layout.slotSize / 2;
        int y = layout.gridY + layout.slotSize / 2;

        int slot = screen.pixelToCraftingSlot(x, y, PANEL_W, PANEL_H);
        assertEquals(0, slot);
    }

    @Test
    public void clickSecondRowSecondColReturnsSlot3() {
        CraftingScreenLayout layout = new CraftingScreenLayout(PANEL_W, PANEL_H);

        // Slot (1,1) = index 3 in a 2x2 grid
        int x = layout.craftingStartX + 1 * (layout.slotSize + layout.slotGap) + layout.slotSize / 2;
        int y = layout.gridY + 1 * (layout.slotSize + layout.slotGap) + layout.slotSize / 2;

        int slot = screen.pixelToCraftingSlot(x, y, PANEL_W, PANEL_H);
        assertEquals(3, slot);
    }

    @Test
    public void clickOutsideCraftingGridReturnsNeg1() {
        int slot = screen.pixelToCraftingSlot(10, 10, PANEL_W, PANEL_H);
        assertEquals(-1, slot);
    }

    @Test
    public void clickInGapBetweenSlotsReturnsNeg1() {
        CraftingScreenLayout layout = new CraftingScreenLayout(PANEL_W, PANEL_H);

        // Click in the gap between slot 0 and slot 1 (horizontal gap)
        int x = layout.craftingStartX + layout.slotSize + layout.slotGap / 2;
        int y = layout.gridY + layout.slotSize / 2;

        int slot = screen.pixelToCraftingSlot(x, y, PANEL_W, PANEL_H);
        assertEquals(-1, slot);
    }

    // --- handleClick routes to crafting grid ---

    @Test
    public void clickCraftingSlotPlacesItemFromCursor() {
        // Put wood in P1's inventory, pick it up
        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "Wood", 1);
        player1.getInventory().setSlot(0, wood);
        cursor.pickUp(player1.getInventory(), 0);
        assertFalse(cursor.isEmpty());

        // Click on crafting slot 0
        CraftingScreenLayout layout = new CraftingScreenLayout(PANEL_W, PANEL_H);
        int x = layout.craftingStartX + layout.slotSize / 2;
        int y = layout.gridY + layout.slotSize / 2;

        screen.handleClick(x, y, true, PANEL_W, PANEL_H);

        // Cursor should be empty, item should be in crafting grid
        assertTrue(cursor.isEmpty());
        assertNotNull(cs.getSlot(0));
        assertEquals(ItemType.WOOD, cs.getSlot(0).getType());
    }

    @Test
    public void clickCraftingSlotPicksUpItemWhenCursorEmpty() {
        // Place item directly in crafting grid
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone", 2);
        cs.placeIn(0, stone, 0);

        // Click on crafting slot 0 with empty cursor
        CraftingScreenLayout layout = new CraftingScreenLayout(PANEL_W, PANEL_H);
        int x = layout.craftingStartX + layout.slotSize / 2;
        int y = layout.gridY + layout.slotSize / 2;

        screen.handleClick(x, y, true, PANEL_W, PANEL_H);

        // Cursor should hold the stone
        assertFalse(cursor.isEmpty());
        assertEquals(ItemType.STONE, cursor.getHeldItem().getType());
        assertNull(cs.getSlot(0));
    }

    @Test
    public void craftingClickDisabledWhenCraftingNotOpen() {
        screen.setCraftingOpen(false);

        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "Stone", 2);
        cs.placeIn(0, stone, 0);

        CraftingScreenLayout layout = new CraftingScreenLayout(PANEL_W, PANEL_H);
        int x = layout.craftingStartX + layout.slotSize / 2;
        int y = layout.gridY + layout.slotSize / 2;

        screen.handleClick(x, y, true, PANEL_W, PANEL_H);

        // Crafting click should not have happened — cursor still empty
        assertTrue(cursor.isEmpty());
        // Stone still in grid
        assertNotNull(cs.getSlot(0));
    }
}
