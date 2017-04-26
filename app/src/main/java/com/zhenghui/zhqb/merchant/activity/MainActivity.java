package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.AssetsModel;
import com.zhenghui.zhqb.merchant.model.UserModel;
import com.zhenghui.zhqb.merchant.util.ImageUtil;
import com.zhenghui.zhqb.merchant.util.MoneyUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;
import com.zzhoujay.richtext.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Leiq on 2016/12/26.
 * 商户端主页
 */

public class MainActivity extends MyBaseActivity implements EMMessageListener {


    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.img_photo)
    CircleImageView imgPhoto;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_bill)
    TextView txtBill;
    @BindView(R.id.txt_cash)
    TextView txtCash;
    @BindView(R.id.layout_service)
    LinearLayout layoutService;
    @BindView(R.id.layout_notice)
    LinearLayout layoutNotice;
    @BindView(R.id.layout_store)
    LinearLayout layoutStore;
    @BindView(R.id.layout_mall)
    LinearLayout layoutMall;
    @BindView(R.id.txt_point)
    TextView txtPoint;
    @BindView(R.id.txt_introduce)
    TextView txtIntroduce;
    @BindView(R.id.txt_earnings)
    TextView txtEarnings;

    private UserModel model;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;
    private SharedPreferences.Editor editor;

    private List<AssetsModel> list;
    private double frb;
    private String frbCode;

    public static MainActivity instance;

    private boolean storeFlag = false;
    private boolean anotherDeviceFlag = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    txtPoint.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    txtPoint.setVisibility(View.GONE);
                    break;

                case 3:
                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("userId", null);
                    editor.putString("token", null);
                    editor.commit();

                    // 实例化Intent
                    Intent intent = new Intent();
                    // 设置Intent的action属性
                    intent.setAction("com.zhenghui.zhqb.merchant.receiver.LogoutReceiver");
                    // 发出广播
                    sendBroadcast(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        if (userInfoSp.getBoolean("first", true)) { //第一次进入
            showTip();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userInfoSp.getString("userId", null) != null) {
            getData();
            getDatas();
            getStroe();
            getServiceUnReade();
            addMessageListener();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        instance = this;

        list = new ArrayList<>();
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        editor = userInfoSp.edit();
        editor.putString("level", "0");
        editor.commit();
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

    //    private void setWindow() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();MyBaseActivity
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//    }

    private void showTip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否打开新消息通知?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putBoolean("push", true);
                        editor.putBoolean("first", false);
                        editor.commit();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putBoolean("push", false);
                        editor.putBoolean("first", false);
                        editor.commit();
                    }
                })
                .show();
    }

    @OnClick({R.id.img_photo, R.id.txt_bill, R.id.txt_cash, R.id.layout_service, R.id.layout_notice, R.id.layout_store, R.id.layout_mall, R.id.txt_introduce, R.id.txt_earnings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                startActivity(new Intent(MainActivity.this, PersonalActivity.class).putExtra("userModel", model));
                break;

            case R.id.txt_bill:
                startActivity(new Intent(MainActivity.this, BillActivity.class).putExtra("code", frbCode));
                break;

            case R.id.txt_cash:
//                if (userInfoSp.getString("identityFlag", null).equals("1")) { //identityFlag 实名认证标示 1有 0 无

                    if (userInfoSp.getString("tradepwdFlag", null).equals("1")) { // tradepwdFlag 交易密码标示 1有 0 无

                        startActivity(new Intent(MainActivity.this, WithdrawalsActivity.class)
                                .putExtra("balance", frb)
                                .putExtra("accountNumber", frbCode));

                    } else {

                        Toast.makeText(this, "请先设置交易密码", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ModifyTradeActivity.class).putExtra("isModify", false));

                    }

//                } else {
//                    Toast.makeText(this, "请先实名认证", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, AuthenticateActivity.class));
//                }
                break;

            case R.id.layout_service:
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                intent.putExtra("nickName", "客服");
                intent.putExtra("myPhoto", userInfoSp.getString("photo", ""));
                intent.putExtra("otherPhoto", "");
                intent.putExtra(EaseConstant.EXTRA_USER_ID, "androidkefu");
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                startActivity(intent);
                break;

            case R.id.layout_notice:
                startActivity(new Intent(MainActivity.this, SystemMessageActivity.class));
                break;

            case R.id.layout_store:
                if (storeFlag) {
                    startActivity(new Intent(MainActivity.this, StoreManageActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, StoreContractActivity.class));
                }
                break;

            case R.id.layout_mall:
                startActivity(new Intent(MainActivity.this, ManageActivity.class));
                break;

            case R.id.txt_introduce:
                startActivity(new Intent(MainActivity.this, RichTextActivity.class).putExtra("ckey","new_start"));
//                startActivity(new Intent(MainActivity.this, ParameterActivity.class));
                break;

            case R.id.txt_earnings:
                System.out.println("userInfoSp.getString(\"level\",\"-1\")="+userInfoSp.getString("level","-1"));
                // 不是理财商家
                if(userInfoSp.getString("level","0").equals("1")){
                    statement(view);
                }else if(userInfoSp.getString("level","0").equals("2")){ // 是理财商家
                    startActivity(new Intent(MainActivity.this, EarningsActivity.class));
                }else if(userInfoSp.getString("level","0").equals("0")){
                    Toast.makeText(MainActivity.this, "您的店铺还未上架", Toast.LENGTH_SHORT).show();
                }


                break;
        }
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
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {

        ImageUtil.photo(model.getUserExt().getPhoto(), imgPhoto, this);
    }


    private void getStroe() {
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
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() != 0) {
                        storeFlag = true;
                        if(jsonArray.getJSONObject(0).getJSONObject("store").getString("level").equals("2")){
                            txtName.setText("公益商家");
                        }else{
                            txtName.setText(jsonArray.getJSONObject(0).getJSONObject("store").getString("name"));
                        }
                        editor.putString("storeCode", jsonArray.getJSONObject(0).getJSONObject("store").getString("code"));
                        editor.putString("level", jsonArray.getJSONObject(0).getJSONObject("store").getString("level"));
                        editor.commit();
                    } else {
                        storeFlag = false;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802503", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
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
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAssets() {
        for (AssetsModel model : list) {
            if (model.getCurrency().equals("FRB")) {
                txtMoney.setText("¥" + MoneyUtil.moneyFormatDouble(model.getAmount()) + "");
                frbCode = model.getAccountNumber();
                frb = model.getAmount();

            }
        }
    }


    private void getServiceUnReade() {
        try {
            Message msg = new Message();
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(SERVICE_ID);
            if (conversation != null) {
                if (conversation.getUnreadMsgCount() > 0) {
                    msg.what = 1;
                } else {
                    msg.what = 2;
                }
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMessageListener() {
        EaseUI.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            EaseUI.getInstance().getNotifier().onNewMsg(message);
            getServiceUnReade();
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("error=" + error);

                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Toast.makeText(MainActivity.this, "帐号已被移除", Toast.LENGTH_SHORT).show();
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {

                        if (anotherDeviceFlag) {
                            anotherDeviceFlag = false;
                            // 显示帐号在其他设备登录
//                            Toast.makeText(MainActivity.this, "帐号在其他设备登录", Toast.LENGTH_SHORT).show();
                            logout();
                        }

                    } else {


                    }
                }
            });
        }
    }

    private void logout() {

        EMClient.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                System.out.println("logout()------>onError()");
                System.out.println("code=" + code);
                System.out.println("message=" + message);
            }
        });
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
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

                startActivity(new Intent(MainActivity.this, EarningsActivity.class));

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
