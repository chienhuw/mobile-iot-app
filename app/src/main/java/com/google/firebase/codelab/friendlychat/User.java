package com.google.firebase.codelab.friendlychat;

/**
 * Created by saurabh on 4/30/18.
 */

public class User {
    public String userid;
    public String username;
    public String profilePhoto;
    public String latitude;
    public String longitude;
    public String pickupAddress;
    public String pName;
    public String dName;
    public String bio;
    public String phoneNumber;
    public String dropoffAddress;
    public boolean checkCar;

    User(){

    }

    User(String userid, String username, String profilePhoto, String latitude, String longitude, String pickupAddress, String pName, String dropoffAddress, String dName, String bio, String phoneNumber, boolean checkCar){
        this.userid = userid;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bio = bio;
        this.pName = pName;
        this.dName = dName;
        this.dropoffAddress =dropoffAddress;
        this.pickupAddress =pickupAddress;
        this.phoneNumber = phoneNumber;
        this.checkCar = checkCar;
    }
}
