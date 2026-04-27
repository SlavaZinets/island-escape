package com.islandescape.boat;

import com.islandescape.structures.WorldStructure;

import java.util.HashSet;
import java.util.Set;


public class BoatWreck extends WorldStructure {

    private final BoatRepairSystem repairSystem;
    private final Set<Integer> boardedPlayers = new HashSet<>();

    public BoatWreck(double x, double y, String name) {
        super(x, y, name);
        this.repairSystem = new BoatRepairSystem();
    }

    // The repair system is a long-lived field; both GamePanel and
    // InventoryScreen reference the same instance.
    public BoatRepairSystem getRepairSystem() {
        return repairSystem;
    }

    public boolean isFullyRepaired() {
        return repairSystem.isFullyRepaired();
    }

    // Attempts to board the given player. Requires:
    // the boat is fully repaired, AND
    // the player is within interaction range (WorldStructure#isPlayerInRange).
    public boolean board(int playerId, double px, double py) {
        if (!isFullyRepaired()) return false;
        if (!isPlayerInRange(px, py)) return false;
        boardedPlayers.add(playerId);
        return true;
    }

    // True if both players (id 0 and id 1) have boarded. GamePanel uses
    // this to transition to GameState.GAME_WON.
    public boolean bothBoarded() {
        return boardedPlayers.contains(0) && boardedPlayers.contains(1);
    }

    public boolean isPlayerBoarded(int playerId) {
        return boardedPlayers.contains(playerId);
    }

    public void reset() {
        repairSystem.reset();
        boardedPlayers.clear();
    }
}
