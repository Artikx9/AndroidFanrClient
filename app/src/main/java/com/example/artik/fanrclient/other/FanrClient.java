package com.example.artik.fanrclient.other;

import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.Model.RecipeNew;
import com.example.artik.fanrclient.Model.RecipeUpdate;
import com.example.artik.fanrclient.Model.UserRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface FanrClient {

    @POST("users/signup")
    Call<String> signUp(@Body UserRegister user);

    @GET("recipe/all")
    Call<List<Recipe>> getAll(@Header("Authorization") String authorization);

    @GET("recipe/my")
    Call<List<Recipe>> getMy(@Header("Authorization") String authorization);

    @POST("recipe/add")
    Call<Void> addRecipe(@Header("Authorization") String authorization, @Body RecipeNew recipeNew);

    @POST("recipe/update")
    Call<Void> updateRecipe(@Header("Authorization") String authorization, @Body RecipeUpdate recipeNew);

    @GET("recipe/delete")
    Call<Void> deleteRecipe(@Header("Authorization") String authorization,@Query("id") int Id);

    @GET("recipe/version")
    Call<Integer> findVersion(@Header("Authorization") String authorization,@Query("id") int Id);

    @GET("recipe/recipe")
    Call<Recipe> idInRecipe(@Header("Authorization") String authorization,@Query("id") int Id);
}
