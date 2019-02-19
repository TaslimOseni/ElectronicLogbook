package com.dabinu.app.electroniclogbook.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.student.StudentActivity;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(2000, 1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish(){
                SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);

                if(sharedPreferences.getString("login", "false").equals("true")){
                    startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                }

            }
        }.start();

    }
}