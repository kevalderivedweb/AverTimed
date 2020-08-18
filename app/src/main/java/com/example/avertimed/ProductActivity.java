package com.example.avertimed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.avertimed.API.CategoryRequest;
import com.example.avertimed.API.HomeRequest;
import com.example.avertimed.API.ProductRequest;
import com.example.avertimed.Adapter.NewProductAdapter;
import com.example.avertimed.Model.CategoryModel;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

public class ProductActivity extends AppCompatActivity {


    private TabLayout indicator;

    private String[] name_arry = {"Primary Care","Primary Care","Primary Care","Primary Care","Primary Care","Primary Care"};
    private String[] Product_name = {"Jeeback G2 Teens...","Andon BPS...","Jeeback G2 Teens...","Jeeback G2 Teens...","Jeeback G2 Teens...","Doctor"};
    private String[] Price_array = {"$61","$70","$77","$79","$70","$778"};
    private int[] img_arry = {R.drawable.product_1,R.drawable.product_2,R.drawable.product_3,R.drawable.product_4,R.drawable.product_5,R.drawable.product_6};
    private RecyclerView c_rvreq,t_rvreq;
    private SugestAdapter mAdapter;
    private SugestAdapter2 mAdapter_2;
    private LinearLayout category;
    private LinearLayout top_trend;
    private TextView search_activity;
    private LinearLayout profile;
    private LinearLayout transaction;
    private LinearLayout ln_message;
    private LinearLayout ln_category;
    private RequestQueue requestQueue;
    private ArrayList<CategoryModel> categoryModels_Imageview = new ArrayList<>();
    private RecyclerView new_product;
    private NewProductAdapter New_Product_mAdapter;
    private LinearLayout newproduct;
    private ImageAdapter adapterView;
    private ArrayList<CategoryModel> categoryModels_Cat=  new ArrayList<>();
    private ArrayList<CategoryModel> categoryModels_NewProduct = new ArrayList<>();
    private ArrayList<CategoryModel> categoryModels_TopTrend=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator=(TabLayout)findViewById(R.id.indicator);
        category=(LinearLayout)findViewById(R.id.category);
        top_trend=(LinearLayout)findViewById(R.id.top_trend);
        search_activity=(TextView) findViewById(R.id.search_activity);
        profile=(LinearLayout) findViewById(R.id.profile);
        ln_message=(LinearLayout) findViewById(R.id.ln_message);
        ln_category=(LinearLayout) findViewById(R.id.ln_category);

        transaction = findViewById(R.id.transaction);

        requestQueue = Volley.newRequestQueue(ProductActivity.this);//Creating the RequestQueue


        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ProductActivity.this,PlaceOrder.class);
                startActivity(i);
            }
        });

        ln_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ProductActivity.this,MessageUserList.class);
                startActivity(i);
            }
        });

        ln_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ProductActivity.this,AllCategories.class);
                startActivity(i);
            }
        });

        findViewById(R.id.msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ProductActivity.this,MessageUserList.class);
                startActivity(i);
            }
        });

