package com.tibbiodev.diyabetim;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.data.DiyabetimContract.FoodEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.BloodSugarEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.UsefulInfoEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.ReminderInfoEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.ReminderEntry;
import com.tibbiodev.diyabetim.data.DiyabetimDbHelper;
import com.tibbiodev.diyabetim.models.UsefulInfo;

import java.util.Map;
import java.util.Set;

/**
 * Created by User on 4.12.2016.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public static void validateCursor(ContentValues expectedValues, Cursor valueCursor){

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet){

            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse(-1 == index);

            String expectedValue
                    = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(index));
        }
    }

    public static ContentValues getFoodContentValues(){

        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_BESIN_ISMI, "Elma");
        values.put(FoodEntry.COLUMN_GLISEMIK_INDEKS, 38);
        values.put(FoodEntry.COLUMN_GLISEMIK_YUK, 6);

        return values;
    }

    public static ContentValues getBloodSugarValues(){
        ContentValues values = new ContentValues();
        values.put(BloodSugarEntry.COLUMN_MEASUREMENT, 106);
        values.put(BloodSugarEntry.COLUMN_DATETEXT, "20161203");
        values.put(BloodSugarEntry.COLUMN_TIMETEXT, "1649");
        values.put(BloodSugarEntry.COLUMN_MEASUREMENT_TYPE, BloodSugarEntry.MEASUREMENT_TYPE_1);
        values.put(BloodSugarEntry.COLUMN_MEAL_TIME, "-");

        return values;
    }

    public static ContentValues getReminderInfoInsulinValues(){
        ContentValues values = new ContentValues();
        values.put(ReminderInfoEntry.COLUMN_TIMETEXT, "1230");
        values.put(ReminderInfoEntry.COLUMN_NOTE, "Test Insulin Note 2");
        values.put(ReminderInfoEntry.COLUMN_TYPE, ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);
        values.put(ReminderInfoEntry.COLUMN_REMINDER_ENABLE, ReminderInfoEntry.REMINDER_ENABLE);

        return values;
    }

    public static ContentValues getReminderInfoPillValues(){
        ContentValues values = new ContentValues();
        values.put(ReminderInfoEntry.COLUMN_TIMETEXT, "1630");
        values.put(ReminderInfoEntry.COLUMN_NOTE, "Test Pill Note");
        values.put(ReminderInfoEntry.COLUMN_TYPE, ReminderInfoEntry.REMINDER_INFO_TYPE_PILL);

        return values;
    }

    public static ContentValues getUsefulInfoValues(){
        ContentValues values = new ContentValues();
        values.put(UsefulInfoEntry.COLUMN_INFO_TITLE, "Test Title");
        values.put(UsefulInfoEntry.COLUMN_INFO_LINK, "Test Link");
        values.put(UsefulInfoEntry.COLUMN_INFO_SUMMARY, "Test Summary");
        return values;
    }

    public void testGetType(){
        // content://com.tibbiodev.diyabetim/food
        String type = mContext.getContentResolver().getType(FoodEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.tibbiodev.diyabetim/food
        assertEquals(FoodEntry.CONTENT_TYPE, type);

        int id = 1;
        // content://com.tibbiodev.diyabetim/food/1
        type = mContext.getContentResolver().getType(FoodEntry.buildFoodUri(id));
        // vnd.android.cursor.item/com.tibbiodev.diyabetim/food
        assertEquals(FoodEntry.CONTENT_ITEM_TYPE, type);

        // content://com.tibbiodev.diyabetim/bloodsugar
        type = mContext.getContentResolver().getType(BloodSugarEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.tibbiodev.diyabetim/bloodsugar
        assertEquals(BloodSugarEntry.CONTENT_TYPE, type);

        // content://com.tibbiodev.diyabetim/bloodsugar/1
        type = mContext.getContentResolver().getType(BloodSugarEntry.buildBloodSugarUri(id));
        // vnd.android.cursor.item/com.tibbiodev.diyabetim/bloodsugar
        assertEquals(BloodSugarEntry.CONTENT_ITEM_TYPE, type);

        // content://com.tibbiodev.diyabetim/usefulinfo
        type = mContext.getContentResolver().getType(UsefulInfoEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.tibbiodev.diyabetim/usefulinfo
        assertEquals(UsefulInfoEntry.CONTENT_TYPE, type);

        // content://com.tibbiodev.diyabetim/usefulinfo/1
        type = mContext.getContentResolver().getType(UsefulInfoEntry.buildUsefulUri(id));
        // vnd.android.cursor.item/com.tibbiodev.diyabetim/usefulinfo
        assertEquals(UsefulInfoEntry.CONTENT_ITEM_TYPE, type);

        // content://com.tibbiodev.diyabetim/reminderinfo
        type = mContext.getContentResolver().getType(ReminderInfoEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.tibbiodev.diyabetim/reminderinfo
        assertEquals(ReminderInfoEntry.CONTENT_TYPE, type);

        // content://com.tibbiodev.diyabetim/reminderinfo/1
        type = mContext.getContentResolver().getType(ReminderInfoEntry.buildReminderInfoUri(id));
        // vnd.android.cursor.item/com.tibbiodev.diyabetim/usefulinfo
        assertEquals(ReminderInfoEntry.CONTENT_ITEM_TYPE, type);

        // content://com.tibbiodev.diyabetim/reminderinfo/type/1
        type = mContext.getContentResolver().getType(ReminderInfoEntry.buildReminderInfoWithType(id));
        // vnd.android.cursor.item/com.tibbiodev.diyabetim/reminderinfo
        assertEquals(ReminderInfoEntry.CONTENT_TYPE, type);
    }

    public void testInsertReadProvider(){


        /*
         * test for food values
         */
        ContentValues foodValues = getFoodContentValues();

        Uri foodUri = mContext.getContentResolver().insert(FoodEntry.CONTENT_URI, foodValues);
        long foodRowId = ContentUris.parseId(foodUri);

        // verify we got a row back
        assertTrue(foodRowId != -1);
        Log.d(LOG_TAG, "Food New row id: " + foodRowId);

        Uri foodIdUri = FoodEntry.buildFoodUri(foodRowId);
        Cursor cursor = mContext.getContentResolver().query(
          foodIdUri, null,  null, null, null
        );


        if(cursor.moveToFirst()){
            validateCursor(foodValues, cursor);
        }
        else{
            fail("No food data returned!");
        }

        /*
         * test for blood sugar values
         */
        ContentValues bloodSugarValues = getBloodSugarValues();

        Uri bloodSugarUri = mContext.getContentResolver().insert(
                BloodSugarEntry.CONTENT_URI, bloodSugarValues
        );
        long bloodSugarRowId = ContentUris.parseId(bloodSugarUri);

        // verify we got a row back
        assertTrue(bloodSugarRowId != -1);
        Log.d(LOG_TAG, "Blood Sugar New row id: " + bloodSugarRowId);

        Uri bloodSugarIdUri = BloodSugarEntry.buildBloodSugarUri(bloodSugarRowId);
        cursor = mContext.getContentResolver().query(
                bloodSugarIdUri,
                null,
                null,
                null,
                null
        );


        if(cursor.moveToFirst()){
            validateCursor(bloodSugarValues, cursor);
        }
        else{
            fail("No blood sugar data returned!");
        }

        /*
         * test for useful info
         */
        ContentValues usefulInfoValues = getUsefulInfoValues();

        Uri usefulInfoUri = mContext.getContentResolver().insert(
                UsefulInfoEntry.CONTENT_URI, usefulInfoValues
        );
        long usefulInfoRowId = ContentUris.parseId(usefulInfoUri);

        // verify we got a row back
        assertTrue(usefulInfoRowId != -1);
        Log.d(LOG_TAG, "Useful info New Row Id : " + usefulInfoRowId);

        Uri usefulInfoIdUri = UsefulInfoEntry.buildUsefulUri(usefulInfoRowId);
        cursor = mContext.getContentResolver().query(
                usefulInfoUri,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            validateCursor(usefulInfoValues, cursor);
        }
        else{
            fail("No useful info data returned!");
        }


        /*
         * test for reminder info
         */
        ContentValues reminderInfoValues = getReminderInfoInsulinValues();

        Uri reminderInfoUri = mContext.getContentResolver().insert(
                ReminderInfoEntry.CONTENT_URI, reminderInfoValues
        );
        long reminderInfoRowId = ContentUris.parseId(reminderInfoUri);

        // verify we got a row back
        assertTrue(reminderInfoRowId != -1);
        Log.d(LOG_TAG, "Reminder info New Row Id : " + reminderInfoRowId);

        Uri reminderInfoIdUri = ReminderInfoEntry.buildReminderInfoUri(reminderInfoRowId);
        cursor = mContext.getContentResolver().query(
                reminderInfoIdUri,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            validateCursor(reminderInfoValues, cursor);

            // reminder for insulin
            ContentValues reminderValues = new ContentValues();
            reminderValues.put(DiyabetimContract.ReminderEntry.COLUMN_REMINDERINFO_ID,
                    reminderInfoRowId);
            reminderValues.put(DiyabetimContract.ReminderEntry.COLUMN_DATETTEXT, "20161204");
            reminderValues.put(DiyabetimContract.ReminderEntry.COLUMN_IS_IT_DONE,
                    DiyabetimContract.ReminderEntry.REMINDER_NOT_DONE);

            Uri reminderUri = mContext.getContentResolver().insert(
                    DiyabetimContract.ReminderEntry.CONTENT_URI,
                    reminderValues
            );
            long reminderRowId = ContentUris.parseId(reminderUri);

            // verify we got a row back
            assertTrue(reminderRowId != -1);
            Log.d(LOG_TAG, "reminder new row id : " + reminderRowId);

            Uri reminderIdUri = DiyabetimContract.ReminderEntry.buildReminderUri(reminderRowId);
            cursor = mContext.getContentResolver().query(
                    reminderIdUri,
                    null,
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()){
                validateCursor(reminderValues, cursor);
                int idColumnIndex = cursor.getColumnIndex(ReminderInfoEntry._ID);
                int id = cursor.getInt(idColumnIndex);
                int timeColumnIndex = cursor.getColumnIndex(ReminderInfoEntry.COLUMN_TIMETEXT);
                String time = cursor.getString(timeColumnIndex);
                int noteColumnIndex = cursor.getColumnIndex(ReminderInfoEntry.COLUMN_NOTE);
                String note = cursor.getString(noteColumnIndex);
                int dateColumnIndex = cursor.getColumnIndex(ReminderEntry.COLUMN_DATETTEXT);
                String date = cursor.getString(dateColumnIndex);
                Log.v(LOG_TAG, "Reminder info id : " + id +
                        ", time : " + time +
                        ", note : " + note +
                        ", date : " + date);

            }
            else{
                fail("No reminder data returned!");
            }
        }
        else{
            fail("No reminder info data returned!");
        }

        /*
         * test for reminder info with type
         */
        Uri reminderInfoWithTypeUri = ReminderInfoEntry.
                buildReminderInfoWithType(ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);
        cursor = mContext.getContentResolver().query(
                reminderInfoWithTypeUri,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()){
            int idColumnIndex = cursor.getColumnIndex(ReminderInfoEntry._ID);
            int id = cursor.getInt(idColumnIndex);
            int timeColumnIndex = cursor.getColumnIndex(ReminderInfoEntry.COLUMN_TIMETEXT);
            String time = cursor.getString(timeColumnIndex);
            int noteColumnIndex = cursor.getColumnIndex(ReminderInfoEntry.COLUMN_NOTE);
            String note = cursor.getString(noteColumnIndex);
            Log.v(LOG_TAG, "Cursor id : " + id + ", time : " + time + ", note : " + note);
        }

        /*
         * test for reminder info with date
         */
        Uri reminderWithDateUri = ReminderEntry.buildReminderWithDate("20161203");
        cursor = mContext.getContentResolver().query(
                reminderWithDateUri,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()){
            int idColumnIndex = cursor.getColumnIndex(ReminderInfoEntry._ID);
            int id = cursor.getInt(idColumnIndex);
            int timeColumnIndex = cursor.getColumnIndex(ReminderInfoEntry.COLUMN_TIMETEXT);
            String time = cursor.getString(timeColumnIndex);
            int noteColumnIndex = cursor.getColumnIndex(ReminderInfoEntry.COLUMN_NOTE);
            String note = cursor.getString(noteColumnIndex);
            int dateColumnIndex = cursor.getColumnIndex(ReminderEntry.COLUMN_DATETTEXT);
            String date = cursor.getString(dateColumnIndex);
            Log.v(LOG_TAG, "Reminder info id : " + id +
                    ", time : " + time +
                    ", note : " + note +
                    ", date : " + date);
        }

    }
}
