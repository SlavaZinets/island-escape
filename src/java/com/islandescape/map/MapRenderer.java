package com.islandescape.map;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MapRenderer {


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
    // render the map
    public void render(Graphics2D g, TileMap map){
        System.out.println("Tileset loaded: " + (tilesetImage != null));
        if(tilesetImage != null) {
            System.out.println("Tileset size: " + tilesetImage.getWidth() + "x" + tilesetImage.getHeight());
        }
        String[] layers = {"DeepWater", "Water", "Sand", "Jungle", "Trees", "Lake", "StoneLand"};
        for(String layer: layers){
            TileLayer tileLayer = map.getLayer(layer);
            if(tileLayer == null){
                System.out.println("WARNING: Layer not found: " + layer);
                continue;
            }
            renderLayer(g, map, tileLayer);
        }
    }
    // draw the layer on the screen
    private void renderLayer(Graphics2D g, TileMap map, TileLayer layer) {
        for (int row = 0; row < map.getHeight(); row++) {
            for(int col = 0; col < map.getWidth(); col++){
                int tileId = layer.getTileAt(row, col);
                if(isEmpty(tileId)) continue;
                int descX = col * tileSize;
                int descY = row * tileSize;
                int srcX = getTileSourceX(tileId);
                int srcY = getTileSourceY(tileId);
                g.drawImage(tilesetImage, descX, descY, descX + tileSize, descY + tileSize, srcX, srcY, srcX + tileSize, srcY + tileSize, null);
            }

        }
    }


}
