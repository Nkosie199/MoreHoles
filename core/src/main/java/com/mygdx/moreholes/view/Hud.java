package com.mygdx.moreholes.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.mygdx.moreholes.world.Burrow;
import com.mygdx.moreholes.world.Molly;
import com.mygdx.moreholes.world.World;

/** The strip above the field (day, score, health, air, food, dirt) and the warnings and day banner over it. */
class Hud {
    private static final float TOP = Layout.FIELD_HEIGHT;
    private static final float BAR_Y = TOP + 14;
    private static final float BAR_WIDTH = 120;
    private static final float BAR_HEIGHT = 14;
    private static final float[] BAR_X = {150, 290, 430};
    private static final float DIRT_X = 575;
    private static final float PIP = 10;
    private static final float BANNER_SECONDS = 2.5f;
    private static final float LOW_AIR = 40f;
    private static final float LOW_FOOD = 25f;

    void drawShapes(ShapeRenderer shapes, World world) {
        Molly molly = world.molly();
        shapes.setColor(Palette.HUD_BACK);
        shapes.rect(0, TOP, Layout.WIDTH, Layout.HUD_HEIGHT);
        drawBar(shapes, BAR_X[0], molly.health(), Palette.HEALTH);
        drawBar(shapes, BAR_X[1], molly.air(), Palette.AIR);
        drawBar(shapes, BAR_X[2], molly.food(), Palette.FOOD);
        for (int i = 0; i < Molly.MAX_DIRT; i++) {
            shapes.setColor(i < molly.dirt() ? Palette.DIRT : Palette.BAR_BACK);
            shapes.rect(DIRT_X + i * (PIP + 2), BAR_Y, PIP, BAR_HEIGHT);
        }
    }

    void drawText(SpriteBatch batch, BitmapFont font, World world) {
        font.getData().setScale(0.6f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Day " + world.clock().day(), 12, TOP + 54);
        font.getData().setScale(0.45f);
        font.draw(batch, "Score " + world.score(), 12, TOP + 26);
        font.setColor(0.8f, 0.8f, 0.85f, 1f);
        font.draw(batch, "Health", BAR_X[0], TOP + 52);
        font.draw(batch, "Air", BAR_X[1], TOP + 52);
        font.draw(batch, "Food", BAR_X[2], TOP + 52);
        font.draw(batch, "Dirt", DIRT_X, TOP + 52);
        drawWarning(batch, font, world.molly());
        drawDayBanner(batch, font, world);
    }

    private void drawBar(ShapeRenderer shapes, float x, float value, Color color) {
        shapes.setColor(Palette.BAR_BACK);
        shapes.rect(x, BAR_Y, BAR_WIDTH, BAR_HEIGHT);
        shapes.setColor(color);
        shapes.rect(x, BAR_Y, BAR_WIDTH * value / Molly.FULL, BAR_HEIGHT);
    }

    private void drawWarning(SpriteBatch batch, BitmapFont font, Molly molly) {
        String warning = null;
        if (molly.air() < LOW_AIR) {
            warning = "No air! Dig a hole up to the surface";
        } else if (molly.food() < LOW_FOOD) {
            warning = "Hungry! Grubs turn up on the surface";
        }
        if (warning == null) {
            return;
        }
        font.getData().setScale(0.5f);
        font.setColor(Palette.WARNING);
        font.draw(batch, warning, 0, Burrow.SURFACE_ROW * Layout.TILE - 12, Layout.WIDTH, Align.center, false);
    }

    private void drawDayBanner(SpriteBatch batch, BitmapFont font, World world) {
        float seconds = world.clock().secondsIntoDay();
        if (seconds > BANNER_SECONDS) {
            return;
        }
        font.getData().setScale(1.4f);
        font.setColor(1f, 1f, 1f, 1f - seconds / BANNER_SECONDS);
        font.draw(batch, "Day " + world.clock().day(), 0, Layout.FIELD_HEIGHT / 2f + 40, Layout.WIDTH, Align.center,
            false);
    }
}
