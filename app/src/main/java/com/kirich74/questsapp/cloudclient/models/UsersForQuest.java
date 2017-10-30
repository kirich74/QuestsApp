package com.kirich74.questsapp.cloudclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsersForQuest {

    @SerializedName("email")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}