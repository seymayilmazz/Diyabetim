package com.tibbiodev.diyabetim.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tibbiodev.diyabetim.data.DiyabetimContract.FoodEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.BloodSugarEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.ReminderInfoEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.ReminderEntry;
import com.tibbiodev.diyabetim.data.DiyabetimContract.UsefulInfoEntry;
import com.tibbiodev.diyabetim.models.UsefulInfo;


/**
 * Created by User on 2.11.2016.
 */
public class DiyabetimDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "diyabetim.db";

    public DiyabetimDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FOOD_TABLE =
                "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +
                        FoodEntry._ID + " INTEGER PRIMARY KEY," +
                        FoodEntry.COLUMN_BESIN_ISMI + " TEXT NOT NULL, " +
                        FoodEntry.COLUMN_GLISEMIK_INDEKS + " TEXT NOT NULL, " +
                        FoodEntry.COLUMN_GLISEMIK_YUK + " TEXT NOT NULL, " +
                        "UNIQUE (" + FoodEntry.COLUMN_BESIN_ISMI + ") ON CONFLICT REPLACE"
                        + " )";

        final String SQL_CREATE_BLOOD_SUGAR_TABLE =
                "CREATE TABLE " + BloodSugarEntry.TABLE_NAME + " (" +
                        BloodSugarEntry._ID + " INTEGER PRIMARY KEY," +
                        BloodSugarEntry.COLUMN_MEASUREMENT + " INTEGER NOT NULL, " +
                        BloodSugarEntry.COLUMN_DATETEXT + " TEXT NOT NULL, " +
                        BloodSugarEntry.COLUMN_TIMETEXT + " TEXT NOT NULL," +
                        BloodSugarEntry.COLUMN_MEASUREMENT_TYPE + " INTEGER NOT NULL," +
                        BloodSugarEntry.COLUMN_MEAL_TIME + " TEXT" +
                        " )";

        final String SQL_CREATE_REMINDER_INFO_TABLE =
                "CREATE TABLE " + ReminderInfoEntry.TABLE_NAME + " (" +
                        ReminderInfoEntry._ID + " INTEGER PRIMARY KEY," +
                        ReminderInfoEntry.COLUMN_TIMETEXT + " TEXT NOT NULL, " +
                        ReminderInfoEntry.COLUMN_NOTE + " TEXT NOT NULL, " +
                        ReminderInfoEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                        ReminderInfoEntry.COLUMN_REMINDER_ENABLE + " INTEGER NOT NULL"
                        + " )";

        final String SQL_CREATE_REMINDER_TABLE =
                "CREATE TABLE " + ReminderEntry.TABLE_NAME + " (" +
                        ReminderEntry._ID + " INTEGER PRIMARY KEY," +
                        ReminderEntry.COLUMN_REMINDERINFO_ID + " INTEGER NOT NULL," +
                        ReminderEntry.COLUMN_IS_IT_DONE + " INTEGER NOT NULL," +
                        ReminderEntry.COLUMN_DATETTEXT + " TEXT NOT NULL," +
                        " FOREIGN KEY (" + ReminderEntry.COLUMN_REMINDERINFO_ID + ") REFERENCES " +
                        ReminderInfoEntry.TABLE_NAME + " (" + ReminderInfoEntry._ID + ")," +
                        " UNIQUE (" + ReminderEntry.COLUMN_REMINDERINFO_ID + "," +
                        ReminderEntry.COLUMN_DATETTEXT + " ) ON CONFLICT REPLACE" +
                        ")";

        final String SQL_CREATE_USEFUL_INFO_TABLE =
                "CREATE TABLE " + UsefulInfoEntry.TABLE_NAME + " (" +
                        UsefulInfoEntry._ID + " INTEGER PRIMARY KEY," +
                        UsefulInfoEntry.COLUMN_INFO_TITLE + " TEXT NOT NULL, " +
                        UsefulInfoEntry.COLUMN_INFO_SUMMARY + " TEXT NOT NULL, " +
                        UsefulInfoEntry.COLUMN_INFO_LINK + " TEXT NOT NULL, " +
                        "UNIQUE (" + UsefulInfoEntry.COLUMN_INFO_TITLE + ") ON CONFLICT REPLACE"
                        + " )";

        db.execSQL(SQL_CREATE_FOOD_TABLE);
        db.execSQL(SQL_CREATE_BLOOD_SUGAR_TABLE);
        db.execSQL(SQL_CREATE_REMINDER_INFO_TABLE);
        db.execSQL(SQL_CREATE_REMINDER_TABLE);
        db.execSQL(SQL_CREATE_USEFUL_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + FoodEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + BloodSugarEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + UsefulInfoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + ReminderInfoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + ReminderEntry.TABLE_NAME);
        onCreate(db);
    }
}
