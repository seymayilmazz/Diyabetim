package com.tibbiodev.diyabetim.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tibbiodev.diyabetim.R;

/**
 * Created by User on 17.12.2016.
 */
public class BloodSugarDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener onClickListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.seker_olcum_tipi_sec)
                .setItems(R.array.blood_sugar_dialog, onClickListener);
        return builder.create();
    }

    public void setOnClickListener(DialogInterface.OnClickListener listener){
        onClickListener = listener;
    }
}
