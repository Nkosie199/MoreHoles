package com.mygdx.moreholes.world;

import java.util.Random;

/**
 * The ground Molly lives in. Rows 0 to {@link #TOP_SOIL_ROW} are soil, {@link #SURFACE_ROW} is the open ground above
 * it. A dug tile in the top soil row is a hole: it lets air in, and lets snakes in too.
 */
public class Burrow {
    public static final int COLS = 20;
    public static final int SOIL_ROWS = 15;
    public static final int TOP_SOIL_ROW = SOIL_ROWS - 1;
    public static final int SURFACE_ROW = SOIL_ROWS;

    private static final float ROCK_CHANCE = 0.09f;
    private static final int ROCK_FREE_ROWS = 2;

    private final Tile[][] tiles = new Tile[COLS][SOIL_ROWS];
    private boolean[][] airReach;

    public Burrow() {
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < SOIL_ROWS; y++) {
                tiles[x][y] = Tile.SOIL;
            }
        }
    }

    /** Scatters rocks below the top rows, keeping a column of soil around {@code keepClearX} for the starting den. */
    public static Burrow withRocks(Random random, int keepClearX) {
        Burrow burrow = new Burrow();
        for (int x = 0; x < COLS; x++) {
            if (Math.abs(x - keepClearX) <= 2) {
                continue;
            }
            for (int y = 0; y < SOIL_ROWS - ROCK_FREE_ROWS; y++) {
                if (random.nextFloat() < ROCK_CHANCE) {
                    burrow.tiles[x][y] = Tile.ROCK;
                }
            }
        }
        return burrow;
    }

    public static boolean inSoil(int x, int y) {
        return x >= 0 && x < COLS && y >= 0 && y < SOIL_ROWS;
    }

    public static boolean onSurface(int x, int y) {
        return y == SURFACE_ROW && x >= 0 && x < COLS;
    }

    public Tile tileAt(int x, int y) {
        return tiles[x][y];
    }

    public boolean isOpen(int x, int y) {
        return onSurface(x, y) || (inSoil(x, y) && tiles[x][y] == Tile.TUNNEL);
    }

    public boolean isTunnel(int x, int y) {
        return inSoil(x, y) && tiles[x][y] == Tile.TUNNEL;
    }

    public boolean canDig(int x, int y) {
        return inSoil(x, y) && tiles[x][y] == Tile.SOIL;
    }

    public void dig(int x, int y) {
        setTile(x, y, Tile.TUNNEL);
    }

    public void fill(int x, int y) {
        setTile(x, y, Tile.SOIL);
    }

    public void placeRock(int x, int y) {
        setTile(x, y, Tile.ROCK);
    }

    public boolean isHole(int x) {
        return isTunnel(x, TOP_SOIL_ROW);
    }

    /** True if fresh air reaches this tile: it is on the surface, or joined to a hole by tunnels. */
    public boolean hasAir(int x, int y) {
        if (onSurface(x, y)) {
            return true;
        }
        if (!inSoil(x, y)) {
            return false;
        }
        if (airReach == null) {
            airReach = floodFromHoles();
        }
        return airReach[x][y];
    }

    private void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
        airReach = null;
    }

    private boolean[][] floodFromHoles() {
        boolean[][] reached = new boolean[COLS][SOIL_ROWS];
        int[] queue = new int[COLS * SOIL_ROWS];
        int head = 0;
        int tail = 0;
        for (int x = 0; x < COLS; x++) {
            if (isHole(x)) {
                reached[x][TOP_SOIL_ROW] = true;
                queue[tail++] = x * SOIL_ROWS + TOP_SOIL_ROW;
            }
        }
        while (head < tail) {
            int cell = queue[head++];
            int x = cell / SOIL_ROWS;
            int y = cell % SOIL_ROWS;
            for (Direction d : Direction.values()) {
                int nx = x + d.dx;
                int ny = y + d.dy;
                if (isTunnel(nx, ny) && !reached[nx][ny]) {
                    reached[nx][ny] = true;
                    queue[tail++] = nx * SOIL_ROWS + ny;
                }
            }
        }
        return reached;
    }
}
