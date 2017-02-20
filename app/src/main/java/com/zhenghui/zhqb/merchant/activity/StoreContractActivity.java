package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.qiniu.android.http.ResponseInfo;
import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.adapter.RecyclerViewAdapter;
import com.zhenghui.zhqb.merchant.model.StoreModel;
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

/**
 * Created by Leiq on 2016/12/26.
 * 店铺管理
 */

public class StoreContractActivity extends MyBaseActivity implements GeocodeSearch.OnGeocodeSearchListener {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_btn)
    TextView txtBtn;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.txt_type)
    TextView txtType;
    @BindView(R.id.layout_type)
    LinearLayout layoutType;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.txt_orientation)
    TextView txtOrientation;
    @BindView(R.id.layout_orientation)
    LinearLayout layoutOrientation;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_advertisement)
    EditText edtAdvertisement;
    @BindView(R.id.edt_legalPerson)
    EditText edtLegalPerson;
    @BindView(R.id.edt_referrer)
    EditText edtReferrer;
    @BindView(R.id.edt_use)
    EditText edtUse;
    @BindView(R.id.edt_nonuse)
    EditText edtNonuse;
    @BindView(R.id.edt_detail)
    EditText edtDetail;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String type = "1";
    private String imageUrl = "";
    private String latitude = "";
    private String longitude = "";
    private String addressName = "";

    private String[] storeType = {"美食", "KTV", "美发", "便利店", "足浴", "酒店", "亲子", "蔬果"};


    private boolean isCover = true;
    private boolean isModifi = false;

    private List<String> listPic;
    private List<String> listPicUrl;
    private RecyclerViewAdapter recyclerViewAdapter;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private StoreModel model;

    private GeocodeSearch geocoderSearch;

    private String mCurrentProviceName;
    private String mCurrentCityName;
    private String mCurrentDistrictName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_contract);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initRecyclerView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {

        listPic = new ArrayList<>();
        listPicUrl = new ArrayList<>();

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        isModifi = getIntent().getBooleanExtra("isModifi",false);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        if(isModifi){
            txtBtn.setText("修改");
            getDatas();
        }else {
            txtBtn.setText("保存");
        }
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StoreContractActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(StoreContractActivity.this, listPic,listPicUrl);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initEvent() {
    }

    @OnClick({R.id.layout_back, R.id.txt_btn, R.id.layout_type, R.id.layout_location, R.id.img_add, R.id.img_cover, R.id.layout_orientation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_btn:
                if(checkData()){
                    commit();
                }
                break;

            case R.id.img_cover:
                isCover = true;
                choosePhoto(view);
                break;

            case R.id.layout_type:
                chooseBankCard();
                break;

            case R.id.layout_location:
//                chooseAddress(view);
                cityPicker();


                break;

            case R.id.layout_orientation:
                startActivityForResult(new Intent(StoreContractActivity.this, MapActivity.class), 0);
                break;

            case R.id.img_add:
                isCover = false;
                choosePhoto(view);
                break;
        }
    }

    private void chooseBankCard() {
        new AlertDialog.Builder(this).setTitle("请选择店铺类型").setSingleChoiceItems(
                storeType, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        txtType.setText(storeType[which]);
                        type = (which + 1) + "";
                        System.out.println("type="+type);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
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

        txtTitle.setText("选择店铺封面");

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

        if(data == null){
            return;
        }

        if (requestCode == ImageUtil.RESULT_LOAD_IMAGE) {
            if (isCover) {
                Glide.with(StoreContractActivity.this).load(album(data)).into(imgCover);
            } else {
                listPic.add(album(data));
                recyclerViewAdapter.notifyDataSetChanged();
            }

            new QiNiuUtil(StoreContractActivity.this, album(data),null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    System.out.println("key=" + key);

                    if(isCover){
                        imageUrl = key;
                    }else{
                        listPicUrl.add(key);
                    }

                }
            }, true);

        } else if (requestCode == ImageUtil.RESULT_CAMARA_IMAGE) {

            if (isCover) {
                Glide.with(StoreContractActivity.this).load(camara(data)).into(imgCover);
            } else {
                listPic.add(camara(data));
                recyclerViewAdapter.notifyDataSetChanged();

            }

            new QiNiuUtil(StoreContractActivity.this, camara(data),null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    if(isCover){
                        imageUrl = key;
                    }else{
                        listPicUrl.add(key);
                    }
                }
            }, true);


        } else if (requestCode == 0 && resultCode == 0) {
            longitude = data.getStringExtra("longitude");
            latitude = data.getStringExtra("latitude");
            addressName = data.getStringExtra("addressName");
            txtOrientation.setText(addressName);
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
        String fileName = "";
        if (bundle != null) {
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream b = null;
            File file = new File("sdcard/DCIM/Camera/");
            file.mkdirs();// 创建文件夹
            fileName = "sdcard/DCIM/Camera/" + name;
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
        }

        return fileName;
    }

    private void cityPicker(){
        CityPicker cityPicker = new CityPicker.Builder(StoreContractActivity.this)
                .textSize(18)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#ffffff")
                .backgroundPop(0xa0000000)
                .confirTextColor("#FE4332")
                .cancelTextColor("#FE4332")
                .province("北京市")
                .city("北京市")
                .district("昌平区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                mCurrentProviceName = citySelected[0];
                //城市
                mCurrentCityName = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                mCurrentDistrictName = citySelected[2];
                //邮编
                String code = citySelected[3];

                txtLocation.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
            }
        });
    }



    private void commit() {
        String pic = "";

        for(String s : listPicUrl){
            pic = pic + s + "||" ;
        }
        System.out.println("pic="+pic);

        JSONObject object = new JSONObject();
        try {
            if(isModifi){
                object.put("code", userInfoSp.getString("storeCode",null));
            }
            object.put("name", edtName.getText().toString().trim());
            object.put("type", type);
            object.put("legalPersonName", edtLegalPerson.getText().toString().trim());
            object.put("userReferee", edtReferrer.getText().toString().trim());
            object.put("rate1", (Double.parseDouble(edtUse.getText().toString().trim())/100)+"");
            object.put("rate2", (Double.parseDouble(edtNonuse.getText().toString().trim())/100)+"");
            object.put("slogan", edtAdvertisement.getText().toString().trim());
            object.put("adPic", imageUrl);
            object.put("pic", pic.substring(0, pic.length() - 2));
            object.put("description", edtDetail.getText().toString().trim());
            object.put("province", mCurrentProviceName);
            object.put("city", mCurrentCityName);
            object.put("area", mCurrentDistrictName);
            object.put("address", edtAddress.getText().toString().trim());
            object.put("longitude", longitude);
            object.put("latitude", latitude);
            object.put("bookMobile", edtPhone.getText().toString().trim());
            object.put("smsMobile", "");
            object.put("pdf", "");
            object.put("token", userInfoSp.getString("token", null));
            object.put("owner", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String httCode = "";
        if(isModifi){
            httCode = "808203";

        }else {
            httCode = "808201";
        }

        new Xutil().post(httCode, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    if(isModifi){
                        Toast.makeText(StoreContractActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(StoreContractActivity.this, "签约成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StoreContractActivity.this,StoreManageActivity.class));
                    }

                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StoreContractActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StoreContractActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkData() {

        if(imageUrl.equals("")){
            Toast.makeText(this, "请添加店铺封面", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.equals("")) {
            Toast.makeText(this, "请选择店铺类型", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写店铺名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtLocation.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择所在省市区", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtAddress.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtOrientation.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请定位店铺经纬度", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPhone.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写店铺联系电话", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtAdvertisement.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写店铺广告", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtLegalPerson.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写法人姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtReferrer.getText().toString().trim().length() != 11) {
            Toast.makeText(this, "请填写正确的推荐人账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtUse.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写使用抵扣券分成比例", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtNonuse.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写不使用抵扣券分成比例", Toast.LENGTH_SHORT).show();
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

    /**
     * 获取商家详情
     */
    private void getDatas(){
        JSONObject object = new JSONObject();
        try {
            object.put("fromUser",userInfoSp.getString("userId",null));
            object.put("code",userInfoSp.getString("storeCode",null));
            object.put("token",userInfoSp.getString("token",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808209", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<StoreModel>(){}.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StoreContractActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StoreContractActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        ImageUtil.glide(model.getAdPic(),imgCover,this);
        imageUrl = model.getAdPic();

        txtType.setText(storeType[(Integer.parseInt(model.getType())-1)]);
        type = model.getType();

        edtName.setText(model.getName());
        txtLocation.setText(model.getProvince()+model.getCity()+model.getArea());
        edtAddress.setText(model.getAddress());

        mCurrentProviceName = model.getProvince();
        mCurrentCityName = model.getCity();
        mCurrentDistrictName = model.getArea();

        edtPhone.setText(model.getBookMobile());
        edtAdvertisement.setText(model.getSlogan());
        edtLegalPerson.setText(model.getLegalPersonName());
        edtReferrer.setText(model.getRefereeMobile());
        edtDetail.setText(model.getDescription());

        edtUse.setText((model.getRate1()*100)+"");
        edtNonuse.setText(model.getRate2()*100+"");

        String[] pic =  model.getPic().split("\\|\\|");
        for (int i = 0; i<pic.length; i++){
            listPic.add(pic[i]);
            listPicUrl.add(pic[i]);
        }
        recyclerViewAdapter.notifyDataSetChanged();

        latitude = model.getLatitude();
        longitude = model.getLongitude();

        LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
        getAddress(latLonPoint);

    }

    private void getAddress(LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int i) {
        if (i == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress();
            } else {
                addressName = longitude+","+latitude;
            }

        }else{
            addressName = "获取位置失败";
            Log.i("MapActivity","onRegeocodeSearched------>wrong");
        }

        txtOrientation.setText(addressName);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
