package com.example.xavin.flowmusic;

import android.media.MediaPlayer;

public class PlayControl {
    static MediaPlayer mp=null;
    public static MediaPlayer getMediaPlayer(){
        if(mp==null){
           mp= new MediaPlayer();
        }
        return mp;
    }
}
