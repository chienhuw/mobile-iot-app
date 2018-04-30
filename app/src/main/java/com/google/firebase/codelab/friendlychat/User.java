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
    public String address;
    public String bio;
    User(){

    }
    User(String userid, String username, String profilePhoto, String latitude, String longitude, String address,String bio){
        this.userid = userid;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address =address;
        this.bio = bio;
    }
}
