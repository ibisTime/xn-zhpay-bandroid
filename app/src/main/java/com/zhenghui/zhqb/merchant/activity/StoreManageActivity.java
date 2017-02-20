package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.model.StoreModel;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.ImageUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leiq on 2016/12/26.
 * 店铺签约
 */

public class StoreManageActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.layout_store)
    LinearLayout layoutStore;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;
    @BindView(R.id.layout_status)
    LinearLayout layoutStatus;
    @BindView(R.id.layout_discount)
    LinearLayout layoutDiscount;


    private ArrayList<StoreModel> list;

    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        System.out.println("onCreate");

        inits();
        initEvent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void inits() {
        list = new ArrayList<>();

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    private void initEvent() {
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtStatus.getText().toString().equals("待审核")
                        || txtStatus.getText().toString().equals("已下架")
                        || txtStatus.getText().toString().equals("审核不通过")){
                    Toast.makeText(StoreManageActivity.this, "店铺还未通过审核，不能开店", Toast.LENGTH_SHORT).show();
                    switchButton.setChecked(false);
                    return;
                }else{
                    openOrClose();
                }
            }
        });
    }


    @OnClick({R.id.layout_back, R.id.layout_discount, R.id.layout_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_discount:
                if (txtStatus.getText().toString().equals("待审核")
                        || txtStatus.getText().toString().equals("已下架")
                        || txtStatus.getText().toString().equals("审核不通过")){
                    Toast.makeText(this, "店铺还未通过审核，不能添加折扣券", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(StoreManageActivity.this, DiscountManageActivity.class).putExtra("storeCode",list.get(0).getCode()));
                break;

            case R.id.layout_store:
                startActivity(new Intent(StoreManageActivity.this, StoreContractActivity.class).putExtra("isModifi",true));
                break;
        }
    }

    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808215", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();
                try {
                    JSONArray jsonObject = new JSONArray(result);
                    Gson gson = new Gson();
                    ArrayList<StoreModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<StoreModel>>() {
                    }.getType());
                    list.clear();
                    list.addAll(lists);
                    editor.putString("storeCode",list.get(0).getCode());
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setView();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StoreManageActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StoreManageActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openOrClose() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("code", list.get(0).getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808206", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                getData();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StoreManageActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StoreManageActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        ImageUtil.glide(list.get(0).getAdPic(), imgCover, this);
        txtName.setText(list.get(0).getName());

        System.out.println("list.get(0).getStatus()="+list.get(0).getStatus());

        if (list.get(0).getStatus().equals("0")) {
            txtStatus.setText("待审核");
        } else if (list.get(0).getStatus().equals("1")) {
            txtStatus.setText("已上架，关店");
            switchButton.setChecked(false);
        } else if (list.get(0).getStatus().equals("2")) {
            txtStatus.setText("已上架，开店");
            switchButton.setChecked(true);
        } else if (list.get(0).getStatus().equals("3")) {
            txtStatus.setText("已下架");
        } else {
            txtStatus.setText("审核不通过");
        }
    }
}
