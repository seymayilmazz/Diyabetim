package com.tibbiodev.diyabetim.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by User on 3.12.2016.
 */
public class DiyabetimProvider extends ContentProvider {

    private static final int FOOD = 100;
    private static final int FOOD_ID = 101;

    private static final int USEFUL_INFO = 200;
    private static final int USEFUL_INFO_ID = 201;

    private static final int BLOOD_SUGAR = 300;
    private static final int BLOOD_SUGAR_ID = 301;

    private static final int REMINDER_INFO = 400;
    private static final int REMINDER_INFO_ID = 401;
    private static final int REMINDER_INFO_TYPE = 402;

    private static final int REMINDER = 500;
    private static final int REMINDER_ID = 501;
    private static final int REMINDER_WITH_DATE = 502;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DiyabetimDbHelper mOpenHelper;
    private static final SQLiteQueryBuilder sReminderByReminderInfoQueryBuilder;

    static {
        sReminderByReminderInfoQueryBuilder = new SQLiteQueryBuilder();
        sReminderByReminderInfoQueryBuilder.setTables(
                DiyabetimContract.ReminderEntry.TABLE_NAME + " INNER JOIN " +
                        DiyabetimContract.ReminderInfoEntry.TABLE_NAME +
                        " ON " + DiyabetimContract.ReminderEntry.TABLE_NAME +
                        "." + DiyabetimContract.ReminderEntry.COLUMN_REMINDERINFO_ID +
                        " = " + DiyabetimContract.ReminderInfoEntry.TABLE_NAME +
                        "." + DiyabetimContract.ReminderInfoEntry._ID
        );
    }


    private static final String sReminderWithDate =
            DiyabetimContract.ReminderEntry.TABLE_NAME +
                    "." + DiyabetimContract.ReminderEntry.COLUMN_DATETTEXT + " = ? ";

    private Cursor getReminderWithId(Uri uri, String[] projection, String sortOrder){

        return sReminderByReminderInfoQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                DiyabetimContract.ReminderEntry.TABLE_NAME + "." +
                DiyabetimContract.ReminderEntry._ID + " = '" +
                        ContentUris.parseId(uri) + "'",
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReminder(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){

        String date = DiyabetimContract.ReminderEntry.getStartDateFromUri(uri);

        if(date != null) {
            selection = sReminderWithDate;
            selectionArgs = new String[] {date};
        }

        return sReminderByReminderInfoQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

    }

    private static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DiyabetimContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DiyabetimContract.PATH_FOOD, FOOD);
        matcher.addURI(authority, DiyabetimContract.PATH_FOOD + "/#", FOOD_ID);

        matcher.addURI(authority, DiyabetimContract.PATH_USEFUL_INFO, USEFUL_INFO);
        matcher.addURI(authority, DiyabetimContract.PATH_USEFUL_INFO + "/#", USEFUL_INFO_ID);

        matcher.addURI(authority, DiyabetimContract.PATH_BLOOD_SUGAR, BLOOD_SUGAR);
        matcher.addURI(authority, DiyabetimContract.PATH_BLOOD_SUGAR + "/#", BLOOD_SUGAR_ID);

        matcher.addURI(authority, DiyabetimContract.PATH_REMINDER_INFO, REMINDER_INFO);
        matcher.addURI(authority, DiyabetimContract.PATH_REMINDER_INFO + "/#", REMINDER_INFO_ID);
        matcher.addURI(authority, DiyabetimContract.PATH_REMINDER_INFO + "/type/#", REMINDER_INFO_TYPE);

