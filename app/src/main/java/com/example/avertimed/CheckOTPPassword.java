package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.avertimed.API.CheckOTPRequest;
import com.example.avertimed.API.ForgotRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckOTPPassword extends AppCompatActivity {


    private ImageView m_done;
    private RequestQueue requestQueue;
    private TextView email_text;
    private EditText et1,et2,et3,et4;
    private String EmailId="";
    private String OTP;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_otp);
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


        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            EmailId= null;
        } else {
            EmailId= extras.getString("email");
        }


        email_text = findViewById(R.id.email_text);
        et1 = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        email_text.setText("We have sent an email on "+EmailId+" please check your email.");
        m_done = findViewById(R.id.m_done);
        requestQueue = Volley.newRequestQueue(CheckOTPPassword.this);//Creating the RequestQueue

        m_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString().isEmpty()){
                    Toast.makeText(CheckOTPPassword.this,"Please enter OTP",Toast.LENGTH_SHORT).show();

                }else if(et2.getText().toString().isEmpty()){
                    Toast.makeText(CheckOTPPassword.this,"Please enter OTP",Toast.LENGTH_SHORT).show();

                }else if(et3.getText().toString().isEmpty()){
                    Toast.makeText(CheckOTPPassword.this,"Please enter OTP",Toast.LENGTH_SHORT).show();

                }else if(et4.getText().toString().isEmpty()){
                    Toast.makeText(CheckOTPPassword.this,"Please enter OTP",Toast.LENGTH_SHORT).show();

                }else {
                    OTP = et1.getText().toString()+
                            et2.getText().toString()+
                            et3.getText().toString()+
                            et4.getText().toString();

                    Check_Email(EmailId,OTP);
                }
            }
        });

        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));



    }

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.editText1:
                    if(text.length()==1)
                        et2.requestFocus();
                    break;
                case R.id.editText2:
                    if(text.length()==1)
                        et3.requestFocus();
                    else if(text.length()==0)
                        et1.requestFocus();
                    break;
                case R.id.editText3:
                    if(text.length()==1)
                        et4.requestFocus();
                    else if(text.length()==0)
                        et2.requestFocus();
                    break;
                case R.id.editText4:
                    if(text.length()==0)
                        et3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }


    private void Check_Email(String Email,String OTP) {
        final KProgressHUD progressDialog = KProgressHUD.create(CheckOTPPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        CheckOTPRequest loginRequest = new CheckOTPRequest(Email,OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(CheckOTPPassword.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CheckOTPPassword.this,NewPassword_Activity.class);
                    intent.putExtra("email",EmailId);
                    startActivity(intent);
                    finish();
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
                    Toast.makeText(CheckOTPPassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(CheckOTPPassword.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(CheckOTPPassword.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);
    }
}