package com.islandescape.player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PlayerSpriteTest {

    private static final String PATH = "src/resources/player/player_frames.png";

    private static PlayerSprite sprite;

    @BeforeAll
    static void setUp() {
        sprite = new PlayerSprite(PATH);
    }


    @Test
    void testFrameWidthTimesColsEqualsImageWidth() {
        assertEquals(sprite.getImageWidth(), sprite.getFrameWidth() * PlayerSprite.COLS);
        assertEquals(sprite.getImageHeight(), sprite.getFrameHeight() * PlayerSprite.ROWS);
    }

    @Test
    void testGetFrameReturnsNonNull() {
        for (int row = 0; row < PlayerSprite.ROWS; row++) {
            for (int col = 0; col < PlayerSprite.COLS; col++) {
                BufferedImage frame = sprite.getFrame(row, col);
                assertNotNull(frame);
                assertEquals(sprite.getFrameWidth(), frame.getWidth());
                assertEquals(sprite.getFrameHeight(), frame.getHeight());
            }
        }
    }


    @Test
    void testGetFlippedFrame() {
        BufferedImage original = sprite.getFrame(1, 0);
        BufferedImage flipped = sprite.getFlippedFrame(1, 0);

        assertNotNull(flipped);
        assertEquals(original.getWidth(), flipped.getWidth());
        assertEquals(original.getHeight(), flipped.getHeight());
    }
}
