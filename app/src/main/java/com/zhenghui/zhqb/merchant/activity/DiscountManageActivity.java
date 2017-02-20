package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.adapter.DiscountAdapter;
import com.zhenghui.zhqb.merchant.model.DiscountModel;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.RefreshLayout;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leiq on 2016/12/25.
 * 折扣券管理
 */

public class DiscountManageActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.list_discount)
    ListView listDiscount;
    @BindView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private int page = 1;
    private int pageSize = 10;

    private String storeCode;

    private List<DiscountModel> list;
    private DiscountAdapter adapter;

    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_manage);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();
        initRefreshLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new DiscountAdapter(this,list);
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        storeCode = getIntent().getStringExtra("storeCode");

    }

    private void initListView() {
        listDiscount.setAdapter(adapter);
        listDiscount.setOnItemClickListener(this);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    @OnClick({R.id.layout_back, R.id.img_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.img_add:
                startActivity(new Intent(DiscountManageActivity.this,DiscountAddActivity.class));
                break;
        }
    }

    private void getList() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", "");
            object.put("type", "");
            object.put("storeCode", storeCode);
            object.put("status", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808224", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    List<DiscountModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<DiscountModel>>() {
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
                Toast.makeText(DiscountManageActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountManageActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                getList();
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
                getList();
            }
        }, 1500);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.get(i).getStatus().equals("1")){
            startActivity(new Intent(DiscountManageActivity.this, DiscountAddActivity.class)
                    .putExtra("isModifi",true)
                    .putExtra("status",true)
                    .putExtra("code",list.get(i).getCode()));
        }else if(list.get(i).getStatus().equals("2")){ //已下架
            startActivity(new Intent(DiscountManageActivity.this, DiscountAddActivity.class)
                    .putExtra("isModifi",true)
                    .putExtra("status",false)
                    .putExtra("code",list.get(i).getCode()));
        }

    }
}
