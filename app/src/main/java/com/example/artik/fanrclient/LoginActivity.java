package com.example.artik.fanrclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artik.fanrclient.other.Helper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {
    public static String SOAP_RESP="";
    Button button;
    EditText password,username;
    TextView register;

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Нажмите еще раз чтобы выйти",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }, 3 * 1000);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.link_to_register);
        button = findViewById(R.id.btnLogin);
        password =findViewById(R.id.edtPassword);
        username =findViewById(R.id.edtLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btnLogin:
                        try {
                            new SoapAsyncTask().execute(username.getText().toString(), password.getText().toString()).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this,SOAP_RESP, Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }


    public void onLogin(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    private class SoapAsyncTask extends AsyncTask<String, Void, String> {

        String request;

        @Override
        protected String doInBackground(String... params) {
            request = SOAPRequest(params[0], params[1]);
            if(request != null)
            {
                SOAP_RESP ="Authorization successful";
              new Helper(LoginActivity.this).setToken(request);
              startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }

        private String SOAPRequest(String username,String pass) {
            String SOAP_ACTION = "http://artik.com/soap/getLoginRequest";
            String METHOD_NAME = "getLoginRequest";
            String NAMESPACE = "http://artik.com/soap";
            String URL = Helper.HOST + "soap/login.wsdl";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            PropertyInfo info1 = new PropertyInfo();
            info1.setName("username");
            info1.setValue(username);
            info1.setType(String.class);
            PropertyInfo info2 = new PropertyInfo();
            info2.setName("password");
            info2.setValue(pass);
            info2.setType(String.class);
            request.addProperty(info1);
            request.addProperty(info2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            try {
                httpTransportSE.debug = true;
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive ) envelope.getResponse();
                return  response.toString();
            } catch (Exception e) {
                SOAP_RESP = e.getMessage();
            }

            return null;
        }
    }



}
