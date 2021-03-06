package com.example.shanggmiqr.transaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.MaterialDBDetailAdapter;
import com.example.shanggmiqr.bean.MaterialBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/21.
 */

public class MaterialDBDetail extends AppCompatActivity {
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private ListView tableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_db_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper3 = new MyDataBaseHelper(MaterialDBDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableListView = (ListView) findViewById(R.id.materialdb_list);
        List<MaterialBean.DataBean> list = queryMaterialDB();
        MaterialDBDetailAdapter adapter = new MaterialDBDetailAdapter(MaterialDBDetail.this, list);
        tableListView.setAdapter(adapter);
    }

    private List<MaterialBean.DataBean> queryMaterialDB() {
        ArrayList<MaterialBean.DataBean> list = new ArrayList<MaterialBean.DataBean>();
        String sql2 = "select " + "code" + ","+ "marbasclass" + "," + "ename" + ","+ "materialspec" + ","+ "materialtype" + "," + "name" + "," +  "orgcode" + "," +  "measdoccode" + "," +  "brandname" + ","+ "enablestate" + " from " + "Material";//注意：这里有单引号
        Cursor cursor = db3.rawQuery(sql2, null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                MaterialBean.DataBean bean = new MaterialBean.DataBean();
                bean.code = cursor.getString(cursor.getColumnIndex("code"));
                bean.marbasclass = cursor.getString(cursor.getColumnIndex("marbasclass"));
                bean.ename = cursor.getString(cursor.getColumnIndex("ename"));
                bean.materialspec = cursor.getString(cursor.getColumnIndex("materialspec"));
                bean.materialtype = cursor.getString(cursor.getColumnIndex("materialtype"));
                bean.name = cursor.getString(cursor.getColumnIndex("name"));
                bean.measdoccode = cursor.getString(cursor.getColumnIndex("measdoccode"));
                bean.orgcode = cursor.getString(cursor.getColumnIndex("orgcode"));
                bean.brandname = cursor.getString(cursor.getColumnIndex("brandname"));
                bean.enablestate = cursor.getInt(cursor.getColumnIndex("enablestate"));
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
