package org.gemafrzen.meinwetter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.gemafrzen.meinwetter.R;
import org.gemafrzen.meinwetter.weatherdata.WeatherAtLocation;
import org.gemafrzen.meinwetter.weatherdata.WeatherEntry;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private WeatherAtLocation currentWeather;
    private String location;
    private String appid;

    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location Parameter 1.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String location) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, location);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getString(ARG_PARAM1);
            currentWeather = new WeatherAtLocation(location);
        }
    }

    public String getLocation(){
        return this.location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mListener.onFragmentInteraction(Uri.parse(location));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void updateWidgets(WeatherAtLocation updatedWeather){
        if(updatedWeather.getLocation().equals(this.location)){
            currentWeather = updatedWeather;
            setWidgets();
        }
    }

    public void setWidgets(){
        View view = getView();

        WeatherEntry entry = currentWeather.getCurrentWeather();
        if(entry == null) return;

        TextView textview_temperature = (TextView) view.findViewById(R.id.text_temperature);
        TextView textview_location = (TextView) view.findViewById(R.id.text_location);
        TextView textview_cloudiness = (TextView) view.findViewById(R.id.text_cloudiness);
        TextView textview_condition = (TextView) view.findViewById(R.id.text_condition);
        TextView textview_coordinates = (TextView) view.findViewById(R.id.text_coordinates);
        TextView textview_humidity = (TextView) view.findViewById(R.id.text_humidity);
        TextView textview_pressure = (TextView) view.findViewById(R.id.text_pressure);
        TextView textview_rain_snow_volume = (TextView) view.findViewById(R.id.text_rain_snow_volume);
        TextView textview_wind = (TextView) view.findViewById(R.id.text_wind);
        TextView lbl_rain_snow_volume = (TextView) view.findViewById(R.id.lbl_rain_snow_volume);
        ImageView iconWeather = (ImageView) view.findViewById(R.id.image_weather_icon);

        textview_temperature.setText(entry.getCurrentTemperature() + "° C");
        textview_location.setText(entry.getLocation());
        textview_cloudiness.setText(entry.getCloudiness() + "%");
        textview_condition.setText(entry.getDescription());
        textview_coordinates.setText(entry.getLatitude() + ", " + entry.getLongitude());
        textview_humidity.setText(entry.getHumidity() + "%");
        textview_pressure.setText(entry.getPressure() + " hPa");
        textview_wind.setText(entry.getWindspeed() + "; " + entry.getWinddegree());


        if(entry.getRainvolume() == 0.0 || entry.getSnowvolume() == 0.0){
            textview_rain_snow_volume.setVisibility(View.INVISIBLE);
            lbl_rain_snow_volume.setVisibility(View.INVISIBLE);
        }
        else {
            String text_volume = "";

            if(entry.getRainvolume() > 0.0) text_volume += "" + entry.getRainvolume() ;
            if(entry.getSnowvolume() > 0.0) text_volume = text_volume + ((text_volume.equals("")) ? "" : "; ") + entry.getSnowvolume();

            textview_rain_snow_volume.setText(text_volume);
            textview_rain_snow_volume.setVisibility(View.VISIBLE);
            lbl_rain_snow_volume.setVisibility(View.VISIBLE);
        }

        int id = getResources().getIdentifier("weather" + entry.getCurrentIcon() , "drawable", getContext().getPackageName());
        iconWeather.setImageResource(id);
    }
}
