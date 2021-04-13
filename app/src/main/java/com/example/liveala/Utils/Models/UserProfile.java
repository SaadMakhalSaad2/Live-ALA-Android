package com.example.liveala.Utils.Models;

import java.io.Serializable;

public class UserProfile implements Serializable {
    public String userType;
    public String email;
    public String hall;
    public String room;
    public String name;
    public String id;
    public String imageUrl;

    public UserProfile() {
    }

    public String getHall() {
        return hall;
    }

    public String getRoom() {
        return room;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
