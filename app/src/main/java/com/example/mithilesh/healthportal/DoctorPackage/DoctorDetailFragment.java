package com.example.mithilesh.healthportal.DoctorPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithilesh.healthportal.Database;
import com.example.mithilesh.healthportal.Helpers.DataFetcher;
import com.example.mithilesh.healthportal.HospitalPackage.HospitalDetailFragment;
import com.example.mithilesh.healthportal.ImagePagerAdapter;
import com.example.mithilesh.healthportal.Model.PlaceDetail;
import com.example.mithilesh.healthportal.Model.PlacePhoto;
import com.example.mithilesh.healthportal.Model.PlaceReviews;
import com.example.mithilesh.healthportal.Model.User;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.databinding.FragmentPlaceDetailBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DoctorDetailFragment extends Fragment {

    private static final String TAG = "DoctorDetailFragment";
    private static final String EXTRA_PLACE_ID = "DoctorDetailFragment.place.id";
    private TextView placeName,placeVicinity,placeTimeings,placeRating,placeAddress,placeTotalRatingUser;
    private RecyclerView reviewsRecyclerView;
    private String placeId;
    private ProgressDialog progressDialog;
    private ViewPager imageViewPager;
    private int CURRENT_PAGE=0;
    private Button appointmentButton;
    public static DoctorDetailFragment createInstance(String place_id){
        Bundle args = new Bundle();
        args.putString(EXTRA_PLACE_ID,place_id);

        DoctorDetailFragment fragment = new DoctorDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeId = getArguments().getString(EXTRA_PLACE_ID);


        if (placeId != null){
            Log.i(TAG, "onCreate: placeId received: "+placeId);
            new DoctorDetailTask().execute(placeId);
        }else {
            Log.i(TAG, "onCreate: placeId null ");
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentPlaceDetailBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_place_detail,
                container,
                false
        );

        this.placeName = binding.placeName;
        this.placeVicinity =  binding.placeVicinity;
        this.placeTimeings = binding.placeTimeings;
        this.placeRating = binding.placeRating;
        this.placeTotalRatingUser = binding.totalRatingUser;
        this.placeAddress = binding.placeAddress;
        this.reviewsRecyclerView = binding.reviewsRecyclerView;
        this.imageViewPager = binding.imageViewPager;
        this.appointmentButton = binding.bookAppointment;
        return binding.getRoot();
    }

    private void setupView(PlaceDetail place){
        Log.i(TAG, "setupView: Data received ");
        placeName.setText(place.getName());
        placeVicinity.setText(place.getVicinity());
        placeTimeings.setText(place.getOpenWeekdayText());
        placeRating.setText(String.valueOf(place.getRating())+" Star");
        placeTotalRatingUser.setText("Total ratings: "+String.valueOf(place.getUserRatingsTotal()));
        placeAddress.setText(place.getFormattedAddress());

        setImagesForPlace(place);

        reviewsRecyclerView.setAdapter(new ReviewAdapter(place.getPlaceReviews()));
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        appointmentButton.setOnClickListener(e->{
            dialContactPhone(place.getInternationalPhoneNumber());
        });
    }

    private void setImagesForPlace(PlaceDetail place) {
        List<PlacePhoto> placePhotos = place.getPhotos();
        Log.i(TAG, "setImagesForPlace: got list: "+placePhotos);

        List<String> imageUrls = new ArrayList<>();

        for(PlacePhoto placePhoto:placePhotos){
            StringBuilder str = new StringBuilder();
            str.append("https://maps.googleapis.com/maps/api/place/photo?")
                    .append("maxheight=220")
                    .append("&photoreference=").append(placePhoto.getPhotoRefrence())
                    .append("&key=AIzaSyDIH6VyEfL8bbFi5F5Srn5r3pFfYFyPpf0");

            imageUrls.add(str.toString());
        }

        Log.i(TAG, "setImagesForPlace: Image Urls: "+imageUrls);

        imageViewPager.setAdapter(new ImagePagerAdapter(imageUrls,getActivity()));
        enableAutoScroll();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void enableAutoScroll(){
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run()
            {

                imageViewPager.setCurrentItem(CURRENT_PAGE, true);
                if(CURRENT_PAGE == Integer.MAX_VALUE)
                {
                    CURRENT_PAGE = 0;
                }
                else
                {
                    ++CURRENT_PAGE ;
                }

            }
        };


       Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 2500);


    }

    class DoctorDetailTask extends AsyncTask<String,Void,PlaceDetail>{

        @Override
        protected PlaceDetail doInBackground(String... strings) {

            return  new DataFetcher().getPlaceDetail(strings[0]);
        }

        @Override
        protected void onPostExecute(PlaceDetail placeDetail) {
            super.onPostExecute(placeDetail);
            Log.i(TAG, "onPostExecute: Data Downloaded ");
            Log.i(TAG, "onPostExecute: "+placeDetail);

            if (progressDialog.isShowing()){
                progressDialog.hide();
            }

            if (placeDetail != null){
                Log.i(TAG, "onPostExecute: Setting up view");
                setupView(placeDetail);
            }else {
                Toast.makeText(getActivity(), "Error occured try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView authorName,authorText;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.authorName);
            authorText = itemView.findViewById(R.id.authorText);

        }

        public void bind(PlaceReviews review){
            authorText.setText(review.getText());
            authorName.setText(review.getAuthorName());
        }

    }

    class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>{

        List<PlaceReviews> reviews;

        public ReviewAdapter(List<PlaceReviews> reviews) {
            this.reviews = reviews;
            Log.i(TAG, "ReviewAdapter: Reviews: "+reviews.size());
        }

        @NonNull
        @Override
        public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(
                    R.layout.list_item_review,
                    viewGroup,
                    false
            );

            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
            reviewViewHolder.bind(reviews.get(i));
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }
    }


}
