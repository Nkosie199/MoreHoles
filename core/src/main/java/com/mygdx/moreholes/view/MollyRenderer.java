package com.mygdx.moreholes.view;

import static com.mygdx.moreholes.view.Layout.TILE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.moreholes.world.Direction;
import com.mygdx.moreholes.world.Molly;

/** Draws Molly from her sprites, facing the way she last moved. She shakes while digging and blinks when hurt. */
class MollyRenderer implements Disposable {
    private static final float SHORT_SIDE = TILE * 20f / 34f;

    private final Texture right = new Texture("molly_rightview.png");
    private final Texture left = new Texture("molly_leftview.png");
    private final Texture up = new Texture("molly_uprightview.png");
    private final Texture down = new Texture("molly_downrightview.png");

    void draw(SpriteBatch batch, Molly molly, float time) {
        if (molly.isHurting() && MathUtils.sin(time * 30f) < 0) {
            return;
        }
        float shake = molly.isDigging() ? MathUtils.sin(time * 60f) * 1.5f : 0;
        float x = molly.body().drawX() * TILE + shake;
        float y = molly.body().drawY() * TILE;
        Direction facing = molly.facing();
        if (facing == Direction.LEFT || facing == Direction.RIGHT) {
            Texture texture = facing == Direction.LEFT ? left : right;
            batch.draw(texture, x, y + (TILE - SHORT_SIDE) / 2f, TILE, SHORT_SIDE);
        } else {
            Texture texture = facing == Direction.UP ? up : down;
            batch.draw(texture, x + (TILE - SHORT_SIDE) / 2f, y, SHORT_SIDE, TILE);
        }
    }

    @Override
    public void dispose() {
        right.dispose();
        left.dispose();
        up.dispose();
        down.dispose();
    }
}
