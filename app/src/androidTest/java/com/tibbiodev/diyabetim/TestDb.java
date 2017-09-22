package com.tibbiodev.diyabetim;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;


import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.data.DiyabetimContract.FoodEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.BloodSugarEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.ReminderInfoEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.ReminderEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.UsefulInfoEntry;
import com.tibbiodev.diyabetim.data.DiyabetimDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by User on 2.12.2016.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb(){
        mContext.deleteDatabase(DiyabetimDbHelper.DATABASE_NAME);

        DiyabetimDbHelper dbHelper = new DiyabetimDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        assertEquals(true, db.isOpen());

        db.close();
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
        values.put(ReminderInfoEntry.COLUMN_TIMETEXT, "1630");
        values.put(ReminderInfoEntry.COLUMN_NOTE, "Test Insulin Note");
        values.put(ReminderInfoEntry.COLUMN_TYPE, ReminderInfoEntry.REMINDER_INFO_TYPE_INSULIN);
        values.put(ReminderInfoEntry.COLUMN_REMINDER_ENABLE, ReminderInfoEntry.REMINDER_ENABLE);

        return values;
    }

    public static ContentValues getReminderInfoPillValues(){
        ContentValues values = new ContentValues();
        values.put(ReminderInfoEntry.COLUMN_TIMETEXT, "1630");
        values.put(ReminderInfoEntry.COLUMN_NOTE, "Test Pill Note");
        values.put(ReminderInfoEntry.COLUMN_TYPE, ReminderInfoEntry.REMINDER_INFO_TYPE_PILL);
        values.put(ReminderInfoEntry.COLUMN_REMINDER_ENABLE, ReminderInfoEntry.REMINDER_ENABLE);

        return values;
    }

    public static ContentValues getUsefulInfoValues(){
        ContentValues values = new ContentValues();
        values.put(UsefulInfoEntry.COLUMN_INFO_TITLE, "Test Title");
        values.put(UsefulInfoEntry.COLUMN_INFO_LINK, "Test Link");
        values.put(UsefulInfoEntry.COLUMN_INFO_SUMMARY, "Test Summary");

        return values;
    }

    public static void validateCursor(ContentValues exceptedValues, Cursor valueCursor){

        Set<Map.Entry<String, Object>> valueSet = exceptedValues.valueSet();

        for(Map.Entry<String, Object> entry : valueSet){

            String columnName = entry.getKey();
            int columnIndex = valueCursor.getColumnIndex(columnName);
            assertTrue(columnIndex != -1);

            String expectedValue
                    = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(columnIndex));

        }

    }

    public void testInsertReadDb(){

        DiyabetimDbHelper dbHelper = new DiyabetimDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*
         * test for food values
         */
        ContentValues foodValues = getFoodContentValues();

        long foodRowId;
        foodRowId = db.insert(FoodEntry.TABLE_NAME, null, foodValues);

        // verify we got a row back
        assertTrue(foodRowId != -1);
        Log.d(LOG_TAG, "Food New row id: " + foodRowId);

        Cursor cursor = db.query(
                FoodEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
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

        long bloodSugarRowId;
        bloodSugarRowId = db.insert(BloodSugarEntry.TABLE_NAME, null, bloodSugarValues);

        // verify we got a row back
        assertTrue(bloodSugarRowId != -1);
        Log.d(LOG_TAG, "Blood Sugar New row id: " + bloodSugarRowId);

        cursor = db.query(
                BloodSugarEntry.TABLE_NAME,
                null,
                null,
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

        long usefulInfoRowId;
        usefulInfoRowId = db.insert(UsefulInfoEntry.TABLE_NAME, null, usefulInfoValues);

        // verify we got a row back
        assertTrue(usefulInfoRowId != -1);
        Log.d(LOG_TAG, "Useful info New Row Id : " + usefulInfoRowId);

        cursor = db.query(
                UsefulInfoEntry.TABLE_NAME,
                null,
                null,
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
        ContentValues reminderInfoInsulinValues = getReminderInfoInsulinValues();

        // for insulin
        long reminderInfoInsulinRowId;
        reminderInfoInsulinRowId =
                db.insert(ReminderInfoEntry.TABLE_NAME,
                        null,
                        reminderInfoInsulinValues);

        // verify we got a row back
        assertTrue(reminderInfoInsulinRowId != -1);
        Log.d(LOG_TAG, "Reminder info Row id for insulin : " + reminderInfoInsulinRowId);

        cursor = db.query(
                ReminderInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            validateCursor(reminderInfoInsulinValues, cursor);

            // reminder for insulin
            ContentValues reminderValues = new ContentValues();
            reminderValues.put(ReminderEntry.COLUMN_REMINDERINFO_ID, reminderInfoInsulinRowId);
            reminderValues.put(ReminderEntry.COLUMN_DATETTEXT, "20161203");
            reminderValues.put(ReminderEntry.COLUMN_IS_IT_DONE, ReminderEntry.REMINDER_NOT_DONE);

            long reminderRowId = db.insert(ReminderEntry.TABLE_NAME,
                    null, reminderValues);

            // verify we got a row back
            assertTrue(reminderRowId != -1);
            Log.d(LOG_TAG, "reminder new row id : " + reminderRowId);

            cursor = db.query(
                    ReminderEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()){
                validateCursor(reminderValues, cursor);
            }
            else{
                fail("No reminder data returned!");
            }
        }
        else{
            fail("No reminder info insulin data returned!");
        }


        // for pill
        ContentValues reminderInfoPillValues = getReminderInfoPillValues();

        long reminderInfoPillRowId;
        reminderInfoPillRowId =
                db.insert(ReminderInfoEntry.TABLE_NAME,
                        null,
                        reminderInfoPillValues);

        // verify we got a row back
        assertTrue(reminderInfoPillRowId != 1);
        Log.d(LOG_TAG, "Reminder info Row id for pill : " + reminderInfoPillRowId);

        cursor = db.query(
                ReminderInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.move(2)){
            validateCursor(reminderInfoPillValues, cursor);

            // reminder for pill
            ContentValues reminderValues = new ContentValues();
            reminderValues.put(ReminderEntry.COLUMN_REMINDERINFO_ID, reminderInfoPillRowId);
            reminderValues.put(ReminderEntry.COLUMN_DATETTEXT, "20161203");
            reminderValues.put(ReminderEntry.COLUMN_IS_IT_DONE, ReminderEntry.REMINDER_NOT_DONE);

            long reminderRowId = db.insert(ReminderEntry.TABLE_NAME,
                    null, reminderValues);

            // verify we got a row back
            assertTrue(reminderRowId != -1);
            Log.d(LOG_TAG, "reminder new row id : " + reminderRowId);

            cursor = db.query(
                    ReminderEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if(cursor.move(2)){
                validateCursor(reminderValues, cursor);
            }
            else{
                fail("No reminder data returned!");
            }
        }
        else{
            fail("No reminder info pill data returned!");
        }

    }

}
