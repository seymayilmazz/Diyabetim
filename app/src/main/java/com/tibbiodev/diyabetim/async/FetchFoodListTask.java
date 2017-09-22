package com.tibbiodev.diyabetim.async;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.models.Food;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by User on 15.12.2016.
 */
public class FetchFoodListTask extends AsyncTask<Void, Void, List<Food>> {

    private final String LOG_TAG = FetchFoodListTask.class.getSimpleName();
    private Context mContext;

    public FetchFoodListTask(Context context){
        mContext = context;
    }

    @Override
    protected List<Food> doInBackground(Void... params) {

        /* url connection */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        /* request sonucu gelicek json verisi */
        String foodJsonStr = null;


        try {

            /* iletisim icin temel url */
            final String CONTACT_BASE_URL = "http://kafkastemizlik.com/apiversion/food.php";

            /* uri'yi olustur */
            Uri builtUri = Uri.parse(CONTACT_BASE_URL).buildUpon().build();

            /* url'yi olustur */
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            /* baglantiyi ac */
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /* baglantidan gelen input akisini al */
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            /* input akisi bos ise */
            if(inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            /* okunan uzunluk sifir ise */
            if(buffer.length() == 0){
                return null;
            }

            foodJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Food Json String : " + foodJsonStr);


        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;

        }
        finally {

            /* url connection varsa kapat */
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            /* reader aciksa kapat */
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }

            }

        }

        Gson gson = new Gson();


        Type foodListType = new TypeToken<ArrayList<Food>>(){}.getType();
        List<Food> foodList = gson.fromJson(foodJsonStr, foodListType);

        return foodList;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Food> foodList) {

        Vector<ContentValues> cVVector = new Vector<ContentValues>(foodList.size());
        for (Food food : foodList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DiyabetimContract.FoodEntry.COLUMN_BESIN_ISMI, food.getIsim());
            contentValues.put(DiyabetimContract.FoodEntry.COLUMN_GLISEMIK_INDEKS, food.getGlisemikIndeks());
            contentValues.put(DiyabetimContract.FoodEntry.COLUMN_GLISEMIK_YUK, food.getGlisemikYuk());
            cVVector.add(contentValues);
        }

        if(cVVector.size() > 0){
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(
                    DiyabetimContract.FoodEntry.CONTENT_URI,
                    cvArray
            );
        }

    }
}
