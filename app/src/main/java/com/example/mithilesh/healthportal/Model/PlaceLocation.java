package com.example.mithilesh.healthportal.Model;

import java.util.HashMap;
import java.util.Map;

public class PlaceLocation {
    private Double lat,lon;

    public PlaceLocation(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("lat",lat);
        map.put("lon",lon);
        return map;
    }


    @Override
    public String toString() {
        return lat+","+lon;
    }
}
