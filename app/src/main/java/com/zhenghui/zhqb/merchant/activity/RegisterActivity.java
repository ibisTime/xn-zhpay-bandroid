package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
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

public class RegisterActivity extends MyBaseActivity {


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
    @BindView(R.id.edt_userReferee)
    EditText edtUserReferee;
    @BindView(R.id.edt_referrer)
    EditText edtReferrer;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.txt_treaty)
    TextView txtTreaty;


    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

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
        setContentView(R.layout.activity_register);
        setWindow();
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void setWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.layout_back, R.id.btn_send, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if (isCodeSended) {
                    Toast.makeText(RegisterActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtPhone.getText().toString().trim().length() != 11) {
                        Toast.makeText(RegisterActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendCode();
                }

                break;

            case R.id.btn_register:
                if (edtPhone.getText().toString().trim().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCode.getText().toString().trim().length() != 4) {
                    Toast.makeText(RegisterActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtPassword.getText().toString().trim().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (edtUserReferee.getText().toString().trim().length() != 11) {
//                    Toast.makeText(RegisterActivity.this, "请填写推荐人手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                register();
                break;
        }
    }

    private void register() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("loginPwd", edtPassword.getText().toString().trim());
            object.put("smsCaptcha", edtCode.getText().toString().trim());
            object.put("loginPwdStrength", "1");
            object.put("userReferee", edtUserReferee.getText().toString().trim());
            object.put("kind", "f2");
            object.put("isRegHx", "1");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("object.toString()=" + object.toString());

        new Xutil().post("805041", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(RegisterActivity.this, "注册成功,为您自动登录", Toast.LENGTH_SHORT).show();
                login();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RegisterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("bizType", "805041");
            object.put("kind", "f1");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805904", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                startTime();
                Toast.makeText(RegisterActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RegisterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(){
        JSONObject object = new JSONObject();
        try {
            object.put("loginName",edtPhone.getText().toString().trim());
            object.put("loginPwd",edtPassword.getText().toString().trim());
            object.put("kind","f2");
            object.put("companyCode","");
            object.put("systemCode",appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805043",object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    editor.putString("userId",jsonObject.getString("userId"));
                    editor.putString("token",jsonObject.getString("token"));
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                signin();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RegisterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signin(){
        EMClient.getInstance().login(userInfoSp.getString("userId",null), "888888", new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));

            }

            @Override
            public void onError(int i, String s) {
                Log.i("Qian","登录失败 " + i + ", " + s);
            }

            @Override
            public void onProgress(int i, String s) {

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
