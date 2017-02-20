package com.zhenghui.zhqb.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends Activity {

    @BindView(R.id.txt_pass)
    TextView txtPass;

    int time = 3500;
    private Timer timer;
    private TimerTask task;

    private SharedPreferences userInfoSp;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                startApp();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        startTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @OnClick(R.id.txt_pass)
    public void onClick() {

    }

    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.arg1 = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, time);
    }

    private void stopTime() {
        timer.cancel();
    }

    private void startApp() {
        if(userInfoSp.getString("userId",null) != null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
        }else {
            startActivity(new Intent(StartActivity.this,LoginActivity.class));
        }
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        stopTime();
        finish();
    }


}
