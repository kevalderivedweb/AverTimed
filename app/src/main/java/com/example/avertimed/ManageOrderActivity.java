package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.avertimed.API.CancelOrderRequest;
import com.example.avertimed.API.GetCurrentOrderRequest;
import com.example.avertimed.API.UserSession;
import com.example.avertimed.Adapter.CurrentProductAdapter;
import com.example.avertimed.Model.OrderModel;
import com.example.avertimed.Model.Product;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ManageOrderActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private UserSession session;
    private ArrayList<OrderModel> orderModels =  new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private int IntPage = 1;
    private int last_size = 0;
    private CurrentProductAdapter mAdapter;
    private RecyclerView category_view;
    private String Order = "0";
    private LinearLayout LN_STATUS;
    private boolean isCurrentOrder = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageorders);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        requestQueue = Volley.newRequestQueue(ManageOrderActivity.this);//Creating the RequestQueue
        session = new UserSession(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            Order =(bundle.getString("Order"));
        }

        category_view = findViewById(R.id.category_view);
        LN_STATUS = findViewById(R.id.status);
        mAdapter = new CurrentProductAdapter(ManageOrderActivity.this,orderModels, new CurrentProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                if(Objects.requireNonNull(Order).equals("1")){
                    CancelOrder(orderModels.get(item).getUserOrderID(),item);
                }

            }

            @Override
            public void onItemClickSUB(int position, int item) {
                int Id = Integer.parseInt(orderModels.get(position).getProducts().get(item).getProductID());
                Intent intent = new Intent(ManageOrderActivity.this, GeneralPracticeActivity.class);
                intent.putExtra("ProductId",Id);
                startActivity(intent);

            }
        });
        category_view.setHasFixedSize(true);
        linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        category_view.setLayoutManager(linearLayout);
        category_view.setAdapter(mAdapter);
        category_view.setNestedScrollingEnabled(false);

       final LinearLayout current = findViewById(R.id.current);
       final LinearLayout past = findViewById(R.id.past);
       final TextView txtcurrent = findViewById(R.id.txt_current);
       final TextView txtpast = findViewById(R.id.txt_past);





        if(Objects.requireNonNull(Order).equals("1")){
            LN_STATUS.setVisibility(View.GONE);
            orderModels.clear();
            GetAddress("manage-orders",IntPage);
        }else {
            orderModels.clear();
            GetAddress("get-current-orders",IntPage);
        }

       current.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               isCurrentOrder = true;
               current.setBackgroundColor(getResources().getColor(R.color.sky_blue));
               txtcurrent.setTextColor(getResources().getColor(R.color.white));
               past.setBackgroundColor(getResources().getColor(R.color.white));
               txtpast.setTextColor(getResources().getColor(R.color.black));
               orderModels.clear();
               GetAddress("get-current-orders",IntPage);
           }
       });

        txtpast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCurrentOrder = false;
                current.setBackgroundColor(getResources().getColor(R.color.white));
                txtcurrent.setTextColor(getResources().getColor(R.color.black));
                past.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                txtpast.setTextColor(getResources().getColor(R.color.white));
                orderModels.clear();
                GetAddress("get-past-orders",IntPage);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        category_view.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                IntPage = page;
                if (page!=last_size){
                    int FinalOAgeSIze = page+1;
                    if(Objects.requireNonNull(Order).equals("1")){
                        LN_STATUS.setVisibility(View.GONE);
                        GetAddress("manage-orders",FinalOAgeSIze);
                    }else {
                        if(isCurrentOrder){
                            GetAddress("get-current-orders",FinalOAgeSIze);
                        }else {
                            GetAddress("get-past-orders",FinalOAgeSIze);
                        }


                    }

                }
            }
        });

    }

    private void GetAddress(String MethodName,int page)  {

        final KProgressHUD progressDialog = KProgressHUD.create(ManageOrderActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetCurrentOrderRequest loginRequest = new GetCurrentOrderRequest(MethodName,page,new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray object = jsonObject.getJSONArray("data");
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

                        if(Objects.requireNonNull(Order).equals("1")){
                            orderModel.setOrderStatus("Cancel");
                        }else {
                            orderModel.setOrderStatus(object1.getString("OrderStatus"));
                        }

                        orderModel.setDate(object1.getString("date"));
                        ArrayList<Product> ProductModels =  new ArrayList<>();
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
                    Toast.makeText(ManageOrderActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(ManageOrderActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ManageOrderActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ManageOrderActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            // params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

    private void CancelOrder(String OrderId, final int pos)  {

        final KProgressHUD progressDialog = KProgressHUD.create(ManageOrderActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        CancelOrderRequest loginRequest = new CancelOrderRequest(OrderId,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    orderModels.remove(pos);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(ManageOrderActivity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(ManageOrderActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ManageOrderActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ManageOrderActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Accept", "application/json");
                params.put("Authorization","Bearer "+ session.getAPIToken());
                return params;
            }};
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }


}