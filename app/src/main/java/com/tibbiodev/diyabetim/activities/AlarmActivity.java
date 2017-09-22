package com.tibbiodev.diyabetim.activities;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.fragments.InsulinSaatlerimFragment;
import com.tibbiodev.diyabetim.fragments.SekerOlcumlerimFragment;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private ImageView imageViewNo;
    private TextView textViewAlarmTip;
    private TextView textViewAlarmSaat;
    private TextView textViewAlarmNot;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        imageViewNo = (ImageView) findViewById(R.id.no);
        textViewAlarmTip = (TextView) findViewById(R.id.alarmTip);
        textViewAlarmSaat = (TextView) findViewById(R.id.alarmSaat);
        textViewAlarmNot = (TextView) findViewById(R.id.alarmNot);

        Integer id = getIntent().getIntExtra("ReminderId", 0);

        Cursor cursor = getContentResolver().query(
                DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                InsulinSaatlerimFragment.REMINDER_COLUMNS,
                DiyabetimContract.ReminderInfoEntry._ID + " = '" +
                        id + "'",
                null,
                null
        );

        cursor.moveToFirst();
        if(cursor.getInt(InsulinSaatlerimFragment.COL_TYPE) ==
                DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN){
            textViewAlarmTip.setText(R.string.insulin_saat);
        }
        else{
            textViewAlarmTip.setText(R.string.ilac_saat);
        }

        Date date = null;
        String timeText = cursor.getString(InsulinSaatlerimFragment.COL_TIMETEXT);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfForTime = new SimpleDateFormat("HHmm");
        try {
            date = sdfForTime.parse(timeText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);
        String latestTime =
                String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format("%02d", calendar.get(Calendar.MINUTE));
        textViewAlarmSaat.setText(latestTime);

        String note = cursor.getString(InsulinSaatlerimFragment.COL_NOTE);
        textViewAlarmNot.setText(note);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(AlarmActivity.this, alarmUri);
        ringtone.play();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);


        imageViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                vibrator.cancel();
                finish();
            }
        });

    }
}
