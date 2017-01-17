package com.mzglinicki.ossomapp.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Marcin on 17.01.2017.
 */

public class RatedItemsTable {

    public final static String TABLE_NAME = "RATED_ITEMS_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_RATED_ITEM_ID = "RATED_ITEM_ID";
    public static final String COLUMN_NOTE = "NOTE";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_RATED_ITEM_ID + " integer not null, "
            + COLUMN_NOTE + " integer not null "
            + ")";

    public static void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(final SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}