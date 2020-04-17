package com.noobcode.fond.Model;

import java.util.List;

public class User {
    String name;
    String age;
    int numberOfImages;
    String gender;
    double longitude;
    double latitude;
    String address;
    String facebookdp;
    String facebookid;
    String imageUris;
    String matchWith;
    String lookingFor;
    String firebaseId;
    List<String> liked;
    List<String> likedBy;
    String bio;
    String auth;

    public User(String name, String age, int numberOfImages, String gender, double longitude, double latitude, String address, String facebookdp, String facebookid, String imageUris, String matchWith, String lookingFor, String firebaseId, List<String> liked, List<String> likedBy, String bio, String auth) {
        this.name = name;
        this.age = age;
        this.numberOfImages = numberOfImages;
        this.gender = gender;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.facebookdp = facebookdp;
        this.facebookid = facebookid;
        this.imageUris = imageUris;
        this.matchWith = matchWith;
        this.lookingFor = lookingFor;
        this.firebaseId = firebaseId;
        this.liked = liked;
        this.likedBy = likedBy;
        this.bio = bio;
        this.auth = auth;
    }

    public User() {
    }


    public String getAuth() {
        return auth;
    }

    public String getBio() {
        return bio;
    }

    public List<String> getLiked() {
        return liked;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public String getGender() {
        return gender;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getFacebookdp() {
        return facebookdp;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public String getImageUris() {
        return imageUris;
    }

    public String getMatchWith() {
        return matchWith;
    }

    public String getLookingFor() {
        return lookingFor;
    }
}
