package com.example.artik.fanrclient.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Recipe implements Serializable {
    @SerializedName("id")
    private Integer Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("contents")
    private String Contents;
    @SerializedName("cooking")
    private String Cooking;
    @SerializedName("version")
    private Integer Version;
    @SerializedName("date")
    private long Date;

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    public String getCooking() {
        return Cooking;
    }

    public void setCooking(String cooking) {
        Cooking = cooking;
    }

    public Integer getVersion() {
        return Version;
    }

    public void setVersion(Integer version) {
        Version = version;
    }


}
