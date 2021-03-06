package org.gemafrzen.meinwetter.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.gemafrzen.meinwetter.R;

import java.util.Arrays;
import java.util.LinkedList;

public class ChooseLocationActivity extends AppCompatActivity {

    private LinkedList<String> results;
    private ArrayAdapter<String> adapter;

    protected void setResult(String listitem){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",listitem);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_location);

        // TODO from DB
        if(results == null) results = new LinkedList<>(Arrays.asList("Wandlitz", "Berlin", "Stockholm", "Madrid"));

        adapter = new ArrayAdapter<String>(this, R.layout.activity_choose_location_listview, results);

        ListView listView = (ListView) findViewById(R.id.locationlist);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Object o = ((ListView)arg0).getItemAtPosition(position);
                String item = (String) adapter.getItem(position);

                setResult(item);
            }
        });
    }
}
