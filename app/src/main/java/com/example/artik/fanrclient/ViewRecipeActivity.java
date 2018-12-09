package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.other.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewRecipeActivity extends Activity implements View.OnClickListener {

    TextView txtName,txtContents,txtCooking,txtData;
    Intent intent;
    Button btnSave, btnDelete, btnUpdate;
    Recipe recipe;

    DBHelper sqlHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        sqlHelper = new DBHelper(this);
        db = sqlHelper.getWritableDatabase();
        txtName = findViewById(R.id.txtName);
        txtContents = findViewById(R.id.txtContents);
        txtCooking = findViewById(R.id.txtCooking);
        txtData = findViewById(R.id.txtDate);
        btnSave = findViewById(R.id.btnSaveRecipe);
        btnSave.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnDeleteRecipe);
        btnDelete.setOnClickListener(this);
        btnUpdate = findViewById(R.id.btnUpdateRecipe);
        btnUpdate.setOnClickListener(this);
        intent  = getIntent();
       recipe = (Recipe) intent.getSerializableExtra("Recipe");
        viewRecipe(recipe);
        checkRecipe(recipe);

    }

    private void checkRecipe(Recipe recipe) {
        Cursor cursor = db.rawQuery("select * from "+ DBHelper.TABLE
                +" where id = "+ recipe.getId(), null);
        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            if(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_VERSION)) != recipe.getVersion())
                btnUpdate.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        }
        else {
            btnSave.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }


    }

    private void viewRecipe(Recipe recipe) {
        txtName.setText(recipe.getName());
        txtContents.setText(recipe.getContents());
        txtCooking.setText(recipe.getCooking());
        txtData.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date(recipe.getDate())));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSaveRecipe:
                saveRecipe(recipe);
                break;
            case R.id.btnDeleteRecipe:
                deleteRecipe(recipe);
                break;
            case R.id.btnUpdateRecipe:
                updateRecipe(recipe);
                break;
        }
    }

    private void updateRecipe(Recipe recipe) {
        db.update(DBHelper.TABLE, sqlHelper.saveRecipe(recipe),
                DBHelper.COLUMN_ID + "=" + String.valueOf(recipe.getId()), null);
        btnUpdate.setVisibility(View.GONE);
        Toast.makeText(ViewRecipeActivity.this,"Update", Toast.LENGTH_SHORT).show();

    }

    private void deleteRecipe(Recipe recipe) {
        db.delete(DBHelper.TABLE,"id = ?",new String[]{recipe.getId().toString()});
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.GONE);
        Toast.makeText(ViewRecipeActivity.this,"Delete", Toast.LENGTH_SHORT).show();
    }

    private void saveRecipe(Recipe recipe) {
        db.insert(DBHelper.TABLE,null,sqlHelper.saveRecipe(recipe));
        btnSave.setVisibility(View.GONE);
        btnDelete.setVisibility(View.VISIBLE);
        Toast.makeText(ViewRecipeActivity.this,"Save", Toast.LENGTH_SHORT).show();
    }
}
