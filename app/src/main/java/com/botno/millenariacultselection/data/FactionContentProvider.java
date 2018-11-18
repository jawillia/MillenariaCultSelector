package com.botno.millenariacultselection.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by User on 1/19/2015.
 */
public class FactionContentProvider extends ContentProvider {
    private FactionReaderDbHelper dbHelper;

    // used for the UriMacher
    private static final int CULTS = 1;
    private static final int CULTS_ID = 2;
    private static final int CULTS_ACTIVE = 3;

    private static final String AUTHORITY = "com.botno.millenariacultselection.provider";

    private static final String BASE_PATH = "cults";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/cults";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/cult";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, CULTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CULTS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CULTS_ACTIVE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FactionReaderDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(FactionReaderContract.PremadeCult.CULT_TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case CULTS:
                break;
            case CULTS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(FactionReaderContract.PremadeCult._ID + "="
                        + uri.getLastPathSegment());
                break;
            case CULTS_ACTIVE:

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    private void checkColumns(String[] projection) {
        String[] available = { FactionReaderContract.PremadeCult.CULT_COLUMN_NAME,
                FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY, FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION,
                FactionReaderContract.PremadeCult._ID, FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d("Content Provider", "Inserting a database...");
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case CULTS:
                id = sqlDB.insert(FactionReaderContract.PremadeCult.CULT_TABLE_NAME, null, values);
                Log.d("Content Provider", "Premade Cult Inserted");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case CULTS:
                rowsDeleted = sqlDB.delete(FactionReaderContract.PremadeCult.CULT_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case CULTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(FactionReaderContract.PremadeCult.CULT_TABLE_NAME,
                            FactionReaderContract.PremadeCult._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(FactionReaderContract.PremadeCult.CULT_TABLE_NAME,
                            FactionReaderContract.PremadeCult._ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case CULTS:
                rowsUpdated = sqlDB.update(FactionReaderContract.PremadeCult.CULT_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CULTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(FactionReaderContract.PremadeCult.CULT_TABLE_NAME,
                            values,
                            FactionReaderContract.PremadeCult._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(FactionReaderContract.PremadeCult.CULT_TABLE_NAME,
                            values,
                            FactionReaderContract.PremadeCult._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
