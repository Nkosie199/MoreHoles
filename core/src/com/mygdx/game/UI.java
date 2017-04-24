/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author Nkosingiphile
 */
public class UI {
    private static int score = 0;
    private static int level = 1;
    private static int health = 100;
    private static BitmapFont font = new BitmapFont(Gdx.files.internal("calibri.fnt"),Gdx.files.internal("calibri.png"),false);
    private static String str;
    private static SpriteBatch spriteBatch = new SpriteBatch();
    private PlayScreen playscreen;
    
    public void create(PlayScreen playscreen){
        this.playscreen = playscreen;
        System.out.println("Playscreen Object Created in UI class!");
        System.out.println(playscreen);
        System.out.println(playscreen.getMapObject(0, 20));
        
    }

    public void render(){
        spriteBatch.begin();
        font.draw(spriteBatch, "Health: "+ health +"%", 10, 500);
        spriteBatch.end();
        
        str = "Score: "+String.valueOf(score);
        spriteBatch.begin();
        font.draw(spriteBatch, str, 10, 550);
        spriteBatch.end();
        
        str = "Level: "+String.valueOf(level);
        spriteBatch.begin();
        font.draw(spriteBatch, str, 10, 600);
        spriteBatch.end();
        
        if (health <= 0){ // ie. Molly is dead...
            isDead();
        }
    }
    
    public void addScore(int points){
        score += points;
    }
    
    public void addLevel(){
        level += 1;
    }
    
    public void isDead(){     
        playscreen.gameOver();
        playscreen.resetGame();
    }
    
    public static void damage(){
        health = health - 1;
    }

}
