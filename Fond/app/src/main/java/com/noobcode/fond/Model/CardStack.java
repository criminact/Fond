package com.noobcode.fond.Model;

public class CardStack {
    String imageUrl;
    int numberOfImages;
    String name;
    int age;
    String gender;
    String location;
    String FirebaseId;
    String bio;
    String interestedin;
    String lookingfor;
    String imageUris;


    public CardStack(String imageUrl, int numberOfImages, String name, int age, String gender, String location, String FirebaseId, String bio, String interestedin, String lookingfor, String imageUris) {
        this.imageUrl = imageUrl;
        this.numberOfImages = numberOfImages;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.FirebaseId = FirebaseId;
        this.bio = bio;
        this.interestedin = interestedin;
        this.lookingfor = lookingfor;
        this.imageUris = imageUris;
    }

    public String getImageUris() {
        return imageUris;
    }

    public String getBio() {
        return bio;
    }

    public String getInterestedin() {
        return interestedin;
    }

    public String getLookingfor() {
        return lookingfor;
    }

    public String getFirebaseId() {
        return FirebaseId;
    }

    public String getLocation() { return location; }

    public String getGender() {
        return gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
