package com.example.logintest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin,bClear;
    EditText etUsername, etPassword;
    TextView registerLink;
    UserLocalStore userLocalStore;
    int attempt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);
        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        bClear = (Button) findViewById(R.id.bClear);
        bClear.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);




    }
    public void onBackPressed(){

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bLogin:

                if(etUsername.getText().toString().compareTo("")==0 || etUsername.getText().toString().compareTo("") ==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("Error.");
                    builder.setMessage("Username/Password cannot be empty.");
                    builder.setNeutralButton("OK",null);
                    builder.show();
                }
                else{
                    String u = userLocalStore.userLocalDatabase.getString("username","");
                    String p = userLocalStore.userLocalDatabase.getString("passwd","");
                    if(u.matches(etUsername.getText().toString()) && p.matches(etPassword.getText().toString())){
                        userLocalStore.setUserLoggedIn(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Entry Succeeded");
                        builder.setMessage("Welcome");
                        builder.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Login.this,MainActivity.class));
                            }
                        });
                        builder.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Error.");
                        builder.setMessage("Wrong username or password.");
                        builder.setNeutralButton("OK",null);
                        //builder.show();
                        AlertDialog b1Dialog = builder.create();
                        b1Dialog.show();
                        attempt=attempt+1;
                        if(attempt>2){
                            b1Dialog.dismiss();
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
                            builder2.setTitle("Are you sure you have an account?");
                            builder2.setMessage("You've repeatedly entered wrong credentials. Would you like to register?");
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    attempt =0;
                                    startActivity(new Intent(Login.this,Register.class));
                                }
                            });
                            builder2.setNegativeButton("No", null);
                            builder2.show();

                        }
                    }

                }





                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this,Register.class));
                break;
            case R.id.bClear:
                userLocalStore.clearData();
                break;
        }
    }
}