        matcher.addURI(authority, DiyabetimContract.PATH_REMINDER, REMINDER);
        matcher.addURI(authority, DiyabetimContract.PATH_REMINDER + "/#", REMINDER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DiyabetimDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)){
            case FOOD:
                retCursor = db.query(
                        DiyabetimContract.FoodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // food/# */
            case FOOD_ID:
                retCursor = db.query(
                        DiyabetimContract.FoodEntry.TABLE_NAME,
                        projection,
                        DiyabetimContract.FoodEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BLOOD_SUGAR:
                retCursor = db.query(
                        DiyabetimContract.BloodSugarEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // bloodsugar/#
            case BLOOD_SUGAR_ID:
                retCursor = db.query(
                        DiyabetimContract.BloodSugarEntry.TABLE_NAME,
                        projection,
                        DiyabetimContract.BloodSugarEntry._ID + " = '" +
                                ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USEFUL_INFO:
                retCursor = db.query(
                        DiyabetimContract.UsefulInfoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USEFUL_INFO_ID:
                retCursor = db.query(
                        DiyabetimContract.UsefulInfoEntry.TABLE_NAME,
                        projection,
                        DiyabetimContract.UsefulInfoEntry._ID + " = '" +
                                ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REMINDER_INFO:
                retCursor = db.query(
                        DiyabetimContract.ReminderInfoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // reminderinfo
            case REMINDER_INFO_ID:
                retCursor = db.query(
                        DiyabetimContract.ReminderInfoEntry.TABLE_NAME,
                        projection,
                        DiyabetimContract.ReminderInfoEntry._ID + " = '" +
                                ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REMINDER_INFO_TYPE:
                retCursor = db.query(
                        DiyabetimContract.ReminderInfoEntry.TABLE_NAME,
                        projection,
                        DiyabetimContract.ReminderInfoEntry.COLUMN_TYPE + " = '" +
                                ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REMINDER:
                retCursor = getReminder(uri,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            case REMINDER_ID:
                retCursor = getReminderWithId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("unknown uri : " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FOOD:
                return DiyabetimContract.FoodEntry.CONTENT_TYPE;
            case FOOD_ID:
                return DiyabetimContract.FoodEntry.CONTENT_ITEM_TYPE;
            case BLOOD_SUGAR:
                return DiyabetimContract.BloodSugarEntry.CONTENT_TYPE;
            case BLOOD_SUGAR_ID:
                return DiyabetimContract.BloodSugarEntry.CONTENT_ITEM_TYPE;
            case USEFUL_INFO:
                return DiyabetimContract.UsefulInfoEntry.CONTENT_TYPE;
            case USEFUL_INFO_ID:
                return DiyabetimContract.UsefulInfoEntry.CONTENT_ITEM_TYPE;
            case REMINDER_INFO:
                return DiyabetimContract.ReminderInfoEntry.CONTENT_TYPE;
            case REMINDER_INFO_ID:
                return DiyabetimContract.ReminderInfoEntry.CONTENT_ITEM_TYPE;
            case REMINDER_INFO_TYPE:
                return DiyabetimContract.ReminderInfoEntry.CONTENT_TYPE;
            case REMINDER:
                return DiyabetimContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_ID:
                return DiyabetimContract.ReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match){
            case FOOD:{
                long _id = db.insert(DiyabetimContract.FoodEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnUri = DiyabetimContract.FoodEntry.buildFoodUri(_id);
                }
                else{
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case BLOOD_SUGAR:{
                long _id = db.insert(DiyabetimContract.BloodSugarEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = DiyabetimContract.BloodSugarEntry.buildBloodSugarUri(_id);
                }
                else{
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case USEFUL_INFO:{
                long _id = db.insert(DiyabetimContract.UsefulInfoEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = DiyabetimContract.UsefulInfoEntry.buildUsefulUri(_id);
                }
                else{
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REMINDER_INFO:{
                long _id = db.insert(DiyabetimContract.ReminderInfoEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = DiyabetimContract.ReminderInfoEntry.buildReminderInfoUri(_id);
                }
                else{
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REMINDER:{
                long _id = db.insert(DiyabetimContract.ReminderEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = DiyabetimContract.ReminderEntry.buildReminderUri(_id);
                }
                else{
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        switch (match){
            case FOOD:
                rowsDeleted = db.delete(DiyabetimContract.FoodEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case BLOOD_SUGAR:
                rowsDeleted = db.delete(DiyabetimContract.BloodSugarEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case USEFUL_INFO:
                rowsDeleted = db.delete(DiyabetimContract.UsefulInfoEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case REMINDER_INFO:
                rowsDeleted = db.delete(DiyabetimContract.ReminderInfoEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case REMINDER:
                rowsDeleted = db.delete(DiyabetimContract.ReminderEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        if(selection == null || rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case FOOD:
                rowsUpdated = db.update(DiyabetimContract.FoodEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case BLOOD_SUGAR:
                rowsUpdated = db.update(DiyabetimContract.BloodSugarEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case USEFUL_INFO:
                rowsUpdated = db.update(DiyabetimContract.UsefulInfoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case REMINDER_INFO:
                rowsUpdated = db.update(DiyabetimContract.ReminderInfoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case REMINDER:
                rowsUpdated = db.update(DiyabetimContract.ReminderEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknown uri : " + uri);
        }

        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch (match){
            case FOOD:
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(DiyabetimContract.FoodEntry.TABLE_NAME,
                                null, value);
                        if(_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                break;
            case USEFUL_INFO:
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(DiyabetimContract.UsefulInfoEntry.TABLE_NAME,
                                null, value);
                        if(_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}
