package com.zhenghui.zhqb.merchant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.merchant.MyBaseActivity;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.adapter.ParameterAdapter;
import com.zhenghui.zhqb.merchant.model.ParameterModel;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParameterActivity extends MyBaseActivity {

    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_add)
    TextView txtAdd;
    @BindView(R.id.list_parameter)
    ListView listParameter;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private String productCode;
    private boolean isModifi;

    private List<ParameterModel> list;
    private ParameterAdapter adapter;

    private int index = 0;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(index < message.what){
                postParameter();
            }else{
                finish();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);

        inits();
        initsListView();

        if(isModifi){
            getParameter();
        }
    }

    private void inits() {
        list = new ArrayList<>();

        adapter = new ParameterAdapter(this, isModifi, list);

        productCode = getIntent().getStringExtra("code");
        isModifi = getIntent().getBooleanExtra("isModifi",false);
    }

    private void initsListView() {
        listParameter.setAdapter(adapter);
    }

    @OnClick({R.id.layout_back, R.id.txt_add, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if(isModifi){
                    finish();
                }else{
                    new AlertDialog.Builder(this).setTitle("提示")
                            .setMessage("您确定不添加产品规格吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).setNegativeButton("取消", null).show();
                }


                break;

            case R.id.txt_add:
                list.add(new ParameterModel());
                adapter.notifyDataSetChanged();

                break;

            case R.id.btn_confirm:
                System.out.println("check()="+check());
                if(check()){
                    postParameter();
                }
                break;
        }
    }



    private boolean check() {

        for(ParameterModel model : list){
            if(model.getDkey().equals("")){
                Toast.makeText(this, "请填写规格名称", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(model.getDvalue().equals("")){
                Toast.makeText(this, "请填写规格", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void postParameter(){
        final List<ParameterModel> lists = new ArrayList<>();
        for (ParameterModel model : list){
            if(!model.isExist()){
                lists.add(model);
            }
        }
        if(lists.size() == 0){
            finish();
            return;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("productCode", productCode);
            object.put("dkey", lists.get(index).getDkey());
            object.put("dvalue", lists.get(index).getDvalue());
            object.put("orderNo", index);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808030", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                index++;
                Message message = handler.obtainMessage();
                message.what = lists.size();
                handler.sendMessage(message);
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ParameterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ParameterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getParameter(){

        JSONObject object = new JSONObject();
        try {
            object.put("productCode", productCode);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808037", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<ParameterModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ParameterModel>>() {
                    }.getType());

                    list.addAll(lists);
                    for(ParameterModel model : list){
                        model.setExist(true);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ParameterActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ParameterActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
