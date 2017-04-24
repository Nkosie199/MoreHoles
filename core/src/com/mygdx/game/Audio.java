/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

/**
 *
 * @author Nkosingiphile
 */
public class Audio {
    Sound wavSound = Gdx.audio.newSound(Gdx.files.internal("data/wav.wav"));
    Sound oggSound = Gdx.audio.newSound(Gdx.files.internal("data/ogg.ogg"));
    Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("data/mp3.mp3"));
    
//    wavSound.play();
//    oggSound.play(0.5f);
//    
//    long id = mp3Sound.loop();
//    Timer.schedule(new Task(){
//       @Override
//       public void run(){
//          mp3Sound.stop(id);
//          }
//       }, 5.0f);
//    
//    wavSound.dispose();
//    oggSound.dispose();
//    mp3Sound.dispose();

}
