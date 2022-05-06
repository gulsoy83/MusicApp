package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    TextView titleTV, currentTimeTV, totalTimeTV;
    SeekBar seekBar;
    ImageView pauseplay,next,prev,musicicon,mdel;
    ArrayList<AudioModel> musicList;
    AudioModel currentSong;
    MediaPlayer myplayer = MyMediaPlayer.getIntstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        mdel = findViewById(R.id.musdel);

        titleTV = findViewById(R.id.mus_title);
        titleTV.setSelected(true);
        currentTimeTV = findViewById(R.id.current_time);
        totalTimeTV = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pauseplay = findViewById(R.id.pause_play);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        musicicon = findViewById(R.id.musicIconBig);
        musicList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();


        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(myplayer!=null){
                    seekBar.setProgress(myplayer.getCurrentPosition());
                    currentTimeTV.setText(convertToMSS(myplayer.getCurrentPosition()+""));


                    if(myplayer.isPlaying()){
                        pauseplay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    }else
                        pauseplay.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                }
                new Handler().postDelayed(this,100);

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(myplayer!=null && b){
                    myplayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    void setResourcesWithMusic(){
        currentSong = musicList.get(MyMediaPlayer.currentIndex);
        titleTV.setText(currentSong.getTitle());
        totalTimeTV.setText(convertToMSS(currentSong.getDuration()));

        pauseplay.setOnClickListener(view -> pausePlay());
        next.setOnClickListener(view -> playNextMusic());
        prev.setOnClickListener(view -> playPrevMusic());
        mdel.setOnClickListener(view -> deleteMusic());

        playMusic();

    }

    private void deleteMusic() {
        myplayer.reset();
        File mf = new File (currentSong.getPath());
        ActivityCompat.requestPermissions(MusicPlayer.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        Uri uri = Uri.fromFile(mf);
        try{
            mf.delete();
            getContentResolver().delete(uri,null,null);

        }catch(Exception e){
            e.printStackTrace();
        }
        startActivity(new Intent(MusicPlayer.this,MainActivity.class));


    }


    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MusicPlayer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MusicPlayer.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MusicPlayer.this, "Write permission is required.", Toast.LENGTH_SHORT);
        }else
            ActivityCompat.requestPermissions(MusicPlayer.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},313);

    }

    private void playMusic(){
        myplayer.reset();
        try{
            myplayer.setDataSource(currentSong.getPath());
            myplayer.prepare();
            myplayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(myplayer.getDuration());
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private void playNextMusic(){
        if(MyMediaPlayer.currentIndex == musicList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        myplayer.reset();
        setResourcesWithMusic();

    }
    private void playPrevMusic(){
        if(MyMediaPlayer.currentIndex ==0 )
            return;
        MyMediaPlayer.currentIndex-=1;
        myplayer.reset();
        setResourcesWithMusic();

    }
    private void pausePlay(){
        if(myplayer.isPlaying())
            myplayer.pause();
        else
            myplayer.start();
    }

    public static String convertToMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    }



}