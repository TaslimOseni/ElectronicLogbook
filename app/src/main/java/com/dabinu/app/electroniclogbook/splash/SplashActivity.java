package com.dabinu.app.electroniclogbook.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.dept_supervisor.DeptSupervisorActivity;
import com.dabinu.app.electroniclogbook.ind_supervisor.IndSupervisorActivity;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;

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
                SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);

                if(sharedPreferences.getString("login", Constants.NOT_LOGGED_IN).equals(Constants.LOGGED_IN)){

                    switch(sharedPreferences.getString("type", Constants.STUDENT)){

                        case Constants.STUDENT:
                            startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                            break;

                        case Constants.DEPARTMENTAL_SUPERVISOR:
                            startActivity(new Intent(getApplicationContext(), DeptSupervisorActivity.class));
                            break;

                        case Constants.INDUSTRIAL_SUPERVISOR:
                            startActivity(new Intent(getApplicationContext(), IndSupervisorActivity.class));
                            break;
                    }

                }


                else{
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                }

            }
        }.start();

    }
}