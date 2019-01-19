package com.example.mithilesh.healthportal.HospitalPackage;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.mithilesh.healthportal.Model.Place;
import com.example.mithilesh.healthportal.Model.PlaceDetail;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.SingleFragmentActivity;

public class HospitalActivity extends SingleFragmentActivity implements HospitalFragment.HospitalFragmentListener {

    private static final String TAG = "HospitalActivity";
    private Place place;
    private Fragment detailsFragment ;

    @Override
    public Fragment createFragment() {
        return HospitalFragment.getInstance();
    }

    @Override
    public void onItemClick(Place place) {
        Log.i(TAG, "onItemClick: showing details for: "+place.getName());
        Log.i(TAG, "onItemClick: Place id "+place.getPlaceId());
        this.place = place;
        changeFragment(place.getPlaceId());

    }


    private void changeFragment(String placeId){
        detailsFragment = HospitalDetailFragment.createInstance(placeId);
        fm.beginTransaction().replace(R.id.fragment_container,detailsFragment).addToBackStack(null)
                .commit();
    }

}
