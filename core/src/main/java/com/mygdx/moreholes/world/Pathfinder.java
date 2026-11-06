package com.mygdx.moreholes.world;

/** Breadth-first search through tunnels, used by snakes to hunt Molly and to find their way out. */
public final class Pathfinder {
    private static final int CELLS = Burrow.COLS * Burrow.SOIL_ROWS;

    public interface TileTest {
        boolean matches(int x, int y);
    }

    private Pathfinder() {
    }

    /**
     * Returns the first step of a shortest tunnel path from (startX, startY) to a tile that passes {@code goal},
     * or null if there is none. The goal tile itself only has to be in the soil, not empty.
     */
    public static Direction firstStep(Burrow burrow, int startX, int startY, TileTest goal, TileTest blocked) {
        Direction[] firstMove = new Direction[CELLS];
        boolean[] seen = new boolean[CELLS];
        int[] queue = new int[CELLS];
        int head = 0;
        int tail = 0;
        seen[index(startX, startY)] = true;
        queue[tail++] = index(startX, startY);
        while (head < tail) {
            int cell = queue[head++];
            int x = cell / Burrow.SOIL_ROWS;
            int y = cell % Burrow.SOIL_ROWS;
            for (Direction d : Direction.values()) {
                int nx = x + d.dx;
                int ny = y + d.dy;
                if (!Burrow.inSoil(nx, ny) || seen[index(nx, ny)]) {
                    continue;
                }
                Direction first = firstMove[cell] == null ? d : firstMove[cell];
                if (goal.matches(nx, ny)) {
                    return first;
                }
                if (burrow.isTunnel(nx, ny) && !blocked.matches(nx, ny)) {
                    seen[index(nx, ny)] = true;
                    firstMove[index(nx, ny)] = first;
                    queue[tail++] = index(nx, ny);
                }
            }
        }
        return null;
    }

    private static int index(int x, int y) {
        return x * Burrow.SOIL_ROWS + y;
    }
}
