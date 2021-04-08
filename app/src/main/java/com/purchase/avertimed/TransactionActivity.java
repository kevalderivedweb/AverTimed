package com.purchase.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import com.purchase.avertimed.API.GetCurrentOrderRequest;
import com.purchase.avertimed.API.UserSession;
import com.purchase.avertimed.Adapter.CurrentProductAdapter;
import com.purchase.avertimed.Model.OrderModel;
import com.purchase.avertimed.Model.Product;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {


    private UserSession userSession;
    private ArrayList<OrderModel> orderModels =  new ArrayList<>();
    private RecyclerView category_view;
    private CurrentProductAdapter mAdapter;
    private RequestQueue requestQueue;
    private UserSession session;

    private LinearLayoutManager linearLayout;
    private int IntPage = 1;
    private int last_size = 0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));


        requestQueue = Volley.newRequestQueue(TransactionActivity.this);//Creating the RequestQueue


        session = new UserSession(getApplicationContext());



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userSession = new UserSession(getApplicationContext());
        LinearLayout ln_home = findViewById(R.id.ln_home);
        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(TransactionActivity.this,ProductActivity.class);
                startActivity(i);
                finish();

            }
        });
        LinearLayout transaction = findViewById(R.id.transaction);
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(TransactionActivity.this,TransactionActivity.class);
                startActivity(i);
                finish();


            }
        });
        LinearLayout profile=(LinearLayout) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userSession.isLoggedIn()){

                    Intent intent=new Intent(TransactionActivity.this, ProfileActivity.class);
                    if(getIntent().getExtras()!=null) {
                        intent.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);


                }else {

                    Intent intent=new Intent(TransactionActivity.this, Login_Activity.class);
                    if(getIntent().getExtras()!=null) {
                        intent.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);


                }
                finish();

            }
        });
        LinearLayout ln_category=(LinearLayout) findViewById(R.id.ln_category);
        ln_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(TransactionActivity.this,AllCategories.class);
                startActivity(i);
                finish();

            }
        });

        category_view = findViewById(R.id.category_view);
        mAdapter = new CurrentProductAdapter(TransactionActivity.this,orderModels, new CurrentProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

            }

            @Override
            public void onItemClickSUB(int position, int item) {

                int Id = Integer.parseInt(orderModels.get(position).getProducts().get(item).getProductID());
                Intent intent = new Intent(TransactionActivity.this, GeneralPracticeActivity.class);
                intent.putExtra("ProductId",Id);
                startActivity(intent);
            }
        });
        category_view.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        category_view.setLayoutManager(linearLayout);
        category_view.setAdapter(mAdapter);
        category_view.setNestedScrollingEnabled(false);

        category_view.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                IntPage = page;
                if (page!=last_size){
                    int FinalOAgeSIze = page+1;
                    GetAddress("get-transactions",FinalOAgeSIze);
                }
            }
        });

        GetAddress("get-transactions",IntPage);
    }

    private void GetAddress(String MethodName,int page)  {

        final KProgressHUD progressDialog = KProgressHUD.create(TransactionActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetCurrentOrderRequest loginRequest = new GetCurrentOrderRequest(MethodName,page,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    last_size = jsonObject1.getInt("last_page");

                    JSONArray object = jsonObject1.getJSONArray("data");
                    for (int i= 0 ; i < object.length();i++){
                        JSONObject object1 = object.getJSONObject(i);
                        OrderModel orderModel  = new OrderModel();

                        orderModel.setUserOrderID(String.valueOf(object1.getInt("UserOrderID")));
                        orderModel.setOrderCode(object1.getString("OrderCode"));
                        orderModel.setOrderAmount(String.valueOf(object1.getInt("OrderAmount")));
                        orderModel.setTax(String.valueOf(object1.getInt("Tax")));
                        orderModel.setDiscount(String.valueOf(object1.getInt("Discount")));
                        orderModel.setShippingCharge(String.valueOf(object1.getInt("ShippingCharge")));
                        orderModel.setTotalAmount(String.valueOf(object1.getInt("TotalAmount")));
                        orderModel.setShippingCharge(object1.getString("ShippingCharge"));
                        orderModel.setOrderStatus(object1.getString("OrderStatus"));
                        orderModel.setDate(object1.getString("date"));
                        ArrayList<Product> ProductModels = new ArrayList<>();
                        JSONArray products = object.getJSONObject(i).getJSONArray("products");
                        for (int j= 0 ; j < products.length();j++){
                            JSONObject products_details = products.getJSONObject(j);
                            Product product  = new Product();

                            product.setProductID(String.valueOf(products_details.getInt("ProductID")));
                            product.setProductTitleEn(products_details.getString("ProductTitleEn"));
                            product.setProductTitleFr(products_details.getString("ProductTitleFr"));
                            product.setProductCode(products_details.getString("ProductCode"));
                            product.setPrice(String.valueOf(products_details.getInt("Price")));
                            product.setProductImage(products_details.getString("ProductImage"));
                            ProductModels.add(product);
                        }

                        orderModel.setProducts(ProductModels);
                        orderModels.add(orderModel);

                    }

                    mAdapter.notifyDataSetChanged();
                    //  Toast.makeText(ManageOrderActivity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TransactionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(TransactionActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(TransactionActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(TransactionActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
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