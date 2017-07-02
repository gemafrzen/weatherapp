package org.gemafrzen.meinwetter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.gemafrzen.meinwetter.weatherdata.WeatherAtLocation;
import org.gemafrzen.meinwetter.weatherdata.WeatherEntry;

public class DatabaseProvider {

    private Context context;
    private String TAG = WeatherAtLocation.class.getSimpleName();

    /*
     * TODO add Timestamps
     */
    public DatabaseProvider(Context context) {
        this.context = context;
    }


    public void saveWeatherEntry(WeatherEntry we, boolean isForecast, int day){
        ContentValues values;
        long locationID;
        SQLiteDatabase db = MeinWetterDBHelper.getInstance(context).getReadableDatabase();
        WeatherEntry doesLocationExist = getLocation(we.getLocation());

        db.beginTransaction();

        if(doesLocationExist != null) {
            Log.e(TAG, "save : Location exists");
            //delete old entries for this location and forecast
            String selection = MeinWetterContract.WeatherEntry.COLUMN_IS_FORECAST + " = ?" +
                    " AND " + MeinWetterContract.WeatherEntry.COLUMN_FORECAST_DAY + " = ?" +
                    " AND " + MeinWetterContract.WeatherEntry.COLUMN_LOCATIONS_ID + " = ?";

            String[] selectionArgs = {isForecast ? "0" : "1",
                    String.valueOf(day),
                    doesLocationExist.getLocation()};

            db.delete(MeinWetterContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);

            locationID = Long.parseLong(doesLocationExist.getLocation());
        }else{
            //create location entry
            Log.e(TAG, "save : Location does not exist");
            values = new ContentValues();
            values.put(MeinWetterContract.LocationsEntry.COLUMN_LOCATION, we.getLocation());
            values.put(MeinWetterContract.LocationsEntry.COLUMN_COUNTRYCODE, we.getCountrycode());
            values.put(MeinWetterContract.LocationsEntry.COLUMN_LATITUDE, we.getLatitude());
            values.put(MeinWetterContract.LocationsEntry.COLUMN_LONGITUDE, we.getLongitude());

            locationID = db.insert(MeinWetterContract.LocationsEntry.TABLE_NAME, null,values);
        }
        Log.e(TAG, "save : locationID = " + locationID);
        values = new ContentValues();
        values.put(MeinWetterContract.WeatherEntry.COLUMN_LOCATIONS_ID, locationID);
        values.put(MeinWetterContract.WeatherEntry.COLUMN_IS_FORECAST, (isForecast? "'0'," : "'1',"));
        values.put(MeinWetterContract.WeatherEntry.COLUMN_FORECAST_DAY, day);
        values.put(MeinWetterContract.WeatherEntry.COLUMN_DESCRIPTION, we.getDescription());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_CURRENT_TEMPERATURE, we.getCurrentTemperature());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MORNING, we.getTemperatureAtMorning());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_DAY, we.getTemperatureAtDay());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_EVENTING, we.getTemperatureAtEvening());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_NIGHT, we.getTemperatureAtNight());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MIN, we.getTemperatureMin());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MAX, we.getTemperatureMax());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_PRESSURE, we.getPressure());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_HUMIDITY, we.getHumidity());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_WINDSPEED, we.getWindspeed());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_WINDDEGREE, we.getWinddegree());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_CLOUDINESS, we.getCloudiness());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_SNOWVOLUME, we.getSnowvolume());
        values.put(MeinWetterContract.WeatherEntry.COLUMN_RAINVOLUME, we.getRainvolume());

        locationID = db.insert(MeinWetterContract.WeatherEntry.TABLE_NAME, null,values);
        Log.e(TAG, "save : result insert = " + locationID);
        if(locationID != -1) db.setTransactionSuccessful();

        db.endTransaction();
    }


    public WeatherEntry getForecastWeather(String location, int day){
        return getWeatherEntry(location, true, day);
    }


    public WeatherEntry getCurrentWeather(String location){
        return getWeatherEntry(location, false, 0);
    }


    private WeatherEntry getWeatherEntry(String location, boolean isForecast, int day){
        SQLiteDatabase db = MeinWetterDBHelper.getInstance(context).getReadableDatabase();

        WeatherEntry we = getLocation(location);
        Log.e(TAG, "load : getEntry " + we + " isforecast="+isForecast + " number="+day);
        if(we != null) {
            String selection = MeinWetterContract.WeatherEntry.COLUMN_IS_FORECAST + " = ?" +
                    " AND " + MeinWetterContract.WeatherEntry.COLUMN_FORECAST_DAY + " = ?" +
                    " AND " + MeinWetterContract.WeatherEntry.COLUMN_LOCATIONS_ID + " = ?";
            String[] selectionArgs = {isForecast ? "0" : "1", String.valueOf(day), we.getLocation()};

            Cursor cursor = db.query(MeinWetterContract.WeatherEntry.TABLE_NAME,
                null,           // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // don't group the rows
                null,           // don't filter by row groups
                null            // The sort order
            );

            if (cursor.moveToFirst()) {
                Log.e(TAG, "load : curser moved to first ");
                we.setSnowvolume(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_SNOWVOLUME)));
                we.setRainvolume(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_RAINVOLUME)));
                we.setCloudiness(cursor.getInt(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_CLOUDINESS)));
                we.setCurrentIcon(cursor.getString(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_CURRENTICON)));
                we.setCurrentTemperature(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_CURRENT_TEMPERATURE)));
                we.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_DESCRIPTION)));
                we.setHumidity(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_HUMIDITY)));
                we.setPressure(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_PRESSURE)));
                we.setWindspeed(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_WINDSPEED)));
                we.setWinddegree(cursor.getInt(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_WINDDEGREE)));
                we.setTemperatureAtDay(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_DAY)));
                we.setTemperatureAtEvening(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_EVENTING)));
                we.setTemperatureAtMorning(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MORNING)));
                we.setTemperatureAtNight(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_NIGHT)));
                we.setTemperatureMax(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MAX)));
                we.setTemperatureMin(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.WeatherEntry.COLUMN_TEMPERATURE_MIN)));
            }else{
                we = null;
            }

            cursor.close();
        }

        return we;
    }

        /**
         * Returns WeatherEntry - if it exists - with location information
         * @param location
         * @return WeatherEntry.location contains LocationsID
         */
        private WeatherEntry getLocation(String location){
            SQLiteDatabase db = MeinWetterDBHelper.getInstance(context).getReadableDatabase();

            Cursor cursor = db.query(MeinWetterContract.LocationsEntry.TABLE_NAME,
                    null,
                    MeinWetterContract.LocationsEntry.COLUMN_LOCATION + " = ?",
                    new String[] {location},
                    null,
                    null,
                    null
            );

            WeatherEntry we = null;

            if(cursor.moveToFirst()) {
                we = new WeatherEntry();

                we.setLocation(String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.LocationsEntry._ID))));
                we.setCountrycode(cursor.getString(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.LocationsEntry.COLUMN_COUNTRYCODE)));
                we.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.LocationsEntry.COLUMN_LATITUDE)));
                we.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(
                        MeinWetterContract.LocationsEntry.COLUMN_LONGITUDE)));
            }

            cursor.close();

            return we;
        }
}
