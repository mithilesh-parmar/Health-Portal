package com.example.mithilesh.healthportal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class TestActivity extends AppCompatActivity implements LocationListener{

    private static final String TAG = "TestActivity";
    private LocationManager locationManager;
    private String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_CODE = 2413;
    private String provider;

    private Location location;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,true);
        Log.i(TAG, "onCreate: Provider: "+provider);
        if (hasLocationPermission()){
            location = locationManager.getLastKnownLocation(provider);
        }

        if (location == null){
            Log.i(TAG, "onCreate: Location is null");
            locationManager.requestLocationUpdates
                    (provider, 0, 0, this);
        }else {
            Log.i(TAG, "onCreate: Location: "+location);
            onLocationChanged(location);
        }
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    protected void onResume() {
        super.onResume();
        if (hasLocationPermission())
        locationManager.requestLocationUpdates
                (provider, 0, 0, this);
        else requestPermissions(LOCATION_PERMISSIONS,REQUEST_LOCATION_CODE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private boolean hasLocationPermission(){
        int result = ContextCompat.checkSelfPermission(this,LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:{
                if (hasLocationPermission()){

                }
            }
            default:super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.i(TAG, "onLocationChanged: "+lat);
        Log.i(TAG, "onLocationChanged: "+lng);

        Toast.makeText(this, "Location: "+location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "onStatusChanged: "+provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "onProviderEnabled: "+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled: "+provider);
    }
}


