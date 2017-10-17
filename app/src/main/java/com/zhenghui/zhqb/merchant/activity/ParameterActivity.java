package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.adapter.ParameterAdapter;
import com.zhenghui.zhqb.merchant.model.ParameterModel;
import com.zhenghui.zhqb.merchant.util.NumberUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhenghui.zhqb.merchant.R.id.txt_delete;
import static com.zhenghui.zhqb.merchant.R.id.txt_title;

public class ParameterActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.edt_describe)
    EditText edtDescribe;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;
    @BindView(R.id.edt_weight)
    EditText edtWeight;
    @BindView(R.id.edt_rmb)
    EditText edtRmb;
    @BindView(R.id.edt_qbb)
    EditText edtQbb;
    @BindView(R.id.edt_gwb)
    EditText edtGwb;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(txt_delete)
    TextView txtDelete;
    @BindView(txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_payCurrency)
    TextView txtPayCurrency;

    private List<ParameterModel> list;
    private ParameterAdapter adapter;

    private double rmb = 0;
    private double gwb = 0;
    private double qbb = 0;

    private Builder showProvince;
    private String province = "安徽";
    private String[] provinces = {"安徽省", "澳门", "北京市", "重庆市", "福建省", "甘肃省", "广东省", "广西省", "贵州省", "海南省",
            "河北省", "河南省", "黑龙江省", "湖北省", "湖南省", "吉林省", "江苏省", "江西省", "辽宁省", "内蒙古省", "宁夏省", "青海省",
            "山东省", "山西省", "陕西省", "上海市", "四川省", "台湾省", "天津市", "西藏省", "香港", "新疆", "云南省", "浙江省"};
    NumberPicker numberPicker;

    private ParameterModel model;
    private int index;

    private int orderNo;
    private boolean isModify;
    private String productCode;

    private String payCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);

        inits();
        initEditText();
    }

    private void inits() {
        orderNo = getIntent().getIntExtra("orderNo", 0);
        productCode = getIntent().getStringExtra("code");
        isModify = getIntent().getBooleanExtra("isModify", false);


        index = getIntent().getIntExtra("index", 0);
        model = (ParameterModel) getIntent().getSerializableExtra("model");

        payCurrency = getIntent().getStringExtra("payCurrency");

        if (model == null) {
            txtDelete.setVisibility(View.GONE);
        } else {
            edtDescribe.setText(model.getName());
            txtProvince.setText(model.getProvince());
            edtWeight.setText(model.getWeight() + "");
            edtNumber.setText(model.getQuantity() + "");
            edtRmb.setText(NumberUtil.doubleFormatMoney(model.getPrice1()));
            edtGwb.setText(NumberUtil.doubleFormatMoney(model.getPrice2()));
            edtQbb.setText(NumberUtil.doubleFormatMoney(model.getPrice3()));
        }

        if(payCurrency != null){
            switch (payCurrency){
                case "2":
                    txtPayCurrency.setText("人民币");
                    break;

                case "4":
                    txtPayCurrency.setText("钱包币");
                    break;
            }
        }
    }

    private void initEditText() {
        edtRmb.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        edtQbb.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        edtGwb.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edtRmb.setFilters(new InputFilter[]{new InputFilter() {
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

        edtQbb.setFilters(new InputFilter[]{new InputFilter() {
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

        edtGwb.setFilters(new InputFilter[]{new InputFilter() {
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

    @OnClick({R.id.layout_back, R.id.btn_confirm, R.id.layout_address, txt_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
//                if (isModifi) {
                finish();
//                } else {
//                    new AlertDialog.Builder(this).setTitle("提示")
//                            .setMessage("您确定不添加产品规格吗?")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    finish();
//                                }
//                            }).setNegativeButton("取消", null).show();
//                }


                break;

            case R.id.layout_address:

                showProvince = showProvince(ParameterActivity.this);
                showProvince.create().show();
                break;

            case R.id.btn_confirm:
                if (!edtRmb.getText().toString().trim().equals("")) {
                    rmb = Double.parseDouble(edtRmb.getText().toString().trim());
                } else {
                    rmb = 0;
                }
                if (!edtGwb.getText().toString().trim().equals("")) {
                    gwb = Double.parseDouble(edtGwb.getText().toString().trim());
                } else {
                    gwb = 0;
                }
                if (!edtQbb.getText().toString().trim().equals("")) {
                    qbb = Double.parseDouble(edtQbb.getText().toString().trim());
                } else {
                    qbb = 0;
                }

                if (check()) {
                    ParameterModel model = new ParameterModel();
                    model.setName(edtDescribe.getText().toString().trim());
                    model.setQuantity(Integer.parseInt(edtNumber.getText().toString().trim()));
                    model.setProvince(txtProvince.getText().toString().trim());
                    model.setWeight(Double.parseDouble(edtWeight.getText().toString().trim()));
                    model.setPrice1(rmb * 1000);
                    model.setPrice2(gwb * 1000);
                    model.setPrice3(qbb * 1000);

                    if (model != null) {
                        setResult(101, new Intent().putExtra("model", model).putExtra("index", index));
                    } else {
                        setResult(0, new Intent().putExtra("model", model));
                    }
                    finish();

                }


                break;

            case R.id.txt_delete:
                tip();

                break;
        }
    }


    private boolean check() {

        if (edtDescribe.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写类别规格的描述", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtNumber.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写库存", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtProvince.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写发货地", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtWeight.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写重量", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (edtRmb.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请填写人民币金额", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (edtQbb.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请填写钱包币金额", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (edtGwb.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请填写购物币金额", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    public Builder showProvince(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_province, null);// 得到加载view
        numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        numberPicker.setDisplayedValues(provinces);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(provinces.length - 1);
        numberPicker.setOnValueChangedListener(provinceChangedListener);


        Builder loadingDialog = new Builder(context);
        loadingDialog.setMessage("发货地");
        loadingDialog.setView(v);
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        txtProvince.setText(province);
                    }
                });
        loadingDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        province = provinces[0];
                    }
                });
        return loadingDialog;

    }

    private NumberPicker.OnValueChangeListener provinceChangedListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
            //获得城市名
            String[] a = numberPicker.getDisplayedValues();
            province = a[arg2];
        }

    };

//    private void deleteParameter() {
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("code", model.getCode());
//            object.put("token", userInfoSp.getString("token", null));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new Xutil().post(CODE_808031, object.toString(), new Xutil.XUtils3CallBackPost() {
//            @Override
//            public void onSuccess(String result) {
//                finish();
//                Toast.makeText(ParameterActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onTip(String tip) {
//                Toast.makeText(ParameterActivity.this, tip, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(ParameterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

//    private void modifyParameter() {
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("code", model.getCode());
//            object.put("token", userInfoSp.getString("token", null));
//            object.put("name", edtDescribe.getText().toString().trim());
//            object.put("price1", rmb * 1000);
//            object.put("price2", gwb * 1000);
//            object.put("price3", qbb * 1000);
//            object.put("quantity", Integer.parseInt(edtNumber.getText().toString().trim()));
//            object.put("province", txtProvince.getText().toString().trim());
//            object.put("weight", Double.parseDouble(edtWeight.getText().toString().trim()));
//            object.put("orderNo", model.getOrderNo());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new Xutil().post(CODE_808032, object.toString(), new Xutil.XUtils3CallBackPost() {
//            @Override
//            public void onSuccess(String result) {
//                finish();
//            }
//
//            @Override
//            public void onTip(String tip) {
//                Toast.makeText(ParameterActivity.this, tip, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(ParameterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

//    private void addParameter() {
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("productCode", productCode);
//            object.put("token", userInfoSp.getString("token", null));
//            object.put("name", edtDescribe.getText().toString().trim());
//            object.put("price1", rmb * 1000);
//            object.put("price2", gwb * 1000);
//            object.put("price3", qbb * 1000);
//            object.put("quantity", Integer.parseInt(edtNumber.getText().toString().trim()));
//            object.put("province", txtProvince.getText().toString().trim());
//            object.put("weight", Double.parseDouble(edtWeight.getText().toString().trim()));
//            object.put("orderNo", orderNo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new Xutil().post(CODE_808030, object.toString(), new Xutil.XUtils3CallBackPost() {
//            @Override
//            public void onSuccess(String result) {
//                finish();
//            }
//
//            @Override
//            public void onTip(String tip) {
//                Toast.makeText(ParameterActivity.this, tip, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(ParameterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private void tip() {
        new Builder(this).setTitle("提示")
                .setMessage("您确定要删除该规格吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (model != null) {
                            setResult(0, new Intent().putExtra("index", index));
                            finish();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

}
