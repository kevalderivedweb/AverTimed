package com.purchase.avertimed;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.purchase.avertimed.API.ForgotRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.purchase.avertimed.API.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ForgotPassword extends AppCompatActivity {


    private EditText Forgot;
    private ImageView m_done;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private RequestQueue requestQueue;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Forgot = findViewById(R.id.ed_forgot);
        m_done = findViewById(R.id.m_done);
        requestQueue = Volley.newRequestQueue(ForgotPassword.this);//Creating the RequestQueue

        m_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Forgot.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();
                }else if (!Forgot.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }else {
                    
                        GetForgotPassword(Forgot.getText().toString());
                }        
            }
        });

    }

    private void GetForgotPassword(final String Email) {
        final KProgressHUD progressDialog = KProgressHUD.create(ForgotPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ForgotRequest loginRequest = new ForgotRequest(Email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(ForgotPassword.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgotPassword.this,CheckOTPPassword.class);
                    intent.putExtra("email",Email);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(ForgotPassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ForgotPassword.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ForgotPassword.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);
    }


    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserSession userSession = new UserSession(this);
        setLocale(userSession.getLanguageCode());
    }
}