package com.islandescape.ui;

/**
 * Tracks one player's cursor position within the crafting screen.
 * Zone 0 = personal inventory, zone 1 = shared crafting grid.
 */
public class PlayerCursor {

    static final int ZONE_INVENTORY = 0;
    static final int ZONE_GRID = 1;

    private int zone = ZONE_INVENTORY;
    private int index = 0;

    private final int invCols;
    private final int invRows;
    private final int gridCols;
    private final int gridRows;

    public PlayerCursor(int invCols, int invRows, int gridCols, int gridRows) {
        this.invCols = invCols;
        this.invRows = invRows;
        this.gridCols = gridCols;
        this.gridRows = gridRows;
    }

    public int getZone() { return zone; }
    public int getIndex() { return index; }

    public boolean isOnGrid() { return zone == ZONE_GRID; }
    public boolean isOnInventory() { return zone == ZONE_INVENTORY; }

    public void move(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        if (zone == ZONE_INVENTORY) {
            moveInInventory(dx, dy);
        } else {
            moveInGrid(dx, dy);
        }
    }

    private void moveInInventory(int dx, int dy) {
        int col = index % invCols;
        int row = index / invCols;
        int prevRow = row;

        col = clamp(col + dx, 0, invCols - 1);
        row = clamp(row - dy, 0, invRows - 1); // screen Y inverted

        // Up from top row -> jump to grid
        if (dy > 0 && prevRow == 0 && row == 0) {
            zone = ZONE_GRID;
            index = Math.min(index, gridCols * gridRows - 1);
            return;
        }

        index = row * invCols + col;
    }

    private void moveInGrid(int dx, int dy) {
        int col = index % gridCols;
        int row = index / gridCols;
        int prevRow = row;

        col = clamp(col + dx, 0, gridCols - 1);
        row = clamp(row - dy, 0, gridRows - 1);

        // Down from bottom row -> jump to inventory
        if (dy < 0 && prevRow == gridRows - 1 && row == gridRows - 1) {
            zone = ZONE_INVENTORY;
            index = Math.min(index, invCols * invRows - 1);
            return;
        }

        index = row * gridCols + col;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
