package com.dabinu.app.electroniclogbook.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.fragments.WelcomeFragment;
import com.dabinu.app.electroniclogbook.utils.Constants;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("login", Constants.NOT_LOGGED_IN);
        editor.putString("weeks", Constants.DEFAULT_FILL);
        editor.putString("filled_placement", Constants.DEFAULT_FILL);
        editor.putString("statuigba", "no_gba");
        editor.apply();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new WelcomeFragment());
        fragmentTransaction.commit();
    }


    public interface IOnBackPressed{
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}