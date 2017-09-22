package com.tibbiodev.diyabetim.activities;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.data.DiyabetimContract;

import java.util.Calendar;

public class HatirlaticiActivity extends AppCompatActivity {

    private int mReminderType;
    private LinearLayout mListItem;
    private TextView mTextViewReminderTime;
    private EditText mEditTextNote;

    private Calendar mCalendar;

    private ContentValues mContentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hatirlatici);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReminderType = getIntent().getIntExtra("reminderType",
                DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);

        if(mReminderType == DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN){
            getSupportActionBar().setTitle(R.string.insulin_saati_ekle);
        }
        else{
            getSupportActionBar().setTitle(R.string.ilac_saati_ekle);
        }
        mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        mTextViewReminderTime = (TextView) findViewById(R.id.reminderTime);
        mTextViewReminderTime.setText(
                String.format("%02d", hour) + ":" +
                        String.format("%02d", minute)
        );
        mEditTextNote = (EditText) findViewById(R.id.editNote);

        mContentValues = new ContentValues();
        mContentValues.put(DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT,
                String.format("%02d", hour) +
                        String.format("%02d", minute));
        mContentValues.put(DiyabetimContract.ReminderInfoEntry.COLUMN_REMINDER_ENABLE,
                DiyabetimContract.ReminderInfoEntry.REMINDER_ENABLE);
        mContentValues.put(DiyabetimContract.ReminderInfoEntry.COLUMN_TYPE,
                mReminderType);


        mListItem = (LinearLayout) findViewById(R.id.listItem);
        mListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        HatirlaticiActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                mCalendar.set(Calendar.MINUTE, minute);
                                mTextViewReminderTime.setText(
                                        String.format("%02d", hourOfDay) + ":" +
                                                String.format("%02d",minute)
                                );
                                mContentValues.put(
                                        DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT,
                                        String.format("%02d", hourOfDay) +
                                                String.format("%02d", minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_seker_olcum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if(mEditTextNote.getText().length() == 0){
                Toast.makeText(HatirlaticiActivity.this,
                        R.string.not_bos_olamaz,
                        Toast.LENGTH_SHORT).show();
            }
            else{
                mContentValues.put(
                        DiyabetimContract.ReminderInfoEntry.COLUMN_NOTE,
                        mEditTextNote.getText().toString()
                );
                getContentResolver().insert(
                        DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                        mContentValues
                );
                Toast.makeText(HatirlaticiActivity.this,
                        R.string.basariyla_kaydedildii,
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
