package com.mygdx.moreholes.view;

import com.mygdx.moreholes.world.Burrow;

/** Screen sizes in world units (pixels at 1:1). The play field sits under a strip for the HUD. */
public final class Layout {
    public static final int TILE = 32;
    public static final int SKY_ROWS = 2;
    public static final int FIELD_WIDTH = Burrow.COLS * TILE;
    public static final int FIELD_HEIGHT = (Burrow.SURFACE_ROW + 1 + SKY_ROWS) * TILE;
    public static final int HUD_HEIGHT = 64;
    public static final int WIDTH = FIELD_WIDTH;
    public static final int HEIGHT = FIELD_HEIGHT + HUD_HEIGHT;

    private Layout() {
    }
}
