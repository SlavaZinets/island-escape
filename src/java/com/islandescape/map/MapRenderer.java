package com.islandescape.map;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class MapRenderer {

    // Tiled flip flags (stored in upper 3 bits of tile ID)
    private static final int FLAG_HORIZONTAL = 0x80000000;
    private static final int FLAG_VERTICAL   = 0x40000000;
    private static final int FLAG_DIAGONAL   = 0x20000000;
    private static final int MASK_TILE_ID    = 0x1FFFFFFF;

    // check if the tile is empty (raw value with flags)
    public boolean isEmpty(int rawTileId){
        return (rawTileId & MASK_TILE_ID) == 0;
    }

    // extract tile ID without flags
    public int getTileId(int rawTileId) {
        return rawTileId & MASK_TILE_ID;
    }

    // render the map
    public void render(Graphics2D g, TileMap map){
        String[] layers = {"seaToSand", "sand", "Grass", "tree", "stones"};
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
                int rawTileId = layer.getTileAt(row, col);
                if(isEmpty(rawTileId)) continue;

                int tileId = getTileId(rawTileId);
                boolean flipH = (rawTileId & FLAG_HORIZONTAL) != 0;
                boolean flipV = (rawTileId & FLAG_VERTICAL) != 0;
                boolean flipD = (rawTileId & FLAG_DIAGONAL) != 0;

                Tileset tileset = map.getTilesetForTile(tileId);
                if (tileset == null || tileset.getImage() == null) continue;

                int srcX = tileset.getTileSourceX(tileId);
                int srcY = tileset.getTileSourceY(tileId);
                int tileSize = tileset.getTileWidth();

                // Extract the tile from the tileset
                BufferedImage tileImage = tileset.getImage().getSubimage(srcX, srcY, tileSize, tileSize);

                int destX = col * tileSize;
                int destY = row * tileSize;

                if (!flipH && !flipV && !flipD) {
                    // No transform — draw directly
                    g.drawImage(tileImage, destX, destY, null);
                } else {
                    // Apply flip/rotation transforms
                    AffineTransform transform = new AffineTransform();
                    transform.translate(destX, destY);

                    // Tiled uses diagonal flip + horizontal/vertical to encode rotations:

                    if (flipD) {
                        // Diagonal flip = transpose (swap x and y)
                        transform.translate(tileSize / 2.0, tileSize / 2.0);
                        // Transpose: rotate 90° CW then flip horizontally
                        transform.rotate(Math.PI / 2);
                        transform.scale(-1, 1);
                        transform.translate(-tileSize / 2.0, -tileSize / 2.0);
                    }
                    if (flipH) {
                        transform.translate(tileSize / 2.0, tileSize / 2.0);
                        transform.scale(-1, 1);
                        transform.translate(-tileSize / 2.0, -tileSize / 2.0);
                    }
                    if (flipV) {
                        transform.translate(tileSize / 2.0, tileSize / 2.0);
                        transform.scale(1, -1);
                        transform.translate(-tileSize / 2.0, -tileSize / 2.0);
                    }

                    AffineTransform oldTransform = g.getTransform();
                    g.setTransform(transform);
                    g.drawImage(tileImage, 0, 0, null);
                    g.setTransform(oldTransform);
                }
            }
        }
    }
}
