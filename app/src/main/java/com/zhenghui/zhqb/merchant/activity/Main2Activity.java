package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.AssetsModel;
import com.zhenghui.zhqb.merchant.model.MessageModel;
import com.zhenghui.zhqb.merchant.model.MyStoreModel;
import com.zhenghui.zhqb.merchant.model.UserModel;
import com.zhenghui.zhqb.merchant.services.UpdateService;
import com.zhenghui.zhqb.merchant.util.NumberUtil;
import com.zhenghui.zhqb.merchant.util.RefreshLayout;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhenghui.zhqb.merchant.util.Constant.CODE_802503;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_802901;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_804040;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_805056;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_807717;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_808219;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_808275;
import static com.zhenghui.zhqb.merchant.util.Constant.CODE_808276;

public class Main2Activity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.txt_introduce)
    TextView txtIntroduce;
    @BindView(R.id.txt_account)
    TextView txtAccount;
    @BindView(R.id.txt_type)
    TextView txtType;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_withdrawal)
    TextView txtWithdrawal;
    @BindView(R.id.txt_turnover)
    TextView txtTurnover;
    @BindView(R.id.txt_already)
    TextView txtAlready;
    @BindView(R.id.txt_earnings)
    TextView txtEarnings;
    @BindView(R.id.txt_fhq)
    TextView txtFhq;
    @BindView(R.id.layout_fhq)
    LinearLayout layoutFhq;
    @BindView(R.id.txt_notice)
    TextView txtNotice;
    @BindView(R.id.txt_store_status)
    TextView txtStoreStatus;
    @BindView(R.id.txt_store_type)
    TextView txtStoreType;
    @BindView(R.id.layout_store)
    LinearLayout layoutStore;
    @BindView(R.id.txt_good_total)
    TextView txtGoodTotal;
    @BindView(R.id.txt_good_sale)
    TextView txtGoodSale;
    @BindView(R.id.txt_good_out)
    TextView txtGoodOut;
    @BindView(R.id.layout_good)
    LinearLayout layoutGood;
    @BindView(R.id.txt_order_total)
    TextView txtOrderTotal;
    @BindView(R.id.txt_order_send)
    TextView txtOrderSend;
    @BindView(R.id.txt_order_get)
    TextView txtOrderGet;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.txt_order_income)
    TextView txtOrderIncome;
    @BindView(R.id.txt_order_withdraw)
    TextView txtOrderWithdraw;
    @BindView(R.id.txt_bill_status)
    TextView txtBillStatus;
    @BindView(R.id.layout_bill)
    LinearLayout layoutBill;
    @BindView(R.id.txt_order_operation)
    TextView txtOrderOperation;
    @BindView(R.id.txt_account_safe)
    TextView txtAccountSafe;
    @BindView(R.id.txt_account_card)
    TextView txtAccountCard;
    @BindView(R.id.txt_account_logout)
    TextView txtAccountLogout;
    @BindView(R.id.layout_account)
    LinearLayout layoutAccount;
    @BindView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;
    @BindView(R.id.txt_store_record)
    TextView txtStoreRecord;
    @BindView(R.id.txt_record)
    TextView txtRecord;

    private boolean storeFlag = false;

    private ArrayList<MyStoreModel> list;

    private ArrayList<AssetsModel> listAssets;
    private double accountAmount;
    private String accountNumber;

    private SharedPreferences.Editor editor;

    public static Main2Activity instance;

    private UserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        inits();
        initRefreshLayout();

        getVersion();
        getNotice(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
        getStore();
        getMoney();
        getProperty();
        getProduct();
    }

    private void inits() {
        instance = this;

        list = new ArrayList<>();
        listAssets = new ArrayList<>();

        editor = userInfoSp.edit();
        editor.putString("level", "0");
        editor.commit();
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);
    }

    @OnClick({R.id.layout_store, R.id.layout_good, R.id.layout_bill, R.id.layout_order, R.id.txt_notice,
            R.id.layout_account, R.id.txt_withdrawal, R.id.layout_fhq, R.id.txt_introduce,
            R.id.txt_account_logout, R.id.txt_store_record, R.id.txt_account_card, R.id.txt_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_fhq:
                startActivity(new Intent(this, RightsActivity.class));
                break;

            case R.id.txt_notice:
                startActivity(new Intent(this, SystemMessageActivity.class));
                break;

            case R.id.layout_good:
                startActivity(new Intent(Main2Activity.this, ProductManageActivity.class));
                break;

            case R.id.layout_store:
                startActivity(new Intent(Main2Activity.this, StoreManage2Activity.class)
                        .putExtra("status", list.get(0).getStatus()));
                break;

            case R.id.layout_order:
                startActivity(new Intent(Main2Activity.this, GoodOrderActivity.class));
                break;

            case R.id.layout_account:
                startActivity(new Intent(Main2Activity.this, AccountActivity.class));
                break;

            case R.id.txt_withdrawal:
                if (userInfoSp.getString("tradepwdFlag", "").equals("1")) { // tradepwdFlag 支付密码标示 1有 0 无

                    startActivity(new Intent(Main2Activity.this, WithdrawalsActivity.class)
                            .putExtra("balance", accountAmount)
                            .putExtra("accountNumber", accountNumber));

                } else {

                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main2Activity.this, ModifyTradeActivity.class)
                            .putExtra("phone", userInfoSp.getString("mobile", ""))
                            .putExtra("isModify", false));

                }
                break;

            case R.id.layout_bill:
                startActivity(new Intent(Main2Activity.this, BillActivity.class)
                        .putExtra("accountAmount", accountAmount)
                        .putExtra("code", accountNumber));
                break;

            case R.id.txt_introduce:
                startActivity(new Intent(Main2Activity.this, RichTextActivity.class).putExtra("ckey", "new_start"));
                break;

            case R.id.txt_account_logout:
                logOut();
                break;

            case R.id.txt_store_record:
                startActivity(new Intent(Main2Activity.this, StoreRecordActivity.class));
                break;

            case R.id.txt_account_card:
                if (userInfoSp.getString("tradepwdFlag", "").equals("0")) {
                    Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main2Activity.this, ModifyTradeActivity.class)
                            .putExtra("isModify", false)
                            .putExtra("phone", userInfoSp.getString("mobile", "")));
                } else {
                    startActivity(new Intent(Main2Activity.this, BankCardActivity.class));
                }

                break;

            case R.id.txt_record:
                startActivity(new Intent(Main2Activity.this, RecordActivity.class));
                break;
        }
    }


    private void getStore() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808219, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    MyStoreModel model = gson.fromJson(jsonObject.toString(), new TypeToken<MyStoreModel>() {
                    }.getType());

                    list.clear();
                    list.add(model);

                    if (list.size() != 0) {
                        editor.putBoolean("storeFlag", true);
                        editor.putString("storeCode", list.get(0).getCode());
                        editor.putString("level", list.get(0).getLevel());

                        setView();
                    } else {
                        editor.putBoolean("storeFlag", false);

                    }
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        txtName.setText(list.get(0).getName());
        if (list.get(0).getLevel().equals("2")) {
//            txtType.setText("类型：公益型");
//            txtStoreType.setText("公益型商家");

            txtType.setText("类型：普通型");
            txtStoreType.setText("普通型商家");
        } else {
            txtType.setText("类型：普通型");
            txtStoreType.setText("普通型商家");
        }

        if (list.get(0).getStatus().equals("0")) {
            txtStatus.setText("待审核");
            txtStoreStatus.setText("待审核");
        } else if (list.get(0).getStatus().equals("1")) {
            txtStatus.setText("审核通过待上架");
            txtStoreStatus.setText("审核通过待上架");
        } else if (list.get(0).getStatus().equals("2")) {
            txtStatus.setText("营业中");
            txtStoreStatus.setText("营业中");
        } else if (list.get(0).getStatus().equals("3")) {
            txtStatus.setText("歇业中");
            txtStoreStatus.setText("歇业中");
        } else if (list.get(0).getStatus().equals("4")) {
            txtStatus.setText("已下架");
            txtStoreStatus.setText("已下架");
        } else if (list.get(0).getStatus().equals("91")) {
            txtStatus.setText("审核未通过");
            txtStoreStatus.setText("审核未通过");
        }
    }

    private void getMoney() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802503, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<AssetsModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AssetsModel>>() {
                    }.getType());

                    listAssets.addAll(lists);

                    setAssets();

                    getBill();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAssets() {
        for (AssetsModel model : listAssets) {
            if (model.getCurrency().equals("FRB")) {
                accountNumber = model.getAccountNumber();
                accountAmount = model.getAmount();

                txtPrice.setText(NumberUtil.doubleFormatMoney(accountAmount) + "");
                txtAlready.setText("已提现  " + NumberUtil.doubleFormatMoney(model.getOutAmount()) + "");

            }
        }
    }

    private void getProperty() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808275, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtTurnover.setText("累计营业额  " + NumberUtil.doubleFormatMoney(Double.parseDouble(jsonObject.getString("totalProfit"))));
                    txtEarnings.setText("累计分红收益  " + NumberUtil.doubleFormatMoney(Double.parseDouble(jsonObject.getString("totalStockProfit"))));
                    txtFhq.setText("分红权  " + jsonObject.getString("stockCount").split("\\.")[0] + "个");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNotice(final boolean isShow) {

        JSONObject object = new JSONObject();
        try {
            object.put("fromSystemCode", appConfigSp.getString("systemCode", null));
            object.put("channelType", "4");
            object.put("pushType", "");
            object.put("toSystemCode", appConfigSp.getString("systemCode", null));
            object.put("toKind", "2");
            object.put("status", "1");
            object.put("toMobile", "");
            object.put("smsType", "");
            object.put("start", "1");
            object.put("limit", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_804040, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MessageModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MessageModel>>() {
                    }.getType());

                    if (lists.size() > 0) {
                        txtNotice.setText("公告:  " + lists.get(0).getSmsTitle());
                        if(isShow){
                            showNotice(lists.get(0).getSmsTitle(),lists.get(0).getSmsContent());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotice(String title,String content) {
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(content)
                .setPositiveButton("确定", null).show();
    }

    private void getBill() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("accountNumber", accountNumber);
            object.put("dateStart", format.format(new Date()));
            object.put("dateEnd", format.format(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802901, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject object = new JSONObject(result);

                    txtOrderIncome.setText("今日收入  " + NumberUtil.doubleFormatMoney(object.getDouble("incomeAmount")));
                    txtOrderWithdraw.setText("今日提现  " + NumberUtil.doubleFormatMoney(object.getDouble("withdrawAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProduct() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808276, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject object = new JSONObject(result);

                    txtGoodTotal.setText("所有商品  " + object.getInt("productCount"));
                    txtGoodSale.setText("在售商品  " + object.getInt("putOnProductCount"));
                    txtGoodOut.setText("下架商品  " + object.getInt("putOffProductCount"));

                    txtOrderTotal.setText("所有订单  " + object.getInt("orderCount"));
                    txtOrderSend.setText("待发货  " + object.getInt("toSendOrderCount"));
                    txtOrderGet.setText("待收货  " + object.getInt("toReceiveOrderCount"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 退出登录
     */
    private void logOut() {

        RequestParams params = new RequestParams(Xutil.LOGOUT);
        params.addBodyParameter("token", userInfoSp.getString("token", null));

        System.out.println("url=" + Xutil.LOGOUT);
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

                        startActivity(new Intent(Main2Activity.this, LoginActivity.class));
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


        new Xutil().post(CODE_805056, object.toString(), new Xutil.XUtils3CallBackPost() {
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
                    editor.putString("mobile", model.getMobile());

                    editor.commit();

                    txtAccount.setText("账号: " +model.getMobile());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVersion() {
        JSONObject object = new JSONObject();
        try {
            object.put("ckey", "bVersionCode");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(CODE_807717, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    int versionCode = Integer.parseInt(jsonObject.getString("cvalue"));

                    if (versionCode > getVersionCode()) {
                        update();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(Main2Activity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(Main2Activity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("发现新版本请及时更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startService(new Intent(Main2Activity.this, UpdateService.class)
                                .putExtra("appname", "zhsj-release")
                                .putExtra("appurl", "http://m.zhenghuijituan.com/app/zhsj-release.apk"));

                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                getStore();
                getMoney();
                getProperty();
                getNotice(false);
                getProduct();
            }
        }, 1500);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tip();
        }
        return false;
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要退出正汇商家吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("取消", null).show();
    }
}