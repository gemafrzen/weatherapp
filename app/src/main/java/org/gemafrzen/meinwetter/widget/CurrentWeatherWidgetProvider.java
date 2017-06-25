package org.gemafrzen.meinwetter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import org.gemafrzen.meinwetter.activities.CurrentWeatherActivity;
import org.gemafrzen.meinwetter.R;
import org.gemafrzen.meinwetter.weatherdata.WeatherAtLocation;
import org.gemafrzen.meinwetter.weatherdata.WeatherEntry;
import org.gemafrzen.meinwetter.service.CurrentWeatherService;

/**
 * Created by Erik on 30.05.2017.
 */

public class CurrentWeatherWidgetProvider extends AppWidgetProvider {
    // TODO alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 , ClockIntent(context));

    private AppWidgetManager appWidgetManager;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        boolean isFirstInstance = true;
        String location = "Berlin";
        this.appWidgetManager = appWidgetManager;

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                CurrentWeatherWidgetProvider.class);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            // todo should start the service
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_current_weather);

            Intent mServiceIntent = new Intent(context, CurrentWeatherService.class);
            mServiceIntent.putExtra(CurrentWeatherService.WEATHER_SERVICE_EXTRA_STRING_ARRAY, new String[] {location});
            context.startService(mServiceIntent);

            // Register an onClickListener
            Intent intent = new Intent(context, CurrentWeatherActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id.WidgetLayout, pendingIntent);

            //tells appmanager to perform an update to the current widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

            /* TODO load locations from db */
            if (isFirstInstance) {
                location = "Stockholm";
                isFirstInstance = false;
            }
        }
    }
}
