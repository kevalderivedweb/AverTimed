package com.example.avertimed.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.avertimed.API.UserSession;
import com.example.avertimed.Model.CategoryModel;
import com.example.avertimed.R;

import java.util.ArrayList;

public class NewProductAdapter2 extends RecyclerView.Adapter<NewProductAdapter2.MyViewHolder> {

    private final Context mContext;
    private final OnItemClickListener listener;
    private ArrayList<CategoryModel> mDataset;
    private UserSession userSession;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        // each data item is just a string in this case
        public TextView textView;
        public TextView txt_price;


        public MyViewHolder(View v) {
            super(v);
            this.textView = (TextView) itemView.findViewById(R.id.name);
            this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            this.img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewProductAdapter2(Context context,ArrayList<CategoryModel> categoryModels, OnItemClickListener listener) {
        mContext = context;
        mDataset = categoryModels;
        this.listener = listener;
        userSession = new UserSession(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_newproduct_cat, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.textView.setText(mDataset[position]);
        holder.textView.setText(mDataset.get(position).getDescription());
        holder.txt_price.setText(userSession.getCurremcySign()+" "+mDataset.get(position).getTxt_price());
      //  holder.img.setBackgroundResource(mmyDataset[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });

        Glide.with(holder.img.getContext()).load(mDataset.get(position).getCat_name_image()).placeholder(R.drawable.product_1).into(holder.img);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }


}
