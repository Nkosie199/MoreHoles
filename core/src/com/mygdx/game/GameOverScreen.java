/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 *
 * @author Nkosingiphile
 */
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;
    
    public GameOverScreen(Game game){
        this.game = game;
        
        viewport = new FitViewport(700, 650, new OrthographicCamera());
        SpriteBatch btch = ((MoreHoles) game).batch;
        stage = new Stage(viewport, btch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        
        Label gameOverLabel = new Label("GAME OVER", font);
        //Label playAgainLabel = new Label("Click Here To Try Again", font);
        
        table.add(gameOverLabel).expandX();
        table.row();
        //table.add(playAgainLabel).expandX().padTop(10f);
        
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            //game.setScreen(new StartScreen((MoreHoles) game));
            //dispose();
        }
            
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void show() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
}