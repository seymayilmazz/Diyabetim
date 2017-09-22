package com.tibbiodev.diyabetim.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.activities.BaslangicActivity;
import com.tibbiodev.diyabetim.activities.SekerOlcumActivity;
import com.tibbiodev.diyabetim.adapters.BloodSugarCursorAdapter;
import com.tibbiodev.diyabetim.data.DiyabetimContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 2.11.2016.
 */
public class SekerOlcumlerimFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{


    public static final String LOG_TAG = SekerOlcumlerimFragment.class.getSimpleName();

    private static final int BLOODSUGAR_LOADER = 0;

    private ListView mListViewSekerOlcumListesi;
    private int mPosition = ListView.INVALID_POSITION;
    private BloodSugarCursorAdapter mBloodSugarCursorAdapter;

    private static final String[] BLOOD_SUGAR_COLUMNS = {
            DiyabetimContract.BloodSugarEntry._ID,
            DiyabetimContract.BloodSugarEntry.COLUMN_MEASUREMENT,
            DiyabetimContract.BloodSugarEntry.COLUMN_DATETEXT,
            DiyabetimContract.BloodSugarEntry.COLUMN_TIMETEXT,
            DiyabetimContract.BloodSugarEntry.COLUMN_MEASUREMENT_TYPE,
            DiyabetimContract.BloodSugarEntry.COLUMN_MEAL_TIME
    };

    public static final int COL_BLOOD_SUGAR_ID = 0;
    public static final int COL_MEASUREMENT = 1;
    public static final int COL_DATETEXT = 2;
    public static final int COL_TIMETEXT = 3;
    public static final int COL_MEASUREMENT_TYPE = 4;
    public static final int COL_MEAL_TIME = 5;

    private ActionMode mActionMode;

    private List<Integer> mSelectedItemIdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_seker_olcumlerim,
                container,
                false);
        mListViewSekerOlcumListesi = (ListView)  view.findViewById(
                R.id.list_view_seker_olcumleri
        );


        mBloodSugarCursorAdapter = new BloodSugarCursorAdapter(
                getActivity(), null, 0
        );
        mListViewSekerOlcumListesi.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListViewSekerOlcumListesi.setAdapter(mBloodSugarCursorAdapter);
        mListViewSekerOlcumListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if (cursor != null) {

                    if(mActionMode != null){
                        mActionMode.setTitle(mListViewSekerOlcumListesi.getCheckedItemCount() + " öğe seçili");
                        if(mListViewSekerOlcumListesi.isItemChecked(position)){
                            view.setBackgroundResource(R.color.colorSelection);
                            mSelectedItemIdList.add(Integer.valueOf((int)id));
                        }
                        else{
                            if(mListViewSekerOlcumListesi.getCheckedItemCount() == 0){
                                mActionMode.finish();
                                mActionMode = null;
                            }
                            view.setBackgroundResource(R.color.colorTransparent);
                            mSelectedItemIdList.remove(Integer.valueOf((int)id));
                        }

                    }
                }
            }
        });//
        mListViewSekerOlcumListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(mActionMode == null){
                    mActionMode = getActivity().startActionMode(actionModeCallBack);
                    mListViewSekerOlcumListesi.setItemChecked(position, true);
                    view.setBackgroundResource(R.color.colorSelection);
                    mActionMode.setTitle(mListViewSekerOlcumListesi.getCheckedItemCount() + " öğe seçili");
                    mSelectedItemIdList.add(Integer.valueOf((int)id));
                }

                return true;
            }
        });



        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(
                R.id.fab
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SekerOlcumActivity.class));
            }
        });

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(BLOODSUGAR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader");
        String sortOrder = DiyabetimContract.BloodSugarEntry.COLUMN_DATETEXT + " DESC, " +
                DiyabetimContract.BloodSugarEntry.COLUMN_TIMETEXT + " DESC";

        return new CursorLoader(
                getActivity(),
                DiyabetimContract.BloodSugarEntry.CONTENT_URI,
                BLOOD_SUGAR_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBloodSugarCursorAdapter.swapCursor(data);
        if(mPosition != ListView.INVALID_POSITION){
            mListViewSekerOlcumListesi.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBloodSugarCursorAdapter.swapCursor(null);
    }

    private ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.general_cab, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if(itemId == R.id.action_delete){
                deleteAreYouSureDialog();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            mListViewSekerOlcumListesi.clearChoices();
            mListViewSekerOlcumListesi.setAdapter(mBloodSugarCursorAdapter);
        }
    };

    private void deleteAreYouSureDialog(){
        String message  = mSelectedItemIdList.size() + " " +
                getResources().getString(R.string.dialog_delete_message);
        String positiveText = getResources().getString(R.string.dialog_yes_button);
        String negativeText = getResources().getString(R.string.dialog_no_button);
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        for(Integer id: mSelectedItemIdList){
                            getActivity().getContentResolver().delete(
                                    DiyabetimContract.BloodSugarEntry.CONTENT_URI,
                                    DiyabetimContract.BloodSugarEntry._ID + " = '" +
                                            id + "'",
                                    null

                            );

                        }
                        mSelectedItemIdList.clear();
                        Toast.makeText(getActivity(),
                                R.string.silme_basarili,
                                Toast.LENGTH_SHORT).show();
                        mActionMode.finish();
                    }
                })

                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
