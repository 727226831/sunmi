package com.example.shanggmiqr.transaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
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

import com.example.shanggmiqr.BusinessOperation;
import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.OtherBean;
import com.example.shanggmiqr.bean.OtherQueryBean;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherEntryTableAdapter;

import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/9.
 * 其他入库
 */

public class OtherEntry extends AppCompatActivity implements OnClickListener {
    private Button downloadOtherEntryButton;
    private Button queryEntryButton;
    private Button displayallEntryButton;
    private Handler otherEntryHandler = null;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private ListView tableListView;
    private List<OtherBean> listAllPostition;
    private String query_cwarename;
    private String query_uploadflag;
    private ZLoadingDialog dialog;
    private List<String> test;
    private TextView lst_downLoad_ts;
    private Button buttonexport;
    int type;
    String workcode;
    String name="";
    String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_manage);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

         type=getIntent().getIntExtra("type",-1);
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_otherentry);
        //显示最后一次的下载时间

        switch (type){
            case 0:
                name="LatestOtherEntryTSInfo";
                workcode="R09";
                title="其他入库";
                break;
            case 1:
                workcode="R11";
                name="LatestOtherOutgoingTSInfo";
                title="其他出库";
                break;
        }

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
        SharedPreferences latestDBTimeInfo = getSharedPreferences(name, 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", iUrl.begintime);
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
        buttonexport=findViewById(R.id.b_export);
        buttonexport.setOnClickListener(this);
        List<OtherBean> list = queryAll();
        listAllPostition = list;
        final OtherEntryTableAdapter adapter1 = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);


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
                        Toast.makeText(OtherEntry.this, title+"单已经下载", Toast.LENGTH_LONG).show();
                        //插入UI表格数据

                        List<OtherBean> list = queryAll();
                        listAllPostition = list;
                        final OtherEntryTableAdapter adapter = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);


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
                        Toast.makeText(OtherEntry.this, title+"单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
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
                                          DataHelper.showDialog(dialog);
                                        }
                                    });
                                    //其他入库查询，嵌套json,使用两张表存储，通过pobillcode关联
                                    String outEntryData = DataHelper.downloadDatabase(workcode, "1",OtherEntry.this,0);
                                    if (null == outEntryData) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    Gson gson7 = new Gson();
                                    OtherQueryBean otherEntryBean = gson7.fromJson(outEntryData, OtherQueryBean.class);
                                    if (otherEntryBean.getPagetotal() == 1) {
                                        DataHelper.insertOtherDataToDB(db3,otherEntryBean,type);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherEntryHandler.sendMessage(msg);
                                    } else if (otherEntryBean.getPagetotal() < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(OtherEntry.this, title+"单已经是最新", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        DataHelper.insertOtherDataToDB(db3,otherEntryBean,type);
                                        for (int pagenum = 2; pagenum <= otherEntryBean.getPagetotal(); pagenum++) {
                                            String outGoingData2 = DataHelper.downloadDatabase(workcode, String.valueOf(pagenum),OtherEntry.this,0);
                                            OtherQueryBean outGoingBean2 = gson7.fromJson(outGoingData2, OtherQueryBean.class);
                                            DataHelper.insertOtherDataToDB(db3,outGoingBean2,type);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherEntryHandler.sendMessage(msg);
                                    }
                                    String currentTs = getLatestDbilldate();
                                    String systime = Utils.getCurrentDateTimeNew();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences(name, 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_download_ts_begintime", currentTs);
                                    editor5.putString("latest_download_ts_systime", systime);
                                    editor5.commit();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences(name, 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", iUrl.begintime);
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
                                e.printStackTrace();
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
            case R.id.b_export:
                export();
                break;
            case R.id.displayall_other_entry:
                List<OtherBean> list = displayAll();
                listAllPostition = list;
                final OtherEntryTableAdapter adapter = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
                tableListView.setAdapter(adapter);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);



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
            date.add(iUrl.begintime);
        }
        //加1秒
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetemp = df.parse(date.get(0));
        datetemp.setTime(datetemp.getTime() + 1000);
        String addone = df.format(datetemp);
        return addone;
    }



    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherEntry.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditTextOtherEntry = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinnerOtherEntry = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinnerEntry = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        test = DataHelper.queryWarehouseInfo(db3);
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
                bean1 = query(codeNumEditTextOtherEntry.getText().toString(), query_cwarename,query_uploadflag);
                listAllPostition = bean1;
                final OtherEntryTableAdapter adapter3 = new OtherEntryTableAdapter(OtherEntry.this, removeDuplicate(bean1), mListener);
                tableListView.setAdapter(adapter3);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter3.select(position);

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
    ArrayList<OtherBean> bean1;

    private   ArrayList<OtherBean>  removeDuplicate(ArrayList<OtherBean> list)  {
        ArrayList<OtherBean>  beanList=new ArrayList<>();
        beanList.addAll(list);
        for  ( int  i  =   0 ; i  <  beanList.size()  -   1 ; i ++ )  {
            for  ( int  j  =  beanList.size()  -   1 ; j  >  i; j -- )  {
                if  (beanList.get(j).getPobillcode().equals(beanList.get(i).getPobillcode()))  {
                    beanList.remove(j);
                }
            }
        }
        return beanList;
    }
    private void export() {
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi",formatter.format(curDate)+".txt");
        Toast.makeText(OtherEntry.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            outputStream.write(("发货单号"+"\t"+ "单据日期"+"\t"+"物料编码"+"\t"+"物料名称"+"\t"+
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码").getBytes());
            for (int j = 0; j <bean1.size() ; j++) {
                outputStream.write("\r\n".getBytes());

                outputStream.write((bean1.get(j).getPobillcode()+"\t"
                        +bean1.get(j).getDbilldate()+"\t"
                        +bean1.get(j).getMaterialcode()+"\t"
                        +bean1.get(j).getMatrname()+"\t"
                        +bean1.get(j).getMaccode()+"\t"
                        +bean1.get(j).getXlh()+"\t"
                        +bean1.get(j).getProdcutcode()).getBytes());

            }
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<OtherBean> query(String pobillcode, String current_cwarename,String query_uploadflag) {
        ArrayList<OtherBean> list = new ArrayList<OtherBean>();
        Cursor cursor1=null;
        switch (type){
            case 0:
                cursor1 = db3.rawQuery("select otherentry.pobillcode,otherentry.cwarecode,otherentry.cwarename,otherentry.dr, otherentry.dbilldate,otherentrybody.materialcode,otherentry.dr," +
                        "otherentrybody.maccode,otherentrybody.maccode,otherentrybody.nnum, otherentryscanresult.prodcutcode," +
                        "otherentryscanresult.xlh" + " from otherentry left join otherentrybody on otherentry.pobillcode=otherentrybody.pobillcode " +
                        "left join otherentryscanresult on otherentrybody.pobillcode=otherentryscanresult.pobillcode " +
                        "and otherentrybody.vcooporderbcode_b=otherentryscanresult.vcooporderbcode_b where flag=? and otherentry.pobillcode" +
                        " like '%" + pobillcode + "%' and otherentry.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});
                break;
            case 1:
                cursor1 = db3.rawQuery("select otheroutgoing.pobillcode,otheroutgoing.cwarecode,otheroutgoing.cwarename,otheroutgoing.dr, otheroutgoing.dbilldate," +
                        "otheroutgoingbody.materialcode,otheroutgoingbody.maccode,otheroutgoingbody.maccode,otheroutgoingbody.nnum, otheroutgoingscanresult.prodcutcode," +
                        "otheroutgoingscanresult.xlh" + " from otheroutgoing left join otheroutgoingbody on otheroutgoing.pobillcode=otheroutgoingbody.pobillcode " +
                        "left join otheroutgoingscanresult on otheroutgoingbody.pobillcode=otheroutgoingscanresult.pobillcode " +
                        "and otheroutgoingbody.vcooporderbcode_b=otheroutgoingscanresult.vcooporderbcode_b where flag=? and otheroutgoing.pobillcode" +
                        " like '%" + pobillcode + "%' and otheroutgoing.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});
                break;
        }


                //判断cursor中是否存在数据
                while (cursor1.moveToNext()) {
                    OtherBean bean = new OtherBean();
                    bean.pobillcode = cursor1.getString(cursor1.getColumnIndex("pobillcode"));
                    bean.cwarecode = cursor1.getString(cursor1.getColumnIndex("cwarecode"));
                    bean.cwarename = cursor1.getString(cursor1.getColumnIndex("cwarename"));
                    bean.dbilldate = cursor1.getString(cursor1.getColumnIndex("dbilldate"));
                    bean.setMaterialcode(cursor1.getString(cursor1.getColumnIndex("materialcode")));

                    bean.setMaccode(cursor1.getString(cursor1.getColumnIndex("maccode")));
                    bean.setNnum(cursor1.getString(cursor1.getColumnIndex("nnum")));
                    bean.setProdcutcode(cursor1.getString(cursor1.getColumnIndex("prodcutcode")));
                    bean.setXlh(cursor1.getString(cursor1.getColumnIndex("xlh")));
                    bean.dr = cursor1.getInt(cursor1.getColumnIndex("dr"));
                    list.add(bean);
                }
                cursor1.close();

        return list;
    }

    public ArrayList<OtherBean> queryAll() {
        ArrayList<OtherBean> list = new ArrayList<OtherBean>();
        Cursor cursor=null;
         switch (type){
             case 0:
                 cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherEntry where flag=? order by dbilldate desc",
                         new String[]{"N"});
                 break;
             case 1:
                 cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherOutgoing where flag=? order by dbilldate desc", new String[]{"N"});
                 break;
         }


        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherBean bean = new OtherBean();
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
    public ArrayList<OtherBean> displayAll() {
        ArrayList<OtherBean> list = new ArrayList<OtherBean>();
        Cursor cursor = null;
        switch (type){
            case 0:
                cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherEntry order by dbilldate desc",
                        null);
                break;
            case 1:
                cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherOutgoing order by dbilldate desc",
                        null);
                break;
        }
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherBean bean = new OtherBean();
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

            Intent intent=null;
            switch (type){
                case 0:
                    intent = new Intent(OtherEntry.this, OtherEntryDetail.class);
                    break;
                case 1:
                    intent = new Intent(OtherEntry.this, OtherOutgoingDetail.class);
                    break;
            }

            intent.putExtra("current_pobillcode", listAllPostition.get(position).getPobillcode());
            intent.putExtra("current_cwarename", listAllPostition.get(position).getCwarename());
            intent.putExtra("current_cwarecode", listAllPostition.get(position).getCwarecode());
            intent.putExtra("current_dbilldate", listAllPostition.get(position).getDbilldate());
            startActivity(intent);
        }
    };
}
