package com.example.mithilesh.healthportal;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.mithilesh.healthportal.Model.Category;

public class CategoryViewModel extends BaseObservable {

    private Category category;





    @Bindable
    public String getCategory(){
        return category.getName();
    }


    @Bindable
    public void setCategory(Category category) {
        this.category = category;
        notifyChange();
    }

    @Bindable
    public Drawable getIcon(){
        return category.getIcon();
    }

    @BindingAdapter("category")
    public void setCategoryIcon(ImageView icon,int drawableIcon){


    }
}
