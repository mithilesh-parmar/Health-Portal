package com.example.mithilesh.healthportal.DoctorPackage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mithilesh.healthportal.CategoryViewModel;
import com.example.mithilesh.healthportal.Model.Category;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.databinding.FragmentCategoryBinding;
import com.example.mithilesh.healthportal.databinding.ListItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";
    private List<Category> categories;
    private CategoryListener listener;

    public static CategoryFragment createInstane(){
        return new CategoryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CategoryListener) context;
        Log.i(TAG, "onAttach: listener attached");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categories = new ArrayList<>();




        categories.add(new Category("Women's health",getDrawable(R.drawable.women_health),"Gynecologist_Obstetrician"));
        categories.add(new Category("Skin & Hair",getDrawable(R.drawable.skin_hair),"Dermatologist"));
        categories.add(new Category("Child Specialist",getDrawable(R.drawable.child_care),"Pediatrician"));
        categories.add(new Category("General Doctor",getDrawable(R.drawable.general),"General_Doctor"));
        categories.add(new Category("Denatal Care",getDrawable(R.drawable.dental),"Dentist"));
        categories.add(new Category("Ear Nose Throat",getDrawable(R.drawable.ear),"ENT_Specialist"));
        categories.add(new Category("Homeopathy",getDrawable(R.drawable.homeopathy),"homeopathy"));
        categories.add(new Category("Bone & Joint's",getDrawable(R.drawable.bone),"Orthopedist"));
        categories.add(new Category("Sex Specialist",getDrawable(R.drawable.sex),"Sexologist"));
        categories.add(new Category("Eye Specialist",getDrawable(R.drawable.eye),"Ophthalmologist"));
        categories.add(new Category("Digestive Issues",getDrawable(R.drawable.digestive),"Gastroenterologist"));
        categories.add(new Category("Mental Wellness",getDrawable(R.drawable.mental),"Psychiatrist"));
        categories.add(new Category("Physiotherapy",getDrawable(R.drawable.physical_therapy),"Physiotherapist"));
        categories.add(new Category("Diabetes",getDrawable(R.drawable.diabetes),"Diabetologist"));
        categories.add(new Category("Brain & Nervous",getDrawable(R.drawable.brain),"Neurologist"));
        categories.add(new Category("Kidney & Urinary Issues",getDrawable(R.drawable.kidney),"urologist"));
        categories.add(new Category("Ayurveda",getDrawable(R.drawable.homeopathy),"Ayurveda"));
        categories.add(new Category("General Surgery",getDrawable(R.drawable.general),"General_Surgeon"));
        categories.add(new Category("Lungs & Breathing",getDrawable(R.drawable.lungs),"Pulmonologist"));
        categories.add(new Category("Heart",getDrawable(R.drawable.heart),"Cardiologist"));
        categories.add(new Category("Cancer",getDrawable(R.drawable.cancer),"Oncologist"));

    }


    public Drawable getDrawable(int icon){
        return getResources().getDrawable(icon);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentCategoryBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_category,
                container,
                false
        );

        binding.categoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.categoryRecyclerView.setAdapter(new CategoryAdapter(categories));

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        private ListItemCategoryBinding binding;

        public CategoryViewHolder(ListItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setViewModel(new CategoryViewModel());
        }

        public void bind(Category category){
            this.binding.getViewModel().setCategory(category);
            this.binding.executePendingBindings();

        }
    }


    class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
        private List<Category> categoryList;

        public CategoryAdapter(List<Category> categoryList) {
            this.categoryList = categoryList;
        }


        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemCategoryBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.list_item_category,
                    viewGroup,
                    false
            );

            return new CategoryViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
            categoryViewHolder.bind(categoryList.get(i));
            categoryViewHolder.binding.categoryCardView.setOnClickListener(e->{
                Log.i(TAG, "onBindViewHolder: on CategoryClick: "+this.categoryList.get(i).getName());
                if (listener != null)listener.onCategoryClick(this.categoryList.get(i));
            });
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }
    }


    public interface CategoryListener{
        void onCategoryClick(Category category);
    }
}
