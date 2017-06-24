package com.example.lmc3.projetop3_lmc3_nvsfs.models;

/**
 * Created by nataliafrs on 24/06/17.
 */

public class Place {

    private final int id;
    private final String nome;
    private final Double latitude;
    private final Double longitude;

    public Place(int id, String nome, Double latitude, Double longitude) {
        this.id = id;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return nome;
    }
}
