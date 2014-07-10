package ru.rzn.gmyasoedov.test4xxi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * City DB
 */
public class CityDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "city";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_TEMP = "TEMP";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_PRESSURE = "PRESSURE";
    public static final String COLUMN_HUMIDITY = "HUMIDITY";
    public static final String COLUMN_FORECAST = "FORECAST";
    public static final String COLUMN_DATE = "DATE";
    private static final String DATABASE_NAME = "CityDB";
    private static final int DATABASE_VERSION = 1;

    public CityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " +TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_TEMP + " REAL, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_PRESSURE + " INTEGER, " +
                COLUMN_HUMIDITY + " INTEGER, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_FORECAST + " BLOB);");

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, 524901);
        contentValues.put(COLUMN_NAME, "Moscow");
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        contentValues.put(COLUMN_ID, 498817);
        contentValues.put(COLUMN_NAME, "Saint Petersburg");
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
