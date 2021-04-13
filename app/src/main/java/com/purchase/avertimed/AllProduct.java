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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.purchase.avertimed.API.ProductRequest;
import com.purchase.avertimed.API.UserSession;
import com.purchase.avertimed.Adapter.NewProductAdapter;
import com.purchase.avertimed.Model.CategoryModel;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AllProduct extends AppCompatActivity {


    private RequestQueue requestQueue;
    private RecyclerView category_view;
    private NewProductAdapter mAdapter;
    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    private UserSession session;
    private LinearLayoutManager linearLayout;
    private int IntPage = 1;
    private int last_size = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcategories);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        requestQueue = Volley.newRequestQueue(AllProduct.this);//Creating the RequestQueue

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        category_view = findViewById(R.id.category_view);
        mAdapter = new NewProductAdapter(AllProduct.this,categoryModels, new NewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {


                Intent intent = new Intent(AllProduct.this,GeneralPracticeActivity.class);
                intent.putExtra("ProductId",categoryModels.get(item).getCat_id());
                startActivity(intent);
            }
        });
        category_view.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        category_view.setLayoutManager(linearLayout);
        category_view.setAdapter(mAdapter);
        category_view.setNestedScrollingEnabled(false);
        session = new UserSession(getApplicationContext());
        GetProduct(IntPage);
        category_view.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                IntPage = page;
                if (page!=last_size){
                    int FinalOAgeSIze = page+1;
                    GetProduct(FinalOAgeSIze);
                }
            }
        });

    }

    public void GetProduct(int page) {

        final KProgressHUD progressDialog = KProgressHUD.create(AllProduct.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        ProductRequest loginRequest = new ProductRequest(page,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    // Toast.makeText(ProductActivity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();


                    JSONObject object1 = jsonObject.getJSONObject("data");
                    last_size = object1.getInt("last_page");

                    JSONArray jsonArray = object1.getJSONArray("data");

                    for(int i =0 ; i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCat_id(object.getInt("ProductID"));
                        categoryModel.setCat_name_en(object.getString(session.getProductTitle()));
                        categoryModel.setCat_name_image(object.getString("ProductImage"));
                        categoryModel.setDescription(object.getString(session.getDescription()));
                        categoryModel.setTxt_price(object.getString("Price"));
                        categoryModels.add(categoryModel);
                    }

                    mAdapter.notifyDataSetChanged();

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
                    Toast.makeText(AllProduct.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(AllProduct.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(AllProduct.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }

        };        loginRequest.setTag("TAG");
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