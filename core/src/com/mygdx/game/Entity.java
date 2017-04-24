package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.PlayScreen.Direction;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Entity {
	public PlayScreen playscreen;
        public AI ai = new AI();
	public int x;
	public int y;
	public int dx;
	public int dy;
	public int width;
	public int height;  
	public float speed;
	public Texture texture;
        public boolean isPlayer;
        Player player;
        Entity enemy;
	
	public Entity(PlayScreen game, int x, int y, int width, int height, int speed, Texture texture, boolean isPlayer) {
		this.playscreen = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.texture = texture;
                this.isPlayer = isPlayer;
	}
        
        public void replaceEnemy(){
            for (int i=0; i<playscreen.entities.size(); i++){
                if (playscreen.entities.get(i).isPlayer == false){ // if not a player, ie. is an enemy
                    // replace enemy
                    //playscreen.entities.remove(i);

                        // try wait for a few seconds
                        //TimeUnit.SECONDS.sleep(5);
                    System.out.println("Replacing enemy!");
                    //playscreen.entities.add(i, new Entity(this.playscreen, 20, 400, 20, 20, 120, new Texture("enemy.png"), false));
                    this.x=20;
                }
            }
        }

	public void update(float delta) {
            dx = 0; 
            dy = 0;
	}
	
	public void move(int newX, int newY) {
		x = newX;
		y = newY;		
	}
	
	public void render() {       
                // if not a player, ie. is an enemy, render AI logic...
                
                for (Entity entity : playscreen.entities){
                    //System.out.println(entity.isPlayer);
                        if (entity.isPlayer == true){
                            player = (Player) entity;
                           // System.out.println("Player co-ord: "+player.x+", "+player.y);
                        }
                        if (entity.isPlayer == false){ // if not a player, ie. is an enemy
                            enemy = entity;
                            //System.out.println("Enemy co-ord: "+enemy.x+", "+enemy.y);
                        }
                }
                ai.render(enemy, player);
	}

	public void tileCollision(int tile, int tileX, int tileY, int newX, int newY, Direction direction) {
		//System.out.println("tile collision at: " + tileX + " " + tileY);
		
		if(direction == Direction.U) {
			y = tileY * playscreen.tileSize + playscreen.tileSize;
		}
		else if(direction == Direction.D) {
			y = tileY * playscreen.tileSize - height;
		}
		else if(direction == Direction.L) {
			x = tileX * playscreen.tileSize + playscreen.tileSize;
		}
		else if(direction == Direction.R) {
			x = tileX * playscreen.tileSize - width;
		}		
	}

	public void entityCollision(Entity e2, int newX, int newY, Direction direction) {
		//System.out.println("entity collision around: " + newX + " " + newY);
		
		move(newX, newY);
		// could also resolve entity collisions in the same we do tile collision resolution
		// as shown in class
                UI.damage();
	}
}
