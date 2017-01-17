package com.mzglinicki.ossomapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mzglinicki.ossomapp.Constants.DATABASE_NAME;
import static com.mzglinicki.ossomapp.Constants.DATABASE_VERSION;

/**
 * Created by Marcin on 17.01.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        RatedItemsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        RatedItemsTable.onUpgrade(db);
    }

    public void insertData(final int ratedItemId, final int value) {

        final SQLiteDatabase database = getWritableDatabase();

        final ContentValues contentValues = new ContentValues();
        contentValues.put(RatedItemsTable.COLUMN_RATED_ITEM_ID, ratedItemId);
        contentValues.put(RatedItemsTable.COLUMN_NOTE, value);

        database.insert(RatedItemsTable.TABLE_NAME, null, contentValues);
        database.close();
    }

    public Cursor getAllRatedImets() {
        final SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery("select * from " + RatedItemsTable.TABLE_NAME, null);
    }

    public boolean ifRecordExist(final int itemId) {
        final SQLiteDatabase database = getReadableDatabase();
        final String Query = "Select * from " + RatedItemsTable.TABLE_NAME + " where " + RatedItemsTable.COLUMN_RATED_ITEM_ID + " = " + itemId;
        final Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getItemNote(final int itemId) {

        final SQLiteDatabase database = getReadableDatabase();
        final String Query = "Select * from " + RatedItemsTable.TABLE_NAME + " where " + RatedItemsTable.COLUMN_RATED_ITEM_ID + " = " + itemId;

        final Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();
        final int note = cursor.getInt(cursor.getColumnIndex(RatedItemsTable.COLUMN_NOTE));
        cursor.close();
        return note;
    }
}