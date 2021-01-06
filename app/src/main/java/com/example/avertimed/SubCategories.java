package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.avertimed.API.CategoryRequest;
import com.example.avertimed.API.SubCategoryRequest;
import com.example.avertimed.API.UserSession;
import com.example.avertimed.Adapter.CategoryAdapter;
import com.example.avertimed.Adapter.SubCategoryAdapter;
import com.example.avertimed.Model.CategoryModel;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategories extends AppCompatActivity {

    private RequestQueue requestQueue;
    private RecyclerView category_view;
    private SubCategoryAdapter mAdapter;
    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    private int Category_id;
    private UserSession userSession;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategories);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        userSession = new UserSession(getApplicationContext());

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestQueue = Volley.newRequestQueue(SubCategories.this);//Creating the RequestQueue

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        category_view = findViewById(R.id.category_view);
        mAdapter = new SubCategoryAdapter(categoryModels, new SubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                Intent intent= new Intent(SubCategories.this,ProductDetails.class);
                intent.putExtra("Category_id",categoryModels.get(item).getCat_id());
                startActivity(intent);
            }
        });
        category_view.setHasFixedSize(true);
        category_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        category_view.setAdapter(mAdapter);
        category_view.setNestedScrollingEnabled(false);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            Category_id = bundle.getInt("Category_id");
        }

        GetCategory(Category_id);
        
        

    }

    public void GetCategory(int Category_id) {

        Log.e("Category_id", Category_id + " null");
        final KProgressHUD progressDialog = KProgressHUD.create(SubCategories.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        SubCategoryRequest loginRequest = new SubCategoryRequest(Category_id,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    // Toast.makeText(SubCategories.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("ResponseCode").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i =0 ; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCat_id(object.getInt("SubCategoryID"));
                            categoryModel.setCat_name_en(object.getString("SubCategoryNameEn"));
                            categoryModel.setCat_name_fr(object.getString("SubCategoryNameFr"));
                            categoryModel.setCat_name_image("");
                            categoryModels.add(categoryModel);
                        }

                        mAdapter.notifyDataSetChanged();
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
                    Toast.makeText(SubCategories.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(SubCategories.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(SubCategories.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ userSession.getAPIToken());
            return params;
        }};        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

}