package com.zhenghui.zhqb.merchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.model.RightsModel;
import com.zhenghui.zhqb.merchant.util.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RightsAdapter extends BaseAdapter {

    private List<RightsModel> list;
    private Context context;
    private ViewHolder holder;

    public RightsAdapter(Context context, List<RightsModel> list) {
        this.list = list;
        this.context = context;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_rights, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    public void setView(int i) {

        holder.txtUnclaimed.setText(NumberUtil.doubleFormatMoney(list.get(i).getProfitAmount() - list.get(i).getBackAmount()));
        holder.txtReceived.setText(NumberUtil.doubleFormatMoney(list.get(i).getBackAmount()));
        holder.txtCode.setText("ID"+list.get(i).getCode().substring(list.get(i).getCode().length() - 9, list.get(i).getCode().length()));

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.get(i).getCreateDatetime() != null) {
            Date d5 = new Date(list.get(i).getCreateDatetime());
            holder.txtDate.setText(s.format(d5));
        }

    }

    static class ViewHolder {
        @BindView(R.id.txt_code)
        TextView txtCode;
        @BindView(R.id.txt_received)
        TextView txtReceived;
        @BindView(R.id.txt_unclaimed)
        TextView txtUnclaimed;
        @BindView(R.id.txt_date)
        TextView txtDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}