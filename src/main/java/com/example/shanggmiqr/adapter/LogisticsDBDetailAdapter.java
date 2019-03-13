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
import com.example.shanggmiqr.bean.LogisticsCompany;

import java.util.List;


public class LogisticsDBDetailAdapter extends BaseAdapter{

    private List<LogisticsCompany.DatasBean> mContentList;
    private LayoutInflater inflater;
    private MyClickListener2 mListener;

    public LogisticsDBDetailAdapter(Context context, List<LogisticsCompany.DatasBean> list) {
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

        LogisticsCompany.DatasBean goods = (LogisticsCompany.DatasBean) this.getItem(position);

        ViewHolder2 viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.list_item_logistics_companydb, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.logisticsdb_name);
            viewHolder.code = (TextView) convertView.findViewById(R.id.id_logisticsdb_code);
            viewHolder.org = (TextView) convertView.findViewById(R.id.company_logisticsdb_org);
            viewHolder.status = (TextView)convertView.findViewById(R.id.logisticsdb_status);
            viewHolder.ts = (TextView)convertView.findViewById(R.id.logisticsdb_ts);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }

        viewHolder.name.setText(goods.getName());
        viewHolder.name.setTextSize(10);
        viewHolder.code.setText(String.valueOf(goods.getCode()));
        viewHolder.code.setTextSize(10);
        viewHolder.org.setText(goods.getOrg());
        viewHolder.org.setTextSize(10);
        viewHolder.status.setText(String.valueOf(goods.getStatus()));
        viewHolder.status.setTextSize(10);
        viewHolder.ts.setText(String.valueOf(goods.getTs()));
        viewHolder.ts.setTextSize(10);

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
        public TextView name;
        public TextView code;
        public TextView org;
        public TextView status;
        public TextView ts;
    }
}

