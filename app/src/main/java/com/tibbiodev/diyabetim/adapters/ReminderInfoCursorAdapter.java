package com.tibbiodev.diyabetim.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.fragments.FaydaliBilgilerFragment;
import com.tibbiodev.diyabetim.fragments.IlacSaatlerimFragment;
import com.tibbiodev.diyabetim.fragments.InsulinSaatlerimFragment;
import com.tibbiodev.diyabetim.fragments.SekerOlcumlerimFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 4.11.2016.
 */
public class ReminderInfoCursorAdapter extends CursorAdapter {

    public interface OnCheckedListener{
        void onCheckedItem(View view, boolean checkedState);
    }

    public OnCheckedListener mOnCheckedListener;

    public ReminderInfoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setOnCheckedListener(OnCheckedListener l){
        mOnCheckedListener = l;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.hatirlatici_item,
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

        int enable = cursor.getInt(InsulinSaatlerimFragment.COL_REMINDER_ENABLE);
        if(enable == DiyabetimContract.ReminderInfoEntry.REMINDER_ENABLE){
            viewHolder.switchReminderEnable.setChecked(true);
        }
        else{
            viewHolder.switchReminderEnable.setChecked(false);
        }


        viewHolder.switchReminderEnable.setTag(
                cursor.getInt(InsulinSaatlerimFragment.COL_REMINDER_INFO_ID));
        viewHolder.switchReminderEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCheckedListener != null){
                    mOnCheckedListener.onCheckedItem(
                            viewHolder.switchReminderEnable,
                            viewHolder.switchReminderEnable.isChecked()
                    );
                }
            }
        });


    }

    public class ViewHolder{

        public TextView textViewReminderTime;
        public TextView textViewReminderNote;
        public SwitchCompat switchReminderEnable;

        public ViewHolder(View itemView) {
            textViewReminderTime = (TextView) itemView.findViewById(R.id.reminderTime);
            textViewReminderNote = (TextView) itemView.findViewById(R.id.reminderNote);
            switchReminderEnable = (SwitchCompat) itemView.findViewById(R.id.reminderEnable);
        }

    }





}
