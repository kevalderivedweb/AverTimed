package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avertimed.Adapter.ViewCartAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewCart extends AppCompatActivity {


    private DbHelper_MultipleData dbHelper;
    private List<FavDatabaseModel> DataArrayList;
    private RecyclerView category_view;
    private ViewCartAdapter mAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
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



        dbHelper = new DbHelper_MultipleData(ViewCart.this);
        DataArrayList = new ArrayList<>();
        DataArrayList = dbHelper.getFav_Rec();



        category_view = findViewById(R.id.category_view);
        mAdapter = new ViewCartAdapter(DataArrayList, new ViewCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClickPlus(String item, int position) {
                dbHelper.Update(DataArrayList.get(position).getId(),item);
            }

            @Override
            public void onItemClickMinus(String item, int position) {
                dbHelper.Update(DataArrayList.get(position).getId(),item);
            }

            @Override
            public void onItemRemove(String item, int position) {
                dbHelper.delete_rec(item);
                DataArrayList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        category_view.setHasFixedSize(true);
        category_view.setLayoutManager(new GridLayoutManager(ViewCart.this, 1));
        category_view.setAdapter(mAdapter);


        TextView empty_msg = findViewById(R.id.empty_msg);
        TextView place_order = findViewById(R.id.place_order);

        if(DataArrayList.isEmpty()){
            empty_msg.setVisibility(View.VISIBLE);
            place_order.setVisibility(View.GONE);
        }else {
            empty_msg.setVisibility(View.GONE);
            place_order.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(ViewCart.this, PlaceOrder.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}