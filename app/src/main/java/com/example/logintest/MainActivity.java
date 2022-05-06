package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    UserLocalStore userLocalStore;
    RecyclerView recyclerView;
    TextView noMusicText;
    Button bA;
    ArrayList<AudioModel> musicList = new ArrayList<>();

    MediaPlayer mp = MyMediaPlayer.getIntstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =  findViewById(R.id.rec);
        noMusicText = findViewById(R.id.etNotfound);

        userLocalStore = new UserLocalStore(this);
        bA = (Button) findViewById(R.id.bAinfo);
        bA.setOnClickListener(this);


        if(checkPermission() == false){
            requestPermission();
            return;
        }
        String [] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,MediaStore.MediaColumns.DISPLAY_NAME + "");

        while(cursor.moveToNext()){
            AudioModel musicData = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if (new File(musicData.getPath()).exists())
                musicList.add(musicData);

        }

        if(musicList.size()==0){
            noMusicText.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(musicList,getApplicationContext()));
        }


    }

    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "Read Permission is required.", Toast.LENGTH_SHORT);
        }else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},313);

    }


    public void onBackPressed(){
        mp.reset();
        //Toast.makeText(MainActivity.this, "Music is stopped.", Toast.LENGTH_LONG);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(userLocalStore.getUserLoggedInState() != true)
            startActivity(new Intent(this,Login.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bAinfo:
                startActivity(new Intent(this,AccountInformation.class));
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new MusicListAdapter(musicList,getApplicationContext()));
        }
    }
}
