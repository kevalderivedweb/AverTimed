package com.example.avertimed;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.avertimed.API.AndroidMultiPartEntity;
import com.example.avertimed.API.CancelOrderRequest;
import com.example.avertimed.API.CreateOrderRequest;
import com.example.avertimed.API.PlaceOrderRequest;
import com.example.avertimed.API.ServerUtils;
import com.example.avertimed.API.UserSession;
import com.example.avertimed.Adapter.ViewCartAdapter;
import com.example.avertimed.Adapter.ViewCartAdapter2;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity {


    private RelativeLayout payment_comfrimation;
    private ImageView close;
    private TextView pay_now;
    private TextView place_order;
    private LinearLayout order;
    private ImageView minus_bg,plus_bg;
    private EditText edt_qt;
    private int quantity = 20;
    private TextView total;
    private DbHelper_MultipleData dbHelper;
    private List<FavDatabaseModel> DataArrayList;
    private RecyclerView category_view;
    private ViewCartAdapter2 mAdapter;
    private RequestQueue requestQueue;
    private UserSession session;
    private TextView UserAddress;
    private TextView ProductPrice,SHIPPING_COAST,TOTALAMOUNT;
    private String ShippingAddressID="0",OrderAmount="0",TotalAmount="0",Tax="0",Discount="0",ShippingCharge="0";
    private KProgressHUD progressDialog;
    private EditText coupon_code;
    private TextView apply;
    private String CouponID = "";
    private TextView discount;
    private LinearLayout discoundln;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
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


        requestQueue = Volley.newRequestQueue(PlaceOrder.this);//Creating the RequestQueue
        session = new UserSession(getApplicationContext());


        dbHelper = new DbHelper_MultipleData(PlaceOrder.this);
        DataArrayList = new ArrayList<>();
        DataArrayList = dbHelper.getFav_Rec();



        category_view = findViewById(R.id.category_view);
        mAdapter = new ViewCartAdapter2(DataArrayList, new ViewCartAdapter2.OnItemClickListener() {
            @Override
            public void onItemClickPlus(String item, int position) {
               // dbHelper.Update(DataArrayList.get(position).getId(),item);
            }

            @Override
            public void onItemClickMinus(String item, int position) {
               // dbHelper.Update(DataArrayList.get(position).getId(),item);
            }
        });
        category_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        category_view.setAdapter(mAdapter);






        payment_comfrimation = findViewById(R.id.payment_comfrimation);
        order = findViewById(R.id.order);
        discount = findViewById(R.id.discount);
        discoundln = findViewById(R.id.discoundln);
        UserAddress = findViewById(R.id.address);
        coupon_code = findViewById(R.id.coupon_code);
        apply = findViewById(R.id.apply);
        ProductPrice = findViewById(R.id.product_price);
        SHIPPING_COAST = findViewById(R.id.shipping_coast);
        TOTALAMOUNT = findViewById(R.id.total);
        place_order = findViewById(R.id.place_order);
        pay_now = findViewById(R.id.pay_now);
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment_comfrimation.setVisibility(View.GONE);
                order.setVisibility(View.VISIBLE);
            }
        });

        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceOrder.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*payment_comfrimation.setVisibility(View.VISIBLE);
                order.setVisibility(View.GONE);*/

                new CreateOrderToServer().execute();
                //CreateOrder(ShippingAddressID,"",OrderAmount,Tax,Discount,ShippingCharge,TotalAmount,DataArrayList);
            }
        });


        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(PlaceOrder.this, ShippingAddressEdit.class);
                startActivity(intent);
            }
        });

        Log.e("Size",""+DataArrayList.size());
        //PlaceOrderRe(DataArrayList);


        new UploadFileToServer().execute();
      /*  AndroidNetworking.post("http://chessmafia.com/php/Avertimed/api/get-place-order")
                .addBodyParameter("ProductID[]","4")
                .addBodyParameter("Qty[]","10")
                .addHeaders("Authorization",session.getAPIToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Response", response + " null");
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Response", anError.getErrorCode() + " null");
                    }PlaceOrderRe
                });*/


      apply.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(coupon_code.getText().toString().isEmpty()){
                  Toast.makeText(PlaceOrder.this,"Please Enter Coupon Code...",Toast.LENGTH_SHORT).show();
              }else {
                  new UploadFileToServer().execute();
              }
          }
      });


    }

    @Override
    public void onBackPressed() {

        if(payment_comfrimation.getVisibility()==View.VISIBLE)
        {
            payment_comfrimation.setVisibility(View.GONE);
            order.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();

    }


    private void PlaceOrderRe(List<FavDatabaseModel> DataArrayList)  {

        final KProgressHUD progressDialog = KProgressHUD.create(PlaceOrder.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        PlaceOrderRequest loginRequest = new PlaceOrderRequest(DataArrayList,new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();


                JSONObject OrderResponse = null;
                try {
                    OrderResponse = new JSONObject(response);

                    JSONObject Data = OrderResponse.getJSONObject("data");
                    if(Data.isNull("shipping_address")){
                        Toast.makeText(PlaceOrder.this,"Please Enter your Address",Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(PlaceOrder.this, ShippingAddressEdit.class);
                        startActivity(intent);
                    }
                    JSONObject jsonObject = Data.getJSONObject("shipping_address");
                    UserAddress.setText(jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName") + "\n"+
                            jsonObject.getString("ShippingAddress")+"\n"+
                            jsonObject.getString("ShippingCity")+"\n"+
                            jsonObject.getString("ShippingState")+"\n"+
                            jsonObject.getString("ShippingCountry")+"\n"+
                            jsonObject.getString("ShippingPinCode")+"\n"
                            );

                    ProductPrice.setText(" $ "+Data.getString("OrderAmount"));
                    SHIPPING_COAST.setText(" $ "+Data.getString("ShippingCharge"));
                    TOTALAMOUNT.setText(" $ "+Data.getString("TotalAmount"));
                    discount.setText("$ "+Data.getString("Discount"));

                    if(Data.getString("Discount").equals("0.00")){
                        discoundln.setVisibility(View.GONE);
                    }else {
                        discoundln.setVisibility(View.VISIBLE);
                    }
                    ShippingAddressID = jsonObject.getString("ShippingAddressID");
                    OrderAmount = Data.getString("OrderAmount");
                    ShippingCharge = Data.getString("ShippingCharge");
                    TotalAmount = Data.getString("TotalAmount");
                    Tax = Data.getString("Tax");
                    Discount = Data.getString("Discount");
                    //CouponID = jsonObject.getString("CouponID");

                }catch (Exception e){

                 //   Toast.makeText(PlaceOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(PlaceOrder.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(PlaceOrder.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(PlaceOrder.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
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

    private void CreateOrder(String ShippingAddressID,String CoupanId,String OrderAmount,String Tax,String Discount,String ShippingCharge,String TotalAmount,List<FavDatabaseModel> DataArrayList)  {

        final KProgressHUD progressDialog = KProgressHUD.create(PlaceOrder.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        CreateOrderRequest loginRequest = new CreateOrderRequest(ShippingAddressID,CoupanId,OrderAmount,Tax,Discount,ShippingCharge,TotalAmount,DataArrayList,new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();


                JSONObject OrderResponse = null;
                try {
                    OrderResponse = new JSONObject(response);
                    dbHelper.DeleteAll();
                    Toast.makeText(PlaceOrder.this,OrderResponse.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PlaceOrder.this,ProductActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){

                    Toast.makeText(PlaceOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(PlaceOrder.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(PlaceOrder.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(PlaceOrder.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
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



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL+"get-place-order";

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
          progressDialog = KProgressHUD.create(PlaceOrder.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);

            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {}
                        });
                for (int i = 0; i < DataArrayList.size(); i++) {
                    entity.addPart("ProductID[]", new StringBody(DataArrayList.get(i).getId()));
                    entity.addPart("Qty[]", new StringBody(DataArrayList.get(i).getQT()));
                }

                entity.addPart("CouponCode", new StringBody(coupon_code.getText().toString()));

                httppost.setEntity(entity);
                httppost.addHeader("Authorization","Bearer "+session.getAPIToken());
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            Log.e("TAG", "Response from server: " + result);
            progressDialog.dismiss();
            Log.e("responseresult",result);
            // showing the server response in an alert categoryDialog
            //showAlert(result);
            try {
                JSONObject OrderResponse = new JSONObject(result);

                if (OrderResponse.getInt("ResponseCode")==200) {

                    JSONObject Data = OrderResponse.getJSONObject("data");
                    if(Data.isNull("shipping_address")){
                        Toast.makeText(PlaceOrder.this,"Please Enter your Address",Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(PlaceOrder.this, ShippingAddressEdit.class);
                        startActivity(intent);
                    }
                    JSONObject jsonObject = Data.getJSONObject("shipping_address");
                    UserAddress.setText(jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName") + "\n"+
                            jsonObject.getString("ShippingAddress")+"\n"+
                            jsonObject.getString("ShippingCity")+"\n"+
                            jsonObject.getString("ShippingState")+"\n"+
                            jsonObject.getString("ShippingCountry")+"\n"+
                            jsonObject.getString("ShippingPinCode")+"\n"
                    );

                    ProductPrice.setText(" $ "+Data.getString("OrderAmount"));
                    SHIPPING_COAST.setText(" $ "+Data.getString("ShippingCharge"));
                    TOTALAMOUNT.setText(" $ "+Data.getString("TotalAmount"));

                    ShippingAddressID = jsonObject.getString("ShippingAddressID");
                    OrderAmount = Data.getString("OrderAmount");
                    //hippingCharge = Data.getString("ShippingCharge");
                    TotalAmount = Data.getString("TotalAmount");
                    Tax = Data.getString("Tax");
                   Discount = Data.getString("Discount");
                   CouponID = Data.getString("CouponID");

                    if(Data.getString("Discount").equals("0.00")){
                        discoundln.setVisibility(View.GONE);
                        discount.setText("$ "+Discount);
                    }else {
                        discoundln.setVisibility(View.VISIBLE);
                        discount.setText("$ "+Discount);
                    }


                }else{
                    Toast.makeText(PlaceOrder.this,OrderResponse.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(PlaceOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(result);
        }

    }


    private class CreateOrderToServer extends AsyncTask<Void, Integer, String> {
        private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL+"create-order";

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            progressDialog = KProgressHUD.create(PlaceOrder.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);

            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {}
                        });

                httppost.addHeader("Authorization","Bearer "+session.getAPIToken());
                for (int i = 0; i < DataArrayList.size(); i++) {
                    entity.addPart("ProductID[]", new StringBody(DataArrayList.get(i).getId()));
                    entity.addPart("Qty[]", new StringBody(DataArrayList.get(i).getQT()));
                }

                entity.addPart("ShippingAddressID",  new StringBody(ShippingAddressID));
                if(!coupon_code.getText().toString().isEmpty()){
                    entity.addPart("CouponID", new StringBody(CouponID));
                }
                entity.addPart("OrderAmount",  new StringBody(OrderAmount.replace(",","")));
                entity.addPart("Tax",  new StringBody(Tax.replace(",","")));
                entity.addPart("Discount", new StringBody(Discount.replace(",","")));
                entity.addPart("ShippingCharge",  new StringBody("0"));
                entity.addPart("TotalAmount",  new StringBody(TotalAmount.replace(",","")));


                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            Log.e("TAG", "Response from server: " + result);
            progressDialog.dismiss();
            // showing the server response in an alert categoryDialog
            //showAlert(result);

            Log.e("responseresult",result);
            try {
                JSONObject  OrderResponse = new JSONObject(result);
                dbHelper.DeleteAll();
                Toast.makeText(PlaceOrder.this,OrderResponse.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlaceOrder.this,ProductActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e){

                Toast.makeText(PlaceOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(result);
        }

    }



}