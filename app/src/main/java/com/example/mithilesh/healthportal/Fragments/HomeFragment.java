package com.example.mithilesh.healthportal.Fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithilesh.healthportal.DoctorPackage.DoctorActivity;
import com.example.mithilesh.healthportal.HospitalPackage.HospitalActivity;
import com.example.mithilesh.healthportal.HospitalPackage.HospitalFragment;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public static HomeFragment getInstance(){
        return new HomeFragment();
    }
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentHomeBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false
        );


        List<Menu> menus = new ArrayList<>();

        menus.add(new Menu("Hospitals","Hospitals near me","file:///android_asset/ic_doctor.png"));
        menus.add(new Menu("Doctors","Doctors near me","file:///android_asset/ic_doctor.png"));
        menus.add(new Menu("Medications","order medications","file:///android_asset/ic_doctor.png"));
        menus.add(new Menu("Appointments","My appointments","file:///android_asset/ic_doctor.png"));

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        binding.recyclerView.setAdapter(new MenuAdapter(menus));



        return binding.getRoot();
    }

    private void changeActivity(String titile) {
        Intent intent = null;

        if (titile.equals("Hospitals")){
            Log.i(TAG, "changeActivity: Hospitals");
            intent = new Intent(getActivity(), HospitalActivity.class);
        }
        else if (titile.equals("Doctors")){
            Log.i(TAG, "changeActivity: Doctors");
            intent = new Intent(getActivity(), DoctorActivity.class);
        }else if (titile.equals("Medications")){
            Log.i(TAG, "changeActivity: Medications");
            Toast.makeText(getActivity(), "Medications", Toast.LENGTH_SHORT).show();

        }else if (titile.equals("Appointments")){
            Log.i(TAG, "changeActivity: Appointments");
            Toast.makeText(getActivity(), "Appointments", Toast.LENGTH_SHORT).show();
        }

        if (intent != null)
        startActivity(intent);
    }



    class MenuViewHolder extends RecyclerView.ViewHolder{
        private TextView title,caption;
        private ImageView imageView;
        private CardView cardView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            caption = itemView.findViewById(R.id.caption);
            imageView = itemView.findViewById(R.id.iconImageView);
            cardView = itemView.findViewById(R.id.menuCardView);

        }

        public void bind(Menu menu){
            title.setText(menu.getTitile());
            caption.setText(menu.getCaption());
            if (menu.getTitile().equals("Doctors")){
               Drawable drawable = getResources().getDrawable(R.drawable.doctor);
               imageView.setImageDrawable(drawable);
            }else if (menu.getTitile().equals("Hospitals")){
                Drawable drawable = getResources().getDrawable(R.drawable.hospital);
                imageView.setImageDrawable(drawable);
            }else if (menu.getTitile().equals("Medications")){
                Drawable drawable = getResources().getDrawable(R.drawable.medicine);
                imageView.setImageDrawable(drawable);
            }else if (menu.getTitile().equals("Appointments")){
                Drawable drawable = getResources().getDrawable(R.drawable.appointment);
                imageView.setImageDrawable(drawable);
            }
        }
    }

    class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder>{

        List<Menu> menus;

        public MenuAdapter(List<Menu> menus) {
            this.menus = menus;
        }

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_menu,viewGroup,false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i) {
            menuViewHolder.bind(menus.get(i));
            menuViewHolder.cardView.setOnClickListener(e->{
                changeActivity(menus.get(i).getTitile());
            });
        }

        @Override
        public int getItemCount() {
            return menus.size();
        }
    }

    class Menu{

        private String titile,caption,iconurl;


        public Menu(String titile, String caption, String iconurl) {
            this.titile = titile;
            this.caption = caption;
            this.iconurl = iconurl;
        }

        public String getTitile() {
            return titile;
        }

        public void setTitile(String titile) {
            this.titile = titile;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getIconurl() {
            return iconurl;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }
    }


}
