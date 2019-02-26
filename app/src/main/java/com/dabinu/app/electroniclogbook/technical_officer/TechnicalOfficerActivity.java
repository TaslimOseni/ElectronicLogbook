package com.dabinu.app.electroniclogbook.technical_officer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.student.StudentActivity;

public class TechnicalOfficerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_officer);
    }

    public interface IOnBackPressed{
        boolean onBackPressed();
    }


    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof TechnicalOfficerActivity.IOnBackPressed) || !((TechnicalOfficerActivity.IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}