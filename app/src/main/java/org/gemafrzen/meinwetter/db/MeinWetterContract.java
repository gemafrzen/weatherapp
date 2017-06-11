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
    public static final String PATH_PROPERTIES = "properties";
    private MeinWetterContract() {}


    /**Inner class that defines the table contents of the location table. */
    public static final class LocationsEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";

        public static final String COLUMN_LOCATION = "location";

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
    public static final class PropertyEntry implements BaseColumns {

        public static final String TABLE_NAME = "training_day";

        public static final String COLUMN_PROPERTY = "property";
        public static final String COLUMN_VALUE = "value";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROPERTIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + PATH_PROPERTIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + PATH_PROPERTIES;

        // Helper method.
        public static Uri buildTrainingDayUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
