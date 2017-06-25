package org.gemafrzen.meinwetter.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.gemafrzen.meinwetter.R;
import org.gemafrzen.meinwetter.weatherdata.WeatherAtLocation;
import org.gemafrzen.meinwetter.weatherdata.WeatherEntry;
import org.gemafrzen.meinwetter.http.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class CurrentWeatherService extends IntentService {

    public static final String WEATHER_SERVICE_EXTRA_STRING_ARRAY = "locations";
    public static final String BROADCAST_ACTION = "org.gemafrzen.meinwetter.service.BROADCAST";
    public static final String EXTENDED_DATA_LOCATION = "org.gemafrzen.meinwetter.service.LOCATION";
    public static final String EXTENDED_DATA_WEATHER = "org.gemafrzen.meinwetter.service.WEATHER";

    //TODO broadcast error
    private String TAG = WeatherAtLocation.class.getSimpleName();
    private HttpHandler sh;
    private String appid = "";

    private LinkedList<WeatherAtLocation> currentWeatherList;

    public CurrentWeatherService(){
        super("CurrentWeatherService");

        currentWeatherList = new LinkedList<>();
        sh = new HttpHandler();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // processing done here….
        String[] locations = intent.getStringArrayExtra(WEATHER_SERVICE_EXTRA_STRING_ARRAY);

        for(int i = 0; i < locations.length; i++){
            addToCurrentWeatherList(locations[i]);
            collectCurrentWeatherFor(locations[i]);
        }
    }

    private void checkApiID(){
        if(appid == "") appid = this.getResources().getString(R.string.open_weather_map_key);
    }

    private void addToCurrentWeatherList(String location){
        for (WeatherAtLocation wal: currentWeatherList) {
            if(wal.getLocation().equals(location)){
                return;
            }
        }

        currentWeatherList.add(new WeatherAtLocation(location));
    }

    private void sendWeatherToReceiver(WeatherAtLocation newWeatherInformation, boolean sendOnlyIfChanged){
        for (WeatherAtLocation wal: currentWeatherList) {
            if(wal.getLocation().equals(newWeatherInformation.getLocation())){
                if(sendOnlyIfChanged || !wal.equals(newWeatherInformation)){
                    broadcastWeather(newWeatherInformation);
                }
            }
        }
    }

    private void broadcastWeather(WeatherAtLocation newWeatherInformation){
        Intent localIntent = new Intent(this.BROADCAST_ACTION);
        // Puts the status into the Intent
        localIntent.putExtra(this.EXTENDED_DATA_LOCATION, newWeatherInformation.getLocation());
        localIntent.putExtra(this.EXTENDED_DATA_WEATHER, newWeatherInformation);

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public void collectWeatherData(){
        checkApiID();

        String location = "";
        for(int i = 0; i < this.currentWeatherList.size(); i++) {
            location = currentWeatherList.get(i).getLocation();
            collectCurrentWeatherFor(location);
            collectForecastFor(location);
        }
    }

    private void collectCurrentWeatherFor(String location){
        checkApiID();

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location
                + "&units=metric&APPID=" + appid;
        new GetWeather(url, location).execute();
    }

    private void collectForecastFor(String location){
        checkApiID();

        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + location
                + "&cnt=3&units=metric&APPID=" + appid;
        new GetWeather(url, location).execute();
    }

    private class GetWeather extends AsyncTask<Void, Void, Void> {

        private String url;
        private String location;
        private WeatherAtLocation wal;

        public GetWeather(String url, String location) {
            this.url = url;
            this.location = location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            wal = new WeatherAtLocation(location);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            sendWeatherToReceiver(wal, true); //TODO true nicht hardcoden
        }

        private void retrieveCurrentWeather(JSONObject jsonObj) throws JSONException{
            WeatherEntry currentWeather = new WeatherEntry();
            wal.setToday(currentWeather);

            if (jsonObj.has("coord")) {
                JSONObject jsonCoord = jsonObj.getJSONObject("coord");
                currentWeather.setLatitude(jsonCoord.getDouble("lat"));
                currentWeather.setLongitude(jsonCoord.getDouble("lon"));
            }

            if (jsonObj.has("sys")) {
                currentWeather.setCountrycode(jsonObj.getJSONObject("sys").getString("country"));
            }

            if (jsonObj.has("name")) {
                currentWeather.setLocation(jsonObj.getString("name"));
            }

            if (jsonObj.has("main")) {
                JSONObject jsonMain = jsonObj.getJSONObject("main");
                currentWeather.setCurrentTemperature(jsonMain.getDouble("temp"));
                if (jsonMain.has("pressure"))
                    currentWeather.setPressure(jsonMain.getDouble("pressure"));
                if (jsonMain.has("humidity"))
                    currentWeather.setHumidity(jsonMain.getDouble("humidity"));
            }

            if (jsonObj.has("weather")) {
                JSONObject jsonWeather = jsonObj.getJSONArray("weather").getJSONObject(0);
                currentWeather.setCurrentIcon(jsonWeather.getString("icon"));
                currentWeather.setDescription(jsonWeather.getString("description"));
            }

            if (jsonObj.has("wind")) {
                JSONObject jsonWeather = jsonObj.getJSONObject("wind");
                if (jsonWeather.has("speed"))
                    currentWeather.setWindspeed(jsonWeather.getDouble("speed"));
                if (jsonWeather.has("deg"))
                    currentWeather.setWinddegree(jsonWeather.getInt("deg"));
            }

            if (jsonObj.has("clouds")) {
                currentWeather.setCloudiness(jsonObj.getJSONObject("clouds").getInt("all"));
            }

            if (jsonObj.has("rain")) {
                currentWeather.setRainvolume(jsonObj.getJSONObject("rain").getDouble("3h"));
            }

            if (jsonObj.has("snow")) {
                currentWeather.setSnowvolume(jsonObj.getJSONObject("snow").getDouble("3h"));
            }
        }


        private void retrieveForecast(JSONObject jsonObj) throws JSONException{
            if (jsonObj.has("city")) {

                JSONObject jsonCity = jsonObj.getJSONObject("city");
                String location_name = jsonCity.getString("name");
                String location_country = jsonCity.getString("country");
                double location_lat = jsonCity.getDouble("lat");
                double location_lon = jsonCity.getDouble("lon");

                for (int i = 0; i < jsonObj.getInt("cnt"); i++) {
                    WeatherEntry we = new WeatherEntry();
                    wal.addToDaylist(we);

                    we.setLocation(location_name);
                    we.setCountrycode(location_country);
                    we.setLatitude(location_lat);
                    we.setLongitude(location_lon);


                    JSONObject jsonDay = jsonObj.getJSONObject("" + i);

                    if (jsonDay.has("temp")) {
                        JSONObject jsonTemp = jsonDay.getJSONObject("temp");
                        if (jsonTemp.has("morn"))
                            we.setTemperatureAtMorning(jsonTemp.getDouble("morn"));
                        if (jsonTemp.has("day"))
                            we.setTemperatureAtDay(jsonTemp.getDouble("day"));
                        if (jsonTemp.has("eve"))
                            we.setTemperatureAtEvening(jsonTemp.getDouble("eve"));
                        if (jsonTemp.has("night"))
                            we.setTemperatureAtNight(jsonTemp.getDouble("night"));
                        if (jsonTemp.has("min"))
                            we.setTemperatureMin(jsonTemp.getDouble("min"));
                        if (jsonTemp.has("max"))
                            we.setTemperatureMax(jsonTemp.getDouble("max"));
                    }

                    if (jsonDay.has("pressure"))
                        we.setPressure(jsonDay.getDouble("pressure"));
                    if (jsonDay.has("humidity"))
                        we.setHumidity(jsonDay.getDouble("humidity"));
                    if (jsonDay.has("speed"))
                        we.setWindspeed(jsonDay.getDouble("speed"));
                    if (jsonDay.has("deg")) we.setWinddegree(jsonDay.getInt("deg"));
                    if (jsonDay.has("clouds"))
                        we.setCloudiness(jsonDay.getInt("clouds"));
                    if (jsonDay.has("snow"))
                        we.setSnowvolume(jsonDay.getDouble("snow"));
                    if (jsonDay.has("rain"))
                        we.setRainvolume(jsonDay.getDouble("rain"));

                    if (jsonDay.has("weather")) {
                        JSONObject jsonWeather = jsonObj.getJSONArray("weather").getJSONObject(0);
                        we.setCurrentIcon(jsonWeather.getString("icon"));
                        we.setDescription(jsonWeather.getString("description"));
                    }

                }
            }
        }

        protected Void doInBackground(Void... arg0) {
            Log.e(TAG, "ATTEMPT" + url);
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    if (jsonObj.has("cnt")) retrieveForecast(jsonObj);
                    else retrieveCurrentWeather(jsonObj);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            /*
            // TODO anzaige, wenn Fehler in Verarbeitung
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
             */
            }

            return null;
        }
    }
}
