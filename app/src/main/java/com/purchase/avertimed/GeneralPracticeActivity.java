package com.purchase.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.purchase.avertimed.API.ProductDetailsRequest;
import com.purchase.avertimed.API.UserSession;
import com.purchase.avertimed.Model.CategoryModel;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralPracticeActivity extends AppCompatActivity {


    private LinearLayout framelayout;
    private TextView chat1;
    private ImageView ProductImage;
    private TextView ProductTitle;
    private TextView ProductPrice;
    private String ProductDescription = "";
    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    private RequestQueue requestQueue;
    private UserSession session;
    private String recomendation = "";
    private String ProductId;
    private TextView cart;
    private DbHelper_MultipleData dbHelper;
    private List<FavDatabaseModel> DataArrayList;
    private String ProductImageUrl,ProductName;
    private String ProductPriceProduct;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));


        requestQueue = Volley.newRequestQueue(GeneralPracticeActivity.this);//Creating the RequestQueue
        session = new UserSession(getApplicationContext());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            ProductId = String.valueOf(bundle.getInt("ProductId"));
        }

        Log.e("ProductId",ProductId);



        framelayout = findViewById(R.id.framelayout);
        final View view1 = findViewById(R.id.view1);
        final View view2 = findViewById(R.id.view2);
        final View view3 = findViewById(R.id.view3);

        ProductImage = findViewById(R.id.img);
        ProductTitle = findViewById(R.id.product_title);
        ProductPrice = findViewById(R.id.price);



        view1.setBackgroundColor(getResources().getColor(R.color.white));
        view2.setBackgroundColor(getResources().getColor(R.color.sky_blue));
        view3.setBackgroundColor(getResources().getColor(R.color.white));


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.ln1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view1.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                view2.setBackgroundColor(getResources().getColor(R.color.white));
                view3.setBackgroundColor(getResources().getColor(R.color.white));
                replaceFragment(R.id.framelayout,new FirstFragment(),"Fragment");

            }
        });
        findViewById(R.id.ln2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view1.setBackgroundColor(getResources().getColor(R.color.white));
                view2.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                view3.setBackgroundColor(getResources().getColor(R.color.white));

                Bundle bundle = new Bundle();
                bundle.putString("Description", ProductDescription);
                SecondFragment fragobj = new SecondFragment();
                fragobj.setArguments(bundle);

                replaceFragment(R.id.framelayout,fragobj,"Fragment");
            }
        });

        findViewById(R.id.ln3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setBackgroundColor(getResources().getColor(R.color.white));
                view2.setBackgroundColor(getResources().getColor(R.color.white));
                view3.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                Bundle bundle = new Bundle();
                bundle.putString("recomendation", recomendation);
                ThirdFragment fragobj = new ThirdFragment();
                fragobj.setArguments(bundle);
                replaceFragment(R.id.framelayout,fragobj,"Fragment");
            }
        });


        chat1 = findViewById(R.id.chat1);
        cart = findViewById(R.id.cart);
        chat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(GeneralPracticeActivity.this,ChatDetails.class);
                startActivity(i);
            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new DbHelper_MultipleData(GeneralPracticeActivity.this);
                DataArrayList = new ArrayList<>();
                DataArrayList = dbHelper.getFav_Rec();

                for(int i =0 ; i<DataArrayList.size();i++){
                    if(DataArrayList.get(i).getId().equals(ProductId)){
                        Toast.makeText(GeneralPracticeActivity.this,"You have alredy added this product into cart.",Toast.LENGTH_SHORT).show();
                        return;
                    } }

                dbHelper.insert_Rec(ProductName,ProductId,ProductImageUrl,ProductPriceProduct,"1");
                Toast.makeText(GeneralPracticeActivity.this,"Product Insert Successfully.",Toast.LENGTH_SHORT).show();

            }
        });


        findViewById(R.id.wish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper = new DbHelper_MultipleData(GeneralPracticeActivity.this);
                DataArrayList = new ArrayList<>();
                DataArrayList = dbHelper.getFav();

                for(int i =0 ; i<DataArrayList.size();i++){
                    if(DataArrayList.get(i).getId().equals(ProductId)){
                        Toast.makeText(GeneralPracticeActivity.this,"You have alredy added this product into cart.",Toast.LENGTH_SHORT).show();
                        return;
                    } }

                dbHelper.insert_Fav(ProductName,ProductId,ProductImageUrl,ProductPriceProduct,"1");
                Toast.makeText(GeneralPracticeActivity.this,"Product Insert Successfully.",Toast.LENGTH_SHORT).show();


            }
        });
        GetProductDetails(ProductId);

        Log.e("ProductId", ProductId + " null");
    }


    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }


    public void GetProductDetails(String ProductId) {

        final KProgressHUD progressDialog = KProgressHUD.create(GeneralPracticeActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        ProductDetailsRequest loginRequest = new ProductDetailsRequest(ProductId,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    // Toast.makeText(ProductActivity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();


                    JSONObject object1 = jsonObject.getJSONObject("data");

                    Glide.with(GeneralPracticeActivity.this).load(object1.getString("ProductImage")).placeholder(R.drawable.product_1).into(ProductImage);

                    ProductTitle.setText(object1.getString("ProductTitleEn"));
                    ProductPrice.setText(session.getCurremcySign()+" "+object1.getString("Price"));


                    ProductPriceProduct = session.getCurremcySign()+" "+object1.getString("Price");

                    ProductName = object1.getString("ProductTitleEn");
                    ProductImageUrl = object1.getString("ProductImage");

                    ProductDescription = object1.getString("DescriptionEn");

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Description", ProductDescription);
                    SecondFragment fragobj = new SecondFragment();
                    fragobj.setArguments(bundle1);
                    addFragment(R.id.framelayout,fragobj,"Fragment");

                    JSONObject object = object1.getJSONObject("recomendation");




                    JSONArray jsonArray = object.getJSONArray("data");

                    recomendation = object.getJSONArray("data").toString();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                if (error instanceof ServerError)
                    Toast.makeText(GeneralPracticeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(GeneralPracticeActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(GeneralPracticeActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }




}