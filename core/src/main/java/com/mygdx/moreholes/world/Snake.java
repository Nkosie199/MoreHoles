package com.mygdx.moreholes.world;

/**
 * Slides in through a hole and hunts Molly through the tunnels. It cannot dig, so plugged tunnels stop it. After a
 * bite, or once it has been around long enough, it heads back out of the nearest hole.
 */
public class Snake implements Actor {
    public static final int TAIL_LENGTH = 3;
    private static final float GIVE_UP_AFTER_LEAVING = 6f;

    private final GridBody head;
    // Tiles the head has left, newest first; one more than the tail length so each segment can slide.
    private final int[] trailX = new int[TAIL_LENGTH + 1];
    private final int[] trailY = new int[TAIL_LENGTH + 1];
    private final float stepTime;
    private final float lifetime;
    private float stepTimer;
    private float age;
    private boolean leaving;
    private boolean gone;

    public Snake(int holeX, Difficulty difficulty) {
        head = new GridBody(holeX, Burrow.SURFACE_ROW);
        head.stepTo(holeX, Burrow.TOP_SOIL_ROW);
        for (int i = 0; i <= TAIL_LENGTH; i++) {
            trailX[i] = holeX;
            trailY[i] = Burrow.SURFACE_ROW;
        }
        stepTime = difficulty.snakeStepTime();
        lifetime = difficulty.snakeLifetime();
        stepTimer = stepTime;
    }

    @Override
    public void update(float delta, World world) {
        age += delta;
        head.advance(delta, stepTime);
        stepTimer -= delta;
        if (stepTimer > 0) {
            return;
        }
        stepTimer = stepTime;
        leaving = leaving || age > lifetime;
        if (leaving && head.y() == Burrow.TOP_SOIL_ROW) {
            gone = true;
            return;
        }
        Direction step = leaving ? wayOut(world) : wayToMolly(world);
        if (step == null) {
            step = wander(world);
        }
        if (step == null) {
            gone = leaving && age > lifetime + GIVE_UP_AFTER_LEAVING;
            return;
        }
        slither(world, step);
    }

    private void slither(World world, Direction step) {
        int x = head.x() + step.dx;
        int y = head.y() + step.dy;
        if (world.molly().body().isAt(x, y)) {
            world.snakeBites();
            leaving = true;
        } else if (!world.isCreatureAt(x, y)) {
            for (int i = TAIL_LENGTH; i > 0; i--) {
                trailX[i] = trailX[i - 1];
                trailY[i] = trailY[i - 1];
            }
            trailX[0] = head.x();
            trailY[0] = head.y();
            head.stepTo(x, y);
        }
    }

    private Direction wayToMolly(World world) {
        GridBody molly = world.molly().body();
        if (!Burrow.inSoil(molly.x(), molly.y())) {
            return null;
        }
        return Pathfinder.firstStep(world.burrow(), head.x(), head.y(), molly::isAt, world::isCreatureAt);
    }

    private Direction wayOut(World world) {
        return Pathfinder.firstStep(world.burrow(), head.x(), head.y(),
            (x, y) -> y == Burrow.TOP_SOIL_ROW && world.burrow().isHole(x) && !world.isCreatureAt(x, y),
            world::isCreatureAt);
    }

    /** A random open neighbour, avoiding going straight back where it came from if it can. */
    private Direction wander(World world) {
        Direction[] options = new Direction[4];
        int count = 0;
        Direction back = null;
        for (Direction d : Direction.values()) {
            int x = head.x() + d.dx;
            int y = head.y() + d.dy;
            if (!world.burrow().isTunnel(x, y) || world.isCreatureAt(x, y)) {
                continue;
            }
            if (x == trailX[0] && y == trailY[0]) {
                back = d;
            } else {
                options[count++] = d;
            }
        }
        if (count > 0) {
            return options[world.random().nextInt(count)];
        }
        return back;
    }

    public boolean occupies(int x, int y) {
        return head.isAt(x, y);
    }

    @Override
    public boolean isGone() {
        return gone;
    }

    public GridBody head() {
        return head;
    }

    /** Where tail segment {@code i} (0 nearest the head) is drawn, in tiles, sliding along with the head. */
    public float segmentX(int i) {
        return trailX[i + 1] + (trailX[i] - trailX[i + 1]) * head.progress();
    }

    public float segmentY(int i) {
        return trailY[i + 1] + (trailY[i] - trailY[i + 1]) * head.progress();
    }
}
