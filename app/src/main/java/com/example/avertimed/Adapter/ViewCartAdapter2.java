package com.example.avertimed.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.avertimed.FavDatabaseModel;
import com.example.avertimed.R;

import java.util.List;

public class ViewCartAdapter2 extends RecyclerView.Adapter<ViewCartAdapter2.MyViewHolder> {

    private final OnItemClickListener listener;
    private List<FavDatabaseModel> mDataset;
    private int quantity = 1;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img,plus_bg,minus_bg;
        // each data item is just a string in this case
        public TextView productname,productname1;
        public TextView price;
        public TextView edt_qt;


        public MyViewHolder(View v) {
            super(v);
            this.productname = (TextView) itemView.findViewById(R.id.productname);
            this.productname1 = (TextView) itemView.findViewById(R.id.productname1);
            this.price = (TextView) itemView.findViewById(R.id.product_price);
            this.edt_qt = (TextView) itemView.findViewById(R.id.edt_qt);
            this.img = (ImageView) itemView.findViewById(R.id.img);
            this.plus_bg = (ImageView) itemView.findViewById(R.id.plus_bg);
            this.minus_bg = (ImageView) itemView.findViewById(R.id.minus_bg);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ViewCartAdapter2(List<FavDatabaseModel> categoryModels, OnItemClickListener listener) {
        mDataset = categoryModels;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_cart2, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.textView.setText(mDataset[position]);
        quantity = Integer.parseInt(mDataset.get(position).getQT());
        holder.productname.setText(mDataset.get(position).getFavName());
        holder.productname1.setText(mDataset.get(position).getFavName());
        holder.price.setText(mDataset.get(position).getPrice());
        holder.edt_qt.setText(mDataset.get(position).getQT());
      //  holder.img.setBackgroundResource(mmyDataset[position]);
        holder.plus_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(holder);
                listener.onItemClickPlus(holder.edt_qt.getText().toString(),position);
            }
        });

        holder.minus_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(holder);
                listener.onItemClickMinus(holder.edt_qt.getText().toString(),position);
            }
        });

        Glide.with(holder.img.getContext()).load(mDataset.get(position).getImage_url()).placeholder(R.drawable.product_1).into(holder.img);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClickPlus(String item, int position);
        void onItemClickMinus(String item, int position);
    }
    public void decrement(MyViewHolder myViewHolder) {
        quantity = Integer.parseInt(myViewHolder.edt_qt.getText().toString());
        quantity--;
        myViewHolder.edt_qt.setText(String.valueOf(quantity));

    }

    public void increment(MyViewHolder myViewHolder) {
        quantity = Integer.parseInt(myViewHolder.edt_qt.getText().toString());
        quantity++;
        myViewHolder.edt_qt.setText(String.valueOf(quantity));

    }


}
