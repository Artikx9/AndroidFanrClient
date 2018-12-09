package com.example.artik.fanrclient.service;


import android.content.Context;

import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.Model.RecipeNew;
import com.example.artik.fanrclient.Model.RecipeUpdate;
import com.example.artik.fanrclient.Model.UserRegister;
import com.example.artik.fanrclient.other.FanrClient;
import com.example.artik.fanrclient.other.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RecipeService   {
    Retrofit builder;
    public Recipe recipe;
    FanrClient client;
    Context context;
    public RecipeService(Context context) {
        this.context =context;
        recipe = new Recipe();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        builder = new Retrofit.Builder()
                .baseUrl(Helper.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
         client = builder.create(FanrClient.class);
    }



    public  Call<String> signUp(String username, String password, String email)
    {
        return client.signUp(new UserRegister(username,password,email));
    }

    public  Call<Recipe> getInfoRecipe(Integer id)
    {
        return client.idInRecipe(new Helper(context).getToken(), id);
    }

    public Call<Void> addRecipe(String name, String contents, String cooking)
    {
        return client.addRecipe(new Helper(context).getToken(), new RecipeNew(name, contents,cooking));
    }

    public Call<List<Recipe>> getRecipe()
    {
        return client.getAll(new Helper(context).getToken());
    }

    public Call<Void> updateRecipe(int id,String name, String content, String cook)
    {
        return client.updateRecipe(new Helper(context).getToken(), new RecipeUpdate(id,name, content,cook));
    }

    public Call<List<Recipe>> getMyRecipe()
    {
        return client.getMy(new Helper(context).getToken());
    }

    public Call<Void> deleteRecipe(int id)
    {
        return client.deleteRecipe(new Helper(context).getToken(),id);
    }
}
