package com.example.artik.fanrclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.artik.fanrclient.other.Helper;

public class MainActivity extends Activity implements View.OnClickListener {

   Button btnShowAll,btnMyAll, btnAdd, btnMySaves;
    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShowAll = findViewById(R.id.btnShowAll);
        btnMyAll = findViewById(R.id.btnShowMy);
        btnAdd = findViewById(R.id.btnAdd);
        btnMySaves = findViewById(R.id.btnMySaves);
        btnShowAll.setOnClickListener(this);
        btnMyAll.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnMySaves.setOnClickListener(this);

        helper = new Helper(this);
        if(helper.isFirstTimeLaunch())
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

    }


    @Override
    public void onClick(View view) {
      switch (view.getId())
      {
          case R.id.btnShowAll:
              startActivity(new Intent(this, AllRecipeActivity.class));
              break;
          case R.id.btnShowMy:
              startActivity(new Intent(this, MyRecipeActivity.class));
              break;
          case R.id.btnAdd:
              startActivity(new Intent(this, AddActivity.class));
              break;
          case R.id.btnMySaves:
              startActivity(new Intent(this, MySavesActivity.class));
              break;
      }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(new Helper(this).isOnline()) {
            btnAdd.setEnabled(true);
         btnMyAll.setEnabled(true);
         btnShowAll.setEnabled(true);
        }
        else {btnAdd.setEnabled(false);
            btnMyAll.setEnabled(false);
            btnShowAll.setEnabled(false);}
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Вы хотите выйти с аккаунта?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }).setNegativeButton("Нет", null).show();
    }
}
