package com.islandescape.window;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.boat.BoatWreck;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Simulates the 'B' key handler (GamePanel#onBoardKey) at the logic level.

public class GameWinConditionTest {

    private BoatWreck boat;
    private GameState gameState;

    @BeforeEach
    public void setUp() {

        boat = new BoatWreck(0, 0, "Boat");
        gameState = GameState.PLAYING;
    }

    private Item item(ItemType type, int qty) {
        return new Item(type, ItemCategory.CRAFTABLE_RESOURCE, type.name(), "desc", qty);
    }

    private void fullyRepair() {
        BoatRepairSystem brs = boat.getRepairSystem();
        brs.placeIn(0, item(ItemType.PLANK, 3));
        brs.placeIn(1, item(ItemType.MAST, 1));
        brs.placeIn(2, item(ItemType.FRAME, 1));
        brs.placeIn(3, item(ItemType.SAIL, 1));
        brs.placeIn(4, item(ItemType.RUDDER, 1));
        brs.placeIn(5, item(ItemType.FITTINGS, 2));
    }

    // Mirrors what GamePanel#onBoardKey will do when Step 13 lands:
    //   1. ask the boat to board the given player at (px, py);
    //   2. if both players have boarded, transition to GAME_WON.
    private void simulatePressB(int playerId, double px, double py) {
        boat.board(playerId, px, py);
        if (boat.bothBoarded()) {
            gameState = GameState.GAME_WON;
        }
    }





    // win-condition state transition

    @Test
    public void bothPlayersBoardingAfterFullRepairTransitionsToGameWon() {
        fullyRepair();

        simulatePressB(0, 10, 10);
        assertEquals(GameState.PLAYING, gameState,
                "one player alone must not flip the game state");

        simulatePressB(1, 5, 5);
        assertEquals(GameState.GAME_WON, gameState,
                "both players aboard a repaired boat ends the game");
    }

    @Test
    public void singlePlayerBoardingIsInsufficientToWin() {
        fullyRepair();

        simulatePressB(0, 10, 10);

        assertTrue(boat.isPlayerBoarded(0));
        assertFalse(boat.isPlayerBoarded(1));
        assertFalse(boat.bothBoarded());
        assertEquals(GameState.PLAYING, gameState);
    }

    // 'B' is a no-op when the boat isn't fully repaired

    @Test
    public void pressingBWithoutFullRepairDoesNothing() {
        // Boat only partially fixed — missing several slots.
        boat.getRepairSystem().placeIn(0, item(ItemType.PLANK, 3));
        assertFalse(boat.isFullyRepaired());

        simulatePressB(0, 10, 10);
        simulatePressB(1, 5, 5);

        assertFalse(boat.isPlayerBoarded(0), "board() must reject while un-repaired");
        assertFalse(boat.isPlayerBoarded(1));
        assertFalse(boat.bothBoarded());
        assertEquals(GameState.PLAYING, gameState,
                "game state must stay PLAYING until the boat is fully repaired");
    }

    // 'B' out of range is also a no-op even after full repair

    @Test
    public void pressingBOutOfRangeDoesNotBoard() {
        fullyRepair();

        // Boat at (0,0); player at (100,100) → distance ~141 > 50 → out of range.
        simulatePressB(0, 100, 100);
        simulatePressB(1, 100, 100);

        assertFalse(boat.isPlayerBoarded(0), "out-of-range press-B must be ignored");
        assertFalse(boat.isPlayerBoarded(1));
        assertEquals(GameState.PLAYING, gameState);
    }
}
