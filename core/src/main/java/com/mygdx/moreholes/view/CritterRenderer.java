package com.mygdx.moreholes.view;

import static com.mygdx.moreholes.view.Layout.TILE;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.moreholes.world.Bird;
import com.mygdx.moreholes.world.Burrow;
import com.mygdx.moreholes.world.Food;
import com.mygdx.moreholes.world.GridBody;
import com.mygdx.moreholes.world.Human;
import com.mygdx.moreholes.world.Snake;

/** Draws everything that shares the ground with Molly, as simple cartoon shapes. */
class CritterRenderer {
    private static final float HALF = TILE / 2f;
    private static final float SURFACE_Y = Burrow.SURFACE_ROW * TILE;

    void drawFood(ShapeRenderer shapes, Food food, float time) {
        if (food.freshness() < 0.25f && MathUtils.sin(time * 20f) < 0) {
            return;
        }
        float cx = food.x() * TILE + HALF;
        float y = SURFACE_Y + 6;
        shapes.setColor(Palette.GRUB);
        for (int i = 0; i < 4; i++) {
            float wiggle = MathUtils.sin(time * 6f + i) * 1.5f;
            shapes.circle(cx - 9 + i * 6, y + wiggle, 4.5f, 10);
        }
        shapes.setColor(Palette.GRUB_HEAD);
        shapes.circle(cx + 11, y + MathUtils.sin(time * 6f + 4) * 1.5f, 3.5f, 10);
    }

    void drawSnake(ShapeRenderer shapes, Snake snake) {
        for (int i = Snake.TAIL_LENGTH - 1; i >= 0; i--) {
            float radius = HALF * (0.55f - i * 0.08f);
            shapes.setColor(i % 2 == 0 ? Palette.SNAKE : Palette.SNAKE_DARK);
            shapes.circle(snake.segmentX(i) * TILE + HALF, snake.segmentY(i) * TILE + HALF, radius, 14);
        }
        GridBody head = snake.head();
        float cx = head.drawX() * TILE + HALF;
        float cy = head.drawY() * TILE + HALF;
        shapes.setColor(Palette.TONGUE);
        shapes.rectLine(cx, cy, cx + (head.x() - snake.segmentX(0)) * 16, cy + (head.y() - snake.segmentY(0)) * 16, 2);
        shapes.setColor(Palette.SNAKE);
        shapes.circle(cx, cy, HALF * 0.65f, 16);
        shapes.setColor(Palette.EYE);
        shapes.circle(cx - 4, cy + 3, 2.5f, 8);
        shapes.circle(cx + 4, cy + 3, 2.5f, 8);
    }

    void drawHuman(ShapeRenderer shapes, Human human, float time) {
        float cx = human.body().drawX() * TILE + HALF;
        float stride = human.body().isMoving() ? MathUtils.sin(time * 14f) * 3f : 0;
        shapes.setColor(Palette.TROUSERS);
        shapes.rect(cx - 7 + stride, SURFACE_Y, 5, 18);
        shapes.rect(cx + 2 - stride, SURFACE_Y, 5, 18);
        shapes.setColor(Palette.SHIRT);
        shapes.rect(cx - 9, SURFACE_Y + 17, 18, 20);
        shapes.setColor(Palette.SKIN);
        shapes.circle(cx, SURFACE_Y + 44, 8, 14);
        shapes.setColor(Palette.HAT);
        shapes.rect(cx - 11, SURFACE_Y + 49, 22, 3);
        shapes.rect(cx - 7, SURFACE_Y + 51, 14, 6);
        if (human.isSealing()) {
            float lean = human.heading().dx * 4f;
            float dig = MathUtils.sin(time * 10f) * 3f;
            shapes.setColor(Palette.SHOVEL);
            shapes.rectLine(cx + lean, SURFACE_Y + 30, cx + lean * 2, SURFACE_Y - 4 + dig, 3);
            shapes.setColor(Palette.SOIL_TOP);
            shapes.circle(cx + lean * 2, SURFACE_Y - 6 + dig, 5, 10);
        }
    }

    void drawBird(ShapeRenderer shapes, Bird bird, float time) {
        float cx = bird.x() * TILE;
        float cy = bird.y() * TILE;
        float facing = bird.heading();
        float flap = bird.isDiving() ? -6f : MathUtils.sin(time * 12f) * 9f;
        shapes.setColor(Palette.BIRD);
        shapes.triangle(cx - 4, cy, cx + 4, cy, cx - 6 * facing, cy + flap + 8);
        shapes.ellipse(cx - 11, cy - 5, 22, 10, 14);
        shapes.circle(cx + 10 * facing, cy + 3, 5, 10);
        shapes.setColor(Palette.BEAK);
        shapes.triangle(cx + 14 * facing, cy + 5, cx + 14 * facing, cy + 1, cx + 21 * facing, cy + 3);
        shapes.setColor(Palette.EYE);
        shapes.circle(cx + 11 * facing, cy + 5, 1.5f, 6);
    }
}
