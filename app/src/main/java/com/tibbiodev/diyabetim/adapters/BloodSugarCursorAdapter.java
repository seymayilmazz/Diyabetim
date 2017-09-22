package com.tibbiodev.diyabetim.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.fragments.FaydaliBilgilerFragment;
import com.tibbiodev.diyabetim.fragments.SekerOlcumlerimFragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by User on 4.11.2016.
 */
public class BloodSugarCursorAdapter extends CursorAdapter {

    public BloodSugarCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.seker_olcumu_item,
                parent,
                false
        );

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int measurement = cursor.getInt(SekerOlcumlerimFragment.COL_MEASUREMENT);
        viewHolder.textViewSekerOlcumDeger.setText(measurement + " mg/dL");

        String dateText = cursor.getString(SekerOlcumlerimFragment.COL_DATETEXT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String latestDate =
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "/" +
                        calendar.get(Calendar.YEAR);
        viewHolder.textViewSekerOlcumTarih.setText(latestDate);


        String timeText = cursor.getString(SekerOlcumlerimFragment.COL_TIMETEXT);
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

        viewHolder.textViewSekerOlcumSaat.setText(latestTime);

        int measurementType = cursor.getInt(SekerOlcumlerimFragment.COL_MEASUREMENT_TYPE);
        if(measurementType == DiyabetimContract.BloodSugarEntry.MEASUREMENT_TYPE_1){
            viewHolder.textViewSekerOlcumTip.setText(
                    R.string.seker_olcum_tip_1
            );
            if(measurement < 60){
                viewHolder.imageViewSekerUyariIcon.setImageResource(R.drawable.shape_circle_yellow);
            }
            else if(measurement <= 100){
                viewHolder.imageViewSekerUyariIcon.setImageResource(R.drawable.shape_circle_green);
            }
            else{
                viewHolder.imageViewSekerUyariIcon.setImageResource(R.drawable.shape_circle_red);
            }
        }
        else{
            viewHolder.textViewSekerOlcumTip.setText(
                    R.string.seker_olcum_tip_2
            );
            if(measurement < 60){
                viewHolder.imageViewSekerUyariIcon.setImageResource(R.drawable.shape_circle_yellow);
            }
            else if(measurement <= 140){
                viewHolder.imageViewSekerUyariIcon.setImageResource(R.drawable.shape_circle_green);
            }
            else{
                viewHolder.imageViewSekerUyariIcon.setImageResource(R.drawable.shape_circle_red);
            }
        }

        String mealTime = cursor.getString(SekerOlcumlerimFragment.COL_MEAL_TIME);
        try {
            date = sdfForTime.parse(mealTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);
        String latestMealTime = "-";
        if(cursor.getInt(SekerOlcumlerimFragment.COL_MEASUREMENT_TYPE)
                == DiyabetimContract.BloodSugarEntry.MEASUREMENT_TYPE_2){
            latestMealTime = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", calendar.get(Calendar.MINUTE));
        }

        viewHolder.textViewSekerOlcumYemekSaat.setText(latestMealTime);



    }

    public class ViewHolder{

        public TextView textViewSekerOlcumDeger;
        public TextView textViewSekerOlcumTarih;
        public TextView textViewSekerOlcumSaat;
        public TextView textViewSekerOlcumTip;
        public TextView textViewSekerOlcumYemekSaat;
        public ImageView imageViewSekerUyariIcon;

        public ViewHolder(View itemView) {
            textViewSekerOlcumDeger = (TextView) itemView.findViewById(R.id.sekerOlcumDeger);
            textViewSekerOlcumTarih = (TextView) itemView.findViewById(R.id.sekerOlcumTarih);
            textViewSekerOlcumSaat = (TextView) itemView.findViewById(R.id.sekerOlcumSaat);
            textViewSekerOlcumTip = (TextView) itemView.findViewById(R.id.sekerOlcumTip);
            textViewSekerOlcumYemekSaat = (TextView) itemView.findViewById(R.id.sekerOlcumYemekSaat);
            imageViewSekerUyariIcon = (ImageView) itemView.findViewById(R.id.sekerUyariIcon);
        }


    }





}
