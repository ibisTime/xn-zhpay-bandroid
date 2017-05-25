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

public class ModifyTradeActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.btn_send)
    TextView btnSend;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_repassword)
    EditText edtRepassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.txt_title)
    TextView txtTitle;

    private SharedPreferences preferences;
    private SharedPreferences userInfoSp;

    private boolean isModify;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

    private int i = 60;
    private Timer timer;
    private TimerTask task;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            btnSend.setText(i + "秒后重发");
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
        setContentView(R.layout.activity_modify_trade);
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
        edtPhone.setText(getIntent().getStringExtra("phone"));
        isModify = getIntent().getBooleanExtra("isModify", false);

        if (isModify) {
            txtTitle.setText("修改支付密码");
        }else{
            txtTitle.setText("设置支付密码");
        }

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        preferences = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.layout_back, R.id.btn_send, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if (edtPhone.getText().length() == 11) {
                    if (isCodeSended) {
                        Toast.makeText(ModifyTradeActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                    } else {
                        sendCode();
                    }
                } else {
                    Toast.makeText(ModifyTradeActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_confirm:

                if (edtPhone.getText().length() != 11) {
                    Toast.makeText(ModifyTradeActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCode.getText().toString().trim().length() != 4) {
                    Toast.makeText(ModifyTradeActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (edtPassword.getText().toString().trim().length() != 6) {
//                    Toast.makeText(ModifyTradeActivity.this, "请填写6位的原支付密码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (edtRepassword.getText().toString().trim().length() != 6) {
                    Toast.makeText(ModifyTradeActivity.this, "请填写6位的新支付密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                set();

                break;
        }
    }

    private void sendCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtPhone.getText().toString().trim());
            if (isModify) {
                object.put("bizType", "805057");
            } else {
                object.put("bizType", "805045");
            }
            object.put("kind", "f2");
            object.put("systemCode", preferences.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805904", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                startTime();
                Toast.makeText(ModifyTradeActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyTradeActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyTradeActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void set() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            if (isModify) {
                object.put("newTradePwd", edtRepassword.getText().toString().trim());
            } else {
                object.put("tradePwd", edtRepassword.getText().toString().trim());
            }

            object.put("smsCaptcha", edtCode.getText().toString().toString());
            object.put("tradePwdStrength", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String code = "";
        if (isModify) {
            code = "805057";
        } else {
            code = "805045";
        }

        new Xutil().post(code, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if (isModify) {
                    Toast.makeText(ModifyTradeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyTradeActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                }

                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyTradeActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyTradeActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
        btnSend.setText("重新发送");
        timer.cancel();
    }
}
