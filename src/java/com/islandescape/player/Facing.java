package com.islandescape.player;

import com.islandescape.utilities.Direction;

public enum Facing {

    SOUTH(0, false),
    EAST(1, false),
    NORTH(2, false),
    WEST(1, true);

    private final int spriteRow;
    private final boolean flipped;

    Facing(int spriteRow, boolean flipped) {
        this.spriteRow = spriteRow;
        this.flipped = flipped;
    }

    public int getSpriteRow() {
        return spriteRow;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public static Facing fromDirection(Direction d) {
        // Horizontal axis wins on diagonals, so check x first.
        if (d.getX() != 0) {
            return d.getX() > 0 ? EAST : WEST;
        }
        if (d.getY() != 0) {
            return d.getY() > 0 ? NORTH : SOUTH;
        }
        return null;
    }
}
