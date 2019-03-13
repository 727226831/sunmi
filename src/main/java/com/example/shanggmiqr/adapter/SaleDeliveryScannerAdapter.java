package com.example.shanggmiqr.adapter;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.bean.SaleDeliveryScanResultBean;

import java.util.List;


public class SaleDeliveryScannerAdapter extends BaseAdapter{

    private List<SaleDeliveryScanResultBean> mContentList;
    private LayoutInflater inflater;
    private ScannerClickListener mListener;

    public SaleDeliveryScannerAdapter(Context context, List<SaleDeliveryScanResultBean> list, ScannerClickListener listener) {
        this.mContentList = list;
        inflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mContentList != null) {
            ret = mContentList.size();
        }
        return ret;
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

        SaleDeliveryScanResultBean goods = (SaleDeliveryScanResultBean) this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_saledelivery_qr_scanner, null);
            viewHolder.matrcode = (TextView) convertView.findViewById(R.id.saledelivery_scanner_matrcode);
            viewHolder.num = (TextView) convertView.findViewById(R.id.saledelivery_scanner_num);
            viewHolder.boxcode = (TextView) convertView.findViewById(R.id.saledelivery_scanner_boxcode);
            viewHolder.platecode = (TextView)convertView.findViewById(R.id.saledelivery_scanner_platecode);
            viewHolder.prodcutcode = (TextView) convertView.findViewById(R.id.saledelivery_scanner_productcode);
            viewHolder.itemuploadflag = (TextView) convertView.findViewById(R.id.saledelivery_scanner_itemuploadflag);
            viewHolder.delBtn = (Button)convertView.findViewById(R.id.saledelivery_scanner_delbutton);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.matrcode.setText(goods.getMatrcode());
        viewHolder.matrcode.setTextSize(12);
        viewHolder.num.setText(String.valueOf(goods.getNum()));
        viewHolder.num.setTextSize(12);
        viewHolder.platecode.setText(goods.getPlatecode());
        viewHolder.platecode.setTextSize(12);
        viewHolder.boxcode.setText(goods.getBoxcode());
        viewHolder.boxcode.setTextSize(12);
        viewHolder.prodcutcode.setText(goods.getProdcutcode());
        viewHolder.prodcutcode.setTextSize(12);
        viewHolder.itemuploadflag.setText(goods.getItemuploadflag());
        viewHolder.itemuploadflag.setTextSize(12);
        //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
     //   viewHolder.viewBtn.setBackgroundColor(Color.parseColor("#B4B3B3"));
        viewHolder.delBtn.setText("删除");
        viewHolder.delBtn.setTag(position);
        viewHolder.delBtn.setOnClickListener(mListener);


        return convertView;
    }

    /**
     * 用于回调的抽象类
     */
    public static abstract class ScannerClickListener implements View.OnClickListener {
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
        public TextView matrcode;
        public TextView num;
        public TextView prodcutcode;
        public TextView boxcode;
        public TextView platecode;
        public TextView itemuploadflag;
        public Button delBtn;
    }
}

