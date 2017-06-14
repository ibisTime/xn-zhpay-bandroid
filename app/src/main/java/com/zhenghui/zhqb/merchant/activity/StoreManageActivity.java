package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.MyStoreModel;
import com.zhenghui.zhqb.merchant.util.ImageUtil;
import com.zhenghui.zhqb.merchant.util.RefreshLayout;
import com.zhenghui.zhqb.merchant.util.Xutil;
import com.zzhoujay.richtext.RichText;

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

public class StoreManageActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener {


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
    @BindView(R.id.layout_upgrade)
    LinearLayout layoutUpgrade;
    @BindView(R.id.swipe_container)
    RefreshLayout swipeContainer;


    private ArrayList<MyStoreModel> list;

    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();
        initRefreshLayout();
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
                        || txtStatus.getText().toString().equals("审核不通过")) {
                    Toast.makeText(StoreManageActivity.this, "店铺还未通过审核，不能开店", Toast.LENGTH_SHORT).show();
                    switchButton.setChecked(false);
                    return;
                } else {
                    openOrClose();
                }
            }
        });
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
    }

    @OnClick({R.id.layout_back, R.id.layout_discount, R.id.layout_store, R.id.layout_upgrade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_discount:
                if (txtStatus.getText().toString().equals("待审核")
                        || txtStatus.getText().toString().equals("已下架")
                        || txtStatus.getText().toString().equals("审核不通过")) {
                    Toast.makeText(this, "店铺还未通过审核，不能添加折扣券", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(StoreManageActivity.this, DiscountManageActivity.class).putExtra("storeCode", list.get(0).getCode()));
                break;

            case R.id.layout_store:
                startActivity(new Intent(StoreManageActivity.this, StoreContractActivity.class).putExtra("isModifi", true));
                break;

            case R.id.layout_upgrade:
                System.out.println("userInfoSp.getString(\"level\",\"0\")=" + userInfoSp.getString("level", "0"));

                // 不是理财商家
                if (userInfoSp.getString("level", "0").equals("1")) {
                    statement(view);
                } else if (userInfoSp.getString("level", "0").equals("2")) { // 是理财商家
                    startActivity(new Intent(StoreManageActivity.this, EarningsActivity.class));
                }

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

        new Xutil().post("808219", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();
                try {
                    JSONArray jsonObject = new JSONArray(result);
                    Gson gson = new Gson();
                    ArrayList<MyStoreModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<MyStoreModel>>() {
                    }.getType());
                    list.clear();
                    list.addAll(lists);
                    editor.putString("storeCode", list.get(0).getCode());
                    editor.putString("level", list.get(0).getLevel());
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
        ImageUtil.glide(list.get(0).getAdvPic(), imgCover, this);
        txtName.setText(list.get(0).getName());

        System.out.println("list.get(0).getStatus()=" + list.get(0).getStatus());

        if (list.get(0).getStatus().equals("0")) {
            txtStatus.setText("待审核");
        } else if (list.get(0).getStatus().equals("1")) {
            txtStatus.setText("审核通过待上架");
            switchButton.setChecked(false);
        } else if (list.get(0).getStatus().equals("2")) {
            txtStatus.setText("已上架，开店");
            switchButton.setChecked(true);
        } else if (list.get(0).getStatus().equals("3")) {
            txtStatus.setText("已上架，关店");
        } else if (list.get(0).getStatus().equals("4")) {
            txtStatus.setText("已下架");
        } else if (list.get(0).getStatus().equals("91")) {
            txtStatus.setText("审核不通过: " + list.get(0).getRemark());
        }
//        txtStatus.setText(txtStatus.getText()+",");
    }

    private TextView content;

    private void statement(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_statement, null);

        content = (TextView) mview.findViewById(R.id.txt_content);
        TextView ok = (TextView) mview.findViewById(R.id.txt_ok);
        LinearLayout layoutStatement = (LinearLayout) mview.findViewById(R.id.layout_statement);

        getStatement();

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
                getLevelUp();
            }
        });

        layoutStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    public void getStatement() {
        JSONObject object = new JSONObject();
        try {
            object.put("ckey", "store_up_statement");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("807717", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    RichText.from(jsonObject.getString("note")).into(content);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    public void getLevelUp() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", userInfoSp.getString("storeCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808207", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                finish();
                startActivity(new Intent(StoreManageActivity.this, EarningsActivity.class));

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

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                getData();
                // 更新数据
                // 更新完后调用该方法结束刷新

            }
        }, 1500);
    }
}
