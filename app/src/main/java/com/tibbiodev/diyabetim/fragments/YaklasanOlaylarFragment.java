package com.tibbiodev.diyabetim.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.activities.HatirlaticiActivity;
import com.tibbiodev.diyabetim.activities.HatirlaticiDuzenleActivity;
import com.tibbiodev.diyabetim.adapters.OlaylarCursorAdapter;
import com.tibbiodev.diyabetim.adapters.ReminderInfoCursorAdapter;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.models.UsefulInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 2.11.2016.
 */
public class YaklasanOlaylarFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = InsulinSaatlerimFragment.class.getSimpleName();

    private static final int EVENT_LOADER = 0;

    private ListView mListViewYaklasanOlaylar;
    private int mPosition = ListView.INVALID_POSITION;
    private OlaylarCursorAdapter mYaklasanOlaylarCursorAdapter;

    private List<UsefulInfo> bilgiListesi = new ArrayList<>();

    public static final String[] REMINDER_COLUMNS = {
            DiyabetimContract.ReminderInfoEntry._ID,
            DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT,
            DiyabetimContract.ReminderInfoEntry.COLUMN_NOTE,
            DiyabetimContract.ReminderInfoEntry.COLUMN_TYPE,
            DiyabetimContract.ReminderInfoEntry.COLUMN_REMINDER_ENABLE
    };

    public static final int COL_REMINDER_INFO_ID = 0;
    public static final int COL_TIMETEXT = 1;
    public static final int COL_NOTE = 2;
    public static final int COL_TYPE = 3;
    public static final int COL_REMINDER_ENABLE = 4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaklasan_olaylar,
                container,
                false);

        mListViewYaklasanOlaylar = (ListView) view.findViewById(
                R.id.list_view_yaklasan_olaylar
        );

        mYaklasanOlaylarCursorAdapter = new OlaylarCursorAdapter(
                getActivity(), null, 0
        );
        mListViewYaklasanOlaylar.setAdapter(mYaklasanOlaylarCursorAdapter);


        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.reminder_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = item.getItemId();
        if(itemId == R.id.action_edit){

            return true;
        }
        else if(itemId == R.id.action_delete){

            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(EVENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader");

        Calendar calendar = Calendar.getInstance();
        String timeText = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) + "" +
                String.format("%02d",calendar.get(Calendar.MINUTE));
        String sortOrder = DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT + " ASC";

        return new CursorLoader(
                getActivity(),
                DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                REMINDER_COLUMNS,
                DiyabetimContract.ReminderInfoEntry.COLUMN_REMINDER_ENABLE + " = '" +
                        DiyabetimContract.ReminderInfoEntry.REMINDER_ENABLE + "' and " +
                        DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT + " > '" +
                timeText + "'",
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mYaklasanOlaylarCursorAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListViewYaklasanOlaylar.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mYaklasanOlaylarCursorAdapter.swapCursor(null);
    }
}
