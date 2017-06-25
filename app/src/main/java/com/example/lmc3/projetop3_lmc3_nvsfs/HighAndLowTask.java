package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
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
import java.util.Locale;

public class HighAndLowTask extends AsyncTask<String, Void, Location[]> {

    StringBuilder urlHighAndLow;


    final String JSONLIST = "list";
    final String JSONCITY = "city";
    final String JSONNAME = "name";
    final String JSONTEMP = "temp";
    final String JSONDAY = "day";
    final String JSONTEMPMAX = "max";
    final String JSONTEMPMIN = "min";
    final String JSONCOUNTRY = "country";
    final String JSONWEATHER = "weather";
    final String JSONMAIN = "main";

    String nameCity;
    String weather;
    Double temperatura = 0.0;
    Double temperaturaMax = 0.0;
    Double temperaturaMin = 0.0;
    String country;
    String kindOfMoney;
    String idCity;
    final int NUMDAYS = 1;

    TextView textTemperature;
    TextView textMinTemperature;
    TextView textMaxTemperature;
    TextView valorMoeda;
    TextView local;
    TextView tWeather;
    ImageView imageView;

    Double latitude = 0.0;
    Double longitude = 0.0;

    private final String LOG_TAG = HighAndLowTask.class.getSimpleName();
    private double formatKelvinToCelsius(double temperatura) {
        double tempToCelsius = (Math.round(temperatura) - 273.15);
        return tempToCelsius;
    }

    private Location[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONObject jCity = forecastJson.getJSONObject(JSONCITY);
        nameCity = jCity.getString(JSONNAME);
        country = jCity.getString(JSONCOUNTRY);
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

            JSONArray jweather = dayForecast.getJSONArray(JSONWEATHER);
            weather = jweather.getJSONObject(0).getString(JSONMAIN);

            locationResult[0] = new Location(
                    idCity,
                    longitude.toString(),
                    latitude.toString(),
                    nameCity,
                    temperatura,
                    temperaturaMin,
                    temperaturaMax,
                    weather,
                    country);
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

    public static double round(double value) {

        if(Locale.getDefault() == Locale.ENGLISH) {
            DecimalFormat df = new DecimalFormat("#.##");
            return Double.valueOf(df.format(value));
        } else {
            DecimalFormat df = new DecimalFormat("#,##");
            return Double.valueOf(df.format(value));
        }
    }
}
