package com.mygdx.moreholes.world;

/** Counts the days Molly survives. Each new day is harder than the last. */
public class DayClock {
    public static final float DAY_LENGTH = 45f;

    private int day = 1;
    private float elapsed;
    private Difficulty difficulty = new Difficulty(1);

    /** Returns true when a new day has just begun. */
    boolean advance(float delta) {
        elapsed += delta;
        if (elapsed < DAY_LENGTH) {
            return false;
        }
        elapsed -= DAY_LENGTH;
        day++;
        difficulty = new Difficulty(day);
        return true;
    }

    public int day() {
        return day;
    }

    /** 0 at sunrise, 1 at sunset. */
    public float timeOfDay() {
        return elapsed / DAY_LENGTH;
    }

    public float secondsIntoDay() {
        return elapsed;
    }

    public Difficulty difficulty() {
        return difficulty;
    }
}
