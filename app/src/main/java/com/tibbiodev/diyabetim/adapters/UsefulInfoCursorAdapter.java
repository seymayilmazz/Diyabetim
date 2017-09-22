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
import com.tibbiodev.diyabetim.fragments.FaydaliBilgilerFragment;

/**
 * Created by User on 4.11.2016.
 */
public class UsefulInfoCursorAdapter extends CursorAdapter {

    public UsefulInfoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.useful_info_item,
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

        String title = cursor.getString(FaydaliBilgilerFragment.COL_INFO_TITLE);
        viewHolder.textViewUsefulInfoTitle.setText(title);

        String summary = cursor.getString(FaydaliBilgilerFragment.COL_INFO_SUMMARY);
        viewHolder.textViewUsefulInfoSummary.setText(summary);

    }

    public class ViewHolder{

        public TextView textViewUsefulInfoTitle;
        public TextView textViewUsefulInfoSummary;

        public ViewHolder(View itemView) {
            textViewUsefulInfoTitle = (TextView) itemView.findViewById(R.id.usefulInfoTitle);
            textViewUsefulInfoSummary = (TextView) itemView.findViewById(R.id.usefulInfoSummary);
        }

    }





}
