package com.mygdx.moreholes.world;

public enum Hazard {
    SNAKE("A snake got Molly"),
    BIRD("A bird caught Molly in the open"),
    SUFFOCATION("Molly ran out of air"),
    HUNGER("Molly starved");

    public final String description;

    Hazard(String description) {
        this.description = description;
    }
}
