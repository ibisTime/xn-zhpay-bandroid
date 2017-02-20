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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.adapter.RecyclerViewAdapter;
import com.zhenghui.zhqb.merchant.model.ProductModel;
import com.zhenghui.zhqb.merchant.model.ProductTypeModel;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhenghui.zhqb.merchant.util.ImageUtil.RESULT_CAMARA_IMAGE;

/**
 * Created by Leiq on 2016/12/26.
 * 添加商品
 */

public class ProductAddActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.txt_bigType)
    TextView txtBigType;
    @BindView(R.id.layout_bigType)
    LinearLayout layoutBigType;
    @BindView(R.id.txt_smallType)
    TextView txtSmallType;
    @BindView(R.id.layout_smallType)
    LinearLayout layoutSmallType;
    @BindView(R.id.edt_detail)
    EditText edtDetail;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_confirm)
    TextView txtConfirm;
    @BindView(R.id.edt_advertisement)
    EditText edtAdvertisement;
    @BindView(R.id.edt_quantity)
    EditText edtQuantity;
    @BindView(R.id.edt_costPrice)
    EditText edtCostPrice;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_reason)
    TextView txtReason;

    private String cover = "";

    private boolean isCover = true;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;


    // 所有
    private List<ProductTypeModel> typeList;
    private List<ProductTypeModel> bigTypeList;
    private List<ProductTypeModel> smallTypeList;

    private List<ProductTypeModel> smallList1;
    private List<ProductTypeModel> smallList2;
    private List<ProductTypeModel> smallList3;


    private String[] bigType = {"剁手合集", "0元试购"};
    private String[] smallType1;
    private String[] smallType2;
    private String[] smallType3;

    private String category;
    private String type;

    private List<String> listPic;
    private List<String> listPicUrl;
    private RecyclerViewAdapter recyclerViewAdapter;

    private String code;
    private boolean isModifi;

    private ProductModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getProductType();
        initRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        typeList = new ArrayList<>();
        bigTypeList = new ArrayList<>();
        smallTypeList = new ArrayList<>();

        smallList1 = new ArrayList<>();
        smallList2 = new ArrayList<>();
        smallList3 = new ArrayList<>();

        listPic = new ArrayList<>();
        listPicUrl = new ArrayList<>();

        code = getIntent().getStringExtra("code");
        isModifi = getIntent().getBooleanExtra("isModifi", false);
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductAddActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(ProductAddActivity.this, listPic, listPicUrl);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @OnClick({R.id.layout_back, R.id.img_photo, R.id.layout_bigType, R.id.layout_smallType, R.id.img_add, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.img_photo:
                isCover = true;
                choosePhoto(view);
                break;

            case R.id.layout_bigType:
                chooseBigType();
                break;

            case R.id.layout_smallType:
                if (txtBigType.getText().equals("")) {
                    Toast.makeText(this, "请先选择大类", Toast.LENGTH_SHORT).show();
                    return;
                }
                chooseSmallType();
                break;

            case R.id.img_add:
                isCover = false;
                choosePhoto(view);
                break;

            case R.id.txt_confirm:
                if(checkData()){
                    if (isModifi) {
                        modifi();
                    } else {
                        commit();
                    }
                }


                break;
        }
    }

    /**
     * 选择照片
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

        if (data == null) {
            return;
        }

        if (requestCode == ImageUtil.RESULT_LOAD_IMAGE) {
            if (isCover) {
                Glide.with(ProductAddActivity.this).load(album(data)).into(imgPhoto);
            } else {
                listPic.add(album(data));
                recyclerViewAdapter.notifyDataSetChanged();
            }

            new QiNiuUtil(ProductAddActivity.this, album(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
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
                Glide.with(ProductAddActivity.this).load(camara(data)).into(imgPhoto);
            } else {
                listPic.add(camara(data));
                recyclerViewAdapter.notifyDataSetChanged();

            }

            new QiNiuUtil(ProductAddActivity.this, camara(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
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

    /**
     * 获取产品类别
     */
    private void getProductType() {
        JSONObject object = new JSONObject();
        try {
            object.put("parentCode", "");
            object.put("name", "");
            object.put("type", "");
            object.put("orderColumn", "order_no");
            object.put("orderDir", "asc");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808006", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {


                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<ProductTypeModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<ProductTypeModel>>() {
                    }.getType());

                    typeList.addAll(lists);

                    creatType();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ProductAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ProductAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void creatType() {
        for (ProductTypeModel model : typeList) {
            if (model.getParentCode().equals("0")) {
                if (!model.getName().equals("一元夺宝")) {
                    bigTypeList.add(model);
                }
            } else {
                smallTypeList.add(model);
            }
        }

        setType();
    }

    private void setType() {
        for (ProductTypeModel big : bigTypeList) {
            if (big.getOrderNo() == 1) {
//                bigType[0] = big.getName();
                for (ProductTypeModel small : smallTypeList) {
                    if (small.getParentCode().equals(big.getCode())) {
                        smallList1.add(small);
                    }
                }
            } else if (big.getOrderNo() == 2) {
//                bigType[1] = big.getName();
                for (ProductTypeModel small : smallTypeList) {
                    if (small.getParentCode().equals(big.getCode())) {
                        smallList2.add(small);
                    }
                }
            } else if (big.getOrderNo() == 3) {
//                bigType[2] = big.getName();
                for (ProductTypeModel small : smallTypeList) {
                    if (small.getParentCode().equals(big.getCode())) {
                        smallList3.add(small);
                    }
                }
            }
        }


        smallType1 = new String[smallList1.size()];
        smallType3 = new String[smallList3.size()];

        for (int i = 0; i < smallList1.size(); i++) {
            smallType1[i] = smallList1.get(i).getName();
        }
        if (smallList2.size() != 0) {
            smallType2 = new String[smallList2.size()];
            for (int i = 0; i < smallList2.size(); i++) {
                smallType2[i] = smallList2.get(i).getName();
            }
        }
        for (int i = 0; i < smallList3.size(); i++) {
            smallType3[i] = smallList3.get(i).getName();
        }

        if (isModifi) {
            txtTitle.setText("修改商品");
            getDatas();
        }

    }


    private void chooseBigType() {
        new AlertDialog.Builder(this).setTitle("请选择大类").setSingleChoiceItems(
                bigType, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        txtBigType.setText(bigType[which]);
                        txtSmallType.setText("");
                        category = bigTypeList.get(which).getCode() + "";
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void chooseSmallType() {

        final String[] str;
        if (txtBigType.getText().toString().trim().equals("剁手合集")) {
            str = smallType1;
        } else if (txtBigType.getText().toString().trim().equals("一元夺宝")) {
            str = smallType2;
        } else {
            str = smallType3;
        }

        new AlertDialog.Builder(this).setTitle("请选择小类").setSingleChoiceItems(
                str, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        txtSmallType.setText(str[which]);

                        if (txtBigType.getText().toString().trim().equals("剁手合集")) {
                            type = smallList1.get(which).getCode();
                        } else if (txtBigType.getText().toString().trim().equals("一元夺宝")) {
                            type = smallList2.get(which).getCode();
                        } else if (txtBigType.getText().toString().trim().equals("0元试购")) {
                            type = smallList3.get(which).getCode();
                        }

                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }


    private void modifi() {
        String pic = "";

        for (String s : listPicUrl) {
            pic = pic + s + "||";
        }
        System.out.println("pic=" + pic);

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("category", category);
            object.put("type", type);
            object.put("name", edtName.getText().toString().trim());
            object.put("advTitle", edtAdvertisement.getText().toString().trim());
            object.put("advPic", cover);
            object.put("pic1", pic.substring(0, pic.length() - 2));
            object.put("pic2", "");
            object.put("pic3", "");
            object.put("pic4", "");
            object.put("description", edtDetail.getText().toString().trim());
            object.put("costPrice", "0");
            object.put("quantity", "");
            object.put("updater", userInfoSp.getString("userId", null));
            object.put("remark", "");
            object.put("companyCode", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808012", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    Toast.makeText(ProductAddActivity.this, "修改商品成功", Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = new JSONObject(result);

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ProductAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ProductAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void commit() {
        String pic = "";

        for (String s : listPicUrl) {
            pic = pic + s + "||";
        }
        System.out.println("pic=" + pic);

        JSONObject object = new JSONObject();
        try {
            object.put("category", category);
            object.put("type", type);
            object.put("name", edtName.getText().toString().trim());
            object.put("advTitle", edtAdvertisement.getText().toString().trim());
            object.put("advPic", cover);
            object.put("pic1", pic.substring(0, pic.length() - 2));
            object.put("pic2", "");
            object.put("pic3", "");
            object.put("pic4", "");
            object.put("description", edtDetail.getText().toString().trim());
            object.put("costPrice", "0");
            object.put("quantity", "");
            object.put("updater", userInfoSp.getString("userId", null));
            object.put("remark", "");
            object.put("companyCode", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808010", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    Toast.makeText(ProductAddActivity.this, "添加商品成功", Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(result);

                    jsonObject.getString("code");

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ProductAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ProductAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 获取商品详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808022", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<ProductModel>() {
                    }.getType());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setView();
                setData();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ProductAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ProductAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setView() {
        ImageUtil.glide(model.getAdvPic(), imgPhoto, this);

        for (ProductTypeModel big : bigTypeList) {
            if (big.getCode().equals(model.getCategory())) {
                txtBigType.setText(big.getName());
            }
        }

        for (ProductTypeModel small : smallTypeList) {
            if (small.getCode().equals(model.getType())) {
                txtSmallType.setText(small.getName());
            }
        }

        edtName.setText(model.getName());
        edtAdvertisement.setText(model.getAdvTitle());
        edtDetail.setText(model.getDescription());

        if (model.getStatus().equals("2")) {
            txtReason.setVisibility(View.VISIBLE);
            txtReason.setText("失败原因:"+model.getRemark());
        }else{
            txtReason.setVisibility(View.GONE);
        }

        String[] str = model.getPic1().split("\\|\\|");
        for (int i = 0; i < str.length; i++) {
            listPic.add(str[i]);
            listPicUrl.add(str[i]);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void setData() {
        category = model.getCategory();
        type = model.getType();
        cover = model.getAdvPic();
    }

    private boolean checkData() {

        if(cover.equals("")){
            Toast.makeText(this, "请添加商品封面", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写商品名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtAdvertisement.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写商品广告语", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtBigType.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择商品大类", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtSmallType.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择商品小类", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtDetail.getText().toString().trim().length() < 20) {
            Toast.makeText(this, "店铺详情不能少于20个字", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (listPic.size() == 0){
            Toast.makeText(this, "请添加店铺图片", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
