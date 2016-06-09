package com.mlsdev.serhii.shoplist.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serhii on 6/9/16.
 */
public class User {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("timestamp_joined")
    @Expose
    private long timestampJoined;

    public User() {
    }

    public User(String email, String name, long timestampJoined) {
        this.email = email;
        this.name = name;
        this.timestampJoined = timestampJoined;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestampJoined(long timestampJoined) {
        this.timestampJoined = timestampJoined;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public long getTimestampJoined() {
        return timestampJoined;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
