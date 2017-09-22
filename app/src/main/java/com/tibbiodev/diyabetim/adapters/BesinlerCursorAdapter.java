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
import com.tibbiodev.diyabetim.fragments.BesinTablosuFragment;

/**
 * Created by User on 4.11.2016.
 */
public class BesinlerCursorAdapter extends CursorAdapter {

    public BesinlerCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.besin_tablosu_item,
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

        String besinIsmi = cursor.getString(BesinTablosuFragment.COL_BESIN_ISMI);
        viewHolder.textViewbesinIsmi.setText(besinIsmi);

        int glisemikIndeks = cursor.getInt(BesinTablosuFragment.COL_GLISEMIK_INDEKS);
        viewHolder.textViewbesinGlisemikIndeks.setText(
                Integer.toString(glisemikIndeks));

        int glisemikYuk = cursor.getInt(BesinTablosuFragment.COL_GLISEMIK_YUK);
        viewHolder.textViewbesinGlisemikYuk.setText(
                Integer.toString(glisemikYuk)
        );

        if(glisemikIndeks >= 0 && glisemikIndeks <= 55){
            viewHolder.imageViewbesinUyariIcon.setImageResource(R.drawable.shape_circle_yellow);
        }
        else if(glisemikIndeks >= 56 && glisemikIndeks <= 69){
            viewHolder.imageViewbesinUyariIcon.setImageResource(R.drawable.shape_circle_green);
        }/* >= 70 */
        else{
            viewHolder.imageViewbesinUyariIcon.setImageResource(R.drawable.shape_circle_red);
        }


    }

    public class ViewHolder{

        public TextView textViewbesinIsmi;
        public TextView textViewbesinGlisemikIndeks;
        public TextView textViewbesinGlisemikYuk;
        public ImageView imageViewbesinUyariIcon;


        public ViewHolder(View itemView) {
            textViewbesinIsmi = (TextView) itemView.findViewById(R.id.besinIsmi);
            textViewbesinGlisemikIndeks = (TextView) itemView.findViewById(R.id.besinGlisemikIndeks);
            textViewbesinGlisemikYuk = (TextView) itemView.findViewById(R.id.besinGlisemikYuk);
            imageViewbesinUyariIcon = (ImageView) itemView.findViewById(R.id.besinUyariIcon);
        }

    }





}
