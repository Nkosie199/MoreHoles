package com.mygdx.moreholes.world;

/** How hard a given day is. Like Tetris levels, every day brings more and faster threats. */
public class Difficulty {
    private final int step;

    public Difficulty(int day) {
        this.step = Math.max(0, day - 1);
    }

    public float snakeEvery() {
        return Math.max(3.5f, 11f - 1.3f * step);
    }

    public float snakeStepTime() {
        return Math.max(0.16f, 0.45f - 0.035f * step);
    }

    public int maxSnakes() {
        return Math.min(5, 1 + step / 2);
    }

    public float snakeLifetime() {
        return 18f + 2f * step;
    }

    public float humanEvery() {
        return Math.max(4.5f, 15f - 1.5f * step);
    }

    public float humanStepTime() {
        return Math.max(0.22f, 0.55f - 0.04f * step);
    }

    public float sealTime() {
        return Math.max(0.7f, 2f - 0.15f * step);
    }

    public float birdEvery() {
        return Math.max(3.5f, 12f - 1.2f * step);
    }

    /** In tiles per second. */
    public float birdSpeed() {
        return 3.5f + 0.4f * step;
    }

    public float birdDiveSpeed() {
        return 6f + 0.6f * step;
    }

    public float foodEvery() {
        return 5f + 0.6f * step;
    }

    public float foodLifetime() {
        return Math.max(6f, 13f - 0.7f * step);
    }

    /** Food meter lost per second; a full meter lasts about one day at the start. */
    public float hungerPerSecond() {
        return 2.2f + 0.3f * step;
    }
}
