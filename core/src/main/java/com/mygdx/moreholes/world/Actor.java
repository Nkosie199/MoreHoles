package com.mygdx.moreholes.world;

/** Something in the world besides Molly that acts on its own and eventually leaves. */
interface Actor {
    void update(float delta, World world);

    boolean isGone();
}
