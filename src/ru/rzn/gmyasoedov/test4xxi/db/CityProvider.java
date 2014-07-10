package ru.rzn.gmyasoedov.test4xxi.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import ru.rzn.gmyasoedov.test4xxi.db.CityDBHelper;

/**
 * Content provider for cities
 */
public class CityProvider extends ContentProvider {
    // authority
    public static final String AUTHORITY = "ru.rzn.gmyasoedov.test4xxiprovider";
    public static final int URI_CITY = 1;
    // Uri с указанным ID
    public static final int URI_CITY_ID = 2;
    public static final Uri CITY_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CityDBHelper.TABLE_NAME);
    private static final UriMatcher uriMatcher;
    private CityDBHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CityDBHelper.TABLE_NAME, URI_CITY);
        uriMatcher.addURI(AUTHORITY, CityDBHelper.TABLE_NAME + "/#", URI_CITY_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CityDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        selection = checkUri(uri, selection);
        String orderBy = CityDBHelper.COLUMN_NAME;
        Cursor cursor = dbHelper.getWritableDatabase()
                .query(CityDBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CITY:
                return "cities";
            case URI_CITY_ID:
                return "city";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = dbHelper.getWritableDatabase().insert(CityDBHelper.TABLE_NAME, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(CITY_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = dbHelper.getWritableDatabase().delete(CityDBHelper.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int cnt = dbHelper.getWritableDatabase().update(CityDBHelper.TABLE_NAME, contentValues,
                selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    /**
     * check uri
     * @param uri uri
     * @param selection selection string
     * @return fix selection
     */
    private String checkUri(Uri uri, String selection) {
        switch (uriMatcher.match(uri)) {
            case URI_CITY:
                return selection;
            case URI_CITY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = CityDBHelper.COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CityDBHelper.COLUMN_ID + " = " + id;
                }
                return selection;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }
}
