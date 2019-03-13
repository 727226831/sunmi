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
import com.example.shanggmiqr.bean.MaterialBean;

import java.util.List;


public class MaterialDBDetailAdapter extends BaseAdapter{

    private List<MaterialBean.DataBean> mContentList;
    private LayoutInflater inflater;
    private MyClickListener2 mListener;

    public MaterialDBDetailAdapter(Context context, List<MaterialBean.DataBean> list) {
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

        MaterialBean.DataBean goods = (MaterialBean.DataBean) this.getItem(position);

        ViewHolder2 viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.list_item_materialdb, null);
            viewHolder.code = (TextView) convertView.findViewById(R.id.material_db_code);
            viewHolder.marbasclass = (TextView) convertView.findViewById(R.id.material_db_marbasclass);
            viewHolder.materialspec = (TextView) convertView.findViewById(R.id.material_db_materialspec);
            viewHolder.name = (TextView)convertView.findViewById(R.id.material_db_name);
            viewHolder.orgcode = (TextView)convertView.findViewById(R.id.material_db_orgcode);
            viewHolder.brandname = (TextView)convertView.findViewById(R.id.material_db_brandname);
            viewHolder.enablestate = (TextView)convertView.findViewById(R.id.material_db_enablestate);
            viewHolder.ename = (TextView)convertView.findViewById(R.id.material_db_ename);
            viewHolder.materialtype = (TextView)convertView.findViewById(R.id.material_db_materialtype);
            viewHolder.measdoccode = (TextView)convertView.findViewById(R.id.material_db_measdoccode);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }

        viewHolder.name.setText(goods.getName());
        viewHolder.name.setTextSize(10);
        viewHolder.code.setText(String.valueOf(goods.getCode()));
        viewHolder.code.setTextSize(10);
        viewHolder.ename.setText(goods.getEname());
        viewHolder.ename.setTextSize(10);
        viewHolder.marbasclass.setText(goods.getMarbasclass());
        viewHolder.marbasclass.setTextSize(10);
        viewHolder.materialtype.setText(goods.getMaterialtype());
        viewHolder.materialtype.setTextSize(10);
        viewHolder.materialspec.setText(goods.getMaterialspec());
        viewHolder.materialspec.setTextSize(10);
        viewHolder.measdoccode.setText(goods.getMeasdoccode());
        viewHolder.measdoccode.setTextSize(10);
        viewHolder.orgcode.setText(goods.getOrgcode());
        viewHolder.orgcode.setTextSize(10);
        viewHolder.brandname.setText(goods.getBrandname());
        viewHolder.brandname.setTextSize(10);
        viewHolder.enablestate.setText(String.valueOf(goods.getEnablestate()));
        viewHolder.enablestate.setTextSize(10);

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
        public TextView code;
        public TextView marbasclass;
        public TextView ename;
        public TextView materialspec;
        public TextView materialtype;
        public TextView name;
        public TextView measdoccode;
        public TextView orgcode;
        public TextView brandname;
        public TextView enablestate;
    }
}

