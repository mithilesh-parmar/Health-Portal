package com.example.mithilesh.healthportal.HospitalPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.example.mithilesh.healthportal.Database;
import com.example.mithilesh.healthportal.Helpers.DataFetcher;
import com.example.mithilesh.healthportal.PlaceViewModel;
import com.example.mithilesh.healthportal.LocationPreference;
import com.example.mithilesh.healthportal.Model.Place;
import com.example.mithilesh.healthportal.R;


import com.example.mithilesh.healthportal.databinding.FragmentPlaceListBinding;
import com.example.mithilesh.healthportal.databinding.ListItemPlaceBinding;

import java.util.ArrayList;
import java.util.List;


public class HospitalFragment extends Fragment {

    private static final String TAG = "HospitalFragment";
    private static final String KEYWORD = "hospital";
    private static final String TYPE = "hospital";


    private ProgressDialog progressDialog;
    private List<Place> places = new ArrayList<>();
    private RecyclerView recyclerView;
    private Location currentLocation;
    private HospitalFragmentListener listener;


    public static HospitalFragment getInstance() {
        return new HospitalFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (HospitalFragmentListener) context;
        Log.i(TAG, "onAttach: listener is attached");

        currentLocation= LocationPreference.getLocationPref(getActivity());
        Log.i(TAG, "onAttach: "+currentLocation);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        new HospitalTask().execute();
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
        setUpAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return binding.getRoot();
    }

    private void setUpAdapter(){
        if (isAdded()){
            recyclerView.setAdapter(new PlaceAdapter(places));
        }
    }




    class HospitalTask extends AsyncTask<Void,Void,List<Place>>{

        @Override
        protected List<Place> doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground: Requesting");
            return new DataFetcher(currentLocation,TYPE,-1,KEYWORD).getNearbyPlace();
        }

        @Override
        protected void onPostExecute(List<Place> placesItems) {
            super.onPostExecute(placesItems);
            if (progressDialog.isShowing())progressDialog.hide();

            if (placesItems != null){
                places = placesItems;
               //TODO firebase data
//                Database.getInstance().setHospitals(places);
                Log.i(TAG, "onPostExecute: "+placesItems);
                setUpAdapter();
            }else
                Toast.makeText(getActivity(), "Error occured try agin after some time", Toast.LENGTH_SHORT).show();

        }
    }



    private class PlaceViewHolder extends RecyclerView.ViewHolder{
        private ListItemPlaceBinding binding;
        public PlaceViewHolder(ListItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setViewModel(new PlaceViewModel());
        }
        public void bind(Place place){
            this.binding.getViewModel().setHospital(place);
            this.binding.executePendingBindings();
        }
    }

    private class PlaceAdapter extends RecyclerView.Adapter<PlaceViewHolder> {
        List<Place> places;

        public PlaceAdapter(List<Place> places) {
            this.places = places;
        }

        @NonNull
        @Override
        public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            ListItemPlaceBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.list_item_place,
                    viewGroup,
                    false
            );


            return new PlaceViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i) {
            placeViewHolder.bind(places.get(i));
            placeViewHolder.binding.cardview.setOnClickListener(e->{
                Log.i(TAG, "onBindViewHolder: Clicked on: "+this.places.get(i).getName());
                listener.onItemClick(this.places.get(i));
            });

            placeViewHolder.binding.locateButton.setOnClickListener(e->{
                showOnMap(this.places.get(i).getLocation());
            });
        }

        private void showOnMap(Place.PlaceLocation location) {
            Log.i(TAG, "showOnMap: "+location);
            StringBuilder str = new StringBuilder();
//            "geo:<lat>,<long>?q=<lat>,<long>(Label+Name)"
            str.append("geo:")
                    .append(location.getLat()).append(",").append(location.getLon())
                    .append("?q=").append(location.getLat()).append(",").append(location.getLon());
//            str.append("geo:").append(location.getLat()).append(",").append(location.getLon()).append(("Hospital"));
            Uri gmmIntentUri = Uri.parse(str.toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
        }

        @Override
        public int getItemCount() {
            return places.size();
        }
    }


    public interface HospitalFragmentListener{
        void onItemClick(Place place);
    }
}
