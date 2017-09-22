package com.tibbiodev.diyabetim.fragments;

import android.content.ContentUris;
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
import com.tibbiodev.diyabetim.activities.SekerOlcumActivity;
import com.tibbiodev.diyabetim.adapters.ReminderInfoCursorAdapter;
import com.tibbiodev.diyabetim.adapters.UsefulInfoCursorAdapter;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.models.UsefulInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2.11.2016.
 */
public class InsulinSaatlerimFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = InsulinSaatlerimFragment.class.getSimpleName();

    private static final int INSULIN_TIMES_LOADER = 0;

    private ListView mListViewInsulinTimes;
    private int mPosition = ListView.INVALID_POSITION;
    private ReminderInfoCursorAdapter mInsulinTimesCursorAdapter;

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
        View view = inflater.inflate(R.layout.fragment_insulin_saatlerim,
                container,
                false);

        mListViewInsulinTimes = (ListView) view.findViewById(
                R.id.list_view_insulin_saatler
        );

        mInsulinTimesCursorAdapter = new ReminderInfoCursorAdapter(
                getActivity(), null, 0
        );
        mListViewInsulinTimes.setAdapter(mInsulinTimesCursorAdapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(
                R.id.fab
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HatirlaticiActivity.class);
                intent.putExtra("reminderType",
                        DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);
                startActivity(intent);
            }
        });

        mListViewInsulinTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HatirlaticiDuzenleActivity.class);
                intent.putExtra("reminderType",
                        DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


        mInsulinTimesCursorAdapter.setOnCheckedListener(new ReminderInfoCursorAdapter.OnCheckedListener() {
            @Override
            public void onCheckedItem(View view, boolean checkedState) {
                Integer id = (Integer)view.getTag();
                ContentValues contentValues = new ContentValues();
                int enable = DiyabetimContract.ReminderInfoEntry.REMINDER_ENABLE;
                if(checkedState == false){
                    enable = DiyabetimContract.ReminderInfoEntry.REMINDER_DISABLED;
                }
                contentValues.put(DiyabetimContract.ReminderInfoEntry.COLUMN_REMINDER_ENABLE,
                        enable);
                getActivity().getContentResolver().update(
                        DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                        contentValues,
                        DiyabetimContract.ReminderInfoEntry._ID + " = '" +
                                id + "'",
                        null
                );
            }
        });

        registerForContextMenu(mListViewInsulinTimes);

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
            Intent intent = new Intent(getActivity(), HatirlaticiDuzenleActivity.class);
            intent.putExtra("reminderType",
                    DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);
            intent.putExtra("id", info.id);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.action_delete){
            getActivity().getContentResolver().delete(
                    DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                    DiyabetimContract.ReminderInfoEntry._ID + " = '" +
                            info.id + "'",
                    null
            );
            Toast.makeText(getActivity(),
                    R.string.silme_basarili_item,
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(INSULIN_TIMES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader");
        String sortOrder = DiyabetimContract.ReminderInfoEntry.COLUMN_TIMETEXT + " ASC";

        return new CursorLoader(
                getActivity(),
                DiyabetimContract.ReminderInfoEntry.CONTENT_URI,
                REMINDER_COLUMNS,
                DiyabetimContract.ReminderInfoEntry.COLUMN_TYPE + " = '" +
                        DiyabetimContract.ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN + "'",
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mInsulinTimesCursorAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListViewInsulinTimes.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mInsulinTimesCursorAdapter.swapCursor(null);
    }
}
