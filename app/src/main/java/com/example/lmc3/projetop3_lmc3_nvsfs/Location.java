package com.example.lmc3.projetop3_lmc3_nvsfs;

import java.io.Serializable;


public class Location implements Serializable {

    public String id;
    public String longitude;
    public String latitude;
    public String city;
    public Double temperature;
    public Double minTemperature;
    public Double maxTemperature;
    public String description;

    public Location(String id, String longitude, String latitude, String city, Double temperature, Double minTemperature, Double maxTemperature, String description) {
        this.id = id;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.description = description;
    }
}