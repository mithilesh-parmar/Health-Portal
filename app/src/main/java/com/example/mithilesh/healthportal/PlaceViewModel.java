package com.example.mithilesh.healthportal;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;


import com.example.mithilesh.healthportal.Model.Place;
import com.squareup.picasso.Picasso;

public class PlaceViewModel extends BaseObservable {

    private Place hospital;


    public PlaceViewModel(){
    }


    @Bindable
    public String getCategory(){
        return " ";
    }


    @Bindable
    public String getIconUrl(){return hospital.getIconUrl();}


    @Bindable
    public String getId(){
        return String.valueOf(hospital.getId());
    }

    @Bindable
    public  String getName(){
        return hospital.getName();
    }

    @Bindable
    public String getRating(){
        return String.valueOf(hospital.getRating())+" Stars";
    }

    @Bindable
    public String getOpenNow(){
        if (hospital.isOpenNow())return "Available today";
        else return "Closed today";
    }

    @Bindable
    public String getLocality(){
        return hospital.getVicinity();
    }

    @Bindable
    public String getTotalRating(){
        return String.valueOf(hospital.getUserRatingsTotal())+" Feedbacks";
    }

    @Bindable
    public String getVicinity(){
        return hospital.getVicinity();
    }

    @Bindable
    public void setHospital(Place hospital) {
        this.hospital = hospital;
        notifyChange();
    }


    @BindingAdapter("icon")
    public static void setIcon(ImageView imageView,String url){
        if (!url.equals("")) {
            Picasso.get().load(url).resize(200, 200).into(imageView);
        }
    }
}
