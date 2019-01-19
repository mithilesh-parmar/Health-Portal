package com.example.mithilesh.healthportal.Model;

import android.graphics.drawable.Drawable;

public class Category {
    private String name;
    private Drawable icon;
    private String searchKeyword;

    public Category(String name, Drawable icon, String searchKeyword) {
        this.name = name;
        this.icon = icon;
        this.searchKeyword = searchKeyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
