package com.purchase.avertimed;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.purchase.avertimed.API.ProfileRequest;
import com.purchase.avertimed.API.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageView setting;
    ImageView img;
    private LinearLayout manage_order;
    private LinearLayout ln_shipping;
    private LinearLayout ln_logout;
    private RequestQueue requestQueue;
    private UserSession session;
    private DbHelper_MultipleData dbHelper;
    private List<FavDatabaseModel> DataArrayList;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        final UserSession userSession = new UserSession(getApplicationContext());

        requestQueue = Volley.newRequestQueue(ProfileActivity.this);//Creating the RequestQueue


        session = new UserSession(getApplicationContext());


        dbHelper = new DbHelper_MultipleData(ProfileActivity.this);
        DataArrayList = new ArrayList<>();
        DataArrayList = dbHelper.getFav_Rec();


        setting = findViewById(R.id.setting);
        img = findViewById(R.id.img);
        LinearLayout cart = findViewById(R.id.cart);
        manage_order = findViewById(R.id.manage_order);
        ln_shipping = findViewById(R.id.ln_shipping);
        ln_logout = findViewById(R.id.ln_logout);
        TextView cart_count = findViewById(R.id.cart_count);

        int Size = DataArrayList.size();
        cart_count.setText("" + Size);

        Log.e("Size", "" + Size);

        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Please contact us on : ", Toast.LENGTH_SHORT).show();

            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ViewCart.class);
                startActivity(intent);
            }
        });
        ln_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.logout();
                Intent intent = new Intent(ProfileActivity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);

            }
        });
        ln_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ShippingAddress.class);
                startActivity(intent);
            }
        });

        manage_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, ManageOrderActivity.class);
                i.putExtra("Order", "1");
                startActivity(i);
            }
        });

        findViewById(R.id.qt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, RequestForQT.class);
                startActivity(i);
            }
        });

        findViewById(R.id.fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, FavouriteAvtivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, ManageOrderActivity.class);
                i.putExtra("Order", "2");
                startActivity(i);
            }
        });

        LinearLayout ln_home = findViewById(R.id.ln_home);
        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, ProductActivity.class);
                startActivity(i);
                finish();

            }
        });
        LinearLayout transaction = findViewById(R.id.transaction);
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, TransactionActivity.class);
                startActivity(i);
                finish();
                finish();


            }
        });
        LinearLayout profile = (LinearLayout) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                finish();

            }
        });
        LinearLayout ln_category = (LinearLayout) findViewById(R.id.ln_category);
        ln_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, AllCategories.class);
                startActivity(i);
                finish();
                finish();

            }
        });

        Log.e("tokenId", userSession.getAPIToken());


        GetProfile();
    }

    private void GetProfile() {


        final KProgressHUD progressDialog = KProgressHUD.create(ProfileActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ProfileRequest loginRequest = new ProfileRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    TextView name = findViewById(R.id.name);
                    TextView location = findViewById(R.id.location);
                    TextView fav_count = findViewById(R.id.fav_count);
                    TextView cart_count = findViewById(R.id.cart_count);
                    TextView history_count = findViewById(R.id.history_count);
                    name.setText(jsonObject1.getString("FirstName"));
                    location.setText(jsonObject1.getString("City") + " , " + jsonObject1.getString("Country"));
                    fav_count.setText(jsonObject1.getString("FavouriteProductCnt"));
                    //cart_count.setText("0");
                    history_count.setText("0");
                    Glide.with(ProfileActivity.this).load(jsonObject1.getString("ProfilePic")).placeholder(R.drawable.abcd).circleCrop().into(img);

                    //  Toast.makeText(ProfileActivity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ProfileActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ProfileActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPIToken());
                return params;
            }
        };
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