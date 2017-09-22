package com.tibbiodev.diyabetim.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.activities.AlarmActivity;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.fragments.InsulinSaatlerimFragment;

import java.util.Calendar;

/**
 * Created by User on 19.12.2016.
 */
public class AlarmService extends IntentService {

    /* bildirim yoneticisi(uygulama bildirimi */
    private NotificationManager mNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Calendar calendar = Calendar.getInstance();
        String sortOrder = DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT + " ASC";

        Cursor cursor = getContentResolver().query(
                DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                InsulinSaatlerimFragment.REMINDER_COLUMNS,
                DiyabetimContract.ReminderInfoEntry.COLUMN_REMINDER_ENABLE + " = '" +
                        DiyabetimContract.ReminderInfoEntry.REMINDER_ENABLE + "'",
                null,
                sortOrder
        );

        String currentTimeText = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) + "" +
                String.format("%02d",calendar.get(Calendar.MINUTE));

        while (cursor.moveToNext()){
            String timeText = cursor.getString(InsulinSaatlerimFragment.COL_TIMETEXT);
            Log.v("Service", "alarm current : " + currentTimeText + ", " +
                    "kontrol time : " + timeText);
            if(currentTimeText.compareTo(timeText) == 0){
                Log.v("Service", "alarm calistir: " + timeText);
                Intent intentAlarm = new Intent(this, AlarmActivity.class);
                intentAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentAlarm.putExtra("ReminderId",
                        cursor.getInt(InsulinSaatlerimFragment.COL_REMINDER_INFO_ID));
                startActivity(intentAlarm);
            }
        }


    }
}
