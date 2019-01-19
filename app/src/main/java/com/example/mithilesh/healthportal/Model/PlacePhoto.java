package com.example.mithilesh.healthportal.Model;

import java.io.Serializable;

public class PlacePhoto implements Serializable {
    private String photoRefrence;
    private int height;
    private int width;

    public PlacePhoto(String photoRefrence, int height, int width) {
        this.photoRefrence = photoRefrence;
        this.height = height;
        this.width = width;
    }

    public PlacePhoto() {
    }

    public String getPhotoRefrence() {
        return photoRefrence;
    }

    public void setPhotoRefrence(String photoRefrence) {
        this.photoRefrence = photoRefrence;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
