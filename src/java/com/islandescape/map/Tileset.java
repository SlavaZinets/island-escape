package com.islandescape.map;

import java.awt.image.BufferedImage;

/*
    Tileset holds a tileset image and its properties from a TMX/TSX file.
    firstgid is the first global tile ID belonging to this tileset.
 */
public class Tileset {

    private final int firstgid;
    private final String name;
    private final int tileWidth;
    private final int tileHeight;
    private final int columns;
    private final int tileCount;
    private BufferedImage image;

    public Tileset(int firstgid, String name, int tileWidth, int tileHeight, int columns, int tileCount) {
        this.firstgid = firstgid;
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.columns = columns;
        this.tileCount = tileCount;
    }

    public int getFirstgid() {
        return firstgid;
    }

    public String getName() {
        return name;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getColumns() {
        return columns;
    }

    public int getTileCount() {
        return tileCount;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean containsTile(int tileId) {
        return tileId >= firstgid && tileId < firstgid + tileCount;
    }

    public int getTileSourceX(int tileId) {
        int localId = tileId - firstgid;
        return (localId % columns) * tileWidth;
    }

    public int getTileSourceY(int tileId) {
        int localId = tileId - firstgid;
        return (localId / columns) * tileHeight;
    }
}
