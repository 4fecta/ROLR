package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ProgressBar mProgressBar;
    private TextView mLoadingText;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();
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

    }
}