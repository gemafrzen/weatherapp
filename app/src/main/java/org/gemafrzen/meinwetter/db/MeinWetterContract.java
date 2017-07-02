package org.gemafrzen.meinwetter.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Erik on 15.04.2017.
 */

public final class MeinWetterContract {

    public static final String CONTENT_AUTHORITY = "org.gemafrzen.meinwetter";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOCATIONS = "locations";
    public static final String PATH_WEATHER = "weather";


    private MeinWetterContract() {}


    /**Inner class that defines the table contents of the location table. */
    public static final class LocationsEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";

        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_COUNTRYCODE= "countrycode";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATIONS).build();

        // Custom MIME types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATIONS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATIONS;

        // Helper method
        public static Uri buildProgramUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    /** Inner class that defines the table contents of the trainging day table. */
    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_LOCATIONS_ID = "locationsId";
        public static final String COLUMN_IS_FORECAST = "isForecast";
        public static final String COLUMN_FORECAST_DAY = "forecastDay";
        public static final String COLUMN_CURRENTICON = "currentIcon";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CURRENT_TEMPERATURE = "currentTemperature";
        public static final String COLUMN_TEMPERATURE_MORNING = "temperatureAtMorning";
        public static final String COLUMN_TEMPERATURE_DAY = "temperatureAtDay";
        public static final String COLUMN_TEMPERATURE_EVENTING = "temperatureAtEvening";
        public static final String COLUMN_TEMPERATURE_NIGHT = "temperatureAtNight";
        public static final String COLUMN_TEMPERATURE_MIN = "temperatureMin";
        public static final String COLUMN_TEMPERATURE_MAX = "temperatureMax";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_WINDSPEED = "windspeed";
        public static final String COLUMN_WINDDEGREE = "winddegree";
        public static final String COLUMN_CLOUDINESS = "cloudiness";
        public static final String COLUMN_SNOWVOLUME = "snowvolume";
        public static final String COLUMN_RAINVOLUME = "rainvolume";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + PATH_WEATHER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + PATH_WEATHER;

        // Helper method.
        public static Uri buildTrainingDayUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
