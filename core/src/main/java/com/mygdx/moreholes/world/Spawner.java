package com.mygdx.moreholes.world;

import java.util.Random;

/** Decides when snakes, humans, birds and food turn up, at the pace the day's difficulty sets. */
class Spawner {
    private static final float NO_HOLE_RETRY = 1.5f;

    private final Random random;
    private float snakeTimer = 12f;
    private float humanTimer = 14f;
    private float birdTimer = 6f;
    private float foodTimer = 1.5f;

    Spawner(Random random) {
        this.random = random;
    }

    void update(float delta, World world, Difficulty difficulty) {
        snakeTimer -= delta;
        humanTimer -= delta;
        birdTimer -= delta;
        foodTimer -= delta;
        if (snakeTimer <= 0) {
            boolean spawned = world.snakes().size() < difficulty.maxSnakes() && world.spawnSnake();
            snakeTimer = spawned ? jitter(difficulty.snakeEvery()) : NO_HOLE_RETRY;
        }
        if (humanTimer <= 0) {
            world.spawnHuman(random.nextBoolean());
            humanTimer = jitter(difficulty.humanEvery());
        }
        if (birdTimer <= 0) {
            world.spawnBird(random.nextBoolean());
            birdTimer = jitter(difficulty.birdEvery());
        }
        if (foodTimer <= 0) {
            world.spawnFood();
            foodTimer = jitter(difficulty.foodEvery());
        }
    }

    private float jitter(float seconds) {
        return seconds * (0.75f + random.nextFloat() * 0.5f);
    }
}
