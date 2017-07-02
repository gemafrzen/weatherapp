package org.gemafrzen.meinwetter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erik on 15.04.2017.
 */

public class MeinWetterDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MeinWetter.db";
    private boolean created = false;

    private static MeinWetterDBHelper instance = null;

    /* ---- TABLE LOCATIONS ----------------------------------------------------------------------*/
    private static final String SQL_CREATE_LOCATIONS_ENTRIES =
            "CREATE TABLE " + MeinWetterContract.LocationsEntry.TABLE_NAME + " (" +
                    MeinWetterContract.LocationsEntry._ID + " INTEGER PRIMARY KEY," +
                    MeinWetterContract.LocationsEntry.COLUMN_LOCATION + " TEXT," +
                    MeinWetterContract.LocationsEntry.COLUMN_COUNTRYCODE + " TEXT," +
                    MeinWetterContract.LocationsEntry.COLUMN_LATITUDE + " TEXT," +
                    MeinWetterContract.LocationsEntry.COLUMN_LONGITUDE + " TEXT)";

    private static final String SQL_DELETE_LOCATIONS_ENTRIES =
            "DROP TABLE IF EXISTS " + MeinWetterContract.LocationsEntry.TABLE_NAME;

    /* ---- TABLE TRAINING DAY -------------------------------------------------------------------*/
    private static final String SQL_CREATE_WEATHER_ENTRIES =
            "CREATE TABLE " + MeinWetterContract.WeatherEntry.TABLE_NAME + " (" +
                    MeinWetterContract.WeatherEntry._ID + " INTEGER PRIMARY KEY," +
                    MeinWetterContract.WeatherEntry.COLUMN_LOCATIONS_ID + " INTEGER," +
                    MeinWetterContract.WeatherEntry.COLUMN_IS_FORECAST + " INTEGER," +
                    MeinWetterContract.WeatherEntry.COLUMN_FORECAST_DAY + " INTEGER," +
                    MeinWetterContract.WeatherEntry.COLUMN_CURRENTICON + " TEXT," +
                    MeinWetterContract.WeatherEntry.COLUMN_DESCRIPTION + " TEXT," +
                    MeinWetterContract.WeatherEntry.COLUMN_CURRENT_TEMPERATURE + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MORNING + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_DAY + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_EVENTING + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_NIGHT + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MIN + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MAX + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_PRESSURE + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_HUMIDITY + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_WINDSPEED + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_WINDDEGREE + " INTEGER," +
                    MeinWetterContract.WeatherEntry.COLUMN_CLOUDINESS + " INTEGER," +
                    MeinWetterContract.WeatherEntry.COLUMN_SNOWVOLUME + " REAL," +
                    MeinWetterContract.WeatherEntry.COLUMN_RAINVOLUME + " REAL)";

    private static final String SQL_DELETE_WEATHER_ENTRIES =
            "DROP TABLE IF EXISTS " + MeinWetterContract.WeatherEntry.TABLE_NAME;


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
            db.execSQL(SQL_CREATE_LOCATIONS_ENTRIES);
            db.execSQL(SQL_CREATE_WEATHER_ENTRIES);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_LOCATIONS_ENTRIES);
        db.execSQL(SQL_DELETE_WEATHER_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
