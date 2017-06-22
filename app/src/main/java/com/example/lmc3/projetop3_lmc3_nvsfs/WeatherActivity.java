package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class WeatherActivity extends AppCompatActivity {
    StringBuilder urlHighAndLow;

    String nameCity;
    Double temperatura = 0.0;
    Double temperaturaMax = 0.0;
    Double temperaturaMin = 0.0;
    String description;
    String idCity;
    final int NUMDAYS = 1;

    TextView textTemperature;
    TextView textMinTemperature;
    TextView textMaxTemperature;

    Double latitude = 0.0;
    Double longitude = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navBot);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_weather:
                        return true;
                    case R.id.id_currency:
                        Intent intentCurrency = new Intent(WeatherActivity.this, CurrencyActivity.class);
                        intentCurrency.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentCurrency);
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

        Intent intent = getIntent();

        latitude  = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude",0);

        HighAndLowTask task = new HighAndLowTask();
        task.execute(latitude.toString(),longitude.toString());

        ((TextView) findViewById(R.id.city_name)).setText(nameCity);

        textTemperature = (TextView) findViewById(R.id.temp_atual);
        textTemperature.setText(temperaturaMax.toString());
        textMaxTemperature = (TextView) findViewById(R.id.temp_max);
        textMinTemperature = (TextView) findViewById(R.id.temp_min);

        ((TextView) findViewById(R.id.temp_max)).setText(temperaturaMax.toString());
        ((TextView) findViewById(R.id.temp_min)).setText(temperaturaMin.toString());
    }
    public class HighAndLowTask extends AsyncTask<String, Void, Location[]> {


        private final String LOG_TAG = HighAndLowTask.class.getSimpleName();

        private double formatKelvinToCelsius(double temperatura) {
            double tempToCelsius = (Math.round(temperatura) - 273.15);

            return tempToCelsius;
        }

        private Location[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            final String JSONLIST = "list";
//            final String JSONMAIN = "main";
            final String JSONCITY = "city";
            final String JSONNAME = "name";
            final String JSONTEMP = "temp";
            final String JSONDAY = "day";
            final String JSONTEMPMAX = "max";
            final String JSONTEMPMIN = "min";


            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONObject jCity = forecastJson.getJSONObject(JSONCITY);
            nameCity = jCity.getString(JSONNAME);
            JSONArray weatherArray = forecastJson.getJSONArray(JSONLIST);

            Location[] locationResult = new Location[numDays];

            for (int i = 0; i < weatherArray.length(); i++) {


                JSONObject dayForecast = weatherArray.getJSONObject(i);

                JSONObject jArr = dayForecast.getJSONObject(JSONTEMP);

                temperatura = jArr.getDouble(JSONDAY);
                temperaturaMin = jArr.getDouble(JSONTEMPMIN);
                temperaturaMax = jArr.getDouble(JSONTEMPMAX);

                temperaturaMax = round(formatKelvinToCelsius(temperaturaMax));
                temperaturaMin = round(formatKelvinToCelsius(temperaturaMin));
                temperatura = round(formatKelvinToCelsius(temperatura));

                locationResult[0] = new Location(
                        idCity,
                        "",
                        "",
                        nameCity,
                        temperatura,
                        temperaturaMin,
                        temperaturaMax,
                        "");
            }

            return locationResult;

        }

        @Override
        protected Location[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {
                urlHighAndLow = new StringBuilder();

                urlHighAndLow.append("http://api.openweathermap.org/data/2.5/forecast/daily?lat=");
                urlHighAndLow.append(params[0]);
                urlHighAndLow.append("&lon=");
                urlHighAndLow.append(params[1]);
                urlHighAndLow.append("&cnt=1&APPID=d5e5e7bf0036493556227d17d41219bd");

                Uri builtUri = Uri.parse(urlHighAndLow.toString());

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr, NUMDAYS);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Location[] result) {
            super.onPostExecute(result);
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    ((TextView) findViewById(R.id.city_name)).setText(result[i].city);
                    textTemperature.setText(temperaturaMax.toString());
                    textMaxTemperature = (TextView) findViewById(R.id.temp_max);
                    textMinTemperature = (TextView) findViewById(R.id.temp_min);

                    ((TextView) findViewById(R.id.temp_max)).setText(temperaturaMax.toString());
                    ((TextView) findViewById(R.id.temp_min)).setText(temperaturaMin.toString());
                }
            }
        }
    }
    public static double round(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(value));
    }
}

