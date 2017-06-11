package org.gemafrzen.meinwetter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erik on 15.04.2017.
 */

public class MeinWetterDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MeinWetter.db";
    private boolean created = false;

    private static MeinWetterDBHelper instance = null;

    /* ---- TABLE PROGRAM ------------------------------------------------------------------------*/
    private static final String SQL_CREATE_PROGRAM_ENTRIES =
            "CREATE TABLE " + MeinWetterContract.PropertyEntry.TABLE_NAME + " (" +
                    MeinWetterContract.PropertyEntry._ID + " INTEGER PRIMARY KEY," +
                    MeinWetterContract.PropertyEntry.COLUMN_PROPERTY + " TEXT," +
                    MeinWetterContract.PropertyEntry.COLUMN_VALUE + " TEXT)";

    private static final String SQL_DELETE_PROGRAM_ENTRIES =
            "DROP TABLE IF EXISTS " + MeinWetterContract.PropertyEntry.TABLE_NAME;

    /* ---- TABLE TRAINING DAY -------------------------------------------------------------------*/
    private static final String SQL_CREATE_TRAININGDAY_ENTRIES =
            "CREATE TABLE " + MeinWetterContract.LocationsEntry.TABLE_NAME + " (" +
                    MeinWetterContract.LocationsEntry._ID + " INTEGER PRIMARY KEY," +
                    MeinWetterContract.LocationsEntry.COLUMN_LOCATION + " TEXT)";

    private static final String SQL_DELETE_TRAININGDAY_ENTRIES =
            "DROP TABLE IF EXISTS " + MeinWetterContract.LocationsEntry.TABLE_NAME;



    protected MeinWetterDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MeinWetterDBHelper getInstance(Context context) {
        if(instance == null) {
            instance = new MeinWetterDBHelper(context);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        if(created == false) {
            created = true;
            db.execSQL(SQL_CREATE_PROGRAM_ENTRIES);
            db.execSQL(SQL_CREATE_TRAININGDAY_ENTRIES);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TRAININGDAY_ENTRIES);
        db.execSQL(SQL_DELETE_PROGRAM_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
