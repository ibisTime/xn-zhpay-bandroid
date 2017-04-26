package com.zhenghui.zhqb.merchant.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.ParameterModel;
import com.zhenghui.zhqb.merchant.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeiQ on 2017/4/7.
 */

public class ParameterAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private boolean isModifi;
    private List<ParameterModel> list;
    private SharedPreferences userInfoSp;

    public ParameterAdapter(Context context, boolean isModifi, List<ParameterModel> list) {
        this.list = list;
        this.context = context;
        this.isModifi = isModifi;
        userInfoSp = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_parameter, null);
        holder = new ViewHolder(view);
        view.setTag(holder);

        holder.edtKey.setText(list.get(i).getDkey());
        holder.edtValue.setText(list.get(i).getDvalue());

        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(i).isExist()){
                    deleteParameter(i);
                }else {
                    list.remove(i);
                    notifyDataSetChanged();
                }
            }
        });

        holder.edtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list.get(i).setDkey(editable.toString().trim());
            }
        });

        holder.edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list.get(i).setDvalue(editable.toString().trim());
            }
        });

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.edt_key)
        EditText edtKey;
        @BindView(R.id.edt_value)
        EditText edtValue;
        @BindView(R.id.layout_delete)
        LinearLayout layoutDelete;
        @BindView(R.id.layout_parameter)
        LinearLayout layoutParameter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void deleteParameter(final int i){

        JSONObject object = new JSONObject();
        try {
            object.put("code", list.get(i).getCode());
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808031", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                list.remove(i);
                notifyDataSetChanged();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
