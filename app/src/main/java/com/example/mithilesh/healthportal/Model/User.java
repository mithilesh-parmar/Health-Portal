package com.example.mithilesh.healthportal.Model;

import android.location.Location;

import java.util.List;
import java.util.UUID;

public class User {

    private static User user ;
    private String displayName;
    private String uid;
    private String email;
    private String phoneNumber;
    private Location currentLocation;
    private String geoLocation;

    public static User getInstance(){

        if (user == null){
            synchronized (User.class){
                user = new User();
            }
        }
        return user;
    }

    public static User getInstance(String displayName, String uid, String email, String phoneNumber){
        if (user == null){
            synchronized (User.class){
                user = new User(displayName,uid,email,phoneNumber);
            }
        }
        return user;
    }

    public User(String displayName, String uid, String email, String phoneNumber) {
        this.displayName = displayName;
        this.uid = uid;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User() {

    }

    public static User getUser() {
        return user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public static void setUser(User user) {
        User.user = user;
    }
}
