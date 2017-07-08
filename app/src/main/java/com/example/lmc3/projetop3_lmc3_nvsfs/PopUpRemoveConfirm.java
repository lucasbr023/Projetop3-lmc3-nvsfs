package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by lmc3 on 08/07/2017.
 */

public class PopUpRemoveConfirm extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupremoveconfirm);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widht = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(widht*0.5), (int)(height*0.2));


        Intent intent = getIntent();

        final String cityName =  intent.getStringExtra("cityName");

        Button confirm = (Button)findViewById(R.id.removeConfirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCity(cityName);
                startActivity(new Intent(PopUpRemoveConfirm.this, MainActivity.class));
            }
        });

    }

    private void removeCity(String cityName){
        PlacesHelper db = new PlacesHelper(getApplicationContext());
        db.deleteCity(cityName);
    }
}
