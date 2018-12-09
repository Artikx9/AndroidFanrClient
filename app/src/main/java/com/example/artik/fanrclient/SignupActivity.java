package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artik.fanrclient.Model.UserRegister;
import com.example.artik.fanrclient.other.FanrClient;
import com.example.artik.fanrclient.other.Helper;
import com.example.artik.fanrclient.service.RecipeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignupActivity extends Activity {

    Button button;
    EditText password,username,email;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        button = findViewById(R.id.btnRegister);
        password =findViewById(R.id.reg_password);
        username =findViewById(R.id.reg_username);
        email =findViewById(R.id.reg_email);
        login = findViewById(R.id.link_to_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btnRegister:
                        SignUp(username.getText().toString(),password.getText().toString(), email.getText().toString());
                        break;
                }
            }
        });
    }

    public void SignUp(String username, String password, String email){

       new RecipeService(this).signUp(username,password,email)
        .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200)
                {
                   new Helper(SignupActivity.this).setToken(response.body());
                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                }
                else {Toast.makeText(SignupActivity.this, "Such data is already in use", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSingIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
