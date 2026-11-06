package com.mygdx.moreholes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.moreholes.view.Layout;
import com.mygdx.moreholes.world.Direction;
import com.mygdx.moreholes.world.GridBody;

/**
 * Keyboard: arrows or WASD move and dig, Space packs dirt behind Molly, P or Esc pauses. Touch: hold anywhere and Molly heads
 * that way, tap Molly to pack dirt, tap the top bar to pause.
 */
class Controls {
    private static final float ON_MOLLY = Layout.TILE * 0.75f;

    private final Viewport viewport;
    private final Vector2 touch = new Vector2();

    Controls(Viewport viewport) {
        this.viewport = viewport;
    }

    Direction heldDirection(GridBody molly) {
        Direction key = keyboardDirection();
        if (key != null) {
            return key;
        }
        if (!Gdx.input.isTouched() || !touchInField()) {
            return null;
        }
        float dx = touch.x - centreX(molly);
        float dy = touch.y - centreY(molly);
        if (Math.abs(dx) < ON_MOLLY && Math.abs(dy) < ON_MOLLY) {
            return null;
        }
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        return dy > 0 ? Direction.UP : Direction.DOWN;
    }

    private static Direction keyboardDirection() {
        if (isEitherPressed(Keys.UP, Keys.W)) {
            return Direction.UP;
        }
        if (isEitherPressed(Keys.DOWN, Keys.S)) {
            return Direction.DOWN;
        }
        if (isEitherPressed(Keys.LEFT, Keys.A)) {
            return Direction.LEFT;
        }
        if (isEitherPressed(Keys.RIGHT, Keys.D)) {
            return Direction.RIGHT;
        }
        return null;
    }

    private static boolean isEitherPressed(int key, int otherKey) {
        return Gdx.input.isKeyPressed(key) || Gdx.input.isKeyPressed(otherKey);
    }

    boolean plugPressed(GridBody molly) {
        if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.E)) {
            return true;
        }
        return Gdx.input.justTouched() && touchInField()
            && Math.abs(touch.x - centreX(molly)) < ON_MOLLY && Math.abs(touch.y - centreY(molly)) < ON_MOLLY;
    }

    boolean pausePressed() {
        if (Gdx.input.isKeyJustPressed(Keys.P) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            return true;
        }
        return Gdx.input.justTouched() && !touchInField();
    }

    boolean confirmPressed() {
        return Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Keys.SPACE);
    }

    private boolean touchInField() {
        viewport.unproject(touch.set(Gdx.input.getX(), Gdx.input.getY()));
        return touch.y < Layout.FIELD_HEIGHT;
    }

    private static float centreX(GridBody molly) {
        return (molly.drawX() + 0.5f) * Layout.TILE;
    }

    private static float centreY(GridBody molly) {
        return (molly.drawY() + 0.5f) * Layout.TILE;
    }
}
