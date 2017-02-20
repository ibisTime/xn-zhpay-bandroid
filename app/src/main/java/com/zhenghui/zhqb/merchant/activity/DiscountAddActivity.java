package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.MoneyUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Leiq on 2016/12/26.
 * 添加折扣券
 */

public class DiscountAddActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_commit)
    TextView txtCommit;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.txt_type)
    TextView txtType;
    @BindView(R.id.layout_type)
    LinearLayout layoutType;
    @BindView(R.id.edt_key1)
    EditText edtKey1;
    @BindView(R.id.edt_key2)
    EditText edtKey2;
    @BindView(R.id.txt_dateStart)
    TextView txtDateStart;
    @BindView(R.id.txt_dateEnd)
    TextView txtDateEnd;
    @BindView(R.id.edt_detail)
    EditText edtDetail;
    @BindView(R.id.txt_confirm)
    TextView txtConfirm;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private String[] typeStr = {"满减", "返现"};
    private String type = "";

    private String isPutaway = "1";

    private Calendar calendar;

    private String code;
    private boolean status;
    private boolean isModifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_add);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();
//        initEditText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void initEvent() {
//        switchButton.setChecked(false);
//        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    isPutaway = "1";
//                } else {
//                    isPutaway = "0";
//                }
//            }
//        });
    }

    private void inits() {

        calendar = Calendar.getInstance();

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        code = getIntent().getStringExtra("code");
        isModifi = getIntent().getBooleanExtra("isModifi", false);
        status = getIntent().getBooleanExtra("status", false);
        if (isModifi) {
            gettData();
            txtTitle.setText("修改折扣券");
            txtConfirm.setVisibility(View.VISIBLE);
            if(status){
                txtConfirm.setText("下架");
            } else {
                txtConfirm.setText("上架");
            }
        }
    }

    private void initEditText() {
        edtKey1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edtKey1.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});

        edtKey2.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edtKey2.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }


    @OnClick({R.id.layout_back, R.id.txt_commit, R.id.layout_type, R.id.txt_dateStart, R.id.txt_dateEnd, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_commit:
                if (checkData()) {
                    if (isModifi) {
                        modifiData();
                    } else {
                        postData();
                    }
                }
                break;

            case R.id.txt_dateStart:
                new DatePickerDialog(DiscountAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//
                        if ((month + 1) < 10) {
                            txtDateStart.setText(year + "-0" + (month + 1) + "-" + day);
                        } else {
                            txtDateStart.setText(year + "-" + (month + 1) + "-" + day);
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.txt_dateEnd:
                new DatePickerDialog(DiscountAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//
                        if ((month + 1) < 10) {
                            txtDateEnd.setText(year + "-0" + (month + 1) + "-" + day);
                        } else {
                            txtDateEnd.setText(year + "-" + (month + 1) + "-" + day);
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.txt_confirm:
                soldOut();
                break;
        }
    }

    private void chooseBankCard() {
        new AlertDialog.Builder(this).setTitle("请选择类型").setSingleChoiceItems(
                typeStr, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        txtType.setText(typeStr[which]);
                        type = (which + 1) + "";
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void postData() {

        JSONObject object = new JSONObject();
        try {
            object.put("name", edtName.getText().toString().trim());
            object.put("type", "1");
            object.put("key1", (int) (Double.parseDouble(edtKey1.getText().toString().trim()) * 1000));
            object.put("key2", (int) (Double.parseDouble(edtKey2.getText().toString().trim()) * 1000));
            object.put("description", edtDetail.getText().toString().trim());
            object.put("price", (int) (Double.parseDouble(edtKey2.getText().toString().trim()) * 1000));
            object.put("currency", "CNY");
            object.put("validateStart", txtDateStart.getText().toString().trim());
            object.put("validateEnd", txtDateEnd.getText().toString().trim());
            object.put("isPutaway", "1");
            object.put("storeCode", userInfoSp.getString("storeCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808220", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                Toast.makeText(DiscountAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void modifiData() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("name", edtName.getText().toString().trim());
            object.put("type", "1");
            object.put("key1", (int) (Double.parseDouble(edtKey1.getText().toString().trim()) * 1000));
            object.put("key2", (int) (Double.parseDouble(edtKey2.getText().toString().trim()) * 1000));
            object.put("description", edtDetail.getText().toString().trim());
            object.put("price", (int) (Double.parseDouble(edtKey2.getText().toString().trim()) * 1000));
            object.put("currency", "CNY");
            object.put("validateStart", txtDateStart.getText().toString().trim());
            object.put("validateEnd", txtDateEnd.getText().toString().trim());
            object.put("isPutaway", "1");
            object.put("storeCode", userInfoSp.getString("storeCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808220", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                Toast.makeText(DiscountAddActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void gettData() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808226", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    edtName.setText(jsonObject.getString("name"));

                    edtKey1.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("key1")) + "");
                    edtKey2.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("key2")) + "");

                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = new Date(jsonObject.getString("validateStart"));
                    txtDateStart.setText(s.format(start));

                    Date end = new Date(jsonObject.getString("validateEnd"));
                    txtDateEnd.setText(s.format(end));

                    edtDetail.setText(jsonObject.getString("description"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void soldOut() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808223", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                if(status){
                    Toast.makeText(DiscountAddActivity.this, "下架成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiscountAddActivity.this, "上架成功", Toast.LENGTH_SHORT).show();
                }


                finish();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountAddActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountAddActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkData() {

        if (edtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写折扣券名称", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (edtPrice.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请填写折扣券兑换价格", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (type.equals("")) {
//            Toast.makeText(this, "请选择折扣方式", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (isPutaway.equals("")) {
//            Toast.makeText(this, "请决定是否上架", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (edtKey1.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入最低使用门槛", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtKey2.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入满减金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        int ksy1 = (int) (Double.parseDouble(edtKey1.getText().toString().trim()) * 1000);
        int ksy2 = (int) (Double.parseDouble(edtKey2.getText().toString().trim()) * 1000);

        if (ksy1 < ksy2) {
            Toast.makeText(this, "满减金额不能大于门槛条件哦", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtDateStart.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择有效期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDateEnd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择有效期结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtDetail.getText().toString().trim().length() < 20) {
            Toast.makeText(this, "折扣券使用详详细不能少于20字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

}
