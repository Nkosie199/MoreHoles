package com.mygdx.moreholes.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class WorldTest {
    private static final int TOP = Burrow.TOP_SOIL_ROW;
    private static final float FRAME = 1 / 60f;

    private final Burrow burrow = new Burrow();

    @Test
    void walkingIntoSoilDigsAndCollectsDirt() {
        burrow.dig(5, 5);
        World world = worldWithMollyAt(5, 5);

        digOnce(world, Direction.RIGHT);

        assertTrue(burrow.isTunnel(6, 5));
        assertTrue(world.molly().body().isAt(6, 5));
        assertEquals(1, world.molly().dirt());
        assertEquals(World.DIG_POINTS, world.score());
    }

    @Test
    void rocksStopMolly() {
        burrow.dig(5, 5);
        burrow.placeRock(6, 5);
        World world = worldWithMollyAt(5, 5);

        run(world, 1f, Direction.RIGHT);

        assertTrue(world.molly().body().isAt(5, 5));
    }

    @Test
    void packingDirtFillsTheTunnelBehindMollyAndUsesTheDirt() {
        burrow.dig(5, 5);
        World world = worldWithMollyAt(5, 5);
        digOnce(world, Direction.RIGHT);

        assertTrue(world.plug());

        assertFalse(burrow.isTunnel(5, 5));
        assertEquals(0, world.molly().dirt());
        assertFalse(world.plug());
    }

    @Test
    void mollySuffocatesInATunnelCutOffFromTheSurface() {
        burrow.dig(5, 5);
        World world = worldWithMollyAt(5, 5);

        run(world, Molly.FULL / Molly.AIR_LOSS_PER_SECOND + 1f, null);

        assertEquals(0f, world.molly().air());
        assertTrue(world.molly().health() < Molly.FULL);
        assertEquals(Hazard.SUFFOCATION, world.molly().lastHazard());
    }

    @Test
    void mollyBreathesInTunnelsJoinedToAHole() {
        digShaft(5, 5);
        World world = worldWithMollyAt(5, 5);

        run(world, 20f, null);

        assertEquals(Molly.FULL, world.molly().air());
    }

    @Test
    void mollyClimbsOutOfAHoleAndEatsTheGrubThere() {
        burrow.dig(3, TOP);
        World world = new World(burrow, new Molly(3, TOP), alwaysPicks(3));
        world.spawnFood();
        run(world, 1f, null);
        float hungry = world.molly().food();

        run(world, FRAME, Direction.UP);

        assertTrue(world.molly().isExposed());
        assertTrue(world.foods().isEmpty());
        assertTrue(world.molly().food() > hungry);
        assertEquals(World.FOOD_POINTS, world.score());
    }

    @Test
    void grubsHealMolly() {
        burrow.dig(3, TOP);
        World world = new World(burrow, new Molly(3, TOP), alwaysPicks(3));
        world.snakeBites();
        float bitten = world.molly().health();
        world.spawnFood();

        run(world, FRAME, Direction.UP);

        assertTrue(bitten < Molly.FULL);
        assertEquals(bitten + Food.HEALING, world.molly().health(), 0.01f);
    }

    @Test
    void mollyCanOnlyReachTheSurfaceThroughAHole() {
        burrow.dig(3, TOP - 1);
        World world = worldWithMollyAt(3, TOP - 1);

        digOnce(world, Direction.UP);
        assertTrue(world.molly().body().isAt(3, TOP));

        run(world, FRAME, Direction.UP);
        assertTrue(world.molly().isExposed());
    }

    @Test
    void aSnakeComesInThroughAHoleAndBitesMolly() {
        digShaft(5, 8);
        World world = worldWithMollyAt(5, 8);

        assertTrue(world.spawnSnake());
        run(world, 3f, null);

        assertEquals(Molly.FULL - World.SNAKE_DAMAGE, world.molly().health(), 0.5f);
        assertEquals(Hazard.SNAKE, world.molly().lastHazard());
    }

    @Test
    void aPluggedTunnelKeepsTheSnakeOut() {
        digShaft(5, 8);
        World world = worldWithMollyAt(5, 8);
        burrow.fill(5, 9);

        world.spawnSnake();
        run(world, 5f, null);

        assertEquals(Molly.FULL, world.molly().health(), 0.5f);
    }

    @Test
    void noSnakeCanComeInWithoutAHole() {
        burrow.dig(5, 5);
        World world = worldWithMollyAt(5, 5);

        assertFalse(world.spawnSnake());
        assertTrue(world.snakes().isEmpty());
    }

    @Test
    void aPassingHumanFillsInHoles() {
        digShaft(2, 8);
        World world = worldWithMollyAt(2, 8);

        world.spawnHuman(true);
        run(world, 6f, null);

        assertFalse(burrow.isHole(2));
        assertFalse(burrow.hasAir(2, 8));
    }

    @Test
    void aHumanLeavesTheHoleMollyIsStandingIn() {
        burrow.dig(2, TOP);
        World world = worldWithMollyAt(2, TOP);

        world.spawnHuman(true);
        run(world, 6f, null);

        assertTrue(burrow.isHole(2));
    }

    @Test
    void aBirdStrikesMollyInTheOpen() {
        burrow.dig(2, TOP);
        World world = worldWithMollyAt(2, Burrow.SURFACE_ROW);

        world.spawnBird(true);
        run(world, 3f, null);

        assertEquals(Molly.FULL - World.BIRD_DAMAGE, world.molly().health(), 0.5f);
        assertEquals(Hazard.BIRD, world.molly().lastHazard());
    }

    @Test
    void aBirdCannotReachMollyUnderground() {
        digShaft(2, 8);
        World world = worldWithMollyAt(2, 8);

        world.spawnBird(true);
        run(world, 10f, null);

        assertEquals(Molly.FULL, world.molly().health(), 0.5f);
        assertTrue(world.birds().isEmpty());
    }

    @Test
    void eachNewDayIsHarderAndScores() {
        digShaft(5, 5);
        World world = worldWithMollyAt(5, 5);
        float firstDaySnakes = world.clock().difficulty().snakeEvery();

        run(world, DayClock.DAY_LENGTH + 0.1f, null);

        assertEquals(2, world.clock().day());
        assertTrue(world.clock().difficulty().snakeEvery() < firstDaySnakes);
        assertEquals(World.DAY_POINTS, world.score());
    }

    @Test
    void aNewGameStartsWithMollyBreathingInHerDen() {
        World world = World.newGame(new Random(7));

        GridBody molly = world.molly().body();
        assertTrue(world.burrow().hasAir(molly.x(), molly.y()));
        assertNotNull(world.clock());
        assertFalse(world.isOver());
    }

    @Test
    void theGameEndsWhenMollyDies() {
        burrow.dig(5, 5);
        World world = worldWithMollyAt(5, 5);

        run(world, 60f, null);

        assertTrue(world.isOver());
    }

    private World worldWithMollyAt(int x, int y) {
        return new World(burrow, new Molly(x, y), new Random(1));
    }

    /** A straight tunnel from the surface down to (x, bottom). */
    private void digShaft(int x, int bottom) {
        for (int y = bottom; y <= TOP; y++) {
            burrow.dig(x, y);
        }
    }

    /** Holds the key just long enough to start one dig, then lets go until Molly has moved in. */
    private static void digOnce(World world, Direction direction) {
        run(world, FRAME, direction);
        run(world, Molly.DIG_TIME + Molly.STEP_TIME, null);
    }

    private static void run(World world, float seconds, Direction held) {
        for (float t = 0; t < seconds; t += FRAME) {
            world.update(FRAME, held);
        }
    }

    private static Random alwaysPicks(int value) {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return value;
            }
        };
    }
}
