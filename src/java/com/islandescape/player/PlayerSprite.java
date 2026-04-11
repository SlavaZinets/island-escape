package com.islandescape.player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerSprite {

    public static final int COLS = 6;
    public static final int ROWS = 3;

    private final BufferedImage sheet;
    private final int frameWidth;
    private final int frameHeight;
    private final BufferedImage[][] frames = new BufferedImage[ROWS][COLS];
    private final BufferedImage[][] flippedFrames = new BufferedImage[ROWS][COLS];

    public PlayerSprite(String path) {
        BufferedImage loaded;
        try {
            loaded = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("failed to load player sprite sheet: " + path, e);
        }
        if (loaded == null) {
            throw new RuntimeException("ImageIO returned null for player sprite sheet: " + path);
        }
        this.sheet = loaded;
        this.frameWidth = sheet.getWidth() / COLS;
        this.frameHeight = sheet.getHeight() / ROWS;
    }

    public int getImageWidth() {
        return sheet.getWidth();
    }

    public int getImageHeight() {
        return sheet.getHeight();
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public BufferedImage getFrame(int row, int col) {
        checkBounds(row, col);
        if (frames[row][col] == null) {
            frames[row][col] = sheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight);
        }
        return frames[row][col];
    }

    public BufferedImage getFlippedFrame(int row, int col) {
        checkBounds(row, col);
        if (flippedFrames[row][col] == null) {
            BufferedImage frame = getFrame(row, col);
            int w = frame.getWidth();
            int h = frame.getHeight();
            BufferedImage flippedFrame = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    flippedFrame.setRGB(x, y, frame.getRGB(w - 1 - x, y));
                }
            }
            flippedFrames[row][col] = flippedFrame;
        }
        return flippedFrames[row][col];
    }

    private static void checkBounds(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IndexOutOfBoundsException("frame (" + row + "," + col + ") out of " + COLS + "x" + ROWS + " grid");
        }
    }
}
