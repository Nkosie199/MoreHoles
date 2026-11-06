package com.mygdx.moreholes.world;

/** A position on the grid that slides from one tile to the next, so it can be drawn between tiles. */
public class GridBody {
    private int x;
    private int y;
    private int fromX;
    private int fromY;
    private float progress = 1f;

    public GridBody(int x, int y) {
        place(x, y);
    }

    public void place(int x, int y) {
        this.x = x;
        this.y = y;
        fromX = x;
        fromY = y;
        progress = 1f;
    }

    public void stepTo(int newX, int newY) {
        fromX = x;
        fromY = y;
        x = newX;
        y = newY;
        progress = 0f;
    }

    public void advance(float delta, float stepTime) {
        progress = Math.min(1f, progress + delta / stepTime);
    }

    public boolean isMoving() {
        return progress < 1f;
    }

    /** 0 just after leaving the last tile, 1 once arrived. */
    public float progress() {
        return progress;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public boolean isAt(int tileX, int tileY) {
        return x == tileX && y == tileY;
    }

    public float drawX() {
        return fromX + (x - fromX) * progress;
    }

    public float drawY() {
        return fromY + (y - fromY) * progress;
    }
}
