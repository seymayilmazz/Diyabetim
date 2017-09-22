package com.tibbiodev.diyabetim.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.fragments.InsulinSaatlerimFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 18.12.2016.
 */
public class OlaylarCursorAdapter extends CursorAdapter {


    public OlaylarCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.event_item,
                parent,
                false
        );

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        Date date = null;
        Calendar calendar = Calendar.getInstance();
        String timeText = cursor.getString(InsulinSaatlerimFragment.COL_TIMETEXT);
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
        viewHolder.textViewReminderTime.setText(latestTime);

        String note = cursor.getString(InsulinSaatlerimFragment.COL_NOTE);
        viewHolder.textViewReminderNote.setText(note);

        int type = cursor.getInt(InsulinSaatlerimFragment.COL_TYPE);
        if(type == DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN){
            viewHolder.textViewReminderExtra.setText(R.string.insulin_saat);
        }
        else{
            viewHolder.textViewReminderExtra.setText(R.string.ilac_saat);
        }



    }

    public class ViewHolder{

        public TextView textViewReminderTime;
        public TextView textViewReminderNote;
        public TextView textViewReminderExtra;

        public ViewHolder(View itemView) {
            textViewReminderTime = (TextView) itemView.findViewById(R.id.reminderTime);
            textViewReminderNote = (TextView) itemView.findViewById(R.id.reminderNote);
            textViewReminderExtra = (TextView) itemView.findViewById(R.id.reminderExtra);
        }

    }





}
