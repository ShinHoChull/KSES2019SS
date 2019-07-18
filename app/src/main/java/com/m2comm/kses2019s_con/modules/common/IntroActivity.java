package com.m2comm.kses2019s_con.modules.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.models.MessageDTO;
import com.m2comm.kses2019s_con.views.MainActivity;


public class IntroActivity extends AppCompatActivity {

    private Handler handler;
    private int time = 2000;
    private Globar g;
    //handler
    private CustomHandler customHandler;
    private ImageView intro;
    private Custom_SharedPreferences csp;

    public void init() {
        this.handler = new Handler();
        this.g = new Globar(this);
        this.customHandler = new CustomHandler(this);
        this.csp = new Custom_SharedPreferences(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        this.intro = findViewById(R.id.intro_img);
        this.init();

        if (NetworkState.isNetworkStat(getApplicationContext()) == false) {
            Toast.makeText(getApplicationContext(), "인터넷을 실행시켜주세요", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(this.g.code, this.g.code, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{500, 1000, 500, 1000});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        this.deviceIdCreate();
    }


    public void deviceIdCreate() {
        if (csp.getValue("deviceid", "").equals("")) {
            final String deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            csp.put("deviceid", deviceid);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Message msg = customHandler.obtainMessage();
                    try {
                        g.getParser(g.baseUrl+g.urls.get("setToken")+"?deviceid="+deviceid+"&device=android&code="+g.code);
                        msg.what = CustomHandler.TOKEN_CODE;
                        customHandler.sendMessage(msg);
                    } catch (Exception e) {
                        msg.obj = new MessageDTO("Failed to fetch data.(Intro Error)",
                                e.toString());
                        msg.what = CustomHandler.ALERT_WINDOW_CODE;
                        customHandler.sendMessage(msg);
                    }
                }
            }.start();
        } else {
            this.moveMain();
        }
    }


    public void moveMain() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, time);
    }
}
