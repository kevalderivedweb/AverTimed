package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.avertimed.API.UserSession;

public class TransactionActivity extends AppCompatActivity {


    private UserSession userSession;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
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
        userSession = new UserSession(getApplicationContext());
        LinearLayout ln_home = findViewById(R.id.ln_home);
        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(TransactionActivity.this,ProductActivity.class);
                startActivity(i);

            }
        });
        LinearLayout transaction = findViewById(R.id.transaction);
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(TransactionActivity.this,TransactionActivity.class);
                startActivity(i);


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

            }
        });
        LinearLayout ln_category=(LinearLayout) findViewById(R.id.ln_category);
        ln_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(TransactionActivity.this,AllCategories.class);
                startActivity(i);

            }
        });


    }
}