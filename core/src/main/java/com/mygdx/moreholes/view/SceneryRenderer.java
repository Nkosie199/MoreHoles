package com.mygdx.moreholes.view;

import static com.mygdx.moreholes.view.Layout.TILE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.moreholes.world.Burrow;
import com.mygdx.moreholes.world.DayClock;
import com.mygdx.moreholes.world.Tile;

/**
 * The sky that turns from dawn to dusk, and the ground. Tunnels that air reaches are drawn warmer than stale ones,
 * so the player can see where Molly can breathe.
 */
class SceneryRenderer {
    private static final int SURFACE_Y = Burrow.SURFACE_ROW * TILE;
    private static final float GRASS_HEIGHT = 5f;
    private static final float SUNRISE_END = 0.15f;
    private static final float SUNSET_START = 0.8f;

    private final Color scratch = new Color();

    void drawSky(ShapeRenderer shapes, DayClock clock) {
        float t = clock.timeOfDay();
        shapes.setColor(skyColour(t));
        shapes.rect(0, SURFACE_Y, Layout.FIELD_WIDTH, Layout.FIELD_HEIGHT - SURFACE_Y);
        float sunX = MathUtils.lerp(TILE, Layout.FIELD_WIDTH - TILE, t);
        float sunY = SURFACE_Y + TILE * 0.8f + MathUtils.sin(t * MathUtils.PI) * TILE * 1.7f;
        shapes.setColor(Palette.SUN);
        shapes.circle(sunX, sunY, TILE * 0.45f, 20);
    }

    /** Dawn and dusk are kept short so the long middle of the day is clear blue rather than a muddy blend. */
    private Color skyColour(float timeOfDay) {
        if (timeOfDay < SUNRISE_END) {
            return scratch.set(Palette.DAWN).lerp(Palette.NOON, timeOfDay / SUNRISE_END);
        }
        if (timeOfDay > SUNSET_START) {
            return scratch.set(Palette.NOON).lerp(Palette.DUSK, (timeOfDay - SUNSET_START) / (1f - SUNSET_START));
        }
        return scratch.set(Palette.NOON);
    }

    void drawGround(ShapeRenderer shapes, Burrow burrow) {
        for (int x = 0; x < Burrow.COLS; x++) {
            for (int y = 0; y < Burrow.SOIL_ROWS; y++) {
                drawTile(shapes, burrow, x, y);
            }
            if (!burrow.isHole(x)) {
                shapes.setColor(Palette.GRASS);
                shapes.rect(x * TILE, SURFACE_Y - GRASS_HEIGHT, TILE, GRASS_HEIGHT);
            }
        }
    }

    private void drawTile(ShapeRenderer shapes, Burrow burrow, int x, int y) {
        float px = x * TILE;
        float py = y * TILE;
        Tile tile = burrow.tileAt(x, y);
        if (tile == Tile.TUNNEL) {
            shapes.setColor(burrow.hasAir(x, y) ? Palette.TUNNEL_AIRY : Palette.TUNNEL_STALE);
            shapes.rect(px, py, TILE, TILE);
            return;
        }
        drawSoil(shapes, x, y, px, py);
        if (tile == Tile.ROCK) {
            shapes.setColor(Palette.ROCK);
            shapes.ellipse(px + 2, py + 3, TILE - 4, TILE - 6, 16);
            shapes.setColor(Palette.ROCK_LIGHT);
            shapes.ellipse(px + 7, py + TILE * 0.5f, TILE * 0.35f, TILE * 0.25f, 10);
        }
    }

    private void drawSoil(ShapeRenderer shapes, int x, int y, float px, float py) {
        float depth = 1f - (float) y / Burrow.TOP_SOIL_ROW;
        float speckle = ((x * 7 + y * 13) % 5) * 0.012f;
        scratch.set(Palette.SOIL_TOP).lerp(Palette.SOIL_DEEP, depth);
        scratch.add(speckle, speckle, speckle, 0);
        shapes.setColor(scratch);
        shapes.rect(px, py, TILE, TILE);
    }
}
