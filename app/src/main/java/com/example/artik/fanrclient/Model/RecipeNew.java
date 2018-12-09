package com.example.artik.fanrclient.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by artik on 05.05.2018.
 */

public class RecipeNew {
    @SerializedName("name")
    private String Name;
    @SerializedName("contents")
    private String Contents;
    @SerializedName("cooking")
    private String Cooking;

    public RecipeNew(String name, String contents, String cooking) {
        Name = name;
        Contents = contents;
        Cooking = cooking;
    }
}
