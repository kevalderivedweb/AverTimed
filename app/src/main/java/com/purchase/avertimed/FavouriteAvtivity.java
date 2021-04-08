package com.purchase.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.purchase.avertimed.Adapter.WishListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAvtivity extends AppCompatActivity {


    private int quantity;
    private DbHelper_MultipleData dbHelper;
    private List<FavDatabaseModel> DataArrayList;
    private RecyclerView category_view;
    private WishListAdapter mAdapter;
    private TextView size;

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



        dbHelper = new DbHelper_MultipleData(FavouriteAvtivity.this);
        DataArrayList = new ArrayList<>();
        DataArrayList = dbHelper.getFav();


        size = findViewById(R.id.size);
        category_view = findViewById(R.id.category_view);
        mAdapter = new WishListAdapter(FavouriteAvtivity.this,DataArrayList, new WishListAdapter.OnItemClickListener() {
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
                dbHelper.delete(item);
                DataArrayList.remove(position);
                mAdapter.notifyDataSetChanged();
                DataArrayList = new ArrayList<>();
                DataArrayList = dbHelper.getFav();
                size.setText(DataArrayList.size() + " Product Found");
            }

            @Override
            public void onItemSelect(String position) {

                Log.e("ProductId", position);
                Intent intent = new Intent(FavouriteAvtivity.this,GeneralPracticeActivity.class);
                intent.putExtra("ProductId",Integer.parseInt(position));
                startActivity(intent);

            }
        });
        category_view.setHasFixedSize(true);
        category_view.setLayoutManager(new GridLayoutManager(FavouriteAvtivity.this, 1));
        category_view.setAdapter(mAdapter);


        TextView empty_msg = findViewById(R.id.empty_msg);

        size.setText(DataArrayList.size() + " Product Found");

        if(DataArrayList.isEmpty()){
            empty_msg.setVisibility(View.VISIBLE);
        }else {
            empty_msg.setVisibility(View.GONE);
        }




    }

}