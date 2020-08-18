package com.example.avertimed;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class GeneralPracticeActivity extends AppCompatActivity {


    private LinearLayout framelayout;
    private TextView chat1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));



        framelayout = findViewById(R.id.framelayout);
        final View view1 = findViewById(R.id.view1);
        final View view2 = findViewById(R.id.view2);
        final View view3 = findViewById(R.id.view3);
        view1.setBackgroundColor(getResources().getColor(R.color.sky_blue));
        view2.setBackgroundColor(getResources().getColor(R.color.white));
        view3.setBackgroundColor(getResources().getColor(R.color.white));

        addFragment(R.id.framelayout,new FirstFragment(),"Fragment");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.ln1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view1.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                view2.setBackgroundColor(getResources().getColor(R.color.white));
                view3.setBackgroundColor(getResources().getColor(R.color.white));
                replaceFragment(R.id.framelayout,new FirstFragment(),"Fragment",null);

            }
        });
        findViewById(R.id.ln2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view1.setBackgroundColor(getResources().getColor(R.color.white));
                view2.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                view3.setBackgroundColor(getResources().getColor(R.color.white));
                replaceFragment(R.id.framelayout,new SecondFragment(),"Fragment",null);
            }
        });

        findViewById(R.id.ln3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setBackgroundColor(getResources().getColor(R.color.white));
                view2.setBackgroundColor(getResources().getColor(R.color.white));
                view3.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                replaceFragment(R.id.framelayout,new ThirdFragment(),"Fragment",null);
            }
        });


        chat1 = findViewById(R.id.chat1);
        chat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(GeneralPracticeActivity.this,ChatDetails.class);
                startActivity(i);
            }
        });
    }


    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }


}