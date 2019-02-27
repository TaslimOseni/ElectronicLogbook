package com.dabinu.app.electroniclogbook.ind_supervisor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dabinu.app.electroniclogbook.AlarmManager.AlarmReceiver;
import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.dept_supervisor.DeptSupervisorActivity;
import com.dabinu.app.electroniclogbook.student.fragments.HomeFragment;

import java.util.Calendar;

public class IndSupervisorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ind_supervisor);

        Calendar theCalendar = Calendar.getInstance();

        theCalendar.set(Calendar.HOUR_OF_DAY, 8);
        theCalendar.set(Calendar.MINUTE, 0);
        theCalendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(IndSupervisorActivity.this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(IndSupervisorActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) IndSupervisorActivity.this.getSystemService(IndSupervisorActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, theCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.ind_supervisor.fragments.HomeFragment());
        fragmentTransaction.commit();
    }


    public interface IOnBackPressed{
        boolean onBackPressed();
    }


    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IndSupervisorActivity.IOnBackPressed) || !((IndSupervisorActivity.IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}