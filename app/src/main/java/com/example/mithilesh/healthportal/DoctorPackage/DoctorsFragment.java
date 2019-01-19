package com.example.mithilesh.healthportal.DoctorPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mithilesh.healthportal.Database;
import com.example.mithilesh.healthportal.Helpers.DataFetcher;
import com.example.mithilesh.healthportal.LocationPreference;
import com.example.mithilesh.healthportal.Model.Place;
import com.example.mithilesh.healthportal.Model.PlaceLocation;
import com.example.mithilesh.healthportal.PlaceViewModel;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.databinding.FragmentPlaceListBinding;
import com.example.mithilesh.healthportal.databinding.ListItemPlaceBinding;

import java.util.ArrayList;
import java.util.List;

public class DoctorsFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    private static final String EXTRA_KEYWORD = "keywors";
    private ProgressDialog progressDialog;
    private Location location;
    private List<Place> doctors;
    private RecyclerView recyclerView;
    private String doctorKeyword;
    private DoctorsFragmentListener listener;

    public static DoctorsFragment createInstance(String doctorKeyword){
        Bundle args = new Bundle();
        args.putString(EXTRA_KEYWORD,doctorKeyword);

        DoctorsFragment fragment = new DoctorsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DoctorsFragmentListener) context;
        Log.i(TAG, "onAttach: listener attached");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        Log.i(TAG, "onCreate: Retriving arguments");
        this.doctorKeyword = getArguments().getString(EXTRA_KEYWORD);
        Log.i(TAG, "onCreate: Got Argument: "+doctorKeyword);

        doctors = new ArrayList<>();
        location = LocationPreference.getLocationPref(getActivity());
        Log.i(TAG, "onCreate: location set to: "+location);

        Log.i(TAG, "onCreate:  Executing downloading task");

        progressDialog.show();
        new  DoctorsNearbyTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPlaceListBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_place_list,
                container,
                false
        );
        this.recyclerView = binding.recyclerView;
        setupAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return binding.getRoot();
    }


    private void setupAdapter() {
        Log.i(TAG, "setupAdapter: ");
        if (isAdded()){
            recyclerView.setAdapter(new DoctorAdapter(doctors));
        }
    }

    private void showOnMap(PlaceLocation location) {
        Log.i(TAG, "showOnMap: "+location);
        StringBuilder str = new StringBuilder();

        str.append("geo:")
                .append(location.getLat()).append(",").append(location.getLon())
                .append("?q=").append(location.getLat()).append(",").append(location.getLon());

        Uri gmmIntentUri = Uri.parse(str.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder{
        private ListItemPlaceBinding binding;
        public DoctorViewHolder(ListItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setViewModel(new PlaceViewModel());
        }

        public void bind(Place doctor) {
            this.binding.getViewModel().setHospital(doctor);
            this.binding.executePendingBindings();
        }
    }

    class DoctorAdapter extends RecyclerView.Adapter<DoctorViewHolder>{

        List<Place> doctors;

        public DoctorAdapter(List<Place> doctors) {
            this.doctors = doctors;
        }

        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemPlaceBinding placeBinding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.list_item_place,
                    viewGroup,
                    false
            );

            return new DoctorViewHolder(placeBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder doctorViewHolder, int i) {
                doctorViewHolder.bind(doctors.get(i));
                doctorViewHolder.binding.cardview.setOnClickListener(e->{
                    Log.i(TAG, "onBindViewHolder: onClick: ");
                    listener.onDoctorClick(doctors.get(i));
                });
                doctorViewHolder.binding.locateButton.setOnClickListener(e->{
                    showOnMap(this.doctors.get(i).getLocation());
                });
        }

        @Override
        public int getItemCount() {
            return doctors.size();
        }
    }


    class DoctorsNearbyTask extends AsyncTask<Void,Void,List<Place>>{

        @Override
        protected List<Place> doInBackground(Void... voids) {


            return new DataFetcher(location,"doctor",-1,doctorKeyword).getNearbyPlace();
        }

        @Override
        protected void onPostExecute(List<Place> placesReceived) {
            super.onPostExecute(placesReceived);
            Log.i(TAG, "onPostExecute: Data received: "+placesReceived);
            doctors = placesReceived;
            Log.i(TAG, "onPostExecute: doctors initialized ");
            setupAdapter();
            //TODO set to firebase

//            Database.getInstance().setDoctors(doctorKeyword,placesReceived);
            if (progressDialog.isShowing())progressDialog.hide();
        }

    }

    public interface DoctorsFragmentListener{
        void onDoctorClick(Place place);
    }
}
