package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kyleduo.switchbutton.SwitchButton;
import com.qiniu.android.http.ResponseInfo;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.UserModel;
import com.zhenghui.zhqb.merchant.services.UpdateService;
import com.zhenghui.zhqb.merchant.util.CacheUtil;
import com.zhenghui.zhqb.merchant.util.ImageUtil;
import com.zhenghui.zhqb.merchant.util.QiNiuUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhenghui.zhqb.merchant.util.ImageUtil.RESULT_CAMARA_IMAGE;

/**
 * Created by Leiq on 2016/12/26.
 * 个人设置
 */

public class PersonalActivity extends MyBaseActivity {

    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.img_photo)
    CircleImageView imgPhoto;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_contract)
    TextView txtContract;
    @BindView(R.id.layout_photo)
    LinearLayout layoutPhoto;
    @BindView(R.id.layout_bankCard)
    LinearLayout layoutBankCard;
    @BindView(R.id.layout_account)
    LinearLayout layoutAccount;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;
    @BindView(R.id.layout_about)
    LinearLayout layoutAbout;
    @BindView(R.id.txt_logout)
    TextView txtLogout;
    @BindView(R.id.layout_cache)
    LinearLayout layoutCache;
    @BindView(R.id.txt_cache)
    TextView txtCache;
    @BindView(R.id.txt_versionName)
    TextView txtVersionName;
    @BindView(R.id.layout_version)
    LinearLayout layoutVersion;

    private UserModel model;
    private SharedPreferences userInfoSp;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();

        getStroe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }


    private void inits() {
        model = (UserModel) getIntent().getSerializableExtra("userModel");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        try {
            txtCache.setText(CacheUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtVersionName.setText("V"+getVersionName());
    }

    private void initEvent() {
        final SharedPreferences.Editor editor = userInfoSp.edit();

        System.out.println("userInfoSp.getBoolean(\"push\",false)=" + userInfoSp.getBoolean("push", false));
        if (userInfoSp.getBoolean("push", false)) { // true:当前消息通知开关为 打开状态

            switchButton.setChecked(true);
            // 当天晚上23：59点到第二天晚上0：0点为静音时段。 全天打开消息通知
            JPushInterface.setSilenceTime(getApplicationContext(), 23, 59, 0, 0);
            editor.putBoolean("push", true);

        } else {
            switchButton.setChecked(false);
            // 当天晚上0：0点到第二天晚上23：59点为静音时段。 全天关闭消息通知
            JPushInterface.setSilenceTime(getApplicationContext(), 0, 0, 23, 59);
            editor.putBoolean("push", false);
        }

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("switchButton.isChecked()=" + switchButton.isChecked());
                if (switchButton.isChecked()) { // true:打开消息通知

                    // 当天晚上23：59点到第二天晚上0：0点为静音时段。 全天打开消息通知
                    JPushInterface.setSilenceTime(getApplicationContext(), 23, 59, 0, 0);
                    editor.putBoolean("push", true);

                } else { // false:关闭消息通知

                    // 当天晚上0：0点到第二天晚上23：59点为静音时段。 全天关闭消息通知
                    JPushInterface.setSilenceTime(getApplicationContext(), 0, 0, 23, 59);
                    editor.putBoolean("push", false);

                }
                editor.commit();
            }
        });
    }

    @OnClick({R.id.layout_back, R.id.layout_photo, R.id.layout_bankCard, R.id.layout_account,
            R.id.layout_about, R.id.txt_logout, R.id.layout_cache, R.id.layout_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_photo:
                choosePhoto(view);
                break;

            case R.id.layout_bankCard:
                startActivity(new Intent(PersonalActivity.this, BankCardActivity.class));
                break;

            case R.id.layout_account:
                startActivity(new Intent(PersonalActivity.this, AccountActivity.class));
                break;



            case R.id.txt_logout:
                logout();
                break;

            case R.id.layout_about:
                startActivity(new Intent(PersonalActivity.this, AboutActivity.class));
                break;

            case R.id.layout_cache:
                clearCache();
                break;

            case R.id.layout_version:
                getVersion();
                break;
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


    private void logout() {

        SharedPreferences.Editor editor = userInfoSp.edit();
        editor.putString("userId", null);
        editor.putString("token", null);
        editor.commit();


        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

                finish();
                MainActivity.instance.finish();
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class).putExtra(LoginActivity.TIP, true));


            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 选择头像
     *
     * @param view
     */
    private PopupWindow popupWindow;

    private void choosePhoto(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(
                R.layout.popup_release, null);

        TextView txtPhotograph = (TextView) mview
                .findViewById(R.id.txt_photograph);
        TextView txtAlbum = (TextView) mview
                .findViewById(R.id.txt_album);
        TextView txtCancel = (TextView) mview
                .findViewById(R.id.txt_releasePopup_cancel);

        LinearLayout dismiss = (LinearLayout) mview.findViewById(R.id.quxiao);

        popupWindow = new PopupWindow(mview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);

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

        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        txtAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // 调用android的图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, ImageUtil.RESULT_LOAD_IMAGE);
                popupWindow.dismiss();
            }
        });

        txtPhotograph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMARA_IMAGE);

                popupWindow.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, Gravity.BOTTOM);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //data为B中回传的Intent
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == ImageUtil.RESULT_LOAD_IMAGE) {
                if (data.getData() != null) {
                    Glide.with(PersonalActivity.this).load(album(data)).into(imgPhoto);

                    new QiNiuUtil(PersonalActivity.this, album(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                            updatePhoto(key);
                        }
                    }, true);
                }

            } else if (requestCode == ImageUtil.RESULT_CAMARA_IMAGE) {
                if (data.getExtras() != null) {
                    Glide.with(PersonalActivity.this).load(camara(data)).into(imgPhoto);

                    new QiNiuUtil(PersonalActivity.this, camara(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                            updatePhoto(key);
                        }
                    }, true);
                }

            }

        }
    }

    /**
     * 调用系统相册的操作,在onActivityResult中调用
     *
     * @param data onActivityResult中的Intent
     */
    public String album(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Log.d("picturePath", picturePath);
        BitmapFactory.decodeFile(picturePath);
        return picturePath;
    }

    /**
     * 调用系统相机,在onActivityResult中调用，拍照后保存到sdcard卡中
     *
     * @param data onActivityResult中的Intent
     * @return
     */
    public String camara(Intent data) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            Toast.makeText(this,
                    "SD card is not avaiable/writeable right now.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        String name = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".jpg";
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        FileOutputStream b = null;
        File file = new File("sdcard/DCIM/Camera/");
        file.mkdirs();// 创建文件夹
        String fileName = "sdcard/DCIM/Camera/" + name;
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
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

                    setView();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonalActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonalActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        ImageUtil.photo(model.getUserExt().getPhoto(), imgPhoto, this);
        txtPhone.setText(model.getMobile());
    }

    private void getContractNo() {
        JSONObject object = new JSONObject();
        try {
            object.put("fromUser", userInfoSp.getString("userId", null));
            object.put("code", userInfoSp.getString("storeCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808218", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getString("contractNo") == null) {
                        txtContract.setText("合同编号:暂无");
                    } else {
                        if (jsonObject.getString("contractNo").equals("")) {
                            txtContract.setText("合同编号:暂无");
                        } else {
                            txtContract.setText("合同编号:" + jsonObject.getString("contractNo"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonalActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonalActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePhoto(String url) {
        JSONObject object = new JSONObject();
        try {
            object.put("photo", url);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805077", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(PersonalActivity.this, "头像修改成功", Toast.LENGTH_SHORT).show();
                getData();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonalActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonalActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
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
                    JSONArray jsonObject = new JSONArray(result);
                    if (jsonObject.length() != 0) {
                        getContractNo();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonalActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonalActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVersion(){
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

                    if(versionCode > getVersionCode()){
                        update();
                    }else {
                        Toast.makeText(PersonalActivity.this, "当前已是最新版本哦", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonalActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonalActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("发现新版本请及时更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startService(new Intent(PersonalActivity.this, UpdateService.class)
                                .putExtra("appname", "zhsj-release")
                                .putExtra("appurl", "http://m.zhenghuijituan.com/app/zhsj-release.apk"));

                    }
                }).setNegativeButton("取消", null).show();
    }
}
