package com.tibbiodev.diyabetim.fragments;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tibbiodev.diyabetim.R;

/**
 * Created by User on 2.11.2016.
 */
public class BekleyenOlaylarFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bekleyen_olaylar, container, false);


        return view;
    }
}
