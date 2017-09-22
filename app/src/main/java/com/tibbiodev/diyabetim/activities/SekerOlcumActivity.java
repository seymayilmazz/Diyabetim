package com.tibbiodev.diyabetim.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.adapters.BesinlerCursorAdapter;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.dialog.BloodSugarDialogFragment;
import com.tibbiodev.diyabetim.data.DiyabetimContract.BloodSugarEntry;
import com.tibbiodev.diyabetim.dialog.BloodSugarInputDialogFragment;

import java.util.Calendar;

public class SekerOlcumActivity extends AppCompatActivity {

    private ViewHolder mViewHolderOlcumDegeri;
    private ViewHolder mViewHolderOlcumTip;
    private ViewHolder mViewHolderOlcumTarih;
    private ViewHolder mViewHolderOlcumSaat;
    private ViewHolder mViewHolderOlcumYemekSaat;

    private ContentValues mContentValues;
    private Calendar calendar;
    private Calendar calendarOlcum;

    private TextView mOlcumAnalizDeger;
    private TextView mOlcumAnalizYorum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seker_olcum);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOlcumAnalizDeger = (TextView) findViewById(R.id.olcum_analiz_deger);
        mOlcumAnalizYorum = (TextView) findViewById(R.id.olcum_analiz_yorum);

        mViewHolderOlcumDegeri = new ViewHolder(findViewById(R.id.olcum_degeri));
        mViewHolderOlcumTip = new ViewHolder(findViewById(R.id.olcum_tipi));
        mViewHolderOlcumTarih = new ViewHolder(findViewById(R.id.olcum_tarih));
        mViewHolderOlcumSaat = new ViewHolder(findViewById(R.id.olcum_saat));
        mViewHolderOlcumYemekSaat = new ViewHolder(findViewById(R.id.olcum_yemek_saati));

        calendar = Calendar.getInstance();
        calendarOlcum = (Calendar) calendar.clone();

        mContentValues = new ContentValues();
        mContentValues.put(BloodSugarEntry.COLUMN_DATETEXT,
                calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.MONTH) + 1) +
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        mContentValues.put(BloodSugarEntry.COLUMN_TIMETEXT,
                String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) +
                        String.format("%02d", calendar.get(Calendar.MINUTE))
                );
        mContentValues.put(BloodSugarEntry.COLUMN_MEASUREMENT_TYPE,
                BloodSugarEntry.MEASUREMENT_TYPE_1
                );
        mContentValues.put(BloodSugarEntry.COLUMN_MEAL_TIME, "-");
        prepareViewData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_seker_olcum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_done){
            Integer olcumDeger = mContentValues.getAsInteger(BloodSugarEntry.COLUMN_MEASUREMENT);
            int type =  mContentValues.getAsInteger(BloodSugarEntry.COLUMN_MEASUREMENT_TYPE);

            if(olcumDeger == null){
                Toast.makeText(this,
                        "Ölçüm değeri girilmemiş", Toast.LENGTH_SHORT).show();
            }
            else if(type == BloodSugarEntry.MEASUREMENT_TYPE_2){
                String yemekSaati = mContentValues.getAsString(BloodSugarEntry.COLUMN_MEAL_TIME);
                if(yemekSaati.compareTo("-") == 0){
                    Toast.makeText(this,
                            "Yemek saatinin girilmesi gerekli", Toast.LENGTH_SHORT).show();
                }
                else{
                    int hour = (calendarOlcum.get(Calendar.HOUR_OF_DAY) -
                            calendar.get(Calendar.HOUR_OF_DAY));
                    if(hour < 2){
                        Toast.makeText(this,
                                "Tokluk kan şekerinin yemekten sonra 2 saat sonra ölçülmesi " +
                                        "gerekmektedir.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        getContentResolver().insert(
                                BloodSugarEntry.CONTENT_URI,
                                mContentValues
                        );
                        Toast.makeText(this,
                                "Ölçüm değeri başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                    }

                }

            }
            else{
                getContentResolver().insert(
                        BloodSugarEntry.CONTENT_URI,
                        mContentValues
                );
                Toast.makeText(this,
                        "Ölçüm değeri başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareViewData(){
        mViewHolderOlcumDegeri.setIcon(R.drawable.ic_opacity_black_48dp);
        mViewHolderOlcumDegeri.setTitle(R.string.seker_olcum_deger_title);
        mViewHolderOlcumDegeri.setSubTitle(R.string.seker_olcum_deger_subtitle);
        mViewHolderOlcumDegeri.setDescription(R.string.giris_yapilmamis);
        mViewHolderOlcumDegeri.textViewDescription.setTextColor(
                getResources().getColor(R.color.colorRequiredField)
        );
        mViewHolderOlcumDegeri.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BloodSugarInputDialogFragment bloodSugarInputDialogFragment =
                        new BloodSugarInputDialogFragment();
                bloodSugarInputDialogFragment.setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int olcumDegeri =
                                Integer.parseInt(bloodSugarInputDialogFragment.editTextOlcumDegeri.
                                        getText().toString());
                        mContentValues.put(BloodSugarEntry.COLUMN_MEASUREMENT, olcumDegeri);
                        mViewHolderOlcumDegeri.textViewDescription.setText(
                                bloodSugarInputDialogFragment.editTextOlcumDegeri.
                                        getText() + " mg/dL"
                        );
                        mViewHolderOlcumDegeri.textViewDescription.setTextColor(
                                getResources().getColor(R.color.colorText)
                        );
                        analizGerceklestir();
                        dialog.dismiss();
                    }
                });
                bloodSugarInputDialogFragment.show(
                        getFragmentManager(),
                        "BloodSugarInputDialog"
                );


            }
        });


        mViewHolderOlcumTip.setIcon(R.drawable.ic_panorama_fish_eye_black_48dp);
        mViewHolderOlcumTip.setTitle(R.string.seker_olcum_tip_title);
        mViewHolderOlcumTip.setSubTitle(R.string.seker_olcum_tip_subtitle);
        mViewHolderOlcumTip.setDescription(R.string.seker_olcum_tip_1);
        mViewHolderOlcumTip.textViewDescription.setTextColor(
                getResources().getColor(R.color.colorText)
        );
        mViewHolderOlcumTip.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloodSugarDialogFragment bloodSugarDialogFragment =
                        new BloodSugarDialogFragment();
                bloodSugarDialogFragment.setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == BloodSugarEntry.MEASUREMENT_TYPE_1) {
                            mContentValues.put(
                                    BloodSugarEntry.COLUMN_MEASUREMENT_TYPE,
                                    BloodSugarEntry.MEASUREMENT_TYPE_1
                            );
                            mViewHolderOlcumTip.setDescription(
                                    R.string.seker_olcum_tip_1
                            );
                            mViewHolderOlcumYemekSaat.setDescription(R.string.gerekli_degil);
                            mViewHolderOlcumYemekSaat.textViewDescription.setTextColor(
                                    getResources().getColor(R.color.colorNotRequiredField)
                            );
                            mContentValues.put(BloodSugarEntry.COLUMN_MEAL_TIME, "-");
                        }
                        else{
                            mContentValues.put(
                                    BloodSugarEntry.COLUMN_MEASUREMENT_TYPE,
                                    BloodSugarEntry.MEASUREMENT_TYPE_2
                            );
                            mViewHolderOlcumTip.setDescription(
                                    R.string.seker_olcum_tip_2
                            );
                            mViewHolderOlcumYemekSaat.setDescription(R.string.gerekli);
                            mViewHolderOlcumYemekSaat.textViewDescription.setTextColor(
                                    getResources().getColor(R.color.colorRequiredField)
                            );
                        }
                        analizGerceklestir();
                        dialog.dismiss();
                    }
                });
                bloodSugarDialogFragment.show(
                        getFragmentManager(),
                        "BloodSugarDialog"
                );


            }
        });

        mViewHolderOlcumTarih.setIcon(R.drawable.ic_date_range_black_48dp);
        mViewHolderOlcumTarih.setTitle(R.string.seker_olcum_tarih_title);
        mViewHolderOlcumTarih.setSubTitle(R.string.otomatik_ayarlanir);
        mViewHolderOlcumTarih.textViewSubTitle.setTextColor(
                getResources().getColor(R.color.colorAutoFillField)
        );


        String latestDate =
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" +
                        String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "/" +
                        calendar.get(Calendar.YEAR);
        mViewHolderOlcumTarih.textViewDescription.setText(latestDate);

        mViewHolderOlcumSaat.setIcon(R.drawable.ic_schedule_black_48dp);
        mViewHolderOlcumSaat.setTitle(R.string.seker_olcum_saat_title);
        mViewHolderOlcumSaat.setSubTitle(R.string.otomatik_ayarlanir);
        mViewHolderOlcumSaat.textViewSubTitle.setTextColor(
                getResources().getColor(R.color.colorAutoFillField)
        );
        String latestTime =
                String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format("%02d", calendar.get(Calendar.MINUTE));
        mViewHolderOlcumSaat.textViewDescription.setText(latestTime);

        mViewHolderOlcumYemekSaat.setIcon(R.drawable.ic_restaurant_black_48dp);
        mViewHolderOlcumYemekSaat.setTitle(R.string.seker_olcum_yemek_saati_title);
        mViewHolderOlcumYemekSaat.setSubTitle(R.string.seker_olcum_yemek_saati_subtitle);
        mViewHolderOlcumYemekSaat.setDescription(R.string.gerekli_degil);
        mViewHolderOlcumYemekSaat.textViewDescription.setTextColor(
                getResources().getColor(R.color.colorNotRequiredField)
        );
        mViewHolderOlcumYemekSaat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int type =  mContentValues.getAsInteger(BloodSugarEntry.COLUMN_MEASUREMENT_TYPE);
                if(type == BloodSugarEntry.MEASUREMENT_TYPE_2){

                    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            SekerOlcumActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    mContentValues.put(
                                            BloodSugarEntry.COLUMN_MEAL_TIME,
                                            String.format("%02d",hour) +
                                                    String.format("%02d",minute)
                                    );
                                    mViewHolderOlcumYemekSaat.textViewDescription
                                            .setText(
                                                    String.format("%02d",hourOfDay) + ":" +
                                                            String.format("%02d",minute));
                                    mViewHolderOlcumYemekSaat.textViewDescription.setTextColor(
                                            getResources().getColor(R.color.colorAutoFillField)
                                    );
                                    analizGerceklestir();
                                }
                            }, hour, minute, true);
                    timePickerDialog.show();

                }

            }
        });



    }

    private void analizGerceklestir(){
        Integer deger = mContentValues.getAsInteger(BloodSugarEntry.COLUMN_MEASUREMENT);
        Integer tip = mContentValues.getAsInteger(BloodSugarEntry.COLUMN_MEASUREMENT_TYPE);
        String yemekSaat = mContentValues.getAsString(BloodSugarEntry.COLUMN_MEAL_TIME);

        if(deger != null){
            mOlcumAnalizDeger.setText(deger + " mg/dL");
            if(tip == BloodSugarEntry.MEASUREMENT_TYPE_1){
                if(deger < 60){
                    mOlcumAnalizYorum.setText(R.string.seker_olcum_dusuk);
                }
                else if(deger <= 100){
                    mOlcumAnalizYorum.setText(R.string.seker_olcum_normal);
                }
                else{
                    mOlcumAnalizYorum.setText(R.string.seker_olcum_yuksek);
                }
            }
            else{

                if(yemekSaat.compareTo("-") == 0){
                    mOlcumAnalizYorum.setText(R.string.seker_olcum_analiz_yapilamadi);
                }
                else{

                    int hour = (calendarOlcum.get(Calendar.HOUR_OF_DAY) -
                            calendar.get(Calendar.HOUR_OF_DAY));
                    if(hour < 2){
                        mOlcumAnalizYorum.setText(R.string.seker_olcum_analiz_yapilamadi_tok);
                    }
                    else if(deger < 60){
                        mOlcumAnalizYorum.setText(R.string.seker_olcum_dusuk);
                    }
                    else if(deger <= 140){
                        mOlcumAnalizYorum.setText(R.string.seker_olcum_normal);
                    }
                    else{
                        mOlcumAnalizYorum.setText(R.string.seker_olcum_yuksek);
                    }
                }

            }
        }
    }


    public class ViewHolder{
        public View view;
        public TextView textViewTitle;
        public TextView textViewSubTitle;
        public ImageView imageViewIcon;
        public TextView textViewDescription;


        public ViewHolder(View itemView){
            view = itemView;
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewSubTitle = (TextView) itemView.findViewById(R.id.subtitle);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.icon);
            textViewDescription = (TextView) itemView.findViewById(R.id.description);
        }

        public void setTitle(int resId){
            textViewTitle.setText(resId);
        }

        public void setSubTitle(int resId){
            textViewSubTitle.setText(resId);
        }

        public void setDescription(int resId){
            textViewDescription.setText(resId);
        }

        public void setIcon(int resId){
            imageViewIcon.setImageResource(resId);
        }


    }

}
