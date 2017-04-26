package com.zhenghui.zhqb.merchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenghui.zhqb.merchant.model.OrderModel;
import com.zhenghui.zhqb.merchant.R;
import com.zhenghui.zhqb.merchant.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeiQ on 2016/12/30.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<OrderModel> list;

    public OrderAdapter(Context context, List<OrderModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(list.size() == 0){
            return 1;
        }

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
        if(list.size() == 0){
            view = LayoutInflater.from(context).inflate(R.layout.item_order_no, null);
        }else {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_product, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            setView(i);
        }



        return view;
    }

    public void setView(int position) {
        ImageUtil.glide(list.get(position).getProductOrderList().get(0).getProduct().getAdvPic(),holder.imgPhoto,context);

        holder.txtTitle.setText(list.get(position).getCode());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(list.get(position).getApplyDatetime());
        holder.txtContent.setText(s.format(d5));


        if (list.get(position).getStatus().equals("1")) { // 待支付
            holder.txtInfo.setText("待支付");
        } else if (list.get(position).getStatus().equals("2")) { // 已支付
            holder.txtInfo.setText("已支付");
        } else if (list.get(position).getStatus().equals("3")) { // 已发货
            holder.txtInfo.setText("已发货");
        } else if (list.get(position).getStatus().equals("4")) { // 已收货
            holder.txtInfo.setText("已收货");
        } else if (list.get(position).getStatus().equals("91")) { // 用户取消
            holder.txtInfo.setText("用户取消");
        } else if (list.get(position).getStatus().equals("92")) { // 商户取消
            holder.txtInfo.setText("商户取消");
        } else if (list.get(position).getStatus().equals("93")) {
            holder.txtInfo.setText("快递异常");
        }

    }

    static class ViewHolder {
        @BindView(R.id.img_photo)
        ImageView imgPhoto;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindView(R.id.txt_info)
        TextView txtInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
