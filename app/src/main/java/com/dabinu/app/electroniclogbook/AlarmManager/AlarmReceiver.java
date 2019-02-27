package com.dabinu.app.electroniclogbook.AlarmManager;



import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import com.dabinu.app.electroniclogbook.R;
import java.io.Serializable;



public class AlarmReceiver extends BroadcastReceiver implements Serializable{

    @Override
    public void onReceive(Context context, Intent intent){


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.back)
                .setContentTitle("Don't forget to fill your logbook today")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{3000});


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }
}