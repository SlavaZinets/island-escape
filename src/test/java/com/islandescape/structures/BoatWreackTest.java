package com.islandescape.structures;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.boat.BoatWreck;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoatWreackTest {

    private BoatWreck boat;

    @BeforeEach
    public void setUp() {
        // Boat at origin; WorldStructure#isPlayerInRange returns true within distance 50.
        boat = new BoatWreck(0, 0, "Boat");
    }

    private Item item(ItemType type, int qty) {
        return new Item(type, ItemCategory.CRAFTABLE_RESOURCE, type.name(), "desc", qty);
    }

    // Deposit one of each required item type in the exact required quantity,
    // bringing the boat to isFullyRepaired() == true.
    private void fullyRepair() {
        BoatRepairSystem brs = boat.getRepairSystem();
        brs.placeIn(0, item(ItemType.PLANK, 3));
        brs.placeIn(1, item(ItemType.MAST, 1));
        brs.placeIn(2, item(ItemType.FRAME, 1));
        brs.placeIn(3, item(ItemType.SAIL, 1));
        brs.placeIn(4, item(ItemType.RUDDER, 1));
        brs.placeIn(5, item(ItemType.FITTINGS, 2));
    }

    // repair-system wiring

    @Test
    public void newBoatOwnsNonNullRepairSystem() {
        assertNotNull(boat.getRepairSystem(), "boat must instantiate a repair system");
    }

    @Test
    public void getRepairSystemReturnsSameInstance() {
        // GamePanel and InventoryScreen must reference the SAME repair system.
        assertSame(boat.getRepairSystem(), boat.getRepairSystem());
    }

    @Test
    public void newBoatIsNotFullyRepaired() {
        assertFalse(boat.isFullyRepaired(), "wreck starts un-repaired");
    }

    @Test
    public void isFullyRepairedTrueAfterAllSlotsFilled() {
        fullyRepair();
        assertTrue(boat.isFullyRepaired());
    }

    // boarding

    @Test
    public void boardFailsWhenNotFullyRepaired() {
        // Player in range but the boat is still a wreck.
        assertFalse(boat.board(0, 10, 10), "cannot board a wreck");
        assertFalse(boat.isPlayerBoarded(0));
    }

    @Test
    public void boardFailsWhenPlayerOutOfRange() {
        fullyRepair();
        // Boat at (0,0); player at (100,100) → distance ~141 > 50 → out of range.
        assertFalse(boat.board(0, 100, 100), "out-of-range boarding should be rejected");
        assertFalse(boat.isPlayerBoarded(0));
    }

    @Test
    public void boardSucceedsWhenRepairedAndInRange() {
        fullyRepair();
        assertTrue(boat.board(0, 10, 10));
        assertTrue(boat.isPlayerBoarded(0));
    }

    @Test
    public void boardIsIdempotent() {
        fullyRepair();
        assertTrue(boat.board(0, 10, 10));
        assertTrue(boat.board(0, 10, 10), "boarding again should still succeed");
        assertTrue(boat.isPlayerBoarded(0));
    }

    // bothBoarded win-condition helper

    @Test
    public void bothBoardedFalseInitially() {
        fullyRepair();
        assertFalse(boat.bothBoarded());
    }

    @Test
    public void bothBoardedFalseWhenOnlyOnePlayerBoarded() {
        fullyRepair();
        boat.board(0, 10, 10);
        assertFalse(boat.bothBoarded(), "single-player boarding is insufficient to win");
    }

    @Test
    public void bothBoardedTrueWhenBothPlayersBoarded() {
        fullyRepair();
        boat.board(0, 10, 10);
        boat.board(1, 5, 5);
        assertTrue(boat.bothBoarded(), "both players must board to win");
    }
}
