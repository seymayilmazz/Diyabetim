package com.tibbiodev.diyabetim.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.activities.WebViewActivity;
import com.tibbiodev.diyabetim.adapters.BesinlerCursorAdapter;
import com.tibbiodev.diyabetim.adapters.UsefulInfoCursorAdapter;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.models.Food;
import com.tibbiodev.diyabetim.models.UsefulInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15.12.2016.
 */
public class FaydaliBilgilerFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = FaydaliBilgilerFragment.class.getSimpleName();

    private static final int USEFULINFO_LOADER = 0;

    private ListView mListViewBilgiListesi;
    private int mPosition = ListView.INVALID_POSITION;
    private UsefulInfoCursorAdapter mUsefulInfoCursorAdapter;

    private List<UsefulInfo> bilgiListesi = new ArrayList<>();

    private static final String[] USEFULINFO_COLUMNS = {
            DiyabetimContract.UsefulInfoEntry._ID,
            DiyabetimContract.UsefulInfoEntry.COLUMN_INFO_TITLE,
            DiyabetimContract.UsefulInfoEntry.COLUMN_INFO_SUMMARY,
            DiyabetimContract.UsefulInfoEntry.COLUMN_INFO_LINK
    };

    public static final int COL_INFO_ID = 0;
    public static final int COL_INFO_TITLE = 1;
    public static final int COL_INFO_SUMMARY = 2;
    public static final int COL_INFO_LINK = 3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faydali_bilgiler,
                container,
                false);


        mListViewBilgiListesi = (ListView)  view.findViewById(
                R.id.list_view_faydali_bilgi_tablo
        );

        mUsefulInfoCursorAdapter = new UsefulInfoCursorAdapter(
                getActivity(), null, 0
        );
        mListViewBilgiListesi.setAdapter(mUsefulInfoCursorAdapter);

        mListViewBilgiListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(cursor != null){
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("link", cursor.getString(FaydaliBilgilerFragment.COL_INFO_LINK));
                    intent.putExtra("title", cursor.getString(FaydaliBilgilerFragment.COL_INFO_TITLE));
                    startActivity(intent);
                }


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(USEFULINFO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader");

        return new CursorLoader(
                getActivity(),
                DiyabetimContract.UsefulInfoEntry.CONTENT_URI,
                USEFULINFO_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mUsefulInfoCursorAdapter.swapCursor(data);
        if(mPosition != ListView.INVALID_POSITION){
            mListViewBilgiListesi.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUsefulInfoCursorAdapter.swapCursor(null);
    }
}
