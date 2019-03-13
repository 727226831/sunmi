package com.example.shanggmiqr.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherEntryTableAdapter;
import com.example.shanggmiqr.bean.CommonSendBean;
import com.example.shanggmiqr.bean.OtherEntryBean;
import com.example.shanggmiqr.bean.OtherEntryQuery;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.shanggmiqr.util.Utils.getDefaultEndTime;

/**
 * Created by weiyt.jiang on 2018/8/9.
 */

public class OtherEntry extends AppCompatActivity implements OnClickListener {
    private String otherEntryDataResp;
    private String otherEntryUploadDataResp;
    private Button downloadOtherEntryButton;
    private Button queryEntryButton;
    private Button displayallEntryButton;
    private Handler otherEntryHandler = null;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private ListView tableListView;
    private List<OtherEntryBean> listAllPostition;
    private String chosen_line_Pobillcode;
    private String chosen_line_Cwarename;
    private String chosen_line_Cwarecode;
    private String query_cwarename;
    private String query_uploadflag;
    private ZLoadingDialog dialog;
    private List<String> test;
    private TextView lst_downLoad_ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_manage);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_otherentry);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestOtherEntryTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", "2018-09-01 00:00:01");
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        helper3 = new MyDataBaseHelper(OtherEntry.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        downloadOtherEntryButton = (Button) findViewById(R.id.download_other_entry);
        downloadOtherEntryButton.setOnClickListener(this);
        queryEntryButton = (Button) findViewById(R.id.query_other_entry);
        queryEntryButton.setOnClickListener(this);
        displayallEntryButton = (Button) findViewById(R.id.displayall_other_entry);
        //displayallEntryButton.setVisibility(View.INVISIBLE);
        displayallEntryButton.setOnClickListener(this);
        tableListView = (ListView) findViewById(R.id.list_otherentry);
        dialog = new ZLoadingDialog(OtherEntry.this);
        List<OtherEntryBean> list = queryAll();
        listAllPostition = list;
        final OtherEntryTableAdapter adapter1 = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);
                OtherEntryBean otherEntryBean = (OtherEntryBean) adapter1.getItem(position);
                chosen_line_Pobillcode = otherEntryBean.getPobillcode();
                chosen_line_Cwarename = otherEntryBean.getCwarename();
                chosen_line_Cwarecode = otherEntryBean.getCwarecode();
                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
            }
        });
        otherEntryHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(OtherEntry.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        dialog.dismiss();
                        Toast.makeText(OtherEntry.this, "其他入库单已经下载", Toast.LENGTH_LONG).show();
                        //插入UI表格数据

                        List<OtherEntryBean> list = queryAll();
                        listAllPostition = list;
                        final OtherEntryTableAdapter adapter = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                OtherEntryBean otherEntryBean = (OtherEntryBean) adapter.getItem(position);
                                chosen_line_Pobillcode = otherEntryBean.getPobillcode();
                                chosen_line_Cwarename = otherEntryBean.getCwarename();
                                chosen_line_Cwarecode = otherEntryBean.getCwarecode();
                                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(OtherEntry.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(OtherEntry.this, "其他入库单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_business_operation");
        if ("Y".equals(str)) {
            downloadOtherEntryButton.performClick();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //返回之后重新下载
        //downloadOtherEntryButton.performClick();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(OtherEntry.this, BusinessOperation.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            Intent intent = new Intent(OtherEntry.this, BusinessOperation.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_other_entry:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(OtherEntry.this)) {
                            try {
                                if (isWarehouseDBDownloaed()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                                                    .setLoadingColor(Color.BLUE)//颜色
                                                    .setHintText("Loading...")
                                                    .setCancelable(false)
                                                    .setCanceledOnTouchOutside(false)
                                                    .setHintTextSize(16) // 设置字体大小 dp
                                                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                                                    .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                                                    //     .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                                                    .show();
                                        }
                                    });
                                    //其他入库查询，嵌套json,使用两张表存储，通过pobillcode关联
                                    String outEntryData = downloadDatabase("R09", "1");
                                    if (null == outEntryData) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    Gson gson7 = new Gson();
                                    OtherEntryQuery otherEntryBean = gson7.fromJson(outEntryData, OtherEntryQuery.class);
                                    //     OtherEntryQuery otherEntryBean = JSON.parseObject(outEntryData, OtherEntryQuery.class);
                                    if (otherEntryBean.getPagetotal() == 1) {
                                        insertDownloadDataToDB(otherEntryBean);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherEntryHandler.sendMessage(msg);
                                    } else if (otherEntryBean.getPagetotal() < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(OtherEntry.this, "其他入库单已经是最新", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(otherEntryBean);
                                        for (int pagenum = 2; pagenum <= otherEntryBean.getPagetotal(); pagenum++) {
                                            String outGoingData2 = downloadDatabase("R09", String.valueOf(pagenum));
                                            OtherEntryQuery outGoingBean2 = gson7.fromJson(outGoingData2, OtherEntryQuery.class);
                                            //   OtherEntryQuery outGoingBean2 = JSON.parseObject(outGoingData2, OtherEntryQuery.class);
                                            insertDownloadDataToDB(outGoingBean2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherEntryHandler.sendMessage(msg);
                                    }
                                    String currentTs = getLatestDbilldate();
                                    String systime = Utils.getCurrentDateTimeNew();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestOtherEntryTSInfo", 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_download_ts_begintime", currentTs);
                                    editor5.putString("latest_download_ts_systime", systime);
                                    editor5.commit();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestOtherEntryTSInfo", 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", "2018-09-01 00:00:01");
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(OtherEntry.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                //e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception", e.toString());
                                Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                otherEntryHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            otherEntryHandler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.query_other_entry:
                popupQuery();
                break;
            case R.id.displayall_other_entry:
                List<OtherEntryBean> list = displayAll();
                listAllPostition = list;
                final OtherEntryTableAdapter adapter = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
                tableListView.setAdapter(adapter);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);
                        OtherEntryBean otherEntryBean = (OtherEntryBean) adapter.getItem(position);
                        chosen_line_Pobillcode = otherEntryBean.getPobillcode();
                        chosen_line_Cwarename = otherEntryBean.getCwarename();
                        chosen_line_Cwarecode = otherEntryBean.getCwarecode();
                        //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    private boolean isWarehouseDBDownloaed() {
        Cursor cursor = db3.rawQuery("select name from Warehouse",
                null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getLatestDbilldate() throws ParseException {
        Cursor cursor = db3.rawQuery("select dbilldate from OtherEntry order by dbilldate desc", null);
        ArrayList<String> date = new ArrayList<String>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                date.add(cursor.getString(cursor.getColumnIndex("dbilldate")));
            }
        } else {
            date.add("2018-09-01 00:00:01");
        }
        //加1秒
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetemp = df.parse(date.get(0));
        datetemp.setTime(datetemp.getTime() + 1000);
        String addone = df.format(datetemp);
        return addone;
    }

    private void insertDownloadDataToDB(OtherEntryQuery otherEntryBean) {
        List<OtherEntryQuery.DataBean> otherEntryBeanList = otherEntryBean.getData();
        for (OtherEntryQuery.DataBean ob : otherEntryBeanList) {
            String pobillcode = ob.getPobillcode();
            String cwarecode = ob.getCwarecode();
            String cwarename = ob.getCwarename();
            String dbilldate = ob.getDbilldate();
            String dr =ob.getDr();
            if("0".equals(dr)&& isPobillcodeExist(pobillcode)){
                continue;
            }
            //等于1时
            if("1".equals((dr))|| ("2".equals(dr)&&isPobillcodeExist(pobillcode)))
            {
//                ContentValues contentupdateValues = new ContentValues();
//                contentupdateValues.put("dr", dr);
//                //操作选择一 将现有单据的dr = 0 更改为dr= 1
//                db3.update("OtherEntry",contentupdateValues,"pobillcode=?", new String[]{pobillcode});
                //操作选择二 通过单号删除
                //删除三张表
                db3.beginTransaction();
                try {
                    db3.delete("OtherEntry", "pobillcode=?", new String[]{pobillcode});
                    db3.delete("OtherEntryBody", "pobillcode=?", new String[]{pobillcode});
                    db3.delete("OtherEntryScanResult", "pobillcode=?", new String[]{pobillcode});
                    db3.setTransactionSuccessful();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

               }
               finally {
                    db3.endTransaction();
                }
            }
            if("1".equals(dr)){
                continue;
            }
            List<OtherEntryQuery.DataBean.BodyBean> outGoingDatabodyList = ob.getBody();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            for (OtherEntryQuery.DataBean.BodyBean obb : outGoingDatabodyList) {
                String materialcode = obb.getMaterialcode();
                String maccode = obb.getMaccode();
                int nnum = obb.getNnum();
                String pch = obb.getPch();
                String vcooporderbcode_b = obb.getVcooporderbcode_b();
                String scannum = countScannedQRCode(pobillcode, materialcode, vcooporderbcode_b);
                //这里应该执行的是插入第二个表的操作
                ContentValues valuesInner = new ContentValues();
                valuesInner.put("pobillcode", pobillcode);
                valuesInner.put("materialcode", materialcode);
                valuesInner.put("maccode", maccode);
                valuesInner.put("nnum", nnum);
                valuesInner.put("pch", pch);
                valuesInner.put("uploadnum", "0");
                valuesInner.put("scannum", scannum);
                valuesInner.put("uploadflag", "N");
                valuesInner.put("vcooporderbcode_b", vcooporderbcode_b);
                db3.insert("OtherEntryBody", null, valuesInner);
                valuesInner.clear();
            }
            values.put("pobillcode", pobillcode);
            values.put("cwarecode", cwarecode);
            values.put("cwarename", cwarename);
            values.put("dbilldate", dbilldate);
            values.put("dr", dr);
            values.put("flag", "N");
            // 插入第一条数据
            db3.insert("OtherEntry", null, values);
            values.clear();
        }
    }
    private boolean isPobillcodeExist(String pobillcode) {
        Cursor cursor2 = db3.rawQuery("select pobillcode from OtherEntry where pobillcode=?", new String[]{pobillcode});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            cursor2.close();
            return true;
        }else {
            return false;
        }
    }
    private String countScannedQRCode(String pobillcode, String materialcode, String vcooporderbcode_b) {
        String count = "0";
        Cursor cursor2 = db3.rawQuery("select prodcutcode from OtherEntryScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{pobillcode, materialcode, vcooporderbcode_b});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }

    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherEntry.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditTextOtherEntry = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinnerOtherEntry = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinnerEntry = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        test = queryWarehouseInfo();
        test.add("");
        List<String> uploadflag = new ArrayList();
        uploadflag.add("是");
        uploadflag.add("否");
        final ArrayAdapter adapter = new ArrayAdapter(
                OtherEntry.this, android.R.layout.simple_spinner_item, test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOtherEntry.setAdapter(adapter);
        spinnerOtherEntry.setSelection(test.size() - 1, true);
        query_cwarename = adapter.getItem(test.size() - 1).toString();
        spinnerOtherEntry.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_cwarename = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter adapter2 = new ArrayAdapter(
                OtherEntry.this, android.R.layout.simple_spinner_item, uploadflag);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flag_spinnerEntry.setAdapter(adapter2);
        flag_spinnerEntry.setSelection(uploadflag.size()-1, true);
        if ("是".equals(adapter2.getItem(uploadflag.size()-1).toString())){
            query_uploadflag = "Y";
        } else {
            query_uploadflag = "N";
        }
        flag_spinnerEntry.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("是".equals(adapter2.getItem(i).toString())){
                    query_uploadflag = "Y";
                } else {
                    query_uploadflag = "N";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder ad1 = new AlertDialog.Builder(OtherEntry.this);
        ad1.setTitle("请输入出库单号:");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
              if(query_cwarename == null){
                    query_cwarename = adapter.getItem(test.size() - 1).toString();
                }
                if(query_uploadflag == null){
                    query_uploadflag = "N";
                }
                ArrayList<OtherEntryBean> bean1 = query(codeNumEditTextOtherEntry.getText().toString(), query_cwarename,query_uploadflag);
                listAllPostition = bean1;
                final OtherEntryTableAdapter adapter3 = new OtherEntryTableAdapter(OtherEntry.this, bean1, mListener);
                tableListView.setAdapter(adapter3);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter3.select(position);
                        OtherEntryBean otherOutgoingBean = (OtherEntryBean) adapter3.getItem(position);
                        chosen_line_Pobillcode = otherOutgoingBean.getPobillcode();
                        chosen_line_Cwarename = otherOutgoingBean.getCwarename();
                        chosen_line_Cwarecode = otherOutgoingBean.getCwarecode();
                    }
                });

            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框
    }

    private List<String> queryWarehouseInfo() {
        List<String> cars = new ArrayList<>();
        Cursor cursornew = db3.rawQuery("select name from Warehouse",
                null);
        if (cursornew != null && cursornew.getCount() > 0) {
            while (cursornew.moveToNext()) {
                String name = cursornew.getString(cursornew.getColumnIndex("name"));
                cars.add(name);
            }
            cursornew.close();
        }
        return cars;
    }

    public ArrayList<OtherEntryBean> query(String pobillcode, String current_cwarename,String query_uploadflag) {
        ArrayList<OtherEntryBean> list = new ArrayList<OtherEntryBean>();
        if (pobillcode.length() == 0 || "".equals(pobillcode)) {
            Cursor cursor1 = db3.rawQuery("select pobillcode,cwarecode,cwarename,dbilldate,dr from OtherEntry where flag=? and cwarename like '%" + current_cwarename + "%' order by dbilldate desc" ,new String[]{query_uploadflag});
            //Cursor cursor = db3.rawQuery(sql2, null);
            if (cursor1 != null && cursor1.getCount() > 0) {
                //判断cursor中是否存在数据
                while (cursor1.moveToNext()) {
                    OtherEntryBean bean = new OtherEntryBean();
                    bean.pobillcode = cursor1.getString(cursor1.getColumnIndex("pobillcode"));
                    bean.cwarecode = cursor1.getString(cursor1.getColumnIndex("cwarecode"));
                    bean.cwarename = cursor1.getString(cursor1.getColumnIndex("cwarename"));
                    bean.dbilldate = cursor1.getString(cursor1.getColumnIndex("dbilldate"));
                    bean.dr = cursor1.getInt(cursor1.getColumnIndex("dr"));
                    list.add(bean);
                }
                cursor1.close();
            }
        }else {
//        String sql2 = "select " + "pobillcode"+ ","  + "dbilldate" + "," + "cwarecode" + "," + "cwarename" + " from " + "OtherEntry"
//                + " where " + "pobillcode" + " like '%" + pobillcode + "%'";//注意：这里有单引号
            Cursor cursor = db3.rawQuery("select pobillcode,cwarecode,cwarename,dbilldate,dr from OtherEntry where pobillcode like '%" + pobillcode + "%' order by dbilldate desc", null);
            //Cursor cursor = db3.rawQuery(sql2, null);
            if (cursor != null && cursor.getCount() > 0) {
                //判断cursor中是否存在数据
                while (cursor.moveToNext()) {
                    OtherEntryBean bean = new OtherEntryBean();
                    bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                    bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                    bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                    bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                    bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                    if (queryCwarename(current_cwarename, bean.pobillcode)) {
                        list.add(bean);
                    }
                }
                cursor.close();
            }
        }
        return list;
    }

    private boolean queryCwarename(String current_cwarename, String pobillcode) {

        Cursor cursor = db3.rawQuery("select pobillcode from OtherEntry where pobillcode =? and cwarename=?", new String[]{pobillcode, current_cwarename});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
            }
            cursor.close();
            return true;
        }
        return false;
    }

    public ArrayList<OtherEntryBean> queryAll() {
        ArrayList<OtherEntryBean> list = new ArrayList<OtherEntryBean>();
        List<String> list_update = new ArrayList<String>();
  //      String sql2 = "select " + "pobillcode" + "," + "dbilldate" + "," + "cwarecode" + "," + "cwarename" + "," + "dr" + " from " + "OtherEntry";//注意：这里有单引号
    //    Cursor cursor = db3.rawQuery(sql2, null);
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherEntry where flag=? order by dbilldate desc", new String[]{"N"});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherEntryBean bean = new OtherEntryBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
               /* if (bean.dr == 1) {
                    list_update.add(bean.pobillcode);
                    if(null == list || list.size() ==0){
                    }else{
                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getPobillcode().equals(bean.pobillcode)) {
                                list.remove(i);
                            }
                        }
                    }
                }else{
                    if(null == list_update || list_update.size() ==0){
                        list.add(bean);
                    }else {
                       if(list_update.contains(bean.pobillcode)) {
                       }else{
                           list.add(bean);
                       }
                    }
                }
                */
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }
    public ArrayList<OtherEntryBean> displayAll() {
        ArrayList<OtherEntryBean> list = new ArrayList<OtherEntryBean>();
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherEntry order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherEntryBean bean = new OtherEntryBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * webservice查询下载
     */
    public String downloadDatabase(String workCode, String pagenum) throws Exception {
        String WSDL_URI;
        String namespace;
        String WSDL_URI_current = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace_current = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        SharedPreferences proxySp = getSharedPreferences("configInfo", 0);
        if (proxySp.getString("WSDL_URI", WSDL_URI_current).equals("") || proxySp.getString("namespace", namespace_current).equals("")) {
            WSDL_URI = WSDL_URI_current;
            namespace = namespace_current;
        } else {
            WSDL_URI = proxySp.getString("WSDL_URI", WSDL_URI_current);
            namespace = proxySp.getString("namespace", namespace_current);
        }
        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数string、string1
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestOtherEntryTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2018-09-01 00:00:01");
        String endtime = getDefaultEndTime();
        CommonSendBean userSend = new CommonSendBean(begintime, endtime, pagenum, "0");
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        request.addProperty("string", workCode);
        request.addProperty("string1", userSendBean);
        //request.addProperty("string1", "{\"begintime\":\"1900-01-20 00:00:00\",\"endtime\":\"2018-08-21 00:00:00\", \"pagenum\":\"1\",\"pagetotal\":\"66\"}");
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);
        //  se.call(null, envelope);//调用 version1.2
        //version1.1 需要如下soapaction
        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        otherEntryDataResp = object.getProperty(0).toString();
        return otherEntryDataResp;
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private OtherEntryTableAdapter.MyClickListener mListener = new OtherEntryTableAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            //  Toast.makeText(OtherOutgoing.this,listAllPostition.get(position).getPobillcode(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OtherEntry.this, OtherEntryDetail.class);
            intent.putExtra("current_pobillcode", listAllPostition.get(position).getPobillcode());
            intent.putExtra("current_cwarename", listAllPostition.get(position).getCwarename());
            intent.putExtra("current_cwarecode", listAllPostition.get(position).getCwarecode());
            intent.putExtra("current_dbilldate", listAllPostition.get(position).getDbilldate());
            startActivity(intent);
        }
    };
}
