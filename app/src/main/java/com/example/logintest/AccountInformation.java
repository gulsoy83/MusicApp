package com.example.logintest;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.app.Activity;
        import android.graphics.Color;
        import android.view.View;

        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;


public class AccountInformation extends AppCompatActivity implements View.OnClickListener {
    Button bLogout,bBack;
    EditText etAd, etSoyad, etMail,etUsername;
    UserLocalStore userLocalStore;
    MediaPlayer myplayer = MyMediaPlayer.getIntstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        etAd = (EditText) findViewById(R.id.etAd);
        etSoyad = (EditText) findViewById(R.id.etSoyad);
        etMail = (EditText) findViewById(R.id.etMail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        bLogout = (Button) findViewById(R.id.bLogout);
        bBack = (Button) findViewById(R.id.bBack);
        bLogout.setOnClickListener(this);
        bBack.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

    }

    public void onBackPressed(){
        startActivity(new Intent(this,MainActivity.class));
    }



    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.username);
        etAd.setText(user.ad);
        etSoyad.setText(user.soyad);
        etMail.setText(user.mail);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayUserDetails();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bLogout:
                userLocalStore.setUserLoggedIn(false);
                myplayer.reset();
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bBack:
                startActivity(new Intent(this, MainActivity.class));
                //startActivity(new Intent(this, Login.class));
                break;


        }
    }
}