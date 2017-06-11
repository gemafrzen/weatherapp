package org.gemafrzen.meinwetter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.gemafrzen.meinwetter.CurrentWeatherActivity;
import org.gemafrzen.meinwetter.R;

import java.util.Random;

/**
 * Created by Erik on 30.05.2017.
 */

public class CurrentWeatherWidgetProvider extends AppWidgetProvider {
    // TODO alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 , ClockIntent(context));

    public static final String ACTION_UPDATE_WEATHER = "ACTION_UPDATE_WEATHER";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                CurrentWeatherWidgetProvider.class);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {

            // todo should start the service
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_current_weather);

            // TODO remove Set the text
            remoteViews.setTextViewText(R.id.text_temp_now, "18° C");
            remoteViews.setTextViewText(R.id.text_temp_today, "25° C");

            // Register an onClickListener
            //Intent intent = new Intent(context, CurrentWeatherWidgetProvider.class);
            Intent intent = new Intent(context, CurrentWeatherActivity.class);

            /*
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            */

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id.WidgetLayout, pendingIntent);

            //tells appmanager to perform an update to the current widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(this.ACTION_UPDATE_WEATHER)) {
            //TODO verarbeite WeatherEntry Parcable
            Toast.makeText(context,
                    "OnClick in Provider",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
