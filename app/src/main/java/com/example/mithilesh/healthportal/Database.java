package com.example.mithilesh.healthportal;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mithilesh.healthportal.Model.Place;
import com.example.mithilesh.healthportal.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static final String TAG = "Database";

    public enum PlaceName{
        HOSPITALS("hospitals"),DOCTORS("doctors");

        private String name;

        PlaceName(String name){
            this.name = name;
        }


        @Override
        public String toString() {
            return name;
        }
    }

    private static Database instance;
    private FirebaseDatabase database;


    public static Database getInstance(){
        if (instance == null){
            synchronized (Database.class){
                instance = new Database();
            }
        }
        return instance;
    }

    private Database() {
        database = FirebaseDatabase.getInstance();

    }

    public void setUserData(){
        DatabaseReference userReference = database.getReference("users");
        User user = User.getInstance();
        userReference.child(user.getUid()).setValue(user);
    }

    //TODO write hospital and doctor data
    //TODO build user panel
    //TODO get photos of places

    public void setHospitals(List<Place> places){
        Log.i(TAG, "setHospitals: "+places);
        DatabaseReference hospitalReference = database.getReference("hospitals");

        Map<String,Object> values = new HashMap<>();
        for (int i = 0; i < places.size(); i++) {
            values.put(places.get(i).getId(),places.get(i));
            values.put("place_detail",places.get(i).getPlaceDetail());
        }

        hospitalReference.setValue(values).addOnCompleteListener(e->{
            Log.i(TAG, "setHospitals: Success");
        }).addOnFailureListener(e->{
            Log.i(TAG, "setHospitals: Failure");
        });

    }


    public void setDoctors(String speciality,List<Place> doctors){
        //root node
        Log.i(TAG, "setDoctors: getting ref");
        DatabaseReference specialityRef = database.getReference("doctors").child(speciality);

        Map<String,Object> values = new HashMap<>();

        for (int i = 0; i < doctors.size(); i++) {

            specialityRef.child(doctors.get(i).getPlaceId());

            specialityRef.setValue(doctors.get(i).toMap()).addOnFailureListener(e->{
                Log.i(TAG, "setDoctors: Failed");
            }).addOnSuccessListener(e->{
                Log.i(TAG, "setDoctors: Success");
            });

        }

    }

    public void setDoctorDetail(){

    }

    public void getUserDetails(User user){

    }
}
