package com.zhenghui.zhqb.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.merchant.MyApplication;
import com.zhenghui.zhqb.merchant.model.BankModel;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.MoneyUtil;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leiq on 2016/12/26.
 * 提现
 */

public class WithdrawalsActivity extends MyBaseActivity {


    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_bankCard)
    TextView txtBankCard;
    @BindView(R.id.layout_bankCard)
    LinearLayout layoutBankCard;
    @BindView(R.id.edt_price)
    EditText edtPrice;
    @BindView(R.id.txt_canUsePrice)
    TextView txtCanUsePrice;
    @BindView(R.id.txt_confirm)
    TextView txtConfirm;
    @BindView(R.id.edt_repassword)
    EditText edtRepassword;


    private List<BankModel> list;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private double balance;
    private String accountNumber;

    private String bankcardCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        balance = getIntent().getDoubleExtra("balance", 0.00);
        accountNumber = getIntent().getStringExtra("accountNumber");

        txtCanUsePrice.setText("可提现金额" + MoneyUtil.moneyFormatDouble(balance) + "元");
    }

    @OnClick({R.id.layout_back, R.id.layout_bankCard, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_bankCard:
//                chooseBankCard();
                startActivityForResult(new Intent(WithdrawalsActivity.this, BankCardActivity.class).putExtra("isWithdrawal", true), 0);

                break;

            case R.id.txt_confirm:

                if (!edtPrice.getText().toString().toString().equals("")) {
                    if (Double.parseDouble(edtPrice.getText().toString().trim()) == 0.0) {
                        Toast.makeText(WithdrawalsActivity.this, "金额必须大于等于0.01元", Toast.LENGTH_SHORT).show();
                    } else {
                        if (txtBankCard.getText().toString().equals("选择银行卡")) {
                            Toast.makeText(WithdrawalsActivity.this, "请先选择银行卡", Toast.LENGTH_SHORT).show();
                        } else {
                            if(edtRepassword.getText().toString().length() == 6){
                                withdrawal();
                            }else{
                                Toast.makeText(WithdrawalsActivity.this, "请输入6位支付密码", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(WithdrawalsActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!data.getStringExtra("bankName").equals("")) {
            txtBankCard.setText(data.getStringExtra("bankName"));
        }

        bankcardCode = data.getStringExtra("bankcardCode");


    }

    private void withdrawal() {

        JSONArray accountNumberList = new JSONArray();
        accountNumberList.put(accountNumber);

        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("bankcardNumber", bankcardCode);
            object.put("transAmount", (int) (Double.parseDouble(edtPrice.getText().toString().trim()) * 1000));
            object.put("accountNumber", accountNumber);
            object.put("tradePwd", edtRepassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802526", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(WithdrawalsActivity.this, "提现成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WithdrawalsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WithdrawalsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
