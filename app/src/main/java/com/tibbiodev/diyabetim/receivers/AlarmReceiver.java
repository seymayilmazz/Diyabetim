package com.tibbiodev.diyabetim.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tibbiodev.diyabetim.services.AlarmService;

/**
 * Created by User on 19.12.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final long FIVE_SECOND = 5 * 1000;
    private static final long HALF_MINUTE =  20 * 1000;

    /**
     * yayini yakaladiginda alarmi kur.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarm(context);
    }

    public void setAlarm(Context context){
        /* alarm yoneticisi al */
        AlarmManager manager = (AlarmManager) context.
               getSystemService(Context.ALARM_SERVICE);

        /* intent'i olustur */
        Intent serviceIntent = new Intent(context, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.
                getService(context, 0, serviceIntent, 0);

        /* 5er dk arayla servis cagirilacak */
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                FIVE_SECOND, HALF_MINUTE, pendingIntent);
    }
}
