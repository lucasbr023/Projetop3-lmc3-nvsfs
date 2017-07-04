package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.net.Uri;
import android.os.AsyncTask;

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

public class FetchMoneyTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String json = "";
        String to = "";
        String quantityString = "";
        try {
            to = params[0];
            json = getCurrencyResultFromTo(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return getResultFromFixerJson(json, to);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return to.equals("BRL") ? "100" : "Valor não encontrado :(";
    }

    private String getCurrencyResultFromTo(String to) throws IOException {
        String fixerJsonString = null;
        if (!to.isEmpty()) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String feed = "http://api.fixer.io/latest?base=BRL&symbols=" + to;

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
                fixerJsonString = buffer.toString();

            }catch (IOException e) {
                return null;
            }
        }
        return fixerJsonString;
    }

    private String getResultFromFixerJson(String json, String to) throws JSONException {
        final String JSON_RATES = "rates";
        final String JSON_BASE = "base";
        final String JSON_DATE = "date";
        final String JSON_DESTINATION = to;

        JSONObject jsonObject = new JSONObject(json);
        JSONObject rates = jsonObject.getJSONObject(JSON_RATES);

        String base = jsonObject.getString(JSON_BASE);
        double result = rates.getDouble(to);

        Double resultado = result * 100;
        return resultado.toString();
    }

//    public static double round(double value) {
//        if(Locale.getDefault() == Locale.ENGLISH) {
//            DecimalFormat df = new DecimalFormat("#.##");
//            return Double.valueOf(df.format(value));
//        } else {
//            DecimalFormat df = new DecimalFormat("#,##");
//            return Double.valueOf(df.format(value));
//        }
//    }
}
