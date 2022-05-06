package com.example.logintest;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getIntstance() {
        if(instance==null){
            instance= new MediaPlayer();
        }
        return instance;
    }
    public static int currentIndex = -1;
}
