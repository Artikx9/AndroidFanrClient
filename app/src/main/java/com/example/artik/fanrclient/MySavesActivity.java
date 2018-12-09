package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.artik.fanrclient.Adapter.RecipeAdapter;
import com.example.artik.fanrclient.Adapter.SaveRecipeAdapter;
import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.other.DBHelper;
import com.example.artik.fanrclient.other.Helper;
import com.example.artik.fanrclient.service.RecipeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySavesActivity extends Activity {

    private ListView listMySaves;
    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    List<Recipe> recipeList;
    boolean ckecker = true;
    Button btnUpdateRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saves);
        listMySaves = findViewById(R.id.listMySaves);
        registerForContextMenu(listMySaves);

        btnUpdateRecipe = findViewById(R.id.btnCheck);

        sqlHelper = new DBHelper(this);
        db = sqlHelper.getReadableDatabase();
        recipeList = new ArrayList<>();


        listMySaves.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewRecipe((Recipe) listMySaves.getItemAtPosition(i));
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_context_menu, menu);
        if(ckecker)
        {  menu.findItem(R.id.showNew).setEnabled(false);
            menu.findItem(R.id.upd).setEnabled(false);}
        else {
            menu.findItem(R.id.showNew).setEnabled(true);
            menu.findItem(R.id.upd).setEnabled(true);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = menuInfo.position;
        switch (item.getItemId()) {
            case R.id.del:
                delteRecipe((Recipe) listMySaves.getItemAtPosition(index));
                break;
            case R.id.showNew:
                showNew((Recipe) listMySaves.getItemAtPosition(index));
                break;
            case R.id.upd:
                updateRecipe((Recipe) listMySaves.getItemAtPosition(index));
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    private void updateRecipe(Recipe recipe) {

       new RecipeService(this).getInfoRecipe(recipe.getId()).
                enqueue(new Callback<Recipe>() {
                    @Override
                    public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        db.update(DBHelper.TABLE, sqlHelper.saveRecipe(response.body()),
                                DBHelper.COLUMN_ID + "=" + String.valueOf(response.body().getId()), null);
                        Toast.makeText(MySavesActivity.this,"Recipe update", Toast.LENGTH_SHORT).show();
                        onResume();
                    }

                    @Override
                    public void onFailure(Call<Recipe> call, Throwable t) {

                    }
                });

    }

    private void showNew(Recipe recipe) {

        new RecipeService(this).getInfoRecipe(recipe.getId()).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Intent intent = new Intent(MySavesActivity.this, ViewRecipeActivity.class);
                intent.putExtra("Recipe", response.body());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {

            }
        });

    }

    private void delteRecipe(Recipe recipe) {
        db.delete(DBHelper.TABLE,"id = ?",new String[]{recipe.getId().toString()});
        Toast.makeText(MySavesActivity.this,"Delete", Toast.LENGTH_SHORT).show();
        onResume();
    }

    private void viewRecipe(Recipe recipe) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }


    private void getMyRecipeSaves(boolean update) {

        userCursor =  db.rawQuery("select * from "+ DBHelper.TABLE, null);
        if (userCursor.moveToFirst()) {
            while (!userCursor.isAfterLast()) {
                Recipe nwRecip = new Recipe();
                nwRecip.setId(userCursor.getInt(userCursor.getColumnIndex(DBHelper.COLUMN_ID)));
                nwRecip.setName(userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_NAME)));
                nwRecip.setContents(userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_CONTENT)));
                nwRecip.setCooking(userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_COOK)));
                nwRecip.setDate(userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_DATE)));
                nwRecip.setVersion(userCursor.getInt(userCursor.getColumnIndex(DBHelper.COLUMN_VERSION)));
                recipeList.add(nwRecip);
                userCursor.moveToNext();
            }
        }
        if(update)
         listMySaves.setAdapter(new SaveRecipeAdapter(MySavesActivity.this, recipeList));
        else listMySaves.setAdapter(new RecipeAdapter(MySavesActivity.this, recipeList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        recipeList.clear();
        getMyRecipeSaves(false);
        if(new Helper(this).isOnline())
            btnUpdateRecipe.setVisibility(View.VISIBLE);
        else btnUpdateRecipe.setVisibility(View.GONE);

    }

    public void checkUpdate(View view) {
        recipeList.clear();
        ckecker = false;
        getMyRecipeSaves(true);
    }
}
