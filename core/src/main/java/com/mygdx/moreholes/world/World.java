package com.mygdx.moreholes.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Everything in one game of More Holes, and the rules that tie it together. Knows nothing about drawing. */
public class World {
    static final int DIG_POINTS = 1;
    static final int FOOD_POINTS = 25;
    static final int DAY_POINTS = 100;
    static final float SNAKE_DAMAGE = 25f;
    static final float BIRD_DAMAGE = 30f;
    private static final int START_X = Burrow.COLS / 2;
    private static final int START_Y = Burrow.TOP_SOIL_ROW - 3;

    private final Burrow burrow;
    private final Molly molly;
    private final Random random;
    private final Spawner spawner;
    private final DayClock clock = new DayClock();
    private final List<Snake> snakes = new ArrayList<>();
    private final List<Human> humans = new ArrayList<>();
    private final List<Bird> birds = new ArrayList<>();
    private final List<Food> foods = new ArrayList<>();
    private int score;

    /** A world where nothing turns up by itself, for tests. */
    World(Burrow burrow, Molly molly, Random random) {
        this(burrow, molly, random, null);
    }

    private World(Burrow burrow, Molly molly, Random random, Spawner spawner) {
        this.burrow = burrow;
        this.molly = molly;
        this.random = random;
        this.spawner = spawner;
    }

    /** Molly starts in a small den with one shaft up to the surface. */
    public static World newGame(Random random) {
        Burrow burrow = Burrow.withRocks(random, START_X);
        for (int y = START_Y; y <= Burrow.TOP_SOIL_ROW; y++) {
            burrow.dig(START_X, y);
        }
        burrow.dig(START_X - 1, START_Y);
        burrow.dig(START_X + 1, START_Y);
        return new World(burrow, new Molly(START_X, START_Y), random, new Spawner(random));
    }

    /** @param wanted the direction the player is holding, or null */
    public void update(float delta, Direction wanted) {
        if (isOver()) {
            return;
        }
        if (clock.advance(delta)) {
            score += DAY_POINTS * (clock.day() - 1);
        }
        Difficulty difficulty = clock.difficulty();
        molly.update(delta, this, wanted, difficulty.hungerPerSecond());
        eatFoodUnderMolly();
        updateAll(snakes, delta);
        updateAll(humans, delta);
        updateAll(birds, delta);
        updateAll(foods, delta);
        if (spawner != null) {
            spawner.update(delta, this, difficulty);
        }
    }

    public boolean plug() {
        return !isOver() && molly.plug(this);
    }

    public boolean isOver() {
        return !molly.isAlive();
    }

    /** Snakes and humans block the way; Molly is checked separately. */
    public boolean isCreatureAt(int x, int y) {
        for (Snake snake : snakes) {
            if (snake.occupies(x, y)) {
                return true;
            }
        }
        for (Human human : humans) {
            if (human.occupies(x, y)) {
                return true;
            }
        }
        return false;
    }

    /** A snake can only come in through an open hole that nobody is standing in. */
    boolean spawnSnake() {
        List<Integer> entrances = new ArrayList<>();
        for (int x = 0; x < Burrow.COLS; x++) {
            boolean free = !isCreatureAt(x, Burrow.TOP_SOIL_ROW) && !isCreatureAt(x, Burrow.SURFACE_ROW)
                && !molly.body().isAt(x, Burrow.TOP_SOIL_ROW);
            if (burrow.isHole(x) && free) {
                entrances.add(x);
            }
        }
        if (entrances.isEmpty()) {
            return false;
        }
        snakes.add(new Snake(entrances.get(random.nextInt(entrances.size())), clock.difficulty()));
        return true;
    }

    void spawnHuman(boolean fromLeft) {
        humans.add(new Human(fromLeft, clock.difficulty()));
    }

    void spawnBird(boolean fromLeft) {
        birds.add(new Bird(fromLeft, clock.difficulty()));
    }

    void spawnFood() {
        int x = random.nextInt(Burrow.COLS);
        if (!isFoodAt(x) && !isCreatureAt(x, Burrow.SURFACE_ROW) && !molly.body().isAt(x, Burrow.SURFACE_ROW)) {
            foods.add(new Food(x, clock.difficulty().foodLifetime()));
        }
    }

    void snakeBites() {
        molly.attack(SNAKE_DAMAGE, Hazard.SNAKE);
    }

    void birdStrikes() {
        molly.attack(BIRD_DAMAGE, Hazard.BIRD);
    }

    void addScore(int points) {
        score += points;
    }

    private boolean isFoodAt(int x) {
        for (Food food : foods) {
            if (food.x() == x) {
                return true;
            }
        }
        return false;
    }

    private void eatFoodUnderMolly() {
        if (!molly.isExposed()) {
            return;
        }
        Iterator<Food> it = foods.iterator();
        while (it.hasNext()) {
            if (it.next().x() == molly.body().x()) {
                it.remove();
                molly.eat(Food.NOURISHMENT, Food.HEALING);
                score += FOOD_POINTS * clock.day();
            }
        }
    }

    private void updateAll(List<? extends Actor> actors, float delta) {
        Iterator<? extends Actor> it = actors.iterator();
        while (it.hasNext()) {
            Actor actor = it.next();
            actor.update(delta, this);
            if (actor.isGone()) {
                it.remove();
            }
        }
    }

    Random random() {
        return random;
    }

    public Burrow burrow() {
        return burrow;
    }

    public Molly molly() {
        return molly;
    }

    public DayClock clock() {
        return clock;
    }

    public int score() {
        return score;
    }

    public List<Snake> snakes() {
        return Collections.unmodifiableList(snakes);
    }

    public List<Human> humans() {
        return Collections.unmodifiableList(humans);
    }

    public List<Bird> birds() {
        return Collections.unmodifiableList(birds);
    }

    public List<Food> foods() {
        return Collections.unmodifiableList(foods);
    }
}
