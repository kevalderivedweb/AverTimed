package com.purchase.avertimed;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.purchase.avertimed.API.RegistrationRequest;
import com.purchase.avertimed.API.ServerUtils;
import com.purchase.avertimed.API.UserSession;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class SignUp_Activity extends AppCompatActivity {


    TextView m_login;
    ImageView m_done;
    private ImageView img2,img1;
    private EditText FirstName,LastName,Email,Password_1,Password_2;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private RequestQueue requestQueue;
    private UserSession session;
    private String mCurency;
    private Spinner mCity;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private LocationAddressResultReceiver addressResultReceiver;
    private Location currentLocation;
    private LocationCallback locationCallback;



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
        mCity = (Spinner) findViewById(R.id.city);
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
                    GetRegistration(FirstName.getText().toString(),LastName.getText().toString(),Email.getText().toString(),Password_1.getText().toString(),Password_2.getText().toString(), mCurency,session.GetFirebasetoken());
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

        GetCity();

        addressResultReceiver = new LocationAddressResultReceiver(new Handler());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            }
        };
        startLocationUpdates();


    }

    private void GetRegistration(String FirstName, String Lastname, String Email, String Password, String Password2, final String currency,String DeviceToken) {


        Log.e("currency",currency);
        final KProgressHUD progressDialog = KProgressHUD.create(SignUp_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        RegistrationRequest loginRequest = new RegistrationRequest(FirstName,Lastname,Email,Password,Password2,currency,DeviceToken, new Response.Listener<String>() {
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
                        String mCurrency = object.getString("Currency");
                        String CurrencySign = object.getString("CurrencySign");
                        int IsEnablePushNotification = object.getInt("IsEnablePushNotification");
                        int IsActive = object.getInt("IsActive");
                        session.createLoginSession(UserID,FirstName,LastName,Email,UserType,Language,Currency,APIToken,IsEnablePushNotification,IsActive,mCurrency,CurrencySign);

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
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);


    }

    public void GetCity(){
        final KProgressHUD progressDialog = KProgressHUD.create(SignUp_Activity.this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        progressDialog.show();
        AndroidNetworking.get(ServerUtils.BASE_URL+"get-currency")
                .addHeaders("Accept","application/json")
                .setTag("Feed")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response : ", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("ResponseCode")==200) {

                                try {

                                    JSONArray jsonObject1 = jsonObject.getJSONArray("data");

                                    final String[] CurrencyId = new String[jsonObject1.length()];
                                    String[] Currency = new String[jsonObject1.length()];
                                    final String[] CurrencySign = new String[jsonObject1.length()];

                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        JSONObject object = jsonObject1.getJSONObject(i);
                                        CurrencyId[i] =  object.getString("CurrencyID");
                                        Currency[i] =  object.getString("Currency");
                                        CurrencySign[i] =  object.getString("CurrencySign");
                                    }



                                    ArrayAdapter<String> adapter_age = new ArrayAdapter<String>(SignUp_Activity.this,
                                            android.R.layout.simple_spinner_item, Currency);
                                    mCity.setAdapter(adapter_age);
                                    mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view,
                                                                   int position, long id) {
                                             mCurency = CurrencyId[position];
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                            // TODO Auto-generated method stub
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else {

                                Toast.makeText(SignUp_Activity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(SignUp_Activity.this,"Unauthenticated",Toast.LENGTH_SHORT).show();}
                });

    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(SignUp_Activity.this, "Can't find current address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
            else {
                Toast.makeText(this, "Location permission not granted, " + "restart the app if you want the feature", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                Log.d("Address", "Location null retrying");
                getAddress();
            }
            if (resultCode == 1) {
                Toast.makeText(SignUp_Activity.this, "Address not found, ", Toast.LENGTH_SHORT).show();
            }
            String currentAdd = resultData.getString("address_result");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
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