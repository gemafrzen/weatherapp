package org.gemafrzen.meinwetter.weatherdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erik on 21.05.2017.
 */

public class WeatherEntry implements Parcelable{
    private String location = "";
    private String countrycode = "";
    private String currentIcon = "";
    private String description = "";

    private double currentTemperature = -100.0;

    private double temperatureAtMorning = -100.0;
    private double temperatureAtDay = -100.0;
    private double temperatureAtEvening = -100.0;
    private double temperatureAtNight = -100.0;
    private double temperatureMin = -100.0;
    private double temperatureMax = -100.0;

    private double latitude;
    private double longitude;

    private double pressure; // in hPa
    private double humidity; // in %
    private double windspeed;
    private int winddegree;
    private int cloudiness;
    private double snowvolume;
    private double rainvolume;

    public WeatherEntry(){
    }

    private WeatherEntry(Parcel in) {
        location = in.readString();
        countrycode = in.readString();
        currentIcon = in.readString();
        description = in.readString();
        currentTemperature = in.readDouble();
        temperatureAtMorning = in.readDouble();
        temperatureAtDay = in.readDouble();
        temperatureAtEvening = in.readDouble();
        temperatureAtNight = in.readDouble();
        temperatureMin = in.readDouble();
        temperatureMax = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        pressure = in.readDouble();
        humidity = in.readDouble();
        windspeed = in.readDouble();
        winddegree = in.readInt();
        cloudiness = in.readInt();
        snowvolume = in.readDouble();
        rainvolume = in.readDouble();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCurrentIcon() {
        return currentIcon;
    }

    public void setCurrentIcon(String currentIcon) {
        this.currentIcon = currentIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public double getTemperatureAtMorning() {
        return temperatureAtMorning;
    }

    public void setTemperatureAtMorning(double temperatureAtMorning) {
        this.temperatureAtMorning = temperatureAtMorning;
    }

    public double getTemperatureAtDay() {
        return temperatureAtDay;
    }

    public void setTemperatureAtDay(double temperatureAtDay) {
        this.temperatureAtDay = temperatureAtDay;
    }

    public double getTemperatureAtEvening() {
        return temperatureAtEvening;
    }

    public void setTemperatureAtEvening(double temperatureAtEvening) {
        this.temperatureAtEvening = temperatureAtEvening;
    }

    public double getTemperatureAtNight() {
        return temperatureAtNight;
    }

    public void setTemperatureAtNight(double temperatureAtNight) {
        this.temperatureAtNight = temperatureAtNight;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public int getWinddegree() {
        return winddegree;
    }

    public void setWinddegree(int winddegree) {
        this.winddegree = winddegree;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
    }

    public double getSnowvolume() {
        return snowvolume;
    }

    public void setSnowvolume(double snowvolume) {
        this.snowvolume = snowvolume;
    }

    public double getRainvolume() {
        return rainvolume;
    }

    public void setRainvolume(double rainvolume) {
        this.rainvolume = rainvolume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
        parcel.writeString(countrycode);
        parcel.writeString(currentIcon);
        parcel.writeString(description);
        parcel.writeDouble(currentTemperature);
        parcel.writeDouble(temperatureAtMorning);
        parcel.writeDouble(temperatureAtDay);
        parcel.writeDouble(temperatureAtEvening);
        parcel.writeDouble(temperatureAtNight);
        parcel.writeDouble(temperatureMin);
        parcel.writeDouble(temperatureMax);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(pressure);
        parcel.writeDouble(humidity);
        parcel.writeDouble(windspeed);
        parcel.writeInt(winddegree);
        parcel.writeInt(cloudiness);
        parcel.writeDouble(snowvolume);
        parcel.writeDouble(rainvolume);
    }

    public static final Parcelable.Creator<WeatherEntry> CREATOR
            = new Parcelable.Creator<WeatherEntry>() {
        @Override
        public WeatherEntry createFromParcel(Parcel in) {
            return new WeatherEntry(in);
        }

        @Override
        public WeatherEntry[] newArray(int size) {
            return new WeatherEntry[size];
        }
    };
    
    public boolean equals(WeatherEntry we){
        if (we != null ||
            this.location.equals(we.getLocation()) ||
            this.countrycode.equals(we.getCountrycode()) ||
            this.currentIcon.equals(we.getCurrentIcon()) ||
            this.description.equals(we.getDescription()) ||
            this.currentTemperature == we.getCurrentTemperature() ||
            this.temperatureAtMorning == we.getTemperatureAtMorning () ||
            this.temperatureAtDay == we.getTemperatureAtDay() ||
            this.temperatureAtEvening == we.getTemperatureAtEvening() ||
            this.temperatureAtNight == we.getTemperatureAtNight() ||
            this.temperatureMin == we.getTemperatureMin() ||
            this.temperatureMax == we.getTemperatureMax() ||
            this.latitude == we.getLatitude() ||
            this.longitude == we.getLongitude() ||
            this.pressure == we.getPressure() ||
            this.humidity == we.getHumidity() ||
            this.windspeed == we.getWindspeed() ||
            this.winddegree == we.getWinddegree() ||
            this.cloudiness == we.getCloudiness() ||
            this.snowvolume == we.getSnowvolume() ||
            this.rainvolume == we.getRainvolume())
                return true;
        else return false;
    }
}
