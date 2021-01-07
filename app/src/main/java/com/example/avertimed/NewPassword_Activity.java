package com.example.avertimed;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.avertimed.API.LoginRequest;
import com.example.avertimed.API.LoginSocialRequest;
import com.example.avertimed.API.NewPasswordRequest;
import com.example.avertimed.API.UserSession;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class NewPassword_Activity extends AppCompatActivity {



    ImageView m_done;
    private EditText new_pass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText new_pass2;
    private RequestQueue requestQueue;
    private UserSession session;
    private String EmailId;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        session = new UserSession(getApplicationContext());
        requestQueue = Volley.newRequestQueue(NewPassword_Activity.this);//Creating the RequestQueue



        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            EmailId= null;
        } else {
            EmailId= extras.getString("email");
        }




        m_done = findViewById(R.id.m_done);
        new_pass = findViewById(R.id.email_id);
        new_pass2 = findViewById(R.id.password);







        m_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new_pass.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                }else if(new_pass.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                }else if(!new_pass2.getText().toString().equals(new_pass.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Enter correct Password",Toast.LENGTH_SHORT).show();
                }else {
                    GetLogin(EmailId,new_pass.getText().toString(),new_pass2.getText().toString());
                }

            }
        });

    }



    public void GetLogin(String Email,String newpass,String Password) {

        final KProgressHUD progressDialog = KProgressHUD.create(NewPassword_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        NewPasswordRequest loginRequest = new NewPasswordRequest(Email,newpass,Password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(NewPassword_Activity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewPassword_Activity.this,Login_Activity.class);
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
                    Toast.makeText(NewPassword_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(NewPassword_Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(NewPassword_Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

}