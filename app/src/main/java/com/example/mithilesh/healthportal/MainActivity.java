package com.example.mithilesh.healthportal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mithilesh.healthportal.Fragments.HomeFragment;
import com.example.mithilesh.healthportal.Model.User;
import com.example.mithilesh.healthportal.databinding.ActivityMainBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.location.LocationListener {

    private static final String TAG = "MainActivity";
    private HomeFragment homeFragment = HomeFragment.getInstance();
    private static final int REQUEST_ERROR = 12321;
    private LocationManager locationManager;
    private String provider;
    private Location currentLocation;
    private Location lastKnownLocation;
    private String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_CODE = 2413;


    @SuppressLint({"NewApi", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate: Initializing location Manager");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,true);
        Log.i(TAG, "onCreate: Provider: "+provider);

        if (hasLocationPermission()){
            currentLocation = locationManager.getLastKnownLocation(provider);
        }

        if (currentLocation == null){
            Log.i(TAG, "onCreate: Current location is null ");
            Log.i(TAG, "onCreate: Requesting Current location");
            requestCurrentLocation();
        }else {
            Log.i(TAG, "onCreate: Location received: "+currentLocation);
            onLocationChanged(currentLocation);
        }

        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        binding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        changeFragmentTo(homeFragment);
                        return true;
                    case R.id.navigation_articles:

                        return true;
                    case R.id.navigation_account:

                        return true;
                }
                return false;
            }
        });


        changeFragmentTo(homeFragment);
    }



    @SuppressLint("MissingPermission")
    private void requestCurrentLocation(){
        Log.i(TAG, "requestCurrentLocation: ");
        locationManager.requestLocationUpdates
                (provider, 500, 1, this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = apiAvailability.getErrorDialog(this, resultCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
        }

        Log.d(TAG, "onResume: GoogleApiAvailable");

        if (hasLocationPermission()){
            Log.i(TAG, "onResume: has location permission: ");
            Log.i(TAG, "onResume: requesting location updates");
           requestCurrentLocation();
        }

        else requestPermissions(LOCATION_PERMISSIONS,REQUEST_LOCATION_CODE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void changeFragmentTo(Fragment fragment){
        Log.i(TAG, "changeFragmentTo: "+fragment.toString());
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

//    @SuppressLint("MissingPermission")
//    private void requestLocation() {
//        Log.i(TAG, "requestLocation: Building request");
//        LocationRequest request = LocationRequest.create();
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        request.setInterval(0);
//        request.setNumUpdates(1);
//
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.i(TAG, "onLocationChanged: " + location);
//                lastKnownLocation = location;
//                User.getInstance().setCurrentLocation(lastKnownLocation);
//                geoLocate();
//                Log.i(TAG, "onLocationChanged: Executing task");
//                Database.getInstance().setUserData();
//            }
//        });
//
//    }

    private void geoLocate(){
        Log.i(TAG, "geoLocate: ");

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude(),1);
        } catch (IOException e) {
            Log.i(TAG, "geoLocate: "+e);
            e.printStackTrace();
        }

        if (addresses.size()>0){
            Log.i(TAG, "geoLocate: "+String.valueOf(addresses.get(0)));
            User.getInstance().setGeoLocation(addresses.get(0).getLocality());
            Toast.makeText(this, String.valueOf(addresses.get(0).getLocality()), Toast.LENGTH_SHORT).show();
        }


    }

    private boolean hasLocationPermission(){
        int result = ContextCompat.checkSelfPermission(this,LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:{
                if (hasLocationPermission()){
                    requestCurrentLocation();
                }else requestPermissions(LOCATION_PERMISSIONS,REQUEST_LOCATION_CODE);
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
        lastKnownLocation = location;
        Log.i(TAG, "onLocationChanged: lastKnownLocation set to: "+lastKnownLocation);

        LocationPreference.setLocationPref(this,lastKnownLocation);

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
