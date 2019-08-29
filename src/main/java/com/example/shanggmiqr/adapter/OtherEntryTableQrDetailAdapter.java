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
import com.example.shanggmiqr.bean.OtherOutgoingQrDetailBean;

import java.util.List;


public class OtherEntryTableQrDetailAdapter extends BaseAdapter{

    private List<OtherOutgoingQrDetailBean> mContentList;
    private LayoutInflater inflater;
    private MyClickListener mListener;

    public OtherEntryTableQrDetailAdapter(Context context, List<OtherOutgoingQrDetailBean> list, MyClickListener listener) {
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

        OtherOutgoingQrDetailBean goods = (OtherOutgoingQrDetailBean) this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_otherentry_qrdetail, null);
            viewHolder.platecode = (TextView) convertView.findViewById(R.id.platecode_otherentry_qrdetail);
            viewHolder.boxcode = (TextView) convertView.findViewById(R.id.boxcode_otherentry_qrdetail);
            viewHolder.prodcutcode = (TextView) convertView.findViewById(R.id.productcode_otherentry_qrdetail);
            viewHolder.uploagflag = (TextView) convertView.findViewById(R.id.uploadflag_otherentry_qrdetail);
            viewHolder.delBtn = (Button)convertView.findViewById(R.id.del_otherentry_qrdetail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.platecode.setText(goods.getPlatecode());
        viewHolder.platecode.setTextSize(12);
        viewHolder.boxcode.setText("");
        viewHolder.boxcode.setTextSize(12);
        viewHolder.prodcutcode.setText(goods.getProdcutcode());
        viewHolder.prodcutcode.setTextSize(12);
        viewHolder.uploagflag.setText(goods.getItemuploadflag());
        viewHolder.uploagflag.setTextSize(12);
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
        public TextView prodcutcode;
        public TextView boxcode;
        public TextView platecode;
        public TextView uploagflag;
        public Button delBtn;
    }
}

