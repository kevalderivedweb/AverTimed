package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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




        payment_comfrimation = findViewById(R.id.payment_comfrimation);
        order = findViewById(R.id.order);
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
                payment_comfrimation.setVisibility(View.VISIBLE);
                order.setVisibility(View.GONE);
            }
        });


        minus_bg = findViewById(R.id.minus_bg);
        edt_qt = findViewById(R.id.edt_qt);
        total = findViewById(R.id.total);
        plus_bg = findViewById(R.id.plus_bg);

        minus_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement();
            }
        });

        plus_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment();
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

    public void decrement() {
        if (quantity > 20) {
            quantity--;
            calculation();
            edt_qt.setText(String.valueOf(quantity));
        }
    }

    public void increment() {
        if (quantity < 500) {
            quantity++;
            calculation();
            edt_qt.setText(String.valueOf(quantity));
        }
    }

    public void calculation(){

        int sub_total = 50 * quantity ;

        total.setText("$"+sub_total);
    }

}