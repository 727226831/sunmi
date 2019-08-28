package com.example.shanggmiqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shanggmiqr.bean.MenuBean;
import com.example.shanggmiqr.transaction.AllocateTransfer;
import com.example.shanggmiqr.transaction.LoanBill;
import com.example.shanggmiqr.transaction.OtherEntry;
import com.example.shanggmiqr.transaction.ProductEntry;
import com.example.shanggmiqr.transaction.PurchaseArrival;
import com.example.shanggmiqr.transaction.PurchaseReturn;
import com.example.shanggmiqr.transaction.SaleDelivery;
import com.example.shanggmiqr.util.MyImageView;
import com.example.shanggmiqr.util.Utils;
import com.example.weiytjiang.shangmiqr.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/8.
 */

public class BusinessOperation extends AppCompatActivity {

    private RecyclerView recyclerView;
    Intent intent;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_operation);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        recyclerView=findViewById(R.id.rv_list);

        SharedPreferences currentAccount= getSharedPreferences("current_account", 0);
        MenuBean menuBean=new Gson().fromJson(currentAccount.getString("menubean",""),MenuBean.class);

        NormalAdapter normalAdapter=new NormalAdapter(menuBean.getPower());
        //设置Adapter
        recyclerView.setAdapter(normalAdapter);
        //设置分隔线
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH>{
        //② 创建ViewHolder
        public  class VH extends RecyclerView.ViewHolder{
            TextView textView;
            LinearLayout linearLayout;
            public VH(View v) {
                super(v);
               linearLayout=v.findViewById(R.id.l_layout);
                textView=v.findViewById(R.id.tv_menu);
            }
        }

        private List<MenuBean.Power> mDatas;
        public NormalAdapter(List<MenuBean.Power> data) {
            this.mDatas = data;
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder,final int position) {
            if (mDatas.get(position).getMenucode().equals("XSCK")){

                holder.textView.setBackgroundResource(R.color.color1);
            } else if (mDatas.get(position).getMenucode().equals("QTCK")){

                holder.textView.setBackgroundResource(R.color.color2);
            } else if (mDatas.get(position).getMenucode().equals("QTRK")){

                holder.textView.setBackgroundResource(R.color.color3);
            }else if (mDatas.get(position).getMenucode().equals("CGRK")){

                holder.textView.setBackgroundResource(R.color.color4);
            }else if (mDatas.get(position).getMenucode().equals("JCCK")){
                holder.textView.setBackgroundResource(R.color.color5);
            }else if (mDatas.get(position).getMenucode().equals("CCPRK")){
                holder.textView.setBackgroundResource(R.color.color6);
            } else if (mDatas.get(position).getMenucode().equals("DBCK")){
                holder.textView.setBackgroundResource(R.color.color7);
            }else if (mDatas.get(position).getMenucode().equals("CGTH")){
                holder.textView.setBackgroundResource(R.color.color8);
            }else if (mDatas.get(position).getMenucode().equals("CKCK")){
                holder.textView.setBackgroundResource(R.color.blue);
            }
            holder.textView.setText(mDatas.get(position).getMenuname());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDatas.get(position).getMenucode().equals("XSCK")){
                        intent=new Intent(BusinessOperation.this,SaleDelivery.class);
                        intent.putExtra("type",0);
                    }else if (mDatas.get(position).getMenucode().equals("QTCK")){
                        intent=new Intent(BusinessOperation.this,OtherEntry.class);
                        intent.putExtra("type",2);
                    } else if (mDatas.get(position).getMenucode().equals("QTRK")){
                        intent=new Intent(BusinessOperation.this,OtherEntry.class);
                        intent.putExtra("type",1);
                    }else if (mDatas.get(position).getMenucode().equals("CGRK")){
                        intent=new Intent(BusinessOperation.this,PurchaseArrival.class);
                        intent.putExtra("type",6);
                    }else if (mDatas.get(position).getMenucode().equals("JCCK")){
                        intent=new Intent(BusinessOperation.this,LoanBill.class);
                        intent.putExtra("type",4);
                    }else if (mDatas.get(position).getMenucode().equals("CCPRK")){
                        intent=new Intent(BusinessOperation.this,ProductEntry.class);
                        intent.putExtra("type",3);
                    }  else if (mDatas.get(position).getMenucode().equals("DBCK")){
                        intent=new Intent(BusinessOperation.this,AllocateTransfer.class);
                        intent.putExtra("type",5);
                    }else if (mDatas.get(position).getMenucode().equals("CGTH")){
                        intent=new Intent(BusinessOperation.this,PurchaseReturn.class);
                        intent.putExtra("type",7);

                    }else if (mDatas.get(position).getMenucode().equals("CKCK")){
                        intent=new Intent(BusinessOperation.this,SaleDelivery.class);
                        intent.putExtra("type",8);

                    }
                    intent.putExtra("title",mDatas.get(position).getMenuname());
                    intent.putExtra("from_business_operation", "Y");
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            //LayoutInflater.from指定写法
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
            return new VH(v);
        }
    }







}
