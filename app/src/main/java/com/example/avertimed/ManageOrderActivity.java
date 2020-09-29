package com.example.avertimed;

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
import com.example.avertimed.API.GetCurrentOrderRequest;
import com.example.avertimed.API.UserSession;
import com.example.avertimed.Adapter.CurrentProductAdapter;
import com.example.avertimed.Adapter.SubProductAdapter;
import com.example.avertimed.Model.OrderModel;
import com.example.avertimed.Model.Product;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageOrderActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private UserSession session;
    private ArrayList<OrderModel> orderModels =  new ArrayList<>();
    private ArrayList<Product> ProductModels =  new ArrayList<>();
    private CurrentProductAdapter mAdapter;
    private RecyclerView category_view;

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

        category_view = findViewById(R.id.category_view);
        mAdapter = new CurrentProductAdapter(orderModels, new CurrentProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

            }
        });
        category_view.setHasFixedSize(true);
        category_view.setLayoutManager(new LinearLayoutManager(ManageOrderActivity.this, LinearLayoutManager.VERTICAL, false));
        category_view.setAdapter(mAdapter);
        category_view.setNestedScrollingEnabled(false);

       final LinearLayout current = findViewById(R.id.current);
       final LinearLayout past = findViewById(R.id.past);
       final TextView txtcurrent = findViewById(R.id.txt_current);
       final TextView txtpast = findViewById(R.id.txt_past);

       current.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               current.setBackgroundColor(getResources().getColor(R.color.sky_blue));
               txtcurrent.setTextColor(getResources().getColor(R.color.white));
               past.setBackgroundColor(getResources().getColor(R.color.white));
               txtpast.setTextColor(getResources().getColor(R.color.black));
               GetAddress("get-current-orders");
           }
       });

        txtpast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.setBackgroundColor(getResources().getColor(R.color.white));
                txtcurrent.setTextColor(getResources().getColor(R.color.black));
                past.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                txtpast.setTextColor(getResources().getColor(R.color.white));
                GetAddress("get-past-orders");
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        GetAddress("get-current-orders");
    }

    private void GetAddress(String MethodName)  {

        final KProgressHUD progressDialog = KProgressHUD.create(ManageOrderActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetCurrentOrderRequest loginRequest = new GetCurrentOrderRequest(MethodName,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();
                orderModels.clear();
                ProductModels.clear();


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
                        orderModel.setOrderStatus(object1.getString("UserOrderID"));
                        orderModel.setDate(object1.getString("date"));

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
        requestQueue.add(loginRequest);

    }

}