package com.dabinu.app.electroniclogbook.student;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.student.fragments.HomeFragment;


public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        fragmentTransaction.commit();

    }

    public interface IOnBackPressed{
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof StudentActivity.IOnBackPressed) || !((StudentActivity.IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}