package com.tibbiodev.diyabetim.fragments;

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
import android.widget.Button;
import android.widget.ListView;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.adapters.BesinlerCursorAdapter;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.models.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4.11.2016.
 */
public class BesinTablosuFragment extends Fragment
implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = BesinTablosuFragment.class.getSimpleName();

    private static final int FOOD_LOADER = 0;

    private ListView mListViewBesinListesi;
    private int mPosition = ListView.INVALID_POSITION;
    private BesinlerCursorAdapter mBesinlerCursorAdapter;

    private List<Food> foodListesi = new ArrayList<>();

    private static final String[] FOOD_COLUMNS = {
            DiyabetimContract.FoodEntry._ID,
            DiyabetimContract.FoodEntry.COLUMN_BESIN_ISMI,
            DiyabetimContract.FoodEntry.COLUMN_GLISEMIK_INDEKS,
            DiyabetimContract.FoodEntry.COLUMN_GLISEMIK_YUK
    };

    public static final int COL_BESIN_ID = 0;
    public static final int COL_BESIN_ISMI = 1;
    public static final int COL_GLISEMIK_INDEKS = 2;
    public static final int COL_GLISEMIK_YUK = 3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_besin_tablosu,
                container,
                false);


        mListViewBesinListesi = (ListView)  view.findViewById(
                R.id.list_view_besin_tablo
        );

        Button buttonTabloyuGoster = (Button) view.findViewById(
                R.id.tabloyuGoster
        ) ;
        buttonTabloyuGoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.besinTabloTanitim).setVisibility(View.INVISIBLE);
            }
        });

        mBesinlerCursorAdapter = new BesinlerCursorAdapter(
                getActivity(), null, 0
        );
        mListViewBesinListesi.setAdapter(mBesinlerCursorAdapter);

        prepareBesinData();

        return view;
    }

    private void prepareBesinData(){

        Food food = new Food("Elma", 38, 6);
        foodListesi.add(food);

        food = new Food("Karpuz", 72, 4);
        foodListesi.add(food);

        food = new Food("Ananas", 58, 7);
        foodListesi.add(food);
        food = new Food("Karpuz", 72, 4);
        foodListesi.add(food);

        food = new Food("Ananas", 58, 7);
        foodListesi.add(food);
        food = new Food("Karpuz", 72, 4);
        foodListesi.add(food);

        food = new Food("Ananas", 58, 7);
        foodListesi.add(food);

        food = new Food("Karpuz", 72, 4);
        foodListesi.add(food);

        food = new Food("Ananas", 58, 7);
        foodListesi.add(food);
        food = new Food("Karpuz", 72, 4);
        foodListesi.add(food);

        food = new Food("Ananas", 58, 7);
        foodListesi.add(food);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FOOD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader");
        String sortOrder = DiyabetimContract.FoodEntry.COLUMN_BESIN_ISMI + " ASC";

        return new CursorLoader(
                getActivity(),
                DiyabetimContract.FoodEntry.CONTENT_URI,
                FOOD_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBesinlerCursorAdapter.swapCursor(data);
        if(mPosition != ListView.INVALID_POSITION){
            mListViewBesinListesi.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBesinlerCursorAdapter.swapCursor(null);
    }
}
