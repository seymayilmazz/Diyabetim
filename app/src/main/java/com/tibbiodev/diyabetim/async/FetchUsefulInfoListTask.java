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
import com.tibbiodev.diyabetim.models.UsefulInfo;

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
 * Created by User on 15.12.2016
 */
public class FetchUsefulInfoListTask extends AsyncTask<Void, Void, List<UsefulInfo>> {

    private final String LOG_TAG = FetchUsefulInfoListTask.class.getSimpleName();
    private Context mContext;

    public FetchUsefulInfoListTask(Context context){
        mContext = context;
    }

    @Override
    protected List<UsefulInfo> doInBackground(Void... params) {

        /* url connection */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        /* request sonucu gelicek json verisi */
        String usefulInfoJsonStr = null;


        try {

            /* iletisim icin temel url */
            final String CONTACT_BASE_URL = "http://kafkastemizlik.com/apiversion/usefulinfo.php";

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

            usefulInfoJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Food Json String : " + usefulInfoJsonStr);


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


        Type usefulInfoListType = new TypeToken<ArrayList<UsefulInfo>>(){}.getType();
        List<UsefulInfo> usefulInfoList = gson.fromJson(usefulInfoJsonStr, usefulInfoListType);

        return usefulInfoList;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<UsefulInfo> usefulInfoList) {

        Vector<ContentValues> cVVector = new Vector<ContentValues>(usefulInfoList.size());
        for (UsefulInfo usefulInfo : usefulInfoList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DiyabetimContract.UsefulInfoEntry.COLUMN_INFO_TITLE, usefulInfo.getTitle());
            contentValues.put(DiyabetimContract.UsefulInfoEntry.COLUMN_INFO_LINK, usefulInfo.getLink());
            contentValues.put(DiyabetimContract.UsefulInfoEntry.COLUMN_INFO_SUMMARY, usefulInfo.getSummary());
            cVVector.add(contentValues);
        }

        if(cVVector.size() > 0){
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(
                    DiyabetimContract.UsefulInfoEntry.CONTENT_URI,
                    cvArray
            );
        }

    }
}

