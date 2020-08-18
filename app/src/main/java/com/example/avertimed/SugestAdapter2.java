package com.example.avertimed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.avertimed.Model.CategoryModel;

import java.util.ArrayList;

public class SugestAdapter2 extends RecyclerView.Adapter<SugestAdapter2.MyViewHolder> {

    private final OnItemClickListener listener;
    private final ArrayList<CategoryModel> mCategory;

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
            this.textView = (TextView) itemView.findViewById(R.id.txt);
            this.img = (ImageView) itemView.findViewById(R.id.img);
            this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SugestAdapter2(ArrayList<CategoryModel> categoryModels, OnItemClickListener listener) {
        mCategory = categoryModels;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.row_sugest_2, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.textView.setText(mDataset[position]);
        holder.textView.setText(mCategory.get(position).getCat_name_en());
        holder.txt_price.setText(mCategory.get(position).getTxt_price());
        Glide.with(holder.img.getContext()).load(mCategory.get(position).getCat_name_image()).into(holder.img);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }


}
