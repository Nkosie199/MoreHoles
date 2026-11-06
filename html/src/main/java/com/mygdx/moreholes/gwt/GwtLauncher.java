package com.mygdx.moreholes.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.moreholes.MoreHoles;

/** Launches the browser version, filling the page. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
        config.padVertical = 0;
        config.padHorizontal = 0;
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MoreHoles();
    }
}
