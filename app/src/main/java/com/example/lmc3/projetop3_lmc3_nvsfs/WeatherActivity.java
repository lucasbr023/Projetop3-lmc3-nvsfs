package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import static com.example.lmc3.projetop3_lmc3_nvsfs.R.layout.activity_weather;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class WeatherActivity extends AppCompatActivity {


    public final String[] CURRENCY_LIST = {
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY",
            "CZK", "DKK", "EUR", "GBP", "HKD", "HRK",
            "HUF", "IDR", "ILS", "INR", "JPY", "KRW",
            "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
            "RON", "RUB", "SEK", "SGD", "THB", "TRY",
            "USD", "ZAR"
    };

    Spinner spinnerLocal;
    Spinner spinnerDestination;
    EditText quantity;
    Button calculate;
    TextView result;
    Button invert;

    String country;
    String kindOfMoney;

    TextView textTemperature;
    TextView textMinTemperature;
    TextView textMaxTemperature;
    TextView valorMoeda;
    TextView local;
    ImageView imageView;


    Double latitude = 0.0;
    Double longitude = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_weather);

        spinnerLocal = (Spinner) findViewById(R.id.id_currencies_local);
        spinnerDestination = (Spinner) findViewById(R.id.id_currencies_destination);
        quantity = (EditText) findViewById(R.id.id_quantity);
        calculate = (Button) findViewById(R.id.btn_result);
        result = (TextView) findViewById(R.id.id_result);
        invert =(Button) findViewById(R.id.invert);


        local = (TextView) findViewById(R.id.city_name);
        textTemperature = (TextView) findViewById(R.id.temp_atual);
        textMaxTemperature = (TextView) findViewById(R.id.temp_max);
        textMinTemperature = (TextView) findViewById(R.id.temp_min);
        valorMoeda = (TextView) findViewById(R.id. actual_currency);
        imageView = (ImageView) findViewById(R.id.imageView);

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navBot);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_weather:
                        return true;
                    case R.id.id_map:
                        Intent intentMap = new Intent(WeatherActivity.this, MainActivity.class);
                        intentMap.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentMap);
                        return true;
                }
                return true;
            }
        });

        final ArrayAdapter<String> adapterLocal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CURRENCY_LIST);
        adapterLocal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(adapterLocal);

        final ArrayAdapter<String> adapterDestination = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CURRENCY_LIST);
        adapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDestination.setAdapter(adapterDestination);

        calculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FetchFixerTask task = new FetchFixerTask();

                String local = spinnerLocal.getSelectedItem().toString();
                String destination = spinnerDestination.getSelectedItem().toString();
                String quantityString = quantity.getText().toString();

                try {
                    Currency currency = task.execute(local, destination, quantityString).get();
                    result.setText(String.valueOf(currency.getResult()));
                    // String teste = countryTask.execute("JP").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchFixerTask task = new FetchFixerTask();

                String local1 = spinnerLocal.getSelectedItem().toString();
                String destination1 = spinnerDestination.getSelectedItem().toString();
                String quantityString = quantity.getText().toString();

                spinnerLocal.setSelection(adapterLocal.getPosition(destination1));
                spinnerDestination.setSelection(adapterDestination.getPosition(local1));

                try {
                    Currency currency = task.execute(local1, destination1, quantityString).get();
                    result.setText(String.valueOf(currency.getResult()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });

        Intent intent = getIntent();

        latitude  = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude",0);

        if (latitude == 0 || longitude == 0) {

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            android.location.Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            }
        }

        HighAndLowTask task = new HighAndLowTask();
        try {
            Location[] locations = task.execute(latitude.toString(),longitude.toString()).get();
            for (int i = 0 ; i< locations.length; i++){
                local.setText(locations[i].city);
                country = locations[i].country;
                textTemperature.setText(locations[i].temperature.toString() + "°C");
                textMaxTemperature.setText(locations[i].maxTemperature.toString()+ "°C");
                textMinTemperature.setText(locations[i].minTemperature.toString()+ "°C");
                switch (locations[i].weather){
                    case "Clear":
                        imageView.setImageResource(R.drawable.ic_sunny);
                        break;
                    case "Clouds":
                        imageView.setImageResource(R.drawable.cloud);
                        break;
                    case "Rain":
                        imageView.setImageResource(R.drawable.rain);
                        break;
                    default:
                        imageView.setImageResource(R.drawable.cloud);
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        getKindOfMoneyByLocation();
        getCurrencyByKindOfMoney();

    }

    private void getKindOfMoneyByLocation() {
        FetchCountryTask countryTask = new FetchCountryTask();
        try {
            String moeda = countryTask.execute(country).get();
            kindOfMoney = moeda;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void getCurrencyByKindOfMoney() {
        FetchMoneyTask moneyTask = new FetchMoneyTask();
        String value = "";
        try {
            value = moneyTask.execute(kindOfMoney).get();
            valorMoeda.setText(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

