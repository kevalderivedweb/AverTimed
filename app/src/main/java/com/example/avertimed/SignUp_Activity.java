package com.example.avertimed;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.avertimed.API.LoginRequest;
import com.example.avertimed.API.RegistrationRequest;
import com.example.avertimed.API.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp_Activity extends AppCompatActivity {


    TextView m_login;
    ImageView m_done;
    private ImageView img2,img1;
    private EditText FirstName,LastName,Email,Password_1,Password_2;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private RequestQueue requestQueue;
    private UserSession session;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

        requestQueue = Volley.newRequestQueue(SignUp_Activity.this);//Creating the RequestQueue


        session = new UserSession(getApplicationContext());

        FirstName = findViewById(R.id.ed_fristname);
        LastName = findViewById(R.id.ed_lastname);
        Email = findViewById(R.id.ed_email);
        Password_1 = findViewById(R.id.ed_password);
        Password_2 = findViewById(R.id.ed_password2);
        m_login = findViewById(R.id.m_login);
        m_done = findViewById(R.id.m_done);
        img2 = findViewById(R.id.img2);
        img1 = findViewById(R.id.img1);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img1.setImageResource(R.drawable.shape_round_fill);
                img2.setImageResource(R.drawable.shape_round_unfill);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img1.setImageResource(R.drawable.shape_round_unfill);
                img2.setImageResource(R.drawable.shape_round_fill);
            }
        });

        m_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirstName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter First Name",Toast.LENGTH_SHORT).show();
                }else if(LastName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Last Name",Toast.LENGTH_SHORT).show();
                }else  if(Email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();
                }else if (!Email.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }else if(Password_1.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                }else if(!Password_1.getText().toString().equals(Password_2.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter Valid Password",Toast.LENGTH_SHORT).show();
                }else {
                    GetRegistration(FirstName.getText().toString(),LastName.getText().toString(),Email.getText().toString(),Password_1.getText().toString(),Password_2.getText().toString());
                }
            }
        });

        m_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp_Activity.this,SignUp_Activity.class);
                startActivity(i);
            }
        });
    }

    private void GetRegistration(String FirstName, String Lastname, String Email, String Password, String Password2) {


        final KProgressHUD progressDialog = KProgressHUD.create(SignUp_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        RegistrationRequest loginRequest = new RegistrationRequest(FirstName,Lastname,Email,Password,Password2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(SignUp_Activity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
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

                        Intent intent = new Intent(SignUp_Activity.this,ProductActivity.class);
                        startActivity(intent);
                        finish();
                    }
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
                    Toast.makeText(SignUp_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(SignUp_Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(SignUp_Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);


    }
}