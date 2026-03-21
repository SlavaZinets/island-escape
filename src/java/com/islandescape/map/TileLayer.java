package com.islandescape.map;

public class TileLayer {

    private final String name;
    private final int[][] data;

    public TileLayer(String name, int[][] data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public int getTileAt(int row, int col) {
        return data[row][col];
    }
}
