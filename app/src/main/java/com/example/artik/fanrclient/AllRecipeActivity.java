package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.artik.fanrclient.Adapter.RecipeAdapter;
import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.other.Helper;
import com.example.artik.fanrclient.service.RecipeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRecipeActivity extends Activity {

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipe);

        listView = findViewById(R.id.listRecipe);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewRecipe((Recipe) listView.getItemAtPosition(i));
            }
        });
    }

    private void viewRecipe(Recipe recipe) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    private void getRecipe()
    {
       new RecipeService(this).getRecipe()
        .enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.code() == 200)
                {

                List<Recipe> repos = response.body();
                listView.setAdapter(new RecipeAdapter(AllRecipeActivity.this, repos));
                }else if(response.code() == 500)
                {startActivity(new Intent(AllRecipeActivity.this, LoginActivity.class));}
                else {
                    Toast.makeText(AllRecipeActivity.this,"Filed access", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Toast.makeText(AllRecipeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!new Helper(this).isOnline())
            startActivity(new Intent(this, MainActivity.class));
        getRecipe();

    }
}
