package com.purchase.avertimed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avertimed.Model.LanguageModel;
import com.purchase.avertimed.API.UserSession;
import com.purchase.avertimed.R;

import java.util.ArrayList;

public class LanguageItemAdapter extends RecyclerView.Adapter<LanguageItemAdapter.Viewholder> {

    private final ArrayList<LanguageModel> mLanguageModels;
    private final UserSession usersession;
    private Context context;
    private final OnItemClickListener mListener;

    public LanguageItemAdapter(Context context, ArrayList<LanguageModel> languageModels, OnItemClickListener listener) {
        this.context = context;
        this.mLanguageModels = languageModels;
        this.mListener = listener;
        usersession  = new UserSession(context);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_item_language, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        holder.mlanguage_txt.setText(mLanguageModels.get(position).getLanguage());

        if(mLanguageModels.get(position).getLanguage().equals(usersession.getLanguage())){
            holder.selected_img.setVisibility(View.VISIBLE);
        }else {
            holder.selected_img.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mLanguageModels.get(position).getLanguage(),
                        mLanguageModels.get(position).getCode(),
                        mLanguageModels.get(position).getProductTitle(),
                        mLanguageModels.get(position).getCategoryName(),
                        mLanguageModels.get(position).getSubCategoryName(),
                        mLanguageModels.get(position).getDescription());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLanguageModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView mlanguage_txt;
        ImageView selected_img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            mlanguage_txt = itemView.findViewById(R.id.mlanguage_txt);
            selected_img = itemView.findViewById(R.id.selected_img);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(String name, String code, String productTitle, String categoryName, String subCategoryName, String description);
    }

}
