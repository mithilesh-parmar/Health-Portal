package com.example.mithilesh.healthportal.Model;

import java.io.Serializable;

public class PlaceReviews implements Serializable {
    private String authorName;
    private String authorUrl;
    private String profilePhotoUrl;
    private int rating;
    private String relativeTimeDescription;
    private String text;
    private int time;

    public PlaceReviews() {
    }

    public PlaceReviews(String authorName, String authorUrl, String profilePhotoUrl, int rating, String relativeTimeDescription, String text, int time) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.profilePhotoUrl = profilePhotoUrl;
        this.rating = rating;
        this.relativeTimeDescription = relativeTimeDescription;
        this.text = text;
        this.time = time;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
