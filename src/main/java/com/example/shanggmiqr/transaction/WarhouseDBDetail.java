package com.example.shanggmiqr.transaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.WarehouseDBDetailAdapter;
import com.example.shanggmiqr.bean.Warhouse;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/21.
 */

public class WarhouseDBDetail extends AppCompatActivity {
    private SQLiteDatabase warehousedb;
    private MyDataBaseHelper warehousehelper;
    private ListView warehousetableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_db_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        warehousehelper = new MyDataBaseHelper(WarhouseDBDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        warehousedb = warehousehelper.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        warehousetableListView = (ListView) findViewById(R.id.warhousedb_list);
        List<Warhouse.WarhouseDataBean> listwarehouse = queryUserDB();
        WarehouseDBDetailAdapter adapter = new WarehouseDBDetailAdapter(WarhouseDBDetail.this, listwarehouse);
        warehousetableListView.setAdapter(adapter);
    }

    private List<Warhouse.WarhouseDataBean> queryUserDB() {
        ArrayList<Warhouse.WarhouseDataBean> list = new ArrayList<Warhouse.WarhouseDataBean>();
        String sql2 = "select " + "code" + "," + "name" + "," +  "enablestate" + "," + "orgcode" + " from " + "Warehouse";//注意：这里有单引号
        Cursor cursor = warehousedb.rawQuery(sql2, null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                Warhouse.WarhouseDataBean bean = new Warhouse.WarhouseDataBean();
                bean.code = cursor.getString(cursor.getColumnIndex("code"));
                bean.name = cursor.getString(cursor.getColumnIndex("name"));
                bean.enablestate = cursor.getInt(cursor.getColumnIndex("enablestate"));
                bean.orgcode = cursor.getString(cursor.getColumnIndex("orgcode"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
