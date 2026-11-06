package com.mygdx.moreholes.world;

/**
 * The player. Walks through tunnels, digs into soil by walking into it, and can kick spare dirt into the tunnel
 * behind her to wall snakes out. Needs air (from tunnels joined to a hole) and food (found on the surface).
 */
public class Molly {
    public static final float FULL = 100f;
    public static final int MAX_DIRT = 5;
    static final float STEP_TIME = 0.13f;
    static final float DIG_TIME = 0.22f;
    static final float AIR_LOSS_PER_SECOND = 12f;
    static final float AIR_GAIN_PER_SECOND = 45f;
    static final float SUFFOCATION_DAMAGE_PER_SECOND = 12f;
    static final float STARVATION_DAMAGE_PER_SECOND = 6f;
    private static final float HURT_GRACE = 1f;

    private final GridBody body;
    private Direction facing = Direction.RIGHT;
    private float health = FULL;
    private float air = FULL;
    private float food = FULL;
    private int dirt;
    private Direction digging;
    private float digTimer;
    private float hurtGrace;
    private Hazard lastHazard;

    public Molly(int x, int y) {
        body = new GridBody(x, y);
    }

    void update(float delta, World world, Direction wanted, float hungerPerSecond) {
        hurtGrace -= delta;
        body.advance(delta, STEP_TIME);
        if (digging != null) {
            continueDigging(delta, world);
        } else if (wanted != null && !body.isMoving()) {
            facing = wanted;
            tryStep(world, wanted);
        }
        breathe(delta, world.burrow());
        digest(delta, hungerPerSecond);
    }

    /** Packs dirt into the tunnel tile behind Molly. Returns false if she has no dirt or there is nowhere to put it. */
    boolean plug(World world) {
        int x = body.x() - facing.dx;
        int y = body.y() - facing.dy;
        if (dirt == 0 || digging != null || !world.burrow().isTunnel(x, y) || world.isCreatureAt(x, y)) {
            return false;
        }
        world.burrow().fill(x, y);
        dirt--;
        return true;
    }

    /** A bite or a strike. Ignored for a moment after the last one, so one attack is not counted every frame. */
    boolean attack(float damage, Hazard hazard) {
        if (hurtGrace > 0) {
            return false;
        }
        hurtGrace = HURT_GRACE;
        hurt(damage, hazard);
        return true;
    }

    void eat(float nourishment, float healing) {
        food = Math.min(FULL, food + nourishment);
        health = Math.min(FULL, health + healing);
    }

    private void tryStep(World world, Direction direction) {
        int x = body.x() + direction.dx;
        int y = body.y() + direction.dy;
        if (!canReach(x, y) || world.isCreatureAt(x, y)) {
            return;
        }
        Burrow burrow = world.burrow();
        if (burrow.isOpen(x, y)) {
            body.stepTo(x, y);
        } else if (burrow.canDig(x, y)) {
            digging = direction;
            digTimer = DIG_TIME;
        }
    }

    private boolean canReach(int x, int y) {
        if (Burrow.onSurface(x, y)) {
            return isExposed() || body.y() == Burrow.TOP_SOIL_ROW;
        }
        return Burrow.inSoil(x, y);
    }

    private void continueDigging(float delta, World world) {
        digTimer -= delta;
        if (digTimer > 0) {
            return;
        }
        int x = body.x() + digging.dx;
        int y = body.y() + digging.dy;
        digging = null;
        if (world.isCreatureAt(x, y) || !world.burrow().canDig(x, y)) {
            return;
        }
        world.burrow().dig(x, y);
        world.addScore(World.DIG_POINTS);
        dirt = Math.min(MAX_DIRT, dirt + 1);
        body.stepTo(x, y);
    }

    private void breathe(float delta, Burrow burrow) {
        if (burrow.hasAir(body.x(), body.y())) {
            air = Math.min(FULL, air + AIR_GAIN_PER_SECOND * delta);
            return;
        }
        air = Math.max(0, air - AIR_LOSS_PER_SECOND * delta);
        if (air == 0) {
            hurt(SUFFOCATION_DAMAGE_PER_SECOND * delta, Hazard.SUFFOCATION);
        }
    }

    private void digest(float delta, float hungerPerSecond) {
        food = Math.max(0, food - hungerPerSecond * delta);
        if (food == 0) {
            hurt(STARVATION_DAMAGE_PER_SECOND * delta, Hazard.HUNGER);
        }
    }

    private void hurt(float damage, Hazard hazard) {
        health = Math.max(0, health - damage);
        lastHazard = hazard;
    }

    public boolean isAlive() {
        return health > 0;
    }

    /** On the surface, where birds can see her. */
    public boolean isExposed() {
        return body.y() == Burrow.SURFACE_ROW;
    }

    public boolean isDigging() {
        return digging != null;
    }

    public boolean isHurting() {
        return hurtGrace > 0;
    }

    public GridBody body() {
        return body;
    }

    public Direction facing() {
        return facing;
    }

    public float health() {
        return health;
    }

    public float air() {
        return air;
    }

    public float food() {
        return food;
    }

    public int dirt() {
        return dirt;
    }

    public Hazard lastHazard() {
        return lastHazard;
    }
}
