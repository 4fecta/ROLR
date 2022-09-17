package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.widget.TextView;

import java.util.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import com.google.android.gms.maps.model.MarkerOptions;
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ProgressBar mProgressBar;
    private TextView mLoadingText;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    Leaderboard leaderboard = new Leaderboard();
    Race race = new Race();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //map stuff*********************************************************************************
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        //bottom nav bar stuff**********************************************************************
        bottomNavigationView  = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();

        //BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.leaderboard);
        //badgeDrawable.setVisible(true);
        //badgeDrawable.setNumber(8);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
                        return true;
                    case R.id.race:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,race).commit();
                        return true;
                    case R.id.leaderboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,leaderboard).commit();
                        return true;
                }

                return false;
            }
        });

        //progress bar stuff**************************************************************
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mLoadingText = (TextView) findViewById(R.id.ProgressNumber);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 68){
                    mProgressStatus++;
                    android.os.SystemClock.sleep(20);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng chicago = new LatLng(41.881832, -87.623177);
        /*
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        */
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(chicago));
        addHeatMap(googleMap);
    }
    private void addHeatMap(GoogleMap googleMap) {
        List<LatLng> latLngs = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            latLngs = readItems(R.raw.location_data);
        } catch (JSONException e) {
           // Toast.makeText(context, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(latLngs).radius(35)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        Handler handler = new Handler();
        Runnable r = new Runnable()
        {
            @Override
            public void run(){
                TileOverlay overlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
             }
        };
        handler.postDelayed(r, 10000);

    }

    private List<LatLng> readItems(@RawRes int resource) throws JSONException {
        List<LatLng> result = new ArrayList<>();
        InputStream inputStream = getApplicationContext().getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            result.add(new LatLng(lat, lng));
        }
        return result;
    }


}