package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.adapter.RecyclerViewAdapter;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.ImageUtil;
import com.zhenghui.zhqb.merchant.util.QiNiuUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhenghui.zhqb.merchant.util.ImageUtil.RESULT_CAMARA_IMAGE;

public class DuobaoAddActivity extends MyBaseActivity {

    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_advertisement)
    EditText edtAdvertisement;
    @BindView(R.id.edt_detail)
    EditText edtDetail;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_confirm)
    TextView txtConfirm;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private String cover = "";
    private boolean isCover = true;
    private PopupWindow popupWindow;

    private List<String> listPic;
    private List<String> listPicUrl;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duobao_add);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits(){
        listPic = new ArrayList<>();
        listPicUrl = new ArrayList<>();

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DuobaoAddActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(DuobaoAddActivity.this, listPic, listPicUrl);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @OnClick({R.id.layout_back, R.id.img_photo, R.id.img_add, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.img_photo:
                isCover = true;
                choosePhoto(view);
                break;

            case R.id.img_add:
                isCover = false;
                choosePhoto(view);
                break;

            case R.id.txt_confirm:
                commit();
                break;
        }
    }

    /**
     * 选择照片
     *
     * @param view
     */
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
        TextView txtTitle = (TextView) mview
                .findViewById(R.id.txt_title);

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

        txtTitle.setText("选择封面");

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

        if (requestCode == ImageUtil.RESULT_LOAD_IMAGE) {
            if (isCover) {
                Glide.with(DuobaoAddActivity.this).load(album(data)).into(imgPhoto);
            } else {
                listPic.add(album(data));
                recyclerViewAdapter.notifyDataSetChanged();
            }

            new QiNiuUtil(DuobaoAddActivity.this, album(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    System.out.println("key=" + key);

                    if (isCover) {
                        cover = key;
                    } else {
                        listPicUrl.add(key);
                    }
                }
            }, true);

        } else if (requestCode == ImageUtil.RESULT_CAMARA_IMAGE) {

            if (isCover) {
                Glide.with(DuobaoAddActivity.this).load(camara(data)).into(imgPhoto);
            } else {
                listPic.add(camara(data));
                recyclerViewAdapter.notifyDataSetChanged();

            }

            new QiNiuUtil(DuobaoAddActivity.this, camara(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    if (isCover) {
                        cover = key;
                    } else {
                        listPicUrl.add(key);
                    }
                }
            }, true);
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

    private void commit() {
        String pic = "";

        for (String s : listPicUrl) {
            pic = pic + s + "||";
        }
        System.out.println("pic=" + pic);

        JSONObject object = new JSONObject();
        try {
            object.put("storeCode", userInfoSp.getString("userId", null));
            object.put("name", edtName.getText().toString().trim());
            object.put("slogan", edtAdvertisement.getText().toString().trim());
            object.put("advPic", cover);
            object.put("descriptionPic", pic.substring(0,pic.length()-2));
            object.put("descriptionText", edtDetail.getText().toString().trim());
            object.put("systemCode", appConfigSp.getString("systemCode", null));

//            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808300", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    Toast.makeText(DuobaoAddActivity.this, "添加夺宝商品成功", Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(result);

                    jsonObject.getString("code");

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuobaoAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuobaoAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
