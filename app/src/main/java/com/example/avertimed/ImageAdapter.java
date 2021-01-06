package com.example.avertimed;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.avertimed.Model.CategoryModel;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<CategoryModel> McategoryModels;
  
    ImageAdapter(Context context , ArrayList<CategoryModel>  categoryModels) {
        this.mContext = context;  
        this.McategoryModels = categoryModels;
    }
  
    @Override  
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }  

    @Override  
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        Glide.with(imageView.getContext()).load(McategoryModels.get(position).getCat_name_image()).into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;  
    }  
  
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
        ((ViewPager) container).removeView((ImageView) object);  
    }  
  
    @Override  
    public int getCount() {  
        return McategoryModels.size();
    }  
}  