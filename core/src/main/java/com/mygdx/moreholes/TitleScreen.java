package com.mygdx.moreholes;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.moreholes.view.Layout;

/** Introduces Molly and explains the rules and controls before the first game. */
class TitleScreen extends ScreenAdapter {
    private static final String RULES =
        "Molly the mole has one power: digging. Survive as many days as you can.\n\n"
            + "Holes up to the surface let air into your tunnels,\n"
            + "but snakes slither in through them too.\n"
            + "Humans walk by and fill your holes in.\n"
            + "Grubs turn up on the surface, where birds hunt.\n"
            + "Every day brings more of everything.";
    private static final String CONTROLS =
        "Arrows or WASD: move and dig      Space: pack dirt behind you to block a tunnel      P: pause\n"
            + "Touch: hold where Molly should go, tap Molly to pack dirt";

    private final MoreHoles game;
    private final Controls controls;
    private final Texture molly = new Texture("molly_rightview.png");

    TitleScreen(MoreHoles game) {
        this.game = game;
        this.controls = new Controls(game.viewport());
    }

    @Override
    public void render(float delta) {
        if (controls.confirmPressed()) {
            game.startGame();
            return;
        }
        ScreenUtils.clear(0.16f, 0.1f, 0.07f, 1f);
        game.beginFrame();
        SpriteBatch batch = game.batch();
        BitmapFont font = game.font();
        batch.begin();
        batch.draw(molly, Layout.WIDTH / 2f - 68, 470, 136, 80);
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        font.draw(batch, "More Holes", 0, 440, Layout.WIDTH, Align.center, false);
        font.getData().setScale(0.5f);
        font.setColor(0.93f, 0.86f, 0.75f, 1f);
        font.draw(batch, RULES, 0, 360, Layout.WIDTH, Align.center, true);
        font.getData().setScale(0.42f);
        font.setColor(0.75f, 0.8f, 0.9f, 1f);
        font.draw(batch, CONTROLS, 0, 160, Layout.WIDTH, Align.center, true);
        font.getData().setScale(0.6f);
        font.setColor(1f, 0.85f, 0.3f, 1f);
        font.draw(batch, "Press Enter or tap to start", 0, 80, Layout.WIDTH, Align.center, false);
        batch.end();
    }

    @Override
    public void dispose() {
        molly.dispose();
    }
}
