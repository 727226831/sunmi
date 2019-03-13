package com.example.shanggmiqr.transaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.UserDBDetailAdapter;
import com.example.shanggmiqr.bean.User;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/21.
 */

public class UserDBDetail extends AppCompatActivity {
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private ListView tableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_db_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper3 = new MyDataBaseHelper(UserDBDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableListView = (ListView) findViewById(R.id.userdb_list);
        List<User.UserDataBean> list = queryUserDB();
        UserDBDetailAdapter adapter = new UserDBDetailAdapter(UserDBDetail.this, list);
        tableListView.setAdapter(adapter);
    }

    private List<User.UserDataBean> queryUserDB() {
        ArrayList<User.UserDataBean> list = new ArrayList<User.UserDataBean>();
        String sql2 = "select " + "code" + "," + "name" + "," +  "org" + "," + "enablestate" + " from " + "User";//注意：这里有单引号
        Cursor cursor = db3.rawQuery(sql2, null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                User.UserDataBean bean = new User.UserDataBean();
                bean.name = cursor.getString(cursor.getColumnIndex("name"));
                bean.code = cursor.getString(cursor.getColumnIndex("code"));
                bean.org = cursor.getString(cursor.getColumnIndex("org"));
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
