package org.gemafrzen.meinwetter.activities;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * TODO implement Configuration
 * https://stackoverflow.com/questions/13694186/how-to-create-an-app-widget-with-a-configuration-activity-and-update-it-for-the
 */
public class CurrentWeatherWidgetConfigure extends ChooseLocationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void setResult(String listitem) {

        int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                         AppWidgetManager.INVALID_APPWIDGET_ID);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            //AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(
            //        getBaseContext()).getAppWidgetInfo(mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            resultValue.putExtra("result",listitem);
            setResult(RESULT_OK, resultValue);

            finish();
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.i("I am invalid", "I am invalid");
            finish();
        }

    }
}
