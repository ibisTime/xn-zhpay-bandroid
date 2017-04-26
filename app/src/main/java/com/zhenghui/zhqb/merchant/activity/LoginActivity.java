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
import android.view.inputmethod.InputMethodManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MyBaseActivity {

    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_register)
    TextView txtRegister;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_forget)
    TextView txtForget;

    public static final String TIP = "tip";

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_login);
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
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        if(getIntent() != null){
            boolean isTip = getIntent().getBooleanExtra(TIP,false);
            if(isTip){

            }
        }
    }


    @OnClick({R.id.layout_back, R.id.txt_register, R.id.btn_login, R.id.txt_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;

            case R.id.btn_login:
                if(edtPhone.getText().toString().trim().length() != 11){
                    Toast.makeText(LoginActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtPassword.getText().toString().trim().length() == 0){
                    Toast.makeText(LoginActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
                break;

            case R.id.txt_forget:
                startActivity(new Intent(LoginActivity.this,ModifyPasswordActivity.class));
                break;
        }
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

                    // 收起键盘
                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }

                    } catch (JSONException e) {
                    e.printStackTrace();
                }

//                finish();
                signin();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(LoginActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signin(){
        EMClient.getInstance().login(userInfoSp.getString("userId",null), "888888", new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));

            }

            @Override
            public void onError(int i, String s) {
                Message message = handler.obtainMessage();
                message.obj = "登录失败: " + i + ", " + s;
                handler.sendMessage(message);
                Log.i("Qian","登录失败 " + i + ", " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}
