package com.zhenghui.zhqb.merchant.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.adapter.ProductAdapter;
import com.zhenghui.zhqb.merchant.model.ProductModel;
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

public class ProductFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @BindView(R.id.line_normal)
    View lineNormal;
    @BindView(R.id.layout_normal)
    LinearLayout layoutNormal;
    @BindView(R.id.line_duobao)
    View lineDuobao;
    @BindView(R.id.layout_duobao)
    LinearLayout layoutDuobao;
    @BindView(R.id.list_product)
    ListView listProduct;
    @BindView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    // Fragment主视图
    private View view;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;



    private int normalPage = 1;
    private int normalPageSize = 10;
    private List<ProductModel> normalList;
    private ProductAdapter normalAdapter;


    private int duobaoPage = 1;
    private int duobaoPageSize = 10;

    private boolean isAtNormal = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell, null);
        ButterKnife.bind(this, view);

        inis();
        initEvent();

        getNormal();

        return view;
    }

    private void inis() {
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initEvent() {

    }

    @OnClick({R.id.layout_normal, R.id.layout_duobao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_normal:
                initBtn();
                lineNormal.setVisibility(View.VISIBLE);
                layoutNormal.setBackgroundColor(getResources().getColor(R.color.white));
                getNormal();
                break;

            case R.id.layout_duobao:
                initBtn();
                lineDuobao.setVisibility(View.VISIBLE);
                layoutDuobao.setBackgroundColor(getResources().getColor(R.color.white));
                getDuobao();
                break;
        }
    }

    public void initBtn(){
        lineNormal.setVisibility(View.INVISIBLE);
        lineDuobao.setVisibility(View.INVISIBLE);

        layoutDuobao.setBackgroundColor(getResources().getColor(R.color.disLight));
        layoutNormal.setBackgroundColor(getResources().getColor(R.color.disLight));
    }

    private void getNormal() {
        JSONObject object = new JSONObject();
        try {
            object.put("category", "");
            object.put("type", "");
            object.put("name", "");
            object.put("status", "");
            object.put("location", "");
            object.put("companyCode", userInfoSp.getString("userId", null));
            object.put("start", normalPage);
            object.put("limit", normalPageSize);
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808020", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {


                try {
                    JSONObject jsonObject = new JSONObject(result);


                    Gson gson = new Gson();
                    ArrayList<ProductModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<ProductModel>>() {
                    }.getType());

                    if (normalPage == 1) {
                        normalList.clear();
                    }

                    normalList.addAll(lists);
                    listProduct.setAdapter(normalAdapter);
                    normalAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDuobao() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                if (isAtNormal) {
                    normalPage = 1;
                    getNormal();
                } else {
                    duobaoPage = 1;
                    getDuobao();
                }

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
                if (isAtNormal) {
                    normalPage = normalPage + 1;
                    getNormal();
                } else {
                    duobaoPage = duobaoPage + 1;
                    getDuobao();
                }

            }
        }, 1500);
    }
}
