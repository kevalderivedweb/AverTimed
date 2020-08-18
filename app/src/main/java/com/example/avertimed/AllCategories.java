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

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.avertimed.API.CategoryRequest;
import com.example.avertimed.API.LoginRequest;
import com.example.avertimed.Adapter.CategoryAdapter;
import com.example.avertimed.Model.CategoryModel;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCategories extends AppCompatActivity {


    private RequestQueue requestQueue;
    private RecyclerView category_view;
    private CategoryAdapter mAdapter;
    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcategories);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        requestQueue = Volley.newRequestQueue(AllCategories.this);//Creating the RequestQueue

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        category_view = findViewById(R.id.category_view);
        mAdapter = new CategoryAdapter(categoryModels, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {
                Intent i = new Intent(AllCategories.this, SubCategories.class);
                Log.e("Category_id",""+categoryModels.get(item).getCat_id());
                i.putExtra("Category_id", categoryModels.get(item).getCat_id());
                startActivity(i);
            }
        });
        category_view.setHasFixedSize(true);
        category_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        category_view.setAdapter(mAdapter);
        category_view.setNestedScrollingEnabled(false);

        GetCategory();


    }

    public void GetCategory() {

        final KProgressHUD progressDialog = KProgressHUD.create(AllCategories.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        CategoryRequest loginRequest = new CategoryRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                   // Toast.makeText(AllCategories.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("ResponseCode").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i =0 ; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCat_id(object.getInt("CategoryID"));
                            categoryModel.setCat_name_en(object.getString("CategoryNameEn"));
                            categoryModel.setCat_name_fr(object.getString("CategoryNameFr"));
                            categoryModel.setCat_name_image(object.getString("CategoryImage"));
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
                    Toast.makeText(AllCategories.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(AllCategories.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(AllCategories.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }
}