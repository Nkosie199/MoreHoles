package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.ArrayList;

public class MoreHoles extends Game {
    SpriteBatch batch;
    PlayScreen game;
    
  @Override
  public void resize(int width, int height){
//      camera.viewportWidth = width;
//      camera.viewportHeight = height;
//      camera.update();
  }
  
  @Override
  public void dispose(){
//      tiledmap.dispose();
//      renderer.dispose();
  }
  
  @Override
  public void create() {
          game = new PlayScreen(this);
          game.create();
          batch = game.batch;
          //new edit . . .
          setScreen(new StartScreen(this));
  }
  
  @Override
  public void render () { 
      super.render();      
  }


}
