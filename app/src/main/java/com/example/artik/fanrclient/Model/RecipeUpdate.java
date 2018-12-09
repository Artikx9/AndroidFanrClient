package com.example.artik.fanrclient.Model;

import com.google.gson.annotations.SerializedName;


public class RecipeUpdate {
    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("contents")
    private String Contents;
    @SerializedName("cooking")
    private String Cooking;

    public RecipeUpdate(int id, String name, String contents, String cooking) {
        Id = id;
        Name = name;
        Contents = contents;
        Cooking = cooking;
    }
}
