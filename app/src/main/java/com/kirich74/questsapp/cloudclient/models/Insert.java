package com.kirich74.questsapp.cloudclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Insert {

    @SerializedName("@@identity")
    @Expose
    private String identity;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

}