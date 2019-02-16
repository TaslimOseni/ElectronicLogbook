package com.dabinu.app.electroniclogbook.splash;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(3000, 1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish(){
                //todo: Check login status
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            }
        }.start();

    }
}