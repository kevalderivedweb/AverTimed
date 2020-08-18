package com.example.avertimed;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.avertimed.API.ChangePasswordRequest;
import com.example.avertimed.API.RequestForQTRequest;
import com.example.avertimed.API.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestForQT extends AppCompatActivity {


    private ImageView minus_bg,plus_bg;
    private EditText edt_qt;
    private int quantity;
    private UserSession session;
    private RequestQueue requestQueue;
    private EditText firstname,email,number,category,category1,msg;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestqt);
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
        requestQueue = Volley.newRequestQueue(RequestForQT.this);//Creating the RequestQueue


        session = new UserSession(getApplicationContext());

        minus_bg = findViewById(R.id.minus_bg);
        edt_qt = findViewById(R.id.edt_qt);
        plus_bg = findViewById(R.id.plus_bg);

        minus_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement();
            }
        });

        plus_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment();
            }
        });

        firstname = findViewById(R.id.firstname);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        category = findViewById(R.id.category);
        category1 = findViewById(R.id.category1);
        msg = findViewById(R.id.msg);

        findViewById(R.id.place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter first name",Toast.LENGTH_SHORT).show();
                }else  if(email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_SHORT).show();
                }else  if(number.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter number",Toast.LENGTH_SHORT).show();
                }else  if(category.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Category",Toast.LENGTH_SHORT).show();
                }else  if(category1.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter category",Toast.LENGTH_SHORT).show();
                }else  if(msg.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter message",Toast.LENGTH_SHORT).show();
                }else {
                    GetChangePassword(firstname.getText().toString(),
                            email.getText().toString(),
                            number.getText().toString(),
                            edt_qt.getText().toString(),
                            msg.getText().toString());
                }
            }
        });

    }


    private void GetChangePassword(String Firstname,String Email,String number,String qt,String msg) {


        final KProgressHUD progressDialog = KProgressHUD.create(RequestForQT.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        RequestForQTRequest loginRequest = new RequestForQTRequest(Firstname,Email,number,qt,msg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(RequestForQT.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RequestForQT.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(RequestForQT.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(RequestForQT.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            // params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }


    public void decrement() {
        if (quantity > 1) {
            quantity--;
            edt_qt.setText(String.valueOf(quantity));
        }
    }

    public void increment() {
        if (quantity < 500) {
            quantity++;
            edt_qt.setText(String.valueOf(quantity));
        }
    }

}