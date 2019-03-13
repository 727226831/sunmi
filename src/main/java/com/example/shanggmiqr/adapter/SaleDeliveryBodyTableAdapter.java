package com.example.shanggmiqr.adapter;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.bean.SaleDeliveryBodyBean;

import java.util.List;


public class SaleDeliveryBodyTableAdapter extends BaseAdapter{

    private List<SaleDeliveryBodyBean> mContentList;
    private SaleDeliveryBodyBean saleDeliveryBodyBean;
    private LayoutInflater inflater;
    private MyClickListener2 mListener;
    private int temp = -1;//记录每次点击的按钮的Id

    public SaleDeliveryBodyTableAdapter(Context context, List<SaleDeliveryBodyBean> list, MyClickListener2 listener) {
        this.mContentList = list;
        inflater = LayoutInflater.from(context);
        mListener = listener;
    }
    public void setList(List<SaleDeliveryBodyBean> saleDeliveryBodyBean) {
        this.mContentList = saleDeliveryBodyBean;
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
        int ret = 0;
        if (mContentList != null) {
            ret = mContentList.size();
        }
        return ret;
    }
//    @Override
//    public boolean areAllItemsEnabled() {
//        if (getItem(position).equals())
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
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

        SaleDeliveryBodyBean goods = (SaleDeliveryBodyBean) this.getItem(position);

        ViewHolder2 viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.list_item_sale_delivery_detail, null);
            viewHolder.vcooporderbcode_b = (TextView)convertView.findViewById(R.id.vcooporderbcode_b_saledetail) ;
            viewHolder.cwarename = (TextView)convertView.findViewById(R.id.cwarename_saledetail) ;
            viewHolder.matrcode = (TextView) convertView.findViewById(R.id.matrcode_saledetail);
            viewHolder.matrname = (TextView) convertView.findViewById(R.id.matrname_saledetail);
            viewHolder.nnum = (TextView) convertView.findViewById(R.id.nnum_saledetail);
            viewHolder.scannum = (TextView) convertView.findViewById(R.id.scannum_saledetail);
            viewHolder.customer = (TextView) convertView.findViewById(R.id.customer_saledetail);
            viewHolder.uploadflag=(TextView) convertView.findViewById(R.id.uploadflag_saledetail);
            viewHolder.viewBtn = (Button)convertView.findViewById(R.id.detail_saledetail);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_saledetail);
            viewHolder.radioButton.setClickable(false);
            viewHolder.radioButton.setVisibility(View.VISIBLE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }
        saleDeliveryBodyBean = (SaleDeliveryBodyBean) getItem(position);
        viewHolder.vcooporderbcode_b.setText(goods.getVcooporderbcode_b());
        viewHolder.vcooporderbcode_b.setTextSize(10);
        viewHolder.cwarename.setText(goods.getCwarename());
        viewHolder.cwarename.setTextSize(10);
        viewHolder.matrcode.setText(goods.getMatrcode());
        viewHolder.matrcode.setTextSize(10);
        viewHolder.matrname.setText(goods.getMatrname());
        viewHolder.matrname.setTextSize(10);
        viewHolder.nnum.setText(goods.getNnum());
        viewHolder.nnum.setTextSize(10);
        viewHolder.scannum.setText(goods.getScannnum());
        viewHolder.scannum.setTextSize(10);
        viewHolder.customer.setText(goods.getCustomer());
        viewHolder.customer.setTextSize(10);
        viewHolder.uploadflag.setText(goods.getUploadflag());
        viewHolder.uploadflag.setTextSize(10);
        //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
        viewHolder.viewBtn.setText("更多");
        viewHolder.viewBtn.setTextColor(Color.BLUE);
        viewHolder.viewBtn.setTag(position);
        viewHolder.viewBtn.setOnClickListener(mListener);
        viewHolder.radioButton.setChecked(saleDeliveryBodyBean.isSelected());


        return convertView;
    }

    /**
     * 用于回调的抽象类
     */
    public static abstract class MyClickListener2 implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

    public static class ViewHolder2 {
        public TextView vcooporderbcode_b;
        public TextView cwarename;
        public TextView matrcode;
        public TextView matrname;
        public TextView nnum;
        public TextView scannum;
        public TextView customer;
        public TextView uploadflag;
        public Button viewBtn;
        public RadioButton radioButton;
    }
}

