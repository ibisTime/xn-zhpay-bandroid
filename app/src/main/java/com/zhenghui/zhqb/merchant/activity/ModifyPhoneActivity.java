package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPhoneActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.edt_phone_old)
    EditText edtPhoneOld;
    @BindView(R.id.edt_code_old)
    EditText edtCodeOld;
    @BindView(R.id.btn_send_old)
    TextView btnSendOld;
    @BindView(R.id.edt_phone_new)
    EditText edtPhoneNew;
    @BindView(R.id.edt_code_new)
    EditText edtCodeNew;
    @BindView(R.id.btn_send_new)
    TextView btnSendNew;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.edt_trade)
    EditText edtTrade;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

    private int i = 60;
    private Timer timer;
    private TimerTask task;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            btnSendNew.setText(i + "秒后重发");
            if (msg.arg1 == 0) {
                stopTime();
            } else {
                startTime();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.layout_back, R.id.btn_send_old, R.id.btn_send_new, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send_old:
                sendCode(edtPhoneOld.getText().toString().trim());
                break;

            case R.id.btn_send_new:
                if (isCodeSended) {
                    Toast.makeText(ModifyPhoneActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtPhoneNew.getText().length() != 11) {
                        Toast.makeText(ModifyPhoneActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sendCode(edtPhoneNew.getText().toString().trim());
                }
                break;

            case R.id.btn_confirm:
                if (edtPhoneNew.getText().length() != 11) {
                    Toast.makeText(ModifyPhoneActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCodeNew.getText().toString().trim().length() != 4) {
                    Toast.makeText(ModifyPhoneActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtTrade.getText().toString().trim().length() != 6) {
                    Toast.makeText(ModifyPhoneActivity.this, "请填写正确的支付密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                modifyPhone();
                break;
        }
    }


    private void sendCode(String phone) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", phone);
            object.put("bizType", "805047");
            object.put("kind", "f2");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805904", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                startTime();
                Toast.makeText(ModifyPhoneActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPhoneActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPhoneActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modifyPhone() {
        JSONObject object = new JSONObject();
        try {
            object.put("newMobile", edtPhoneNew.getText().toString().trim());
            object.put("smsCaptcha", edtCodeNew.getText().toString().trim());
            object.put("tradePwd", edtTrade.getText().toString().trim());
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805047", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ModifyPhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPhoneActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPhoneActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 验证码发送倒计时
     */
    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                i--;
                Message message = handler.obtainMessage();
                message.arg1 = i;
                handler.sendMessage(message);
            }

        };

        timer.schedule(task, 1000);
    }

    private void stopTime() {
        isCodeSended = false;
        i = 60;
        btnSendNew.setText("重新发送");
        timer.cancel();
    }

}
