package com.mygdx.moreholes.world;

/**
 * Circles over the surface. If it spots Molly out in the open it dives at her, strikes once, and flies off. Positions
 * are tile centres in tile units, so a bird is not tied to the grid.
 */
public class Bird implements Actor {
    public static final float CRUISE_HEIGHT = Burrow.SURFACE_ROW + 1.8f;
    private static final float SPOT_RANGE = 5f;
    private static final float STRIKE_RANGE = 0.6f;
    private static final float LOWEST = Burrow.SURFACE_ROW + 0.5f;

    private enum State { CRUISE, DIVE, CLIMB }

    private final float speed;
    private final float diveSpeed;
    private float x;
    private float y = CRUISE_HEIGHT;
    private float heading;
    private State state = State.CRUISE;
    private boolean gone;

    public Bird(boolean fromLeft, Difficulty difficulty) {
        x = fromLeft ? -1f : Burrow.COLS + 1f;
        heading = fromLeft ? 1f : -1f;
        speed = difficulty.birdSpeed();
        diveSpeed = difficulty.birdDiveSpeed();
    }

    @Override
    public void update(float delta, World world) {
        Molly molly = world.molly();
        float targetX = molly.body().drawX() + 0.5f;
        float targetY = molly.body().drawY() + 0.5f;
        switch (state) {
            case CRUISE:
                x += heading * speed * delta;
                if (molly.isExposed() && Math.abs(targetX - x) < SPOT_RANGE) {
                    state = State.DIVE;
                }
                break;
            case DIVE:
                if (!molly.isExposed()) {
                    state = State.CLIMB;
                    break;
                }
                dive(delta, targetX, targetY, world);
                break;
            default:
                x += heading * speed * delta;
                y = Math.min(CRUISE_HEIGHT + 1f, y + diveSpeed * 0.5f * delta);
        }
        gone = x < -2f || x > Burrow.COLS + 2f;
    }

    private void dive(float delta, float targetX, float targetY, World world) {
        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance < STRIKE_RANGE) {
            world.birdStrikes();
            state = State.CLIMB;
            return;
        }
        float travel = Math.min(distance, diveSpeed * delta);
        x += dx / distance * travel;
        y = Math.max(LOWEST, y + dy / distance * travel);
        if (dx != 0) {
            heading = Math.signum(dx);
        }
    }

    public boolean isDiving() {
        return state == State.DIVE;
    }

    @Override
    public boolean isGone() {
        return gone;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float heading() {
        return heading;
    }
}
