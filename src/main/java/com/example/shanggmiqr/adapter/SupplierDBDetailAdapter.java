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
import com.example.shanggmiqr.bean.Supplier;

import java.util.List;


public class SupplierDBDetailAdapter extends BaseAdapter{

    private List<Supplier.DataBean> mContentList;
    private LayoutInflater inflater;
    private MyClickListener2 mListener;

    public SupplierDBDetailAdapter(Context context, List<Supplier.DataBean> list) {
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

        Supplier.DataBean goods = (Supplier.DataBean) this.getItem(position);

        ViewHolder2 viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.list_item_supplierdb, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.goods_name_supplierdb_name);
            viewHolder.code = (TextView) convertView.findViewById(R.id.id_supplierdb_code);
            viewHolder.orgcode = (TextView) convertView.findViewById(R.id.supplierdb_org);
      //      viewHolder.state = (TextView)convertView.findViewById(R.id.userdb_state);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }

        viewHolder.name.setText(goods.getName());
        viewHolder.name.setTextSize(10);
        viewHolder.code.setText(String.valueOf(goods.getCode()));
        viewHolder.code.setTextSize(10);
        viewHolder.orgcode.setText(goods.getOrgcode());
        viewHolder.orgcode.setTextSize(10);
    //    viewHolder.state.setText(String.valueOf(goods.getEnablestate()));
    //    viewHolder.state.setTextSize(10);

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
        public TextView orgcode;
   //     public TextView state;
    }
}

