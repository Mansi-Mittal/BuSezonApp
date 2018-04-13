package com.example.mansi.busezon.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mansi.busezon.data.dbContract.userEntry;

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class dbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = dbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "profile.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link dbHelper}.
     *
     * @param context of the app
     */
    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_USER_TABLE =  "CREATE TABLE " + userEntry.TABLE_NAME + " ("
                + userEntry.COLUMN_USER_ID + " STRING NOT NULL, "
                + userEntry.COLUMN_USER + " TEXT NOT NULL, "
                + userEntry.COLUMN_EMAIL + " STRING PRIMARY KEY NOT NULL, "
                + userEntry.COLUMN_number + " STRING NOT NULL, "
                + userEntry.COLUMN_password + " STRING, "
                + userEntry.COLUMN_address + " STRING );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}

