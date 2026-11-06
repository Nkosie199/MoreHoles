package com.mygdx.moreholes.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.moreholes.world.Bird;
import com.mygdx.moreholes.world.Food;
import com.mygdx.moreholes.world.Human;
import com.mygdx.moreholes.world.Snake;
import com.mygdx.moreholes.world.World;

/** Draws a whole game: scenery, creatures, Molly, then the HUD. Shapes and sprites are drawn in separate passes. */
public class WorldView implements Disposable {
    private final SceneryRenderer scenery = new SceneryRenderer();
    private final CritterRenderer critters = new CritterRenderer();
    private final MollyRenderer molly = new MollyRenderer();
    private final Hud hud = new Hud();
    private float time;

    public void draw(World world, ShapeRenderer shapes, SpriteBatch batch, BitmapFont font, float delta) {
        time += delta;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.begin(ShapeType.Filled);
        scenery.drawSky(shapes, world.clock());
        scenery.drawGround(shapes, world.burrow());
        for (Food food : world.foods()) {
            critters.drawFood(shapes, food, time);
        }
        for (Human human : world.humans()) {
            critters.drawHuman(shapes, human, time);
        }
        for (Snake snake : world.snakes()) {
            critters.drawSnake(shapes, snake);
        }
        shapes.end();

        batch.begin();
        molly.draw(batch, world.molly(), time);
        batch.end();

        shapes.begin(ShapeType.Filled);
        for (Bird bird : world.birds()) {
            critters.drawBird(shapes, bird, time);
        }
        hud.drawShapes(shapes, world);
        shapes.end();

        batch.begin();
        hud.drawText(batch, font, world);
        batch.end();
    }

    @Override
    public void dispose() {
        molly.dispose();
    }
}
