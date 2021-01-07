package com.example.avertimed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avertimed.API.UserSession;
import com.example.avertimed.Model.OrderModel;
import com.example.avertimed.R;

import java.util.ArrayList;

public class CurrentProductAdapter extends RecyclerView.Adapter<CurrentProductAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private final Context mContext;
    private final UserSession userSession;
    private ArrayList<OrderModel> mDataset;
    private SubProductAdapter mAdapter;
    private int Pos = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView order_num,price,date,cancel;
        RecyclerView category_view;


        public MyViewHolder(View v) {
            super(v);
            this.order_num = (TextView) itemView.findViewById(R.id.order_num);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.cancel = (TextView) itemView.findViewById(R.id.cancel);
            category_view =(RecyclerView) itemView.findViewById(R.id.category_view);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CurrentProductAdapter(Context ProductContext, ArrayList<OrderModel> categoryModels, OnItemClickListener listener) {
        mDataset = categoryModels;
        this.listener = listener;
        this.mContext = ProductContext;
        userSession = new UserSession(ProductContext);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_mangeorder_time, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.textView.setText(mDataset[position]);

        Pos = position;
        holder.order_num.setText("Order Code : "+mDataset.get(position).getOrderCode());
        holder.price.setText(userSession.getCurremcySign()+" "+mDataset.get(position).getTotalAmount());
        holder.date.setText(mDataset.get(position).getDate());
        holder.cancel.setText(mDataset.get(position).getOrderStatus());
      //  holder.img.setBackgroundResource(mmyDataset[position]);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(Pos);
            }
        });


        Log.e("POS",mDataset.get(Pos).getProducts().get(0).getProductID());

        mAdapter = new SubProductAdapter(mContext,mDataset.get(position).getProducts(), new SubProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                listener.onItemClickSUB(position,item);


            }
        });
        holder.category_view.setHasFixedSize(true);
        holder.category_view.setLayoutManager(new LinearLayoutManager(holder.category_view.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.category_view.setAdapter(mAdapter);
        holder.category_view.setNestedScrollingEnabled(false);

/*
        Glide.with(holder.img.getContext()).load(mDataset.get(position).getCat_name_image()).placeholder(R.drawable.product_1).into(holder.img);
*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
        void onItemClickSUB(int position, int item);
    }


}
