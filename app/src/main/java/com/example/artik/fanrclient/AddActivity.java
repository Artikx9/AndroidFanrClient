package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artik.fanrclient.Model.RecipeNew;
import com.example.artik.fanrclient.service.RecipeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddActivity extends Activity {

    EditText edtName,edtContents,edtCooking;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edtName = findViewById(R.id.edtAddName);
        edtContents = findViewById(R.id.edtAddContent);
        edtCooking = findViewById(R.id.edtAddCook);
        btnAdd = findViewById(R.id.btnNewAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btnNewAdd:
                       addNewRecipe(edtName.getText().toString(), edtContents.getText().toString(), edtCooking.getText().toString());
                        break;
                }
            }
        });
    }
     private void addNewRecipe(String name, String contents, String cooking)
     {
        new RecipeService(this).addRecipe(name,contents,cooking)
         .enqueue(new Callback<Void>() {
             @Override
             public void onResponse(Call<Void> call, Response<Void> response) {
                 if(response.code() == 200)
                 {
                     Toast.makeText(AddActivity.this, "Ready", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(AddActivity.this,MainActivity.class));
                 } else if(response.code() == 500)
                 {startActivity(new Intent(AddActivity.this, LoginActivity.class));}
                 else {Toast.makeText(AddActivity.this, "Bad data", Toast.LENGTH_SHORT).show();}
             }

             @Override
             public void onFailure(Call<Void> call, Throwable t) {
                 Toast.makeText(AddActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
             }
         });
     }
}
