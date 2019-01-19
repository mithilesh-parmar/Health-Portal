package com.example.mithilesh.healthportal.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceDetail implements Serializable {

    private static final String TAG = "PlaceDetail";

    private String formattedAddress;
    private String formattedPhoneNumber;
    private String id;
    private String internationalPhoneNumber;
    private String name;
    private String placeId;
    private int rating;
    private String url;
    private int userRatingsTotal;
    private String vicinity;
    private String website;
    private boolean isOpen;
    private List<String> openWeekdayText;
    private List<PlacePhoto> photos;
    private List<PlaceReviews> placeReviews;



    public PlaceDetail(String formattedAddress, String formattedPhoneNumber, String id, String internationalPhoneNumber, String name, String placeId, int rating, String url, int userRatingsTotal, String vicinity, String website, boolean isOpen, List<String> openWeekdayText, List<PlacePhoto> photos, List<PlaceReviews> placeReviews) {
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.id = id;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.name = name;
        this.placeId = placeId;
        this.rating = rating;
        this.url = url;
        this.userRatingsTotal = userRatingsTotal;
        this.vicinity = vicinity;
        this.website = website;
        this.isOpen = isOpen;
        this.openWeekdayText = openWeekdayText;
        this.photos = photos;
        this.placeReviews = placeReviews;
    }

    public PlaceDetail() {
        openWeekdayText = new ArrayList<>();
        photos = new ArrayList<>();
        placeReviews = new ArrayList<>();

    }



    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getOpenWeekdayText() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < openWeekdayText.size(); i++) {
            String[] line = openWeekdayText.get(i).split(":");

            stringBuilder.append(line[0]).append("\t").append(line[1]).append("\n");
        }
        return stringBuilder.toString();
    }

    public void setOpenWeekdayText(List<String> openWeekdayText) {
        this.openWeekdayText = openWeekdayText;
    }

    public List<PlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PlacePhoto> photos) {
        this.photos = photos;
    }

    public List<PlaceReviews> getPlaceReviews() {
        Log.i(TAG, "getPlaceReviews: Reviews size: "+placeReviews.size());
        return placeReviews;
    }

    public void setPlaceReviews(List<PlaceReviews> placeReviews) {
        Log.i(TAG, "setPlaceReviews: Reviews Size: "+placeReviews.size());
        this.placeReviews = placeReviews;
    }


    @Override
    public String toString() {
        return "\nName: "+name+"\n" +vicinity;
    }


    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("formattedPhoneNumber",formattedPhoneNumber);
        map.put("internationalPhoneNumber",internationalPhoneNumber);
        map.put("url",url);
        map.put("website",website);
        map.put("openWeekdayText",openWeekdayText);
//        map.put("photos",photos.toMap());
//        map.put("placeReviews",placeReviews.toMap());

        return map;
    }
}
