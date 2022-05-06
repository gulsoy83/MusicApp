package com.example.logintest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener {
    Button bRegister;
    EditText etAd, etSoyad, etMail, etUsername, etPassword, etPassword2;
    int sayi =0;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etAd = (EditText) findViewById(R.id.etAd);
        etSoyad = (EditText) findViewById(R.id.etSoyad);
        etMail = (EditText) findViewById(R.id.etMail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bRegister:

                String tname = etAd.getText().toString();
                String tsurname = etSoyad.getText().toString();

                String tmail = etMail.getText().toString();
                String tusername = etUsername.getText().toString();
                String tpassword = etPassword.getText().toString();
                String tpassword2 = etPassword2.getText().toString();
                if (tusername.compareTo("") == 0 || tpassword.compareTo("") == 0 || tpassword2.compareTo("") == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("Error.");
                    builder.setMessage("Username/password cannot be empty");
                    builder.setNeutralButton("OK", null);
                    builder.show();
                } else if (tpassword.compareTo(tpassword2) != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("Error.");
                    builder.setMessage("Passwords must match.");
                    builder.setNeutralButton("OK", null);
                    builder.show();
                } else {
                    User newuser = new User(tname, tsurname, tmail, tusername, tpassword);
                    userLocalStore.storeUserData(newuser);
                    startActivity(new Intent(this, Login.class));

                    /*Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, tmail);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);

                    }*/

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , tmail);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Your Account Info");
                    i.putExtra(Intent.EXTRA_TEXT   , "Name: "+tname + "\nSurname: " + tsurname +  "\n");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Register.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }


                    break;
                }
        }
    }
}