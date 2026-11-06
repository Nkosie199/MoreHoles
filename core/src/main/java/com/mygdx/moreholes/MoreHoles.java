package com.mygdx.moreholes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.moreholes.view.Layout;

/** Holds what every screen shares (the view, renderers and font) and starts on the title screen. */
public class MoreHoles extends Game {
    private FitViewport viewport;
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        viewport = new FitViewport(Layout.WIDTH, Layout.HEIGHT);
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("calibri.fnt"), Gdx.files.internal("calibri.png"), false);
        showTitle();
    }

    void showTitle() {
        setScreen(new TitleScreen(this));
    }

    void startGame() {
        setScreen(new PlayScreen(this));
    }

    @Override
    public void setScreen(Screen screen) {
        Screen previous = getScreen();
        super.setScreen(screen);
        if (previous != null) {
            previous.dispose();
        }
    }

    /** Applies the shared view and points both renderers at it. */
    void beginFrame() {
        viewport.apply();
        shapes.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }

    FitViewport viewport() {
        return viewport;
    }

    ShapeRenderer shapes() {
        return shapes;
    }

    SpriteBatch batch() {
        return batch;
    }

    BitmapFont font() {
        return font;
    }
}
