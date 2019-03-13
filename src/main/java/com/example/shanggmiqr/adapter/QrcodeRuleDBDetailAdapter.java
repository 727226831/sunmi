package com.example.shanggmiqr.adapter;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.bean.QrcodeRule;

import java.util.List;


public class QrcodeRuleDBDetailAdapter extends BaseAdapter{

    private List<QrcodeRule.DataBean> mContentList;
    private LayoutInflater inflater;
    private MyClickListener2 mListener;

    public QrcodeRuleDBDetailAdapter(Context context, List<QrcodeRule.DataBean> list) {
        this.mContentList = list;
        inflater = LayoutInflater.from(context);
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

        QrcodeRule.DataBean goods = (QrcodeRule.DataBean) this.getItem(position);

        ViewHolder2 viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.list_item_qrcoderuledb, null);
            viewHolder.code = (TextView) convertView.findViewById(R.id.qrcode_rule_db_code);
            viewHolder.matbasclassname = (TextView) convertView.findViewById(R.id.qrcode_rule_db_matbasclassname);
            viewHolder.length = (TextView) convertView.findViewById(R.id.qrcode_rule_db_length);
            viewHolder.name = (TextView)convertView.findViewById(R.id.qrcode_rule_db_name);
            viewHolder.Matbasclasscode = (TextView)convertView.findViewById(R.id.qrcode_rule_db_Matbasclasscode);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }

        viewHolder.name.setText(goods.getName());
        viewHolder.name.setTextSize(10);
        viewHolder.code.setText(String.valueOf(goods.getCode()));
        viewHolder.code.setTextSize(10);
        viewHolder.length.setText(String.valueOf(goods.getLength()));
        viewHolder.length.setTextSize(10);
        viewHolder.matbasclassname.setText(goods.getMatbasclassname());
        viewHolder.matbasclassname.setTextSize(10);
        viewHolder.Matbasclasscode.setText(goods.getMatbasclasscode());
        viewHolder.Matbasclasscode.setTextSize(10);

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
        public TextView matbasclassname;
        public TextView Matbasclasscode;
        public TextView name;
        public TextView code;
        public TextView length;
    }
}

