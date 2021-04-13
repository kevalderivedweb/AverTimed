package com.purchase.avertimed;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.purchase.avertimed.API.UserSession;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private UserSession session;
    private String importat_message_alert;
    private SwitchCompat swOnOff;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        session = new UserSession(SettingActivity.this);

        swOnOff = findViewById(R.id.swOnOff);

        importat_message_alert = session.getNOTIFICATION_ALERT();

       /* if (session.getFIRST_TIME_NOTIFICATION_ON().equals("1")){
            swOnOff.setChecked(true);

        }*/

        if (session.getNOTIFICATION_ALERT().equals("0")){
            swOnOff.setChecked(false);
        } else {
            swOnOff.setChecked(true);
        }

        swOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (importat_message_alert.equals("1")) {
                    importat_message_alert = "0";
                    session.setNOTIFICATION_ALERT(importat_message_alert);

                } else {
                    importat_message_alert = "1";
                    session.setNOTIFICATION_ALERT(importat_message_alert);

                }
               /* Toast.makeText(Activity_NotificationSettings.this, importat_message_alert + "-" + shipments_notifications + "-" +
                        personalised_notifications + "-" + wishlist_notifications, Toast.LENGTH_SHORT).show();*/
            }
        });

        Log.e("notificationStatus", session.getNOTIFICATION_ALERT() + "--");


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.ln_changepassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(SettingActivity.this,ChangePassword.class);
                startActivity(i);
            }
        });

        findViewById(R.id.ln_changeLanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(SettingActivity.this,ChangeLanguage.class);
                startActivity(i);
            }
        });

        findViewById(R.id.ln_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(SettingActivity.this,EditProfile.class);
                startActivity(i);
            }
        });

        findViewById(R.id.ln_shipping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(SettingActivity.this,ShippingAddress.class);
                startActivity(i);
            }
        });

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