package com.islandescape.resources;

import com.islandescape.map.TileLayer;
import com.islandescape.map.TileMap;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Walks tile layers and emits ResourceNodes.
// Trees occupy a fixed 2-col x 3-row footprint; one node per tree.
// Stones emit one node per non-zero tile.
public final class ResourceSpawner {

    private static final int MASK_TILE_ID = 0x1FFFFFFF;
    private static final int TREE_COLS = 2;
    private static final int TREE_ROWS = 3;

    private ResourceSpawner() {}

    public static Set<Point> disabledTileCoords(List<ResourceNode> nodes) {
        Set<Point> out = new HashSet<>();
        for (ResourceNode node : nodes) {
            if (node.isDisabled()) {
                out.addAll(node.getTileCoords());
            }
        }
        return out;
    }

    public static ArrayList<ResourceNode> spawnFromMap(TileMap map) {
        ArrayList<ResourceNode> nodes = new ArrayList<>();
        nodes.addAll(spawnTrees(map));
        nodes.addAll(spawnStones(map));
        return nodes;
    }

    private static ArrayList<ResourceNode> spawnTrees(TileMap map) {
        ArrayList<ResourceNode> out = new ArrayList<>();
        TileLayer layer = map.getLayer("trees");
        if (layer == null) return out;

        int width = map.getWidth();
        int height = map.getHeight();
        int tileSize = map.getTileSize();
        boolean[][] visited = new boolean[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (visited[row][col]) continue;
                if ((layer.getTileAt(row, col) & MASK_TILE_ID) == 0) continue;

                // (row, col) is the top-left of a tree. Claim the 2x3 block.
                ArrayList<Point> cluster = new ArrayList<>();
                for (int subrow = 0; subrow < TREE_ROWS; subrow++) {
                    for (int subcolumn = 0; subcolumn < TREE_COLS; subcolumn++) {
                        int r = row + subrow;
                        int c = col + subcolumn;
                        if (r >= height || c >= width) continue;
                        visited[r][c] = true;
                        cluster.add(new Point(c, r));
                    }
                }

                double[] centroid = centroidPx(cluster, tileSize);
                Tree node = new Tree(centroid[0], centroid[1]);
                node.setTileCoords(cluster);
                out.add(node);
            }
        }
        return out;
    }

    private static ArrayList<ResourceNode> spawnStones(TileMap map) {
        ArrayList<ResourceNode> out = new ArrayList<>();
        TileLayer layer = map.getLayer("stones");
        if (layer == null) return out;

        int width = map.getWidth();
        int height = map.getHeight();
        int tileSize = map.getTileSize();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((layer.getTileAt(row, col) & MASK_TILE_ID) == 0) continue;

                double cx = col * tileSize + tileSize / 2.0;
                double cy = row * tileSize + tileSize / 2.0;
                Stone node = new Stone(cx, cy);

                ArrayList<Point> newCoords = new ArrayList<>();
                newCoords.add(new Point(col, row));
                node.setTileCoords(newCoords);

                out.add(node);
            }
        }
        return out;
    }


    private static double[] centroidPx(List<Point> cluster, int tileSize) {
        double sumX = 0, sumY = 0;
        for (Point p : cluster) {
            sumX += p.x * tileSize + tileSize / 2.0;
            sumY += p.y * tileSize + tileSize / 2.0;
        }
        int n = cluster.size();
        return new double[]{sumX / n, sumY / n};
    }
}
