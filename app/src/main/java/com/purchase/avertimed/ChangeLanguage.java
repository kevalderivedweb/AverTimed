package com.purchase.avertimed;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.avertimed.Model.LanguageModel;
import com.purchase.avertimed.API.UserSession;
import com.purchase.avertimed.Adapter.LanguageItemAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class ChangeLanguage extends AppCompatActivity {

    private RecyclerView setting_language;
    private LanguageItemAdapter mLanguageAdapter;
    private ArrayList<LanguageModel> languageModels = new ArrayList<>();
    private UserSession userSession;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
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


        userSession = new UserSession(ChangeLanguage.this);

        LanguageModel languageModel = new LanguageModel();
        languageModel.setProductTitle("ProductTitleEn");
        languageModel.setCategoryName("CategoryNameEn");
        languageModel.setSubCategoryName("SubCategoryNameEn");
        languageModel.setLanguage("English");
        languageModel.setCode("en");
        languageModel.setDescription("DescriptionEn");
        languageModels.add(languageModel);

        LanguageModel languageModel1 = new LanguageModel();
        languageModel1.setProductTitle("ProductTitleFr");
        languageModel1.setCategoryName("CategoryNameFr");
        languageModel1.setSubCategoryName("SubCategoryNameFr");
        languageModel1.setLanguage("French");
        languageModel1.setCode("fr");
        languageModel1.setDescription("DescriptionFr");
        languageModels.add(languageModel1);


        setting_language = findViewById(R.id.setting_language);
        setting_language.setLayoutManager(new LinearLayoutManager(ChangeLanguage.this));
        mLanguageAdapter = new LanguageItemAdapter(ChangeLanguage.this, languageModels, new LanguageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, String code, String productTitle, String categoryName, String subCategoryName, String description) {
                userSession.setLanguageCode(code);
                userSession.setProductTitle(productTitle);
                userSession.setCategoryname(categoryName);
                userSession.setSubcategoryname(subCategoryName);
                userSession.setLanguageName(name);
                userSession.setDescription(description);
                mLanguageAdapter.notifyDataSetChanged();

                userSession.setIslanchange(true);


                try{

                   /* Intent mStartActivity = new Intent(SettingDetails.this, Splash.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(SettingDetails.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) SettingDetails.this.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, mPendingIntent);
                    System.exit(0);*/

                    PackageManager packageManager = getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                    ComponentName componentName = intent.getComponent();
                    Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                    startActivity(mainIntent);
                    Runtime.getRuntime().exit(0);



                   /* Intent intent = new Intent(SettingDetails.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();*/


                }catch (Exception e) {
                    e.printStackTrace();
                }
                // after on CLick we are using finish to close and then just after that
                // we are calling startactivity(getIntent()) to open our application

            }
        });
        setting_language.setAdapter(mLanguageAdapter);
        UserSession userSession = new UserSession(this);
        setLocale(userSession.getLanguageCode());


        Log.e("getLanguage", userSession.getLanguage() + "--" +
                userSession.getLanguageCode() + "--" +
                userSession.getProductTitle() + "--" +
                userSession.getCategoryname() + "--" +
                userSession.getSubcategoryname() + "--" +
                userSession.getDescription());

    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserSession userSession = new UserSession(this);
        setLocale(userSession.getLanguageCode());
    }

}