package org.gemafrzen.meinwetter;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by Erik on 21.05.2017.
 */

public class WeatherAtLocation implements Parcelable {

    private WeatherEntry currentWeather; //now
    private ArrayList<WeatherEntry> dayList;  //today tomorrow etc
    private String location = "";

    public WeatherAtLocation(String location){
        this.dayList = new ArrayList<>();
        this.location = location;
    }

    private WeatherAtLocation(Parcel in) {
        WeatherEntry[] parcelableDayList;
        location = in.readString();
        currentWeather = in.readParcelable(org.gemafrzen.meinwetter.WeatherEntry.class.getClassLoader());
        parcelableDayList = (WeatherEntry[]) in.readParcelableArray(org.gemafrzen.meinwetter.WeatherEntry.class.getClassLoader());

        this.dayList = new ArrayList<>();
        for(int i = 0; i < parcelableDayList.length; i++){
            addToDaylist(parcelableDayList[i]);
        }
    }

    public void addToDaylist(WeatherEntry newEntry){
        dayList.add(newEntry);
    }

    public void setToday(WeatherEntry newEntry){
        currentWeather = newEntry;
    }

    public boolean equals(WeatherAtLocation wal){
        if(!this.location.equals(wal.getLocation())) return false;

        if((currentWeather == null && wal.getCurrentWeather() != null) ||
                (currentWeather != null && wal.getCurrentWeather() == null) ||
                !this.currentWeather.equals(wal.getCurrentWeather())) return false;

        if(dayList.size() != wal.getForcastSize()) return false;

        for(int i = 0; i < dayList.size(); i++){
            if(!dayList.get(i).equals(wal.getForcast(i))) return false;
        }

        return true;
    }

    public String getLocation(){
        return location;
    }

    public WeatherEntry getCurrentWeather(){
        return this.currentWeather;
    }

    public WeatherEntry getForcast(int inDays){
        if(dayList == null) return null;
        return dayList.get(inDays);
    }

    public int getForcastSize(){
        return dayList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Parcelable[] parcelableDayList = new Parcelable[dayList.size()];
        for(int j = 0; j < dayList.size(); j++){
            parcelableDayList[j] = dayList.get(i);
        }

        parcel.writeString(location);
        parcel.writeParcelable(currentWeather, 0);
        parcel.writeParcelableArray(parcelableDayList, 0);
    }

    public static final Parcelable.Creator<WeatherAtLocation> CREATOR
            = new Parcelable.Creator<WeatherAtLocation>() {
        @Override
        public WeatherAtLocation createFromParcel(Parcel in) {
            return new WeatherAtLocation(in);
        }

        @Override
        public WeatherAtLocation[] newArray(int size) {
            return new WeatherAtLocation[size];
        }
    };

}
