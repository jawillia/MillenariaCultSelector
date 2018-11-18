package com.botno.millenariacultselection.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static com.botno.millenariacultselection.data.FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE;
import static com.botno.millenariacultselection.data.FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY;
import static com.botno.millenariacultselection.data.FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION;
import static com.botno.millenariacultselection.data.FactionReaderContract.PremadeCult.CULT_COLUMN_ICON;
import static com.botno.millenariacultselection.data.FactionReaderContract.PremadeCult.CULT_COLUMN_NAME;
import static com.botno.millenariacultselection.data.FactionReaderContract.PremadeCult.CULT_TABLE_NAME;

/**
 * Created by Jason Williams on 1/15/2015.
 */
public class FactionReaderDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CultWars.db";
    private static final int DATABASE_VERSION = 1;

    public FactionReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FactionReaderContract.PremadeCult.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.w(FactionReaderDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(FactionReaderContract.PremadeCult.DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertCult(String name, String difficulty, String expansion, byte[] icon, boolean active) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CULT_COLUMN_NAME, name);
        contentValues.put(CULT_COLUMN_DIFFICULTY, difficulty);
        contentValues.put(CULT_COLUMN_EXPANSION, expansion);
        contentValues.put(CULT_COLUMN_ICON, icon);
        int flag = active? 1 : 0;
        contentValues.put(CULT_COLUMN_ACTIVE, flag);
        db.insert(CULT_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateCult(Integer id, String name, String difficulty, String expansion, byte[] icon, boolean active) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CULT_COLUMN_NAME, name);
        contentValues.put(CULT_COLUMN_DIFFICULTY, difficulty);
        contentValues.put(CULT_COLUMN_EXPANSION, expansion);
        contentValues.put(CULT_COLUMN_ICON, icon);
        int flag = active? 1 : 0;
        contentValues.put(CULT_COLUMN_ACTIVE, flag);
        db.update(CULT_TABLE_NAME, contentValues, BaseColumns._ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean activateCult(Integer id, boolean active) {
        SQLiteDatabase db = getWritableDatabase();
        int flag = active? 1 : 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(CULT_COLUMN_ACTIVE, flag);
        db.update(CULT_TABLE_NAME, contentValues, BaseColumns._ID + " = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    public Cursor getCult(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CULT_TABLE_NAME + " WHERE " +
                BaseColumns._ID + "=?";
        Cursor res = db.rawQuery( query, new String[] { Integer.toString(id) } );
        if(res.moveToFirst())
        {
            return res;
        }
        else
        {
            return null;
        }
    }

    public Cursor getAllCults() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + CULT_TABLE_NAME, null );
        return res;
    }

    public Cursor getAllActiveCults() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CULT_TABLE_NAME + " WHERE " +
                FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE + "=?";
        Cursor res = db.rawQuery("SELECT * FROM " + CULT_TABLE_NAME + " WHERE " +
                FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE + "=1", null);
        if(res.moveToFirst())
        {
            return res;
        }
        else
        {
            return null;
        }
    }

    public Integer deleteCult(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CULT_TABLE_NAME,
                BaseColumns._ID + " = ? ",
                new String[] { Integer.toString(id) });
    }
}
