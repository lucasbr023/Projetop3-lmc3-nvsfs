package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navBar = (BottomNavigationView) findViewById(R.id.navBot);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_weather:
                        //todo
                        Log.d("P3", "WEATHER");
                        return true;
                    case R.id.id_currency:
                        //todo
                        Log.d("P3", "CURRENCY");
                        return true;
                    case R.id.id_map:
                        Log.d("P3", "MAP");
                        //todo
                        return true;
                    default:
                        return true;
                }
            }
        });
    }


}
