package com.mygdx.moreholes.world;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class BurrowTest {
    private static final int TOP = Burrow.TOP_SOIL_ROW;

    @Test
    void solidGroundHasNoAirButTheSurfaceDoes() {
        Burrow burrow = new Burrow();

        assertFalse(burrow.hasAir(5, 5));
        assertTrue(burrow.hasAir(5, Burrow.SURFACE_ROW));
    }

    @Test
    void airReachesTunnelsJoinedToAHole() {
        Burrow burrow = new Burrow();
        burrow.dig(4, TOP);
        burrow.dig(4, TOP - 1);
        burrow.dig(5, TOP - 1);

        assertTrue(burrow.isHole(4));
        assertTrue(burrow.hasAir(5, TOP - 1));
    }

    @Test
    void airDoesNotReachATunnelCutOffFromTheSurface() {
        Burrow burrow = new Burrow();
        burrow.dig(4, TOP);
        burrow.dig(8, 3);

        assertFalse(burrow.hasAir(8, 3));
    }

    @Test
    void fillingTheHoleCutsOffTheAir() {
        Burrow burrow = new Burrow();
        burrow.dig(4, TOP);
        burrow.dig(4, TOP - 1);

        burrow.fill(4, TOP);

        assertFalse(burrow.isHole(4));
        assertFalse(burrow.hasAir(4, TOP - 1));
    }

    @Test
    void rocksCannotBeDugAndLeaveTheStartingColumnClear() {
        Burrow burrow = Burrow.withRocks(new Random(3), 10);
        boolean sawRock = false;
        for (int x = 0; x < Burrow.COLS; x++) {
            for (int y = 0; y < Burrow.SOIL_ROWS; y++) {
                if (burrow.tileAt(x, y) == Tile.ROCK) {
                    sawRock = true;
                    assertFalse(burrow.canDig(x, y));
                    assertTrue(Math.abs(x - 10) > 2 && y < TOP - 1);
                }
            }
        }
        assertTrue(sawRock);
    }
}
