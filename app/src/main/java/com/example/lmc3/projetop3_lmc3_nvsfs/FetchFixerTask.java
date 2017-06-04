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

public class FetchFixerTask extends AsyncTask<String, Void, Currency> {
    @Override
    protected Currency doInBackground(String... params) {
        String json = "";
        String to = "";
        String quantityString = "";
        try {
            String from = params[0];
            to = params[1];
            quantityString = params[2];
            json = getCurrencyResultFromTo(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return getResultFromFixerJson(json, to, quantityString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCurrencyResultFromTo(String from, String to) throws IOException {
        String fixerJsonString = null;
        if (!from.isEmpty() && !to.isEmpty()) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String feed = "http://api.fixer.io/latest?base=" + from + "&symbols=" + to;

            try {

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
                fixerJsonString = buffer.toString();

            }catch (IOException e) {
                return null;
            }
        }
        return fixerJsonString;
    }

    private Currency getResultFromFixerJson(String json, String to,String quantity) throws JSONException {
        final String JSON_RATES = "rates";
        final String JSON_BASE = "base";
        final String JSON_DATE = "date";
        final String JSON_DESTINATION = to;

        JSONObject jsonObject = new JSONObject(json);
        JSONObject rates = jsonObject.getJSONObject(JSON_RATES);

        String base = jsonObject.getString(JSON_BASE);
        double result = rates.getDouble(to);
        double quantityDouble = 0;
        if(quantity != null){
            quantityDouble = Double.parseDouble(quantity);
        }
        return new Currency(quantityDouble,base,to, result);

    }
}
