package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.Date;

public class UserProfile implements Serializable {
    public String userType;
    public String email;
    public String hall;
    public String room;
    public String name;
    public String id;
    public String imageUrl;
    public Date lastInspected;
    public String hallOrder;

    public UserProfile() {
    }

    public Date getLastInspected() {
        return lastInspected;
    }

    public String getHallOrder() {
        return hallOrder;
    }

    public void setHallOrder(String hallOrder) {
        this.hallOrder = hallOrder;
    }

    public void setLastInspected(Date lastInspected) {
        this.lastInspected = lastInspected;
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
