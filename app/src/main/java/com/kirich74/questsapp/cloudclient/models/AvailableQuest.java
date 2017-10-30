package com.kirich74.questsapp.cloudclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableQuest {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image_uri")
    @Expose
    private String imageUri;
    @SerializedName("data_json")
    @Expose
    private String dataJson;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("public")
    @Expose
    private String _public;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublic() {
        return _public;
    }

    public void setPublic(String _public) {
        this._public = _public;
    }

}