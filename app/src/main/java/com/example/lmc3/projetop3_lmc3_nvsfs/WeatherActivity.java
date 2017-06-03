package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class WeatherActivity extends AppCompatActivity {
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
    }
}
