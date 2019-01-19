package com.example.mithilesh.healthportal.DoctorPackage;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.mithilesh.healthportal.LocationPreference;
import com.example.mithilesh.healthportal.Model.Category;
import com.example.mithilesh.healthportal.Model.Place;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.SingleFragmentActivity;

import java.util.List;

public class DoctorActivity extends SingleFragmentActivity implements CategoryFragment.CategoryListener,DoctorsFragment.DoctorsFragmentListener {


    private static final String TAG = "DoctorActivity";
    private Fragment doctorsListFragment,doctorDetailFragment;

    @Override
    public Fragment createFragment() {
        return CategoryFragment.createInstane();
    }

    @Override
    public void onCategoryClick(Category category) {
        Log.i(TAG, "onCategoryClick: "+category.getName());
        doctorsListFragment = DoctorsFragment.createInstance(category.getSearchKeyword());
        changeFragment(doctorsListFragment);
    }


    @Override
    public void onDoctorClick(Place place) {
        Log.i(TAG, "onDoctorClick: "+place.getName());
        doctorDetailFragment = DoctorDetailFragment.createInstance(place.getPlaceId());
        changeFragment(doctorDetailFragment);
    }


    private void changeFragment(Fragment fragment){
        fm.beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .addToBackStack("")
                .commit();
    }


}
