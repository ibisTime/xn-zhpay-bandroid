package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.UserModel;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_nickname)
    TextView txtNickname;
    @BindView(R.id.layout_nickname)
    LinearLayout layoutNickname;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.layout_phone)
    LinearLayout layoutPhone;
    @BindView(R.id.txt_authentication)
    TextView txtAuthentication;
    @BindView(R.id.layout_authentication)
    LinearLayout layoutAuthentication;
    @BindView(R.id.layout_loginPwd)
    LinearLayout layoutLoginPwd;
    @BindView(R.id.layout_payPwd)
    LinearLayout layoutPayPwd;
    @BindView(R.id.txt_tr)
    TextView txtTr;

    private UserModel model;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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
        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.layout_back, R.id.layout_nickname, R.id.layout_phone, R.id.layout_authentication, R.id.layout_loginPwd, R.id.layout_payPwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_phone:
                if(model.getTradepwdFlag().equals("0")){
                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify",false).putExtra("phone",model.getMobile()));
                }else{
                    startActivity(new Intent(AccountActivity.this, ModifyPhoneActivity.class));
                }
                break;

            case R.id.layout_loginPwd:
                startActivity(new Intent(AccountActivity.this, ModifyPasswordActivity.class).putExtra("phone",model.getMobile()));
                break;

            case R.id.layout_payPwd:
                if (model.getTradepwdFlag().equals("0")) { // 未设置支付密码
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify", false).putExtra("phone",model.getMobile()));
                } else {
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify", true).putExtra("phone",model.getMobile()));
                }
                break;

            case R.id.layout_authentication:
                if (txtAuthentication.getText().toString().trim().equals("")) {
                    startActivity(new Intent(AccountActivity.this, AuthenticateActivity.class));
                } else {
                    Toast.makeText(this, "您已实名认证", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 获取用户详情
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", preferences.getString("userId", null));
            object.put("token", preferences.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805056", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<UserModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AccountActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AccountActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        txtPhone.setText(model.getMobile());
        txtNickname.setText(model.getNickname());

        if(model.getRealName() != null){

            txtAuthentication.setText(model.getRealName());

        }
    }
}
