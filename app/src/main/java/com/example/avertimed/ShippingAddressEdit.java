package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.androidnetworking.AndroidNetworking;
import com.example.avertimed.API.GetShippingAddressRequest;
import com.example.avertimed.API.SetShippingAddressRequest;
import com.example.avertimed.API.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShippingAddressEdit extends AppCompatActivity {
    private RequestQueue requestQueue;
    private UserSession session;
    private EditText SH_Name,SH_LastName,SH_Address_line_1,SH_Address_line_2,SH_State,SH_Contry,SH_PINCode;
    private int SH_ID;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        AndroidNetworking.initialize(getApplicationContext());
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestQueue = Volley.newRequestQueue(ShippingAddressEdit.this);//Creating the RequestQueue
        session = new UserSession(getApplicationContext());

        SH_Name = findViewById(R.id.sh_name);
        SH_LastName = findViewById(R.id.sh_lastname);
        SH_Address_line_1 = findViewById(R.id.sh_ad1);
        SH_Address_line_2 = findViewById(R.id.sh_ad2);
        SH_State = findViewById(R.id.sh_state);
        SH_Contry = findViewById(R.id.sh_contry);
        SH_PINCode = findViewById(R.id.sh_pincode);

        findViewById(R.id.place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SH_Name.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter First Name",Toast.LENGTH_SHORT).show();
                }else  if(SH_LastName.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter Last Name",Toast.LENGTH_SHORT).show();
                }else  if(SH_Address_line_1.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter Address Line 1",Toast.LENGTH_SHORT).show();
                }else  if(SH_Address_line_2.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter Address Line 2",Toast.LENGTH_SHORT).show();
                }else  if(SH_State.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter State",Toast.LENGTH_SHORT).show();
                }else  if(SH_Contry.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter Country",Toast.LENGTH_SHORT).show();
                }else  if(SH_PINCode.getText().toString().isEmpty()){
                    Toast.makeText(ShippingAddressEdit.this,"Please Enter Pin Code",Toast.LENGTH_SHORT).show();
                }else  {
                    SetAddress(SH_Name.getText().toString(),
                            SH_LastName.getText().toString(),
                            SH_Address_line_1.getText().toString(),
                            SH_Address_line_2.getText().toString(),
                            SH_State.getText().toString(),
                            SH_Contry.getText().toString(),SH_PINCode.getText().toString());                }

            }
        });

        GetAddress();
    }

    private void SetAddress(String name, String last, String ad1, String ad2, String state, String contry, String pin) {
       /* final KProgressHUD progressDialog = KProgressHUD.create(ShippingAddress.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        AndroidNetworking.post("http://chessmafia.com/php/Avertimed/api/update-shipping-address")
                .addBodyParameter("FirstName",name)
                .addBodyParameter("LastName", last)
                .addBodyParameter("ShippingAddress",ad1 )
                .addBodyParameter("ShippingCity", ad2)
                .addBodyParameter("ShippingState", state)
                .addBodyParameter("ShippingCountry", contry)
                .addBodyParameter("ShippingPinCode", pin)
                .addBodyParameter("ShippingLongitude", "7.00")
                .addBodyParameter("ShippingLatitude","7.00" )
                .addHeaders("Authorization",session.getAPIToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("Response", response + " null");
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e("Response", anError.getErrorBody() + " null");
                    }
                });*/

        final KProgressHUD progressDialog = KProgressHUD.create(ShippingAddressEdit.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        SetShippingAddressRequest loginRequest1 = new SetShippingAddressRequest(name,last,ad1,ad2,state,contry,pin,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Intent intent =  new Intent(ShippingAddressEdit.this, PlaceOrder.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(ShippingAddressEdit.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ShippingAddressEdit.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ShippingAddressEdit.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ShippingAddressEdit.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            Log.e("Authorization",session.getAPIToken());
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest1.setTag("TAG1");
        loginRequest1.setShouldCache(false);
        requestQueue.add(loginRequest1);
    }


    private void GetAddress() {

        final KProgressHUD progressDialog = KProgressHUD.create(ShippingAddressEdit.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetShippingAddressRequest loginRequest = new GetShippingAddressRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONObject object = jsonObject.getJSONObject("data");

                    SH_Name.setText(object.getString("FirstName"));
                    SH_LastName.setText(object.getString("LastName"));
                    SH_Address_line_1.setText(object.getString("ShippingAddress"));
                    SH_Address_line_2.setText(object.getString("ShippingCity"));
                    SH_State.setText(object.getString("ShippingState"));
                    SH_Contry.setText(object.getString("ShippingCountry"));
                    SH_PINCode.setText(object.getString("ShippingPinCode"));

                    SH_ID = object.getInt("ShippingAddressID");

                    Toast.makeText(ShippingAddressEdit.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ShippingAddressEdit.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ShippingAddressEdit.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ShippingAddressEdit.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
             params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }



}