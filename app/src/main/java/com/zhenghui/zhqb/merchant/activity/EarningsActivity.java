package com.zhenghui.zhqb.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.AssetsModel;
import com.zhenghui.zhqb.merchant.util.NumberUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EarningsActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_fund)
    TextView txtFund;
    @BindView(R.id.txt_fhq)
    TextView txtFhq;
    @BindView(R.id.txt_earnings)
    TextView txtEarnings;
    @BindView(R.id.layout_earnings)
    LinearLayout layoutEarnings;
    @BindView(R.id.txt_limit)
    TextView txtLimit;
    @BindView(R.id.activity_earnings)
    LinearLayout activityEarnings;
    private List<AssetsModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        ButterKnife.bind(this);

        inits();
        getData();
        getDatas();
        getAccount();
    }

    private void inits() {
        list = new ArrayList<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_earnings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_earnings:
                startActivity(new Intent(this, RightsActivity.class));
                break;
        }
    }

    /**
     * 查询所需消费额
     */
    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808418", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    txtLimit.setText(NumberUtil.doubleFormatMoney(500000 - jsonObject.getDouble("costAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EarningsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EarningsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询分红权与今日收益
     */
    private void getDatas() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808419", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtFhq.setText(jsonObject.getString("stockCount"));
                    txtEarnings.setText(NumberUtil.doubleFormatMoney(jsonObject.getDouble("todayProfitAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EarningsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EarningsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAccount() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", "STORE_POOL_ZHPAY");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802503", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new Gson();
                    List<AssetsModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AssetsModel>>() {
                    }.getType());

                    list.addAll(lists);

                    setAssets();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EarningsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EarningsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAssets() {
        for (AssetsModel model : list) {
            if (model.getCurrency().equals("FRB")) {

                txtFund.setText("¥" + NumberUtil.doubleFormatMoney(model.getAmount()) + "");

            }
        }
    }
}
