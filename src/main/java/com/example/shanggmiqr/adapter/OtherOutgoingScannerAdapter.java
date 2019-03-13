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
import com.example.shanggmiqr.bean.OutgoingScanResultBean;

import java.util.List;


public class OtherOutgoingScannerAdapter extends BaseAdapter{

    private List<OutgoingScanResultBean> mContentList;
    private LayoutInflater inflater;
    private ScannerClickListener21 mListener;

    public OtherOutgoingScannerAdapter(Context context, List<OutgoingScanResultBean> list, ScannerClickListener21 listener) {
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

        OutgoingScanResultBean goods = (OutgoingScanResultBean) this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_otheroutgoing_qr_scanner, null);
            viewHolder.itemuploadflag = (TextView) convertView.findViewById(R.id.otheroutgoing_scanner_itemuploadflag);
            viewHolder.boxcode = (TextView) convertView.findViewById(R.id.otheroutgoing_scanner_boxcode);
            viewHolder.platecode = (TextView)convertView.findViewById(R.id.otheroutgoing_scanner_platecode);
            viewHolder.prodcutcode = (TextView) convertView.findViewById(R.id.otheroutgoing_scanner_productcode);
            viewHolder.delBtn = (Button)convertView.findViewById(R.id.otheroutgoing_scanner_delbutton);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemuploadflag.setText(goods.getItemuploadflag());
        viewHolder.itemuploadflag.setTextSize(12);
        viewHolder.platecode.setText(goods.getPlatecode());
        viewHolder.platecode.setTextSize(12);
        viewHolder.boxcode.setText(goods.getBoxcode());
        viewHolder.boxcode.setTextSize(12);
        viewHolder.prodcutcode.setText(goods.getProdcutcode());
        viewHolder.prodcutcode.setTextSize(12);
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
    public static abstract class ScannerClickListener21 implements View.OnClickListener {
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
        public TextView itemuploadflag;
        public TextView prodcutcode;
        public TextView boxcode;
        public TextView platecode;
        public Button delBtn;
    }
}

