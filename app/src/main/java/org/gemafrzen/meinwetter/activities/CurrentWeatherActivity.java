package org.gemafrzen.meinwetter.activities;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.gemafrzen.meinwetter.R;
import org.gemafrzen.meinwetter.fragments.TabPagerAdapter;
import org.gemafrzen.meinwetter.weatherdata.WeatherAtLocation;
import org.gemafrzen.meinwetter.fragments.WeatherFragment;
import org.gemafrzen.meinwetter.service.CurrentWeatherService;

import java.util.ArrayList;

public class CurrentWeatherActivity extends AppCompatActivity implements WeatherFragment.OnFragmentInteractionListener{
    // TODO end requests more than 1 time per 10 minutes from one device/one API key. Normally the weather is not changing so frequentl
    static final int PICK_LOCATION_REQUEST = 1;  // The request code
    private String TAG = CurrentWeatherActivity.class.getSimpleName();
    private ViewPager mPager;
    private TabPagerAdapter mPagerAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent){
        String intentLocation = intent.getStringExtra(CurrentWeatherService.EXTENDED_DATA_LOCATION);
        for(int i = 0; i < mPagerAdapter.getCount(); i++){

            Fragment frg = mPagerAdapter.getRegisteredFragment(i);

            if(frg instanceof WeatherFragment &&
                    intentLocation.equals(((WeatherFragment)frg).getLocation())){
                ((WeatherFragment) frg).updateWidgets((WeatherAtLocation)
                        intent.getParcelableExtra(CurrentWeatherService.EXTENDED_DATA_WEATHER));
            }
        }
    }


    private void subscribeToService(){
        ArrayList<String> locations = new ArrayList<>();

        for(int i = 0; i < mPagerAdapter.getCount(); i++){

            Fragment frg = mPagerAdapter.getRegisteredFragment(i);
            if(frg instanceof WeatherFragment){
                locations.add(((WeatherFragment) frg).getLocation());
            }
        }

        Intent mServiceIntent = new Intent(this, CurrentWeatherService.class);
        mServiceIntent.putExtra(CurrentWeatherService.WEATHER_SERVICE_EXTRA_STRING_ARRAY, locations.toArray());
        this.startService(mServiceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_weather);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mPagerAdapter = new TabPagerAdapter(getFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        IntentFilter filter = new IntentFilter(CurrentWeatherService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.broadcastReceiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openChooseLocationView(View view) {
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        startActivityForResult(intent, CurrentWeatherActivity.PICK_LOCATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK){
                //new GetWeather(data.getStringExtra("result")).execute();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        Intent mServiceIntent = new Intent(this, CurrentWeatherService.class);
        mServiceIntent.putExtra(CurrentWeatherService.WEATHER_SERVICE_EXTRA_STRING_ARRAY, new String[] {uri.toString()});
        this.startService(mServiceIntent);
    }
}
