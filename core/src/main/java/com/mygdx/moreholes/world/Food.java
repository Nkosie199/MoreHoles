package com.mygdx.moreholes.world;

/** A grub lying on the surface. Feeds and heals Molly, but wriggles away if she does not come up for it in time. */
public class Food implements Actor {
    public static final float NOURISHMENT = 35f;
    public static final float HEALING = 15f;

    private final int x;
    private final float lifetime;
    private float age;

    public Food(int x, float lifetime) {
        this.x = x;
        this.lifetime = lifetime;
    }

    @Override
    public void update(float delta, World world) {
        age += delta;
    }

    @Override
    public boolean isGone() {
        return age >= lifetime;
    }

    /** From 1 when it appears down to 0 when it is about to go. */
    public float freshness() {
        return Math.max(0f, 1f - age / lifetime);
    }

    public int x() {
        return x;
    }
}
