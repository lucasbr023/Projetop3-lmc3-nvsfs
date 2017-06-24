package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lmc3.projetop3_lmc3_nvsfs.models.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nataliafrs on 24/06/17.
 */

public class PlacesHelper extends SQLiteOpenHelper {


    final static String TABELA_PLACES = "TABLE_PALCES";
    final static String COLUNA_NAME = "NAME";
    final static String COLUNA_LAT = "LAT";
    final static String COLUNA_LONG = "LONG";
    final static String _ID = "_id";

    final private static String CREATE_TABLE = "CREATE TABLE "+ TABELA_PLACES +" " +
            "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUNA_NAME + " TEXT  NULL, " +
            COLUNA_LAT + " DOUBLE NOT NULL, " +
            COLUNA_LONG + " DOUBLE NOT NULL)";


    final private static String DATABASE_NAME = "places_db";
    final private static Integer VERSION = 1;
    final private Context mContext;



    public PlacesHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, VERSION);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void incluir(Double latitude, Double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUNA_LAT, latitude);
        contentValues.put(COLUNA_LONG, longitude);
        db.insert(TABELA_PLACES, null, contentValues);
        db.close();
    }

    public List<Place> obter() {


        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABELA_PLACES;

        Cursor cursor = db.rawQuery(sql, null);
        List<Place> items = new ArrayList<Place>();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(_ID));
            String nome = cursor.getString(cursor.getColumnIndex(COLUNA_NAME));
            Double latitude = cursor.getDouble(cursor.getColumnIndex(COLUNA_LAT));
            Double longitude = cursor.getDouble(cursor.getColumnIndex(COLUNA_LONG));
            Place place = new Place (id, nome, latitude, longitude);
            items.add(place);
        }
        cursor.close();
        db.close();
        return items;
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(DATABASE_NAME);
    }

}


