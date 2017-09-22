package com.tibbiodev.diyabetim.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.tibbiodev.diyabetim.R;

/**
 * Created by User on 17.12.2016.
 */
public class BloodSugarInputDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener onClickListener;

    public EditText editTextOlcumDegeri;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View olcumDegerInputView = getActivity().getLayoutInflater().
                inflate(R.layout.olcum_deger_input, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(olcumDegerInputView);
        editTextOlcumDegeri = (EditText) olcumDegerInputView
                .findViewById(R.id.editTextOlcumDeger);

        builder
                .setCancelable(false)
                .setPositiveButton("Tamam", onClickListener)
                .setNegativeButton("İptal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        return builder.create();
    }

    public void setOnClickListener(DialogInterface.OnClickListener listener){
        onClickListener = listener;
    }
}
