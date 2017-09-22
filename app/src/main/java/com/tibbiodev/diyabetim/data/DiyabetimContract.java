package com.tibbiodev.diyabetim.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 2.11.2016.
 */
public class DiyabetimContract {

    public static final String CONTENT_AUTHORITY = "com.tibbiodev.diyabetim";

    // diger uygulamaların iletisim kurmasi icin
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // possible paths
    public static final String PATH_FOOD = "food";
    public static final String PATH_BLOOD_SUGAR = "bloodsugar";
    public static final String PATH_REMINDER_INFO = "reminderinfo";
    public static final String PATH_REMINDER = "reminder";
    public static final String PATH_USEFUL_INFO = "usefulinfo";

    public static final class FoodEntry implements BaseColumns{

        /* content://com.tibbiodev.diyabetim/food */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;

        public static final String TABLE_NAME = "besin";

        public static final String COLUMN_BESIN_ISMI = "besin_ismi";

        public static final String COLUMN_GLISEMIK_INDEKS = "glisemik_indeks";

        public static final String COLUMN_GLISEMIK_YUK = "glisemik_yuk";

        /* content://com.tibbiodev.diyabetim/food/{id} */
        public static Uri buildFoodUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class BloodSugarEntry implements BaseColumns{

        /* content://com.tibbiodev.diyabetim/bloodsugar */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLOOD_SUGAR).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BLOOD_SUGAR;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BLOOD_SUGAR;

        public static final int MEASUREMENT_TYPE_1 = 0; /* aclik kan sekeri */

        public static final int MEASUREMENT_TYPE_2 = 1; /* tokluk kan sekeri */

        public static final String TABLE_NAME = "kansekeri_olcum";

        public static final String COLUMN_MEASUREMENT = "olcum";

        public static final String COLUMN_DATETEXT = "tarih";

        public static final String COLUMN_TIMETEXT = "saat";

        public static final String COLUMN_MEASUREMENT_TYPE = "olcum_tipi";

        public static final String COLUMN_MEAL_TIME = "yemek_zamani";

        /* content://com.tibbiodev.diyabetim/bloodsugar/{id} */
        public static Uri buildBloodSugarUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ReminderInfoEntry implements BaseColumns{

        /* content://com.tibbiodev.diyabetim/reminderinfo */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER_INFO).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REMINDER_INFO;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REMINDER_INFO;

        public static final int REMINDER_INFO_TYPE_PILL = 0;

        public static final int REMINDER_INFO_TYPE_INSULIN = 1;

        public static final int REMINDER_DISABLED = 0;

        public static final int REMINDER_ENABLE = 1;

        public static final String TABLE_NAME = "hatirlatici_bilgi";

        public static final String COLUMN_TIMETEXT = "saat";

        public static final String COLUMN_NOTE = "note";

        public static final String COLUMN_TYPE = "tip";

        public static final String COLUMN_REMINDER_ENABLE = "aktif_mi";

        /* content://com.tibbiodev.diyabetim/reminderinfo/{id} */
        public static Uri buildReminderInfoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /* content://com.tibbiodev.diyabetim/reminderinfo/type/{type} */
        public static Uri buildReminderInfoWithType(int type){
            return ContentUris.withAppendedId(
                    CONTENT_URI.buildUpon().appendPath("type").build(),
                    type
            );
        }

    }

    public static final class ReminderEntry implements BaseColumns{

        /* content://com.tibbiodev.diyabetim/reminder */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REMINDER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REMINDER;

        public static final int REMINDER_NOT_DONE = 0;

        public static final int REMINDER_DONE = 1;

        public static final String TABLE_NAME = "hatirlatici";

        public static final String COLUMN_REMINDERINFO_ID = "hatirlatici_bilgi_id";

        public static final String COLUMN_DATETTEXT = "tarih";

        public static final String COLUMN_IS_IT_DONE = "yapilmis_mi";


        public static Uri buildReminderUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReminderWithDate(String date){
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_DATETTEXT, date)
                    .build();
        }

        public static String getStartDateFromUri(Uri uri){
            return uri.getQueryParameter(COLUMN_DATETTEXT);
        }

    }

    public static final class UsefulInfoEntry implements BaseColumns{

        /* content://com.tibbiodev.diyabetim/usefulinfo */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USEFUL_INFO).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USEFUL_INFO;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USEFUL_INFO;

        public static final String TABLE_NAME = "yararli_bilgi";

        public static final String COLUMN_INFO_TITLE = "baslik";

        public static final String COLUMN_INFO_SUMMARY = "ozet";

        public static final String COLUMN_INFO_LINK = "link";

        /* content://com.tibbiodev.diyabetim/usefulinfo/{id} */
        public static Uri buildUsefulUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
