package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.artik.fanrclient.Adapter.RecipeAdapter;
import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.other.DBHelper;
import com.example.artik.fanrclient.other.FanrClient;
import com.example.artik.fanrclient.other.Helper;
import com.example.artik.fanrclient.service.RecipeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRecipeActivity extends Activity {

    private ListView listMyView;
    DBHelper sqlHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        sqlHelper = new DBHelper(this);
        db = sqlHelper.getWritableDatabase();

        listMyView = findViewById(R.id.listRecipeMy);
        registerForContextMenu(listMyView);

       listMyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               viewRecipe((Recipe) listMyView.getItemAtPosition(i));
           }
       });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = menuInfo.position;

        switch (item.getItemId()) {
            case R.id.delete:
                deleteRecipe((Recipe) listMyView.getItemAtPosition(index));
                break;
            case R.id.save:
                saveRecipe((Recipe) listMyView.getItemAtPosition(index));
                break;
            case R.id.view:
                viewRecipe((Recipe) listMyView.getItemAtPosition(index));
                break;
            case R.id.edit:
                editRecipe((Recipe) listMyView.getItemAtPosition(index));
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    private void editRecipe(Recipe recipe) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    private void viewRecipe(Recipe recipe) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    private void saveRecipe(Recipe recipe) {
        db.insert(DBHelper.TABLE,null,sqlHelper.saveRecipe(recipe));
        Toast.makeText(MyRecipeActivity.this, "Save Recipe", Toast.LENGTH_SHORT).show();
    }

    private void deleteRecipe(Recipe selectedItem) {

       new RecipeService(this).deleteRecipe(selectedItem.getId())
        .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){getRecipe(); listMyView.invalidateViews();}
                else if(response.code() == 500)
                {startActivity(new Intent(MyRecipeActivity.this, LoginActivity.class));}
                else
                    Toast.makeText(MyRecipeActivity.this,"Failed"+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyRecipeActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRecipe()
    {
        new RecipeService(this).getMyRecipe()
       .enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.code() == 200)
                {

                    List<Recipe> repos = response.body();
                    listMyView.setAdapter(new RecipeAdapter(MyRecipeActivity.this, repos));
                } else if(response.code() == 500)
                {startActivity(new Intent(MyRecipeActivity.this, LoginActivity.class));}
                else {
                    Toast.makeText(MyRecipeActivity.this,"Filed access", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MyRecipeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
