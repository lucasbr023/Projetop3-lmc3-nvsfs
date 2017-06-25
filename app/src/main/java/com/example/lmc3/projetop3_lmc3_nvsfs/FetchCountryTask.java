package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class FetchCountryTask extends AsyncTask<String, Void, String> {
    String json = "";
    String currentCoin = "";
    @Override
    protected String doInBackground(String... params) {
        String from = "";
        try {
            from = params[0];
            json = getCurrencyCoinResultFromContry(from);
            currentCoin = getCurrencyCoinFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return currentCoin;
    }

    private String getCurrencyCoinResultFromContry(String from) throws IOException {
        String restcontriesJsonString = null;
        if (!from.isEmpty()) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String feed = "https://restcountries.eu/rest/v2/alpha/" + from;

            try {
                //obter dados da moeda
                Uri uri = Uri.parse(feed.toString());
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection)url.openConnection() ;
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                restcontriesJsonString = buffer.toString();

            }catch (IOException e) {
                return null;
            }
        }
        return restcontriesJsonString;
    }

    private String getCurrencyCoinFromJson(String json) throws JSONException {
        final String JSON_CURRENCIES = "currencies";
        final String JSON_CODE = "code";
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(JSON_CURRENCIES);
        String currencyCoin = jsonArray.getJSONObject(0).getString(JSON_CODE);
        return currencyCoin;
    }
}
