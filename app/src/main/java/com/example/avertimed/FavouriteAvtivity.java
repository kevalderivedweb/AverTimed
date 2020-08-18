package com.example.avertimed;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class FavouriteAvtivity extends AppCompatActivity {

    private ImageView minus_bg,plus_bg;
    private EditText edt_qt;
    private int quantity;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
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

        minus_bg = findViewById(R.id.minus_bg);
        edt_qt = findViewById(R.id.edt_qt);
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

    public void decrement() {
        if (quantity > 1) {
            quantity--;
            edt_qt.setText(String.valueOf(quantity));
        }
    }

    public void increment() {
        if (quantity < 500) {
            quantity++;
            edt_qt.setText(String.valueOf(quantity));
        }
    }

}