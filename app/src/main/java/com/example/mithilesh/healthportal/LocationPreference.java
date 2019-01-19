package com.example.mithilesh.healthportal;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationPreference {

    private static final String TAG = "LocationPreference";
    private static final String PREF_LOCATION_PROVIDER = "LOCATION_PROVIDER";
    private static final String PREF_LOCATION_LATITUDE = "LATITUDE";
    private static final String PREF_LOCATION_LONGITUDE = "LONGITUDE";
    private static final String PREF_LOCATION_ACC = "ACC";
    private static final String PREF_LOCATION_ET = "ET";


    public static boolean containsLocation(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).contains(PREF_LOCATION_LATITUDE);
    }

    public static Location getLocationPref(Context context){
        Location location = new Location(LocationManager.GPS_PROVIDER);

        Double latitude = Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOCATION_LATITUDE,"0"));
        Double longitude = Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOCATION_LONGITUDE,"0"));
        int acc = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_LOCATION_ACC,0);
        String et = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOCATION_ET,"");
        String provider = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOCATION_PROVIDER,"");

        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setProvider(provider);

        Log.i(TAG, "getLocationPref: retriving from preference "+location);
        return location;
    }

    public static void setLocationPref(Context context,Location location){
        Log.i(TAG, "setLocationPref: "+location);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LOCATION_LATITUDE, String.valueOf(location.getLatitude()))
                .putString(PREF_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()))
                .putString(PREF_LOCATION_PROVIDER,location.getProvider())
                .apply();
    }

}
