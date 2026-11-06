package com.mygdx.moreholes.world;

/** Walks across the surface and fills in every hole it comes across, cutting off Molly's air. */
public class Human implements Actor {
    private final GridBody body;
    private final Direction heading;
    private final float stepTime;
    private final float sealTime;
    private float stepTimer;
    private float sealTimer;
    private boolean sealing;
    private boolean gone;

    public Human(boolean fromLeft, Difficulty difficulty) {
        heading = fromLeft ? Direction.RIGHT : Direction.LEFT;
        body = new GridBody(fromLeft ? -1 : Burrow.COLS, Burrow.SURFACE_ROW);
        stepTime = difficulty.humanStepTime();
        sealTime = difficulty.sealTime();
        stepTimer = stepTime;
    }

    @Override
    public void update(float delta, World world) {
        body.advance(delta, stepTime);
        if (sealing) {
            sealTimer -= delta;
            if (sealTimer <= 0) {
                sealing = false;
                if (holeBelowCanBeSealed(world)) {
                    world.burrow().fill(body.x(), Burrow.TOP_SOIL_ROW);
                }
            }
            return;
        }
        stepTimer -= delta;
        if (stepTimer > 0) {
            return;
        }
        stepTimer = stepTime;
        if (holeBelowCanBeSealed(world)) {
            sealing = true;
            sealTimer = sealTime;
            return;
        }
        int x = body.x() + heading.dx;
        if (x < -1 || x > Burrow.COLS) {
            gone = true;
        } else if (!world.molly().body().isAt(x, Burrow.SURFACE_ROW)) {
            body.stepTo(x, Burrow.SURFACE_ROW);
        }
    }

    private boolean holeBelowCanBeSealed(World world) {
        int x = body.x();
        return Burrow.onSurface(x, Burrow.SURFACE_ROW)
            && world.burrow().isHole(x)
            && !world.isCreatureAt(x, Burrow.TOP_SOIL_ROW)
            && !world.molly().body().isAt(x, Burrow.TOP_SOIL_ROW);
    }

    public boolean occupies(int x, int y) {
        return body.isAt(x, y);
    }

    public boolean isSealing() {
        return sealing;
    }

    @Override
    public boolean isGone() {
        return gone;
    }

    public GridBody body() {
        return body;
    }

    public Direction heading() {
        return heading;
    }
}