/*

        product4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ProductActivity.this,GeneralPracticeActivity.class);
                startActivity(i);

            }
        });
*/



        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ProductActivity.this,TransactionActivity.class);
                startActivity(i);

            }
        });





        adapterView = new ImageAdapter(this,categoryModels_Imageview);
        mViewPager.setAdapter(adapterView);
        indicator.setupWithViewPager(mViewPager, true);

        c_rvreq = findViewById(R.id.c_rvreq);
        t_rvreq = findViewById(R.id.t_rvreq);
        new_product = findViewById(R.id.new_product);
        newproduct = findViewById(R.id.newproduct);
        new_product.setHasFixedSize(true);
        new_product.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        New_Product_mAdapter = new NewProductAdapter(categoryModels_NewProduct, new NewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

            }
        });
        new_product.setAdapter(New_Product_mAdapter);
        new_product.setNestedScrollingEnabled(true);
        mAdapter = new SugestAdapter(categoryModels_Cat, new SugestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

            }
        });
        c_rvreq.setHasFixedSize(true);
        c_rvreq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        c_rvreq.setAdapter(mAdapter);
        c_rvreq.setNestedScrollingEnabled(true);

        mAdapter_2 = new SugestAdapter2(categoryModels_TopTrend,new SugestAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

            }
        });
        t_rvreq.setHasFixedSize(true);
        t_rvreq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        t_rvreq.setAdapter(mAdapter_2);
        t_rvreq.setNestedScrollingEnabled(false);


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this,AllCategories.class);
                startActivity(intent);
            }
        });


        top_trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this,AllCategories.class);
                startActivity(intent);
            }
        });


       /* findViewById(R.id.newproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this,AllProduct.class);
                startActivity(intent);
            }
        });*/

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        newproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this,AllProduct.class);
                startActivity(intent);
            }
        });

        search_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        GetCategory();


    }

    public void GetCategory() {

        final KProgressHUD progressDialog = KProgressHUD.create(ProductActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        HomeRequest loginRequest = new HomeRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    // Toast.makeText(ProductActivity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("ResponseCode")==200) {

                        JSONObject object = jsonObject.getJSONObject("data");

                        JSONArray header_products = object.getJSONArray("header-products");
                        JSONArray categories = object.getJSONArray("categories");
                        JSONArray top_trends = object.getJSONArray("top-trends");
                        JSONArray new_products = object.getJSONArray("new-products");

                        for(int i =0 ; i<header_products.length();i++){
                            JSONObject object_header_products = header_products.getJSONObject(i);
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCat_id(object_header_products.getInt("ProductID"));
                            categoryModel.setCat_name_en(object_header_products.getString("ProductTitleEn"));
                          //  categoryModel.setCat_name_fr(object_header_products.getString("CategoryNameFr"));
                            categoryModel.setCat_name_image(object_header_products.getString("ProductImage"));
                            categoryModels_Imageview.add(categoryModel);
                        }


                        for(int i =0 ; i<categories.length();i++){
                            JSONObject object_categories = categories.getJSONObject(i);
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCat_id(object_categories.getInt("CategoryID"));
                            categoryModel.setCat_name_en(object_categories.getString("CategoryNameEn"));
                            categoryModel.setCat_name_fr(object_categories.getString("CategoryNameFr"));
                            categoryModel.setCat_name_image(object_categories.getString("CategoryImage"));
                            categoryModels_Cat.add(categoryModel);
                        }

                        for(int i =0 ; i<new_products.length();i++){
                            JSONObject object_new_products = new_products.getJSONObject(i);
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCat_id(object_new_products.getInt("ProductID"));
                            categoryModel.setCat_name_en(object_new_products.getString("ProductTitleEn"));
                            categoryModel.setCat_name_image(object_new_products.getString("ProductImage"));
                            categoryModel.setDescription(object_new_products.getString("DescriptionEn"));
                            categoryModel.setTxt_price(object_new_products.getString("Price"));
                            categoryModels_NewProduct.add(categoryModel);
                        }

                        for(int i =0 ; i<top_trends.length();i++){
                            JSONObject object_top_trends = top_trends.getJSONObject(i);
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCat_id(object_top_trends.getInt("ProductID"));
                            categoryModel.setCat_name_en(object_top_trends.getString("ProductTitleEn"));
                            categoryModel.setCat_name_image(object_top_trends.getString("ProductImage"));
                            categoryModel.setDescription(object_top_trends.getString("DescriptionEn"));
                            categoryModel.setTxt_price(object_top_trends.getString("Price"));
                            categoryModels_TopTrend.add(categoryModel);
                        }

                        adapterView.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                        New_Product_mAdapter.notifyDataSetChanged();
                        mAdapter_2.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(ProductActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(ProductActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ProductActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ProductActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }



}