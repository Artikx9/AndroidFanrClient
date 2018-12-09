package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.Model.RecipeUpdate;
import com.example.artik.fanrclient.other.FanrClient;
import com.example.artik.fanrclient.other.Helper;
import com.example.artik.fanrclient.service.RecipeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditRecipeActivity extends Activity {

    EditText edtName,edtContents,edtCooking;
    Button btnAdd;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        edtName = findViewById(R.id.edtUpdName);
        edtContents = findViewById(R.id.edtUpdContent);
        edtCooking = findViewById(R.id.edtUpdCook);
        btnAdd = findViewById(R.id.btnUpdate);

        intent  = getIntent();
        final Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");
        edtName.setText(recipe.getName());
        edtContents.setText(recipe.getContents());
        edtCooking.setText(recipe.getCooking());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btnUpdate:
                        updateRecipe(recipe.getId(),edtName.getText().toString(), edtContents.getText().toString(), edtCooking.getText().toString());
                        break;
                }
            }
        });
    }

    private void updateRecipe(int id,String name, String content, String cook) {
        new RecipeService(this).updateRecipe(id,name,content,cook)
        .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200)
                {
                    Toast.makeText(EditRecipeActivity.this, "Ready", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditRecipeActivity.this,MainActivity.class));
                }
                else if(response.code() == 500)
                {startActivity(new Intent(EditRecipeActivity.this, LoginActivity.class));}
                else {Toast.makeText(EditRecipeActivity.this, "Bad data", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditRecipeActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
