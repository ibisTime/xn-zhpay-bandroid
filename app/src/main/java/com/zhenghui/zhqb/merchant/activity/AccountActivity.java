package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.zhenghui.zhqb.merchant.services.UpdateService;
import com.zhenghui.zhqb.merchant.util.CacheUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
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
    @BindView(R.id.layout_bankCard)
    LinearLayout layoutBankCard;
    @BindView(R.id.txt_sign_out)
    TextView txtSignOut;
    @BindView(R.id.txt_cache)
    TextView txtCache;
    @BindView(R.id.layout_cache)
    LinearLayout layoutCache;
    @BindView(R.id.txt_versionName)
    TextView txtVersionName;
    @BindView(R.id.layout_version)
    LinearLayout layoutVersion;
    @BindView(R.id.layout_about)
    LinearLayout layoutAbout;

    private UserModel model;

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

        try {
            txtCache.setText(CacheUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtVersionName.setText("V"+getVersionName());
    }

    @OnClick({R.id.layout_back, R.id.layout_phone, R.id.layout_authentication, R.id.layout_loginPwd,
            R.id.layout_payPwd, R.id.layout_bankCard, R.id.txt_sign_out, R.id.layout_about,
            R.id.layout_cache, R.id.layout_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_phone:
                if (model.getTradepwdFlag().equals("0")) {
                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify", false).putExtra("phone", model.getMobile()));
                } else {
                    startActivity(new Intent(AccountActivity.this, ModifyPhoneActivity.class).putExtra("phone", model.getMobile()));
                }
                break;

            case R.id.layout_loginPwd:
                startActivity(new Intent(AccountActivity.this, ModifyPasswordActivity.class).putExtra("phone", model.getMobile()));
                break;

            case R.id.layout_payPwd:
                if (model.getTradepwdFlag().equals("0")) { // 未设置支付密码
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify", false).putExtra("phone", model.getMobile()));
                } else {
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify", true).putExtra("phone", model.getMobile()));
                }
                break;

            case R.id.layout_authentication:
                if (txtAuthentication.getText().toString().trim().equals("")) {
                    startActivity(new Intent(AccountActivity.this, AuthenticateActivity.class));
                } else {
                    Toast.makeText(this, "您已实名认证", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.layout_bankCard:
                if (model.getTradepwdFlag().equals("0")) {
                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountActivity.this, ModifyTradeActivity.class).putExtra("isModify", false).putExtra("phone", model.getMobile()));
                } else {
                    startActivity(new Intent(AccountActivity.this, BankCardActivity.class));
                }

                break;

            case R.id.txt_sign_out:
//                SharedPreferences.Editor editor = userInfoSp.edit();
//                editor.putString("userId", null);
//                editor.putString("token", null);
//                editor.commit();
                logOut();
                break;

            case R.id.layout_about:
                startActivity(new Intent(AccountActivity.this, RichTextActivity.class).putExtra("ckey","aboutus"));
                break;

            case R.id.layout_cache:
                clearCache();
                break;

            case R.id.layout_version:
                getVersion();
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
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
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

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("identityFlag", model.getIdentityFlag());
                    editor.putString("tradepwdFlag", model.getTradepwdFlag());

                    editor.commit();

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

        if (model.getRealName() != null) {

            txtAuthentication.setText(model.getRealName());

        }
    }

    private void clearCache() {
        CacheUtil.clearAllCache(this);
        try {
            txtCache.setText(CacheUtil.getTotalCacheSize(this));
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getVersion() {
        JSONObject object = new JSONObject();
        try {
            object.put("key", "bVersionCode");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("615917", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    int versionCode = Integer.parseInt(jsonObject.getString("cvalue"));

                    if (versionCode > getVersionCode()) {
                        update();
                    } else {
                        Toast.makeText(AccountActivity.this, "当前已是最新版本哦", Toast.LENGTH_SHORT).show();
                    }

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

    private void update() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("发现新版本请及时更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startService(new Intent(AccountActivity.this, UpdateService.class)
                                .putExtra("appname", "zhsj-release")
                                .putExtra("appurl", "http://m.zhenghuijituan.com/app/zhsj-release.apk"));

                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 退出登录
     */
    private void logOut() {

        RequestParams params = new RequestParams(Xutil.LOGOUT);
        params.addBodyParameter("token", userInfoSp.getString("token", null));

        System.out.println("url=" +  Xutil.LOGOUT);
        System.out.println("token=" + userInfoSp.getString("token", null));

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                System.out.println("result=" + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    System.out.println("jsonObject.getJSONObject(\"data\").getBoolean(\"isSuccess\")=" + jsonObject.getJSONObject("data").getBoolean("isSuccess"));

                    if (jsonObject.getJSONObject("data").getBoolean("isSuccess")) {
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("userId", null);
                        editor.putString("token", null);
                        editor.commit();

                        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                        AccountActivity.this.finish();
                        Main2Activity.instance.finish();
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("onError=" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
