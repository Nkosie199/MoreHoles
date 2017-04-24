/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

/**
 *
 * @author Nkosingiphile
 */
public class PlayScreen implements Screen {
    public Game game;
    Texture texture;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    // new edits . . .
    UI ui = new UI();
//    AI ai = new AI();
    
    	SpriteBatch batch;
	int screenWidth;
	int screenHeight;
        TiledMap tiledmap;
        private OrthogonalTiledMapRenderer renderer;
        private OrthographicCamera camera;
        public boolean gameOver = false;
    
        public Player player;
        public Entity enemy;
	// 1 = block
	// 0 = empty
	// the x and y coordinate system is not what it seems
	// visually x goes down and y across
	// this will make more sense when you compare it to what is drawn
        // 28x22
	public int[][] map = {
		{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}, 
		{2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2}, 
		{2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2},
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2},
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
		{2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2},
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,2},
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2}, 
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2},
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2},
                {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,0,2},
		{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}, 
	};
	int mapWidth = 30;
	int mapHeight = 22;
	int tileSize = 20;
	Texture tileTexture, tileTexture2;
	
	ArrayList<Entity> entities = new ArrayList<Entity>();
	
	enum Axis { X, Y };
	enum Direction { U, D, L, R };
    
    public PlayScreen(Game game){
        // new edits
        this.game = game;
        
//        texture = new Texture("underground.jpg");
//        gameCam = new OrthographicCamera();
//        gamePort = new FitViewport(700, 650, gameCam);
        //
        batch = new SpriteBatch();
        tileTexture = new Texture("ground.png"); 
        tileTexture2 = new Texture("block.png");
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // add some entities including a player
        player = new Player(this, 200, 400, 20, 20, 200, new Texture("molly_rightview.png"), true);
        entities.add(player);
        entities.add(new Entity(this, 20, 400, 20, 20, 120, new Texture("enemy.png"), false));
        //

    }
    
    public void create(){
        ui.create(this); 
    }

    @Override
    public void render(float delta) {         
      	  // update
	  // ---
	  delta = Gdx.graphics.getDeltaTime(); 
	  // update all entities
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  // update entity based on input/ai/physics etc
		  // this is where we determine the change in position
		  e.update(delta);              
                  // new edit: also call render entity so that AI can take effect . . .
                  e.render();
		  // now we try move the entity on the map and check for collisions
		  moveEntity(e, e.x + e.dx, e.y + e.dy);
	  }	  
	  // draw
	  // --
	  // to offset where your map and entities are drawn change the viewport
	  // see libgdx documentation	  
	  Gdx.gl.glClearColor(0, 0, 0, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  batch.begin();         
          //renderer.setView(camera);
          //renderer.render();
	  // draw tile map
	  // go over each row bottom to top
	  for(int y = 0; y < mapHeight; y++) {
		  // go over each column left to right		
		  for(int x = 0; x < mapWidth; x++) {
			  // tile
			  if(map[x][y] == 1) {
				  batch.draw(tileTexture, x * tileSize, y * tileSize);
			  }
			  // draw other types here...
                          if(map[x][y] == 2) {
				  batch.draw(tileTexture2, x * tileSize, y * tileSize);
			  }
		  }
	  }
    	  // draw all entities
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  batch.draw(e.texture, e.x, e.y);
	  }
	  batch.end();
          ui.render();
    }
    public void resetGame(){
        //new UI();
        //new PlayScreen(game);
    }

    @Override
    public void resize(int width, int height) {
//        gamePort.update(width, height);
    }

    //  @Override     
      public void show(){
          TmxMapLoader loader = new TmxMapLoader();
          tiledmap = loader.load("underground.tmx");
          renderer = new OrthogonalTiledMapRenderer(tiledmap);
          camera = new OrthographicCamera();
      }

      public void gameOver(){
          //if (gameOver == true){
              System.out.println("GAME OVER!!!");
              game.setScreen(new GameOverScreen(game));
              //dispose();
          //}
      }
      
      public int getMapObject(int xcord, int ycord){
          int obj = map[xcord][ycord];
          return obj;
      }

      public void moveEntity(Entity e, int newX, int newY) {
              // just check x collisions keep y the same
              moveEntityInAxis(e, Axis.X, newX, e.y);
              // just check y collisions keep x the same
              moveEntityInAxis(e, Axis.Y, e.x, newY);
      }

      public void moveEntityInAxis(Entity e, Axis axis, int newX, int newY) {
              Direction direction; 
              // determine axis direction
              if(axis == Axis.Y) {
                      if(newY - e.y < 0){ 
                          direction = Direction.U;
                      }
                      else{
                          direction = Direction.D;
                      }
              }
              else {
                      if(newX - e.x < 0){
                          direction = Direction.L;
                      }
                      else{
                          direction = Direction.R;
                      }
              }
              if(!tileCollision(e, direction, newX, newY) && !entityCollision(e, direction, newX, newY)) {
                      // full move with no collision
                      e.move(newX, newY);
              }
              // or else if collision with a tile . . .
              // else collision with wither tile or entity occurred 
      }
  
    public boolean tileCollision(Entity e, Direction direction, int newX, int newY) {
            boolean collision = false;
            // determine affected tiles
            int x1 = (int) Math.floor(Math.min(e.x, newX) / tileSize);
            int y1 = (int) Math.floor(Math.min(e.y, newY) / tileSize);
            int x2 = (int) Math.floor((Math.max(e.x, newX) + e.width - 0.1f) / tileSize);
            int y2 = (int) Math.floor((Math.max(e.y, newY) + e.height - 0.1f) / tileSize);	  
            // todo: add boundary checks...
            // tile checks
            for(int x = x1; x <= x2; x++) {
                    for(int y = y1; y <= y2; y++) {
                            if(map[x][y] == 1) {
                                    collision = true;	        
                                    e.tileCollision(map[x][y], x, y, newX, newY, direction);
                                    // digging . . .
                                    if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                                      map[x][y] = 0;
                                    }
                            }
                            if(map[x][y] == 2) {
                                    collision = true;	        
                                    e.tileCollision(map[x][y], x, y, newX, newY, direction);
                            }
                    }
            }
            return collision;
    }

    public boolean entityCollision(Entity e1, Direction direction, int newX, int newY) {
            boolean collision = false;	  
            for(int i = 0; i < entities.size(); i++) {
                    Entity e2 = entities.get(i); 
                    // we don't want to check for collisions between the same entity
                    if(e1 != e2) {
                            // axis aligned rectangle rectangle collision detection
                            if(newX < e2.x + e2.width && e2.x < newX + e1.width &&
                                    newY < e2.y + e2.height && e2.y < newY + e1.height) {
                                    collision = true;

                                    e1.entityCollision(e2, newX, newY, direction);
                            }
                    }
            }
            return collision;
    }

    public void digInDirection(Entity e, Axis axis, int newX, int newY){
        Direction direction;

            // determine axis direction
            if(axis == Axis.Y) {
                    if(newY - e.y < 0) direction = Direction.U;
                    else direction = Direction.D;
            }
            else {
                    if(newX - e.x < 0) direction = Direction.L;
                    else direction = Direction.R;
            }
            if(tileCollision(e, direction, newX, newY) && !entityCollision(e, direction, newX, newY)) {
                    // full move with no collision
                if (map[(int)newX][(int)newY] == 1){
                    map[(int)newX][(int)newY] = 0;
                }
            }
    }

    @Override
    public void dispose() {
        
    }

    @Override
    public void hide() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
}
