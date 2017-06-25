package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lmc3.projetop3_lmc3_nvsfs.models.Place;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapLongClickListener {
    private GoogleMap map;
    private PlacesHelper placesHelper;
    SupportMapFragment mapa;
    public String name;
    public Double latitude;
    public Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesHelper = new PlacesHelper(this);

        mapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapa.getMapAsync(this);
        //Navbar
        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navBot);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_weather:
                        Intent intentWeather = new Intent(MainActivity.this, WeatherActivity.class);
                        intentWeather.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentWeather);
                        return true;
                    case R.id.id_currency:
                        Intent intentCurrency = new Intent(MainActivity.this, CurrencyActivity.class);
                        intentCurrency.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentCurrency);
                        return true;
                    case R.id.id_map:
                        return true;
                }
                return true;
            }
        });


//        PhoneUnlockedReceiver receiver = new PhoneUnlockedReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_USER_PRESENT);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(receiver, filter);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        List<Place> listPlaces =  placesHelper.obter();

        for (int i = 0; i< listPlaces.size(); i++){

            map.addMarker(new MarkerOptions().position(new LatLng(listPlaces.get(i).getLatitude(), listPlaces.get(i).getLongitude())).title(listPlaces.get(i).getNome()));
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                                             // Passing Location (lat and long) to the other activity)
                                             @Override
                                             public void onInfoWindowClick(Marker marker) {

                                                 Intent intentBundle = new Intent(MainActivity.this, WeatherActivity.class);
                                                 Bundle bundle = new Bundle();
                                                 bundle.putDouble("latitude", marker.getPosition().latitude);
                                                 bundle.putDouble("longitude", marker.getPosition().longitude);
                                                 intentBundle.putExtras(bundle);
                                                 startActivity(intentBundle);
                                             }
                                         }
        );

        map.setOnMapLongClickListener(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }



    @Override
    public void onMapLongClick(LatLng latLng) {
        Double l1 = latLng.latitude;
        Double l2 = latLng.longitude;

        latitude = latLng.latitude;
        longitude = latLng.longitude;

        PlacesHelper placesHelper = new PlacesHelper(getApplicationContext());
        HighAndLowTask highAndLowTask = new HighAndLowTask();
        try {
            com.example.lmc3.projetop3_lmc3_nvsfs.Location[] locations = highAndLowTask.execute(latitude.toString(), longitude.toString()).get();
            for (int i = 0; i < locations.length; i++){
                placesHelper.incluir(latitude, longitude, locations[i].city);
                map.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title(locations[i].city));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
