package com.mygdx.moreholes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.moreholes.view.Layout;
import com.mygdx.moreholes.view.WorldView;
import com.mygdx.moreholes.world.World;
import java.util.Random;

/** One game, from Molly's first day until she dies. Can be paused; offers a new game when it is over. */
class PlayScreen extends ScreenAdapter {
    private static final float MAX_FRAME = 0.1f;
    // Stops a key held at the moment Molly dies from starting the next game straight away.
    private static final float RESTART_DELAY = 1f;

    private final MoreHoles game;
    private final Controls controls;
    private final WorldView view = new WorldView();
    private final World world = World.newGame(new Random());
    private boolean paused;
    private float overFor;

    PlayScreen(MoreHoles game) {
        this.game = game;
        this.controls = new Controls(game.viewport());
    }

    @Override
    public void render(float delta) {
        float frame = Math.min(delta, MAX_FRAME);
        if (world.isOver()) {
            overFor += delta;
            if (overFor > RESTART_DELAY && controls.confirmPressed()) {
                game.startGame();
                return;
            }
        } else {
            handleInput();
        }
        if (!paused) {
            world.update(frame, controls.heldDirection(world.molly().body()));
        }
        ScreenUtils.clear(0, 0, 0, 1);
        game.beginFrame();
        view.draw(world, game.shapes(), game.batch(), game.font(), paused ? 0 : frame);
        if (world.isOver()) {
            drawOverlay("Game over", world.molly().lastHazard().description + " on day " + world.clock().day()
                + "\nScore " + world.score() + "\n\nPress Enter or tap to dig again");
        } else if (paused) {
            drawOverlay("Paused", "Press P or tap the top bar to carry on");
        }
    }

    @Override
    public void pause() {
        paused = true;
    }

    private void handleInput() {
        if (controls.pausePressed()) {
            paused = !paused;
        } else if (!paused && controls.plugPressed(world.molly().body())) {
            world.plug();
        }
    }

    private void drawOverlay(String title, String message) {
        ShapeRenderer shapes = game.shapes();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.begin(ShapeType.Filled);
        shapes.setColor(0, 0, 0, 0.65f);
        shapes.rect(0, 0, Layout.WIDTH, Layout.HEIGHT);
        shapes.end();
        SpriteBatch batch = game.batch();
        BitmapFont font = game.font();
        batch.begin();
        font.getData().setScale(1.1f);
        font.setColor(Color.WHITE);
        font.draw(batch, title, 0, Layout.HEIGHT / 2f + 90, Layout.WIDTH, Align.center, false);
        font.getData().setScale(0.6f);
        font.setColor(0.93f, 0.86f, 0.75f, 1f);
        font.draw(batch, message, 0, Layout.HEIGHT / 2f + 20, Layout.WIDTH, Align.center, true);
        batch.end();
    }

    @Override
    public void dispose() {
        view.dispose();
    }
}
