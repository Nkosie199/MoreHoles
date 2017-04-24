package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Entity;

public class Player extends Entity {
	
	public Player(PlayScreen game, int x, int y, int width, int height, int speed, Texture texture, boolean isPlayer) {
		super(game, x, y, width, height, speed, texture, isPlayer);

	}

	@Override
	public void update(float delta) {
		
		dx = 0;
		dy = 0;

		// movement controls
		if(Gdx.input.isKeyPressed(Keys.UP)) {
                    for (Entity entity : playscreen.entities)
                        if (entity.isPlayer)
                            entity.texture = new Texture("molly_uprightview.png");
                            dy = (int) (speed * delta);
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
                    for (Entity entity : playscreen.entities)
                        if (entity.isPlayer)
                            entity.texture = new Texture("molly_downrightview.png");
                            dy = (int) (-speed * delta);
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
                    for (Entity entity : playscreen.entities)
                        if (entity.isPlayer)
                            entity.texture = new Texture("molly_leftview.png");
                            dx = (int) (-speed * delta);
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
                    for (Entity entity : playscreen.entities)
                        if (entity.isPlayer)
                            entity.texture = new Texture("molly_rightview.png");
                            dx = (int) (speed * delta);
		}
                // interaction button
                if(Gdx.input.isKeyPressed(Keys.SPACE)) {
                    // dig --> interaction
		}
                
	}
	
}
