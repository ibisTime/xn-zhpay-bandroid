package com.zhenghui.zhqb.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.adapter.BillAdapter;
import com.zhenghui.zhqb.merchant.model.BillModel;
import com.zhenghui.zhqb.merchant.util.NumberUtil;
import com.zhenghui.zhqb.merchant.util.RefreshLayout;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leiq on 2016/12/26.
 * 账单
 */

public class BillActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.list_bill)
    ListView listBill;
    @BindView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @BindView(R.id.txt_balance)
    TextView txtBalance;
    @BindView(R.id.txt_withdrawal)
    TextView txtWithdrawal;
    @BindView(R.id.txt_history)
    TextView txtHistory;

    private List<BillModel> list;
    private BillAdapter adapter;

    private double accountAmount;
    private String accountNumber;

    private int page = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();
        initRefreshLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new BillAdapter(this, list);

        accountNumber = getIntent().getStringExtra("code");
        accountAmount = getIntent().getDoubleExtra("accountAmount", 0.0);

        txtBalance.setText(NumberUtil.doubleFormatMoney(accountAmount) + "");
    }

    private void initListView() {
        listBill.setAdapter(adapter);
        listBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(BillActivity.this, BillDetailActivity.class)
                        .putExtra("code", list.get(i).getCode()));
            }
        });
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnLoadListener(this);
        swipeContainer.setOnRefreshListener(this);
    }

    @OnClick({R.id.layout_back, R.id.txt_withdrawal, R.id.txt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_withdrawal:
                if (userInfoSp.getString("tradepwdFlag", null).equals("1")) { // tradepwdFlag 支付密码标示 1有 0 无

                    startActivity(new Intent(BillActivity.this, WithdrawalsActivity.class)
                            .putExtra("balance", accountAmount)
                            .putExtra("accountNumber", accountNumber));

                } else {

                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BillActivity.this, ModifyTradeActivity.class)
                            .putExtra("phone", userInfoSp.getString("mobile", ""))
                            .putExtra("isModify", false));

                }
                break;

            case R.id.txt_history:
                startActivity(new Intent(BillActivity.this, BillHistoryActivity.class)
                        .putExtra("code", accountNumber));
                break;
        }
    }

    private void getData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("accountNumber", accountNumber);
            object.put("dateStart", format.format(new Date()));
            object.put("dateEnd", format.format(new Date()));
            object.put("start", page);
            object.put("limit", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802524", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<BillModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<BillModel>>() {
                    }.getType());

                    if(page == 1){
                        list.clear();
                    }

                    list.addAll(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BillActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BillActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();
                // 更新数据
                // 更新完后调用该方法结束刷新
            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getData();
            }
        }, 1500);
    }


}
