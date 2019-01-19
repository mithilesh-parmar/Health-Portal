package com.example.mithilesh.healthportal.Model;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

public class Place {
    private String id;
    private String name;
    private String placeId;
    private Double rating;
    private int userRatingsTotal;
    private String vicinity;
    private PlaceLocation location;
    private boolean openNow;
    private String iconUrl;
    private PlaceDetail placeDetail;
//    private PlacePhotos placePhotos;


    public Place() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(int userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public PlaceLocation getLocation() {
        return location;
    }

    public void setLocation(Double lat,Double lon) {

        this.location = new PlaceLocation(lat,lon);
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public PlaceDetail getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(PlaceDetail placeDetail) {
        this.placeDetail = placeDetail;
    }

    @Override
    public String toString() {

        return "\nName: "+name +"\nPlace Id"+placeId+"\nVicinity: "+vicinity;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("placeId",placeId);
        map.put("rating",rating);
        map.put("userRatingsTotal",userRatingsTotal);
        map.put("vicinity",vicinity);
        map.put("location",location);
        map.put("openNow",openNow);
        map.put("iconUrl",iconUrl);
        map.put("placeDetail",placeDetail.toMap());

        return map;
    }



  public   class PlaceLocation {
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



}
