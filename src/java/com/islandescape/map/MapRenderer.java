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
    private BufferedImage tilesetImage;


    public MapRenderer( int offset, int columns, int tileSize) {
        this.offset = offset;
        this.columns = columns;
        this.tileSize = tileSize;
    }

    // load tileset image
    public void loadTileset(String path) throws IOException{
        tilesetImage = ImageIO.read(new File(path));
    }
    // check if the tile is empty
    public boolean isEmpty(int tileId){
        return tileId == 0;
    }

    // get the x coordinate of the tile in the tileset image
    public int getTileSourceX(int tileId){
        return (tileId - offset) % columns * tileSize;
    }
    // get the y coordinate of the tile in the tileset image
    public int getTileSourceY(int tileId){
        return (tileId - offset) / columns * tileSize;
    }

}
