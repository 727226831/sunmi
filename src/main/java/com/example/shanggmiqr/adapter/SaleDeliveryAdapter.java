package com.example.shanggmiqr.adapter;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.bean.SaleDeliveryBean;

import java.util.List;


public class SaleDeliveryAdapter extends BaseAdapter{

    private List<SaleDeliveryBean> mContentList;
    private LayoutInflater inflater;
    private MyClickListener mListener;
    private SaleDeliveryBean saleDeliveryBean;

    public SaleDeliveryAdapter(Context context, List<SaleDeliveryBean> list, MyClickListener listener) {
        this.mContentList = list;
        inflater = LayoutInflater.from(context);
        mListener = listener;

    }


    public void setList(List<SaleDeliveryBean> saleDeliveryDataBean) {
        this.mContentList = saleDeliveryDataBean;
    }

    // 选中当前选项时，让其他选项不被选中
    public void select(int position) {

        if (!mContentList.get(position).isSelected()) {
            mContentList.get(position).setSelected(true);
            for (int i = 0; i < mContentList.size(); i++) {
                if (i != position) {
                    mContentList.get(i).setSelected(false);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {

        return mContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        SaleDeliveryBean goods = (SaleDeliveryBean) this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_sale_delivery, null);

            viewHolder.vbillcode = (TextView) convertView.findViewById(R.id.vbillcode_sale_delivery);
            viewHolder.dbilldate = (TextView) convertView.findViewById(R.id.dbilldate_sale_delivery);
            viewHolder.dr = (TextView) convertView.findViewById(R.id.dr_sale_delivery);
            viewHolder.viewBtn = (Button)convertView.findViewById(R.id.text_detail_sale_delivery);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radiobutton_upload_sale_delivery);
            viewHolder.radioButton.setClickable(false);
            viewHolder.radioButton.setVisibility(View.VISIBLE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        saleDeliveryBean = (SaleDeliveryBean) getItem(position);

        viewHolder.vbillcode.setText(goods.getVbillcode());
        viewHolder.vbillcode.setTextSize(12);
        viewHolder.dbilldate.setText(goods.getDbilldate());
        viewHolder.dbilldate.setTextSize(12);
        viewHolder.dr.setText(String.valueOf(goods.getDr()));
        viewHolder.dr.setTextSize(12);

        //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
     //   viewHolder.viewBtn.setBackgroundColor(Color.parseColor("#B4B3B3"));
        viewHolder.viewBtn.setText("更多");
        viewHolder.viewBtn.setTextColor(Color.BLUE);
        viewHolder.viewBtn.setTag(position);
        viewHolder.viewBtn.setOnClickListener(mListener);
        viewHolder.radioButton.setChecked(saleDeliveryBean.isSelected());

        return convertView;
    }

    /**
     * 用于回调的抽象类
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

    public static class ViewHolder {
        public TextView vbillcode;
        public TextView dbilldate;
        public TextView dr;
        public Button viewBtn;
        public RadioButton radioButton;
    }
}

