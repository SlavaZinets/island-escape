package com.islandescape.map;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MapRenderer {

    private TileMap map;
    private final int offset;
    private final int columns;
    private final int tileSize;
    private BufferedImage image;


    public MapRenderer( int offset, int columns, int tileSize) {
        this.offset = offset;
        this.columns = columns;
        this.tileSize = tileSize;
    }

}
