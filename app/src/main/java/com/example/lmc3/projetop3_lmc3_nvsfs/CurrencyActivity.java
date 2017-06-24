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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.R.attr.format;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class CurrencyActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_currency);

        spinnerLocal = (Spinner) findViewById(R.id.id_currencies_local);
        spinnerDestination = (Spinner) findViewById(R.id.id_currencies_destination);
        quantity = (EditText) findViewById(R.id.id_quantity);
        calculate = (Button) findViewById(R.id.btn_result);
        result = (TextView) findViewById(R.id.id_result);

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navBot);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_weather:
                        Intent intentWeather = new Intent(CurrencyActivity.this, WeatherActivity.class);
                        intentWeather.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentWeather);
                        return true;
                    case R.id.id_currency:
                        return true;
                    case R.id.id_map:
                        Intent intentMap = new Intent(CurrencyActivity.this, MainActivity.class);
                        intentMap.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentMap);
                        return true;
                }
                return true;
            }
        });

        ArrayAdapter<String> adapterLocal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CURRENCY_LIST);
        adapterLocal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(adapterLocal);

        ArrayAdapter<String> adapterDestination = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CURRENCY_LIST);
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
    }
}
