package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.avertimed.API.ChangePasswordRequest;
import com.example.avertimed.API.UpdateProfile;
import com.example.avertimed.API.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {


    private TextView first_name,last_name,email;
    private RequestQueue requestQueue;
    private UserSession session;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileedit);
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

        TextView Update = findViewById(R.id.update);
        first_name = findViewById(R.id.fs);
        last_name = findViewById(R.id.ls);
        email = findViewById(R.id.email);

        requestQueue = Volley.newRequestQueue(EditProfile.this);//Creating the RequestQueue


        session = new UserSession(getApplicationContext());


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(first_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter First Name",Toast.LENGTH_SHORT).show();
                }else if(last_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Last Name",Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Email Id",Toast.LENGTH_SHORT).show();
                }else {
                    GetChangePassword(first_name.getText().toString(),last_name.getText().toString(), email.getText().toString());

                }
            }
        });

    }

    private void GetChangePassword(String oldPassword, String newPasseord, String confrimPassword) {


        final KProgressHUD progressDialog = KProgressHUD.create(EditProfile.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        UpdateProfile loginRequest = new UpdateProfile(oldPassword,newPasseord,confrimPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                session.logout();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(EditProfile.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("ResponseCode").equals("200")) {

                        JSONObject object = jsonObject.getJSONObject("data");
                        String UserID = object.getString("UserID");
                        String FirstName = object.getString("FirstName");
                        String LastName = object.getString("LastName");
                        String Email = object.getString("Email");
                        String UserType = object.getString("UserType");
                        String Language = object.getString("Language");
                        String Currency = object.getString("Currency");
                        String APIToken = object.getString("APIToken");
                        int IsEnablePushNotification = object.getInt("IsEnablePushNotification");
                        int IsActive = object.getInt("IsActive");
                        session.createLoginSession(UserID,FirstName,LastName,Email,UserType,Language,Currency,APIToken,IsEnablePushNotification,IsActive);


                        finish();
                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(EditProfile.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(EditProfile.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(EditProfile.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }

}