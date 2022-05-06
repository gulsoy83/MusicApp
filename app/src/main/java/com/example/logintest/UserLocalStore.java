package com.example.logintest;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileOutputStream;

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME  ,Context.MODE_PRIVATE);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name" ,user.ad);
        spEditor.putString("surname" ,user.soyad);
        spEditor.putString("mail" ,user.mail);
        spEditor.putString("username" ,user.username);
        spEditor.putString("passwd" ,user.password);
        spEditor.commit();


    }

    public User getLoggedInUser(){

        String name = userLocalDatabase.getString("name","");
        String surname = userLocalDatabase.getString("surname","");
        String mail = userLocalDatabase.getString("mail","");
        String username = userLocalDatabase.getString("username","");
        String password = userLocalDatabase.getString("passwd","");
        User storedusr = new User(name,surname,mail,username,password);
        return storedusr;
    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }
    public boolean getUserLoggedInState(){
        if(userLocalDatabase.getBoolean("loggedIn",false) == true){
            return true;
        }else
            return false;
    }
    public void clearData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